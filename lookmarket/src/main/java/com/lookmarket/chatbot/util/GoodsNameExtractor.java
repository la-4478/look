package com.lookmarket.chatbot.util;

import java.text.Normalizer;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.regex.Pattern;

public class GoodsNameExtractor {

    // ===== 설정값 =====
    private static final int CACHE_TTL_MS = 60_000; // 1분 캐시
    private static final double THRESH_STRICT = 0.85; // 토큰 유사도 엄격 기준
    private static final double THRESH_LENIENT = 0.70; // 느슨 기준
    private static final int MAX_CANDIDATES = 5;

    // ===== 전처리용 =====
    private static final Pattern PUNCT = Pattern.compile("[^0-9A-Za-z가-힣\\s]");
    private static final Pattern MULTI_WS = Pattern.compile("\\s+");

    private static final Set<String> STOP = Set.of(
            "가격","얼마","얼만데","얼마야","재고","스펙","사양","정보","설명","리뷰","평점","후기","문의",
            "보여줘","알려줘","해주세요","해줘","좀","요","요?","요요","그","이","저","은","는","가",
            "을","를","과","와","하고","에서","으로","로","까지","부터","의","에","이다","임","거","것","좀요"
    );

    // ===== 캐시 =====
    private final Supplier<List<String>> goodsNameSupplier; // DB에서 상품명 불러오는 함수
    private volatile List<String> cachedNames = List.of();
    private final AtomicLong lastLoadMs = new AtomicLong(0);

    public GoodsNameExtractor(Supplier<List<String>> goodsNameSupplier) {
        this.goodsNameSupplier = goodsNameSupplier;
    }

    // 공개 API: 단일 추출
    public Optional<String> extractBest(String query) {
        List<String> cands = extractCandidates(query);
        return cands.isEmpty() ? Optional.empty() : Optional.of(cands.get(0));
    }

    // 공개 API: 후보 N개
    public List<String> extractCandidates(String query) {
        String norm = normalize(query);
        List<String> tokens = tokens(norm);
        if (tokens.isEmpty()) return List.of();

        // 후보 토큰 문자열 (명사/키워드 연속부를 다시 붙여보기도 함)
        List<String> tokenPhrases = buildTokenPhrases(tokens);

        List<String> dict = getDict(); // 상품명 사전(캐시)
        if (dict.isEmpty()) return List.of();

        // 1) 정확 일치 (케이스/기호 무시 후)
        Map<String, Double> scored = new HashMap<>();
        for (String d : dict) {
            String dn = normalize(d);
            if (dn.equals(norm) || tokensEqual(tokens, tokens(dn))) {
                scored.put(d, 1.0);
            }
        }
        if (!scored.isEmpty()) return topK(scored, MAX_CANDIDATES);

        // 2) 부분 일치 (문장에 상품명 포함 / 토큰구간 포함)
        for (String d : dict) {
            String dn = normalize(d);
            if (norm.contains(dn)) {
                scored.put(d, 0.95);
                continue;
            }
            for (String phr : tokenPhrases) {
                if (!phr.isBlank() && dn.contains(phr)) {
                    scored.merge(d, 0.90, Math::max);
                }
            }
        }
        List<String> partial = topK(scored, MAX_CANDIDATES);
        if (!partial.isEmpty()) return partial;

        // 3) 토큰 유사도 (Jaccard: 교집합/합집합)
        scored.clear();
        Set<String> qset = new HashSet<>(tokens);
        for (String d : dict) {
            Set<String> dset = new HashSet<>(tokens(normalize(d)));
            double sim = jaccard(qset, dset);
            if (sim >= THRESH_STRICT) {
                scored.put(d, 0.85 + 0.10 * (sim - THRESH_STRICT)/(1-THRESH_STRICT));
            }
        }
        List<String> byJaccard = topK(scored, MAX_CANDIDATES);
        if (!byJaccard.isEmpty()) return byJaccard;

        // 4) 오타 보정(편집거리) + 느슨 Jaccard
        scored.clear();
        for (String d : dict) {
            String dn = normalize(d);
            double simTok = jaccard(qset, new HashSet<>(tokens(dn)));
            if (simTok < THRESH_LENIENT) continue;

            // 편집거리 기반 유사도 (문장 vs 상품명, 토큰문장 vs 상품명 둘 다 봄)
            double sim1 = 1.0 - (levenshtein(norm, dn) / (double) Math.max(1, Math.max(norm.length(), dn.length())));
            String joinTok = String.join(" ", tokens);
            double sim2 = 1.0 - (levenshtein(joinTok, dn) / (double) Math.max(1, Math.max(joinTok.length(), dn.length())));
            double sim = Math.max(sim1, sim2);

            if (sim >= 0.6) { // 타이핑 오타 정도 허용
                scored.put(d, 0.75 + 0.10 * (sim - 0.6)/(1-0.6));
            }
        }
        return topK(scored, MAX_CANDIDATES);
    }

    // ===== 내부 유틸 =====

    private List<String> getDict() {
        long now = System.currentTimeMillis();
        if (now - lastLoadMs.get() > CACHE_TTL_MS) {
            synchronized (this) {
                if (now - lastLoadMs.get() > CACHE_TTL_MS) {
                    List<String> loaded = goodsNameSupplier.get();
                    cachedNames = loaded == null ? List.of() : List.copyOf(loaded);
                    lastLoadMs.set(now);
                }
            }
        }
        return cachedNames;
    }

    private static String normalize(String s) {
        if (s == null) return "";
        String n = Normalizer.normalize(s, Normalizer.Form.NFKC);
        n = n.toLowerCase(Locale.ROOT);
        n = PUNCT.matcher(n).replaceAll(" ");
        n = MULTI_WS.matcher(n).replaceAll(" ").trim();
        return n;
    }

    private static List<String> tokens(String norm) {
        if (norm.isBlank()) return List.of();
        String[] arr = norm.split(" ");
        List<String> out = new ArrayList<>(arr.length);
        for (String t : arr) {
            if (t.length() < 2) continue;
            if (STOP.contains(t)) continue;
            // 조사 꼬리 간단 제거(최소침습) : ~은/는/이/가/을/를
            t = t.replaceAll("(은|는|이|가|을|를)$", "");
            if (!t.isBlank() && !STOP.contains(t)) out.add(t);
        }
        return out;
    }

    private static boolean tokensEqual(List<String> a, List<String> b) {
        if (a.size() != b.size()) return false;
        for (int i = 0; i < a.size(); i++) if (!a.get(i).equals(b.get(i))) return false;
        return true;
    }

    private static List<String> buildTokenPhrases(List<String> toks) {
        // 연속 토큰을 1~3그램으로 만들어봄: ["애플","사과","1kg"] -> ["애플","사과","1kg","애플 사과","사과 1kg","애플 사과 1kg"]
        List<String> out = new ArrayList<>();
        int n = toks.size();
        for (int i=0;i<n;i++) {
            for (int j=i;j<Math.min(n, i+3);j++) {
                out.add(String.join(" ", toks.subList(i, j+1)));
            }
        }
        return out;
    }

    private static double jaccard(Set<String> a, Set<String> b) {
        if (a.isEmpty() && b.isEmpty()) return 1.0;
        Set<String> inter = new HashSet<>(a); inter.retainAll(b);
        Set<String> uni = new HashSet<>(a); uni.addAll(b);
        return uni.isEmpty() ? 0.0 : (inter.size() / (double) uni.size());
    }

    // 간단 Levenshtein (O(nm)): 길이 짧은 한국어/영문 조합에 충분
    private static int levenshtein(String s1, String s2) {
        int n = s1.length(), m = s2.length();
        int[] prev = new int[m+1], curr = new int[m+1];
        for (int j=0;j<=m;j++) prev[j] = j;
        for (int i=1;i<=n;i++) {
            curr[0] = i;
            char c1 = s1.charAt(i-1);
            for (int j=1;j<=m;j++) {
                int cost = (c1 == s2.charAt(j-1)) ? 0 : 1;
                curr[j] = Math.min(Math.min(curr[j-1]+1, prev[j]+1), prev[j-1]+cost);
            }
            int[] tmp = prev; prev = curr; curr = tmp;
        }
        return prev[m];
    }

    private static List<String> topK(Map<String, Double> scored, int k) {
        return scored.entrySet().stream()
                .sorted((a,b) -> Double.compare(b.getValue(), a.getValue()))
                .limit(k)
                .map(Map.Entry::getKey)
                .toList();
    }

    // 간단 인터페이스: DB 의존 제거용
    public interface Supplier<T> { T get(); }
}
