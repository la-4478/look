package com.lookmarket.chatbot.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.lookmarket.chatbot.mapper.GoodsBotMapper;
import com.lookmarket.goods.dao.GoodsDAO;

@Service
public class GoodsAnswerService {
    private final GoodsBotMapper mapper;
    private final GoodsDAO goodsDAO;

    public GoodsAnswerService(GoodsDAO goodsDAO, GoodsBotMapper mapper) {
        this.mapper = mapper;
        this.goodsDAO = goodsDAO;
    }

    public Optional<String> answer(String qRaw) {
        // ① 키워드 추출
        List<String> tokens = extractTokens(qRaw);
        if (tokens.isEmpty()) return Optional.empty();

        // ② DB 검색 (토큰 OR-LIKE)
        // ✅ rows를 명확한 제네릭 컬렉션으로 받기
        List<Map<String, Object>> rows = mapper.searchGoodsByTokens(tokens, 3);

        System.out.println("[GOODS] tokens=" + tokens + " hits=" + (rows == null ? 0 : rows.size()));
        if (rows == null || rows.isEmpty()) return Optional.empty();

        Map<String, Object> g = rows.get(0);

        // 안전 추출 헬퍼 사용 (null/타입 캐스팅 안전)
        int gId    = getAsNumber(g, "g_id").intValue();
        String name = getAsString(g, "g_name");
        // 컬럼명이 g_discription 인데 오타 가능성 매우 큼 → g_description 권장 (아래 매퍼/SQL에서 alias로 맞춰도 됨)
        String desc = getAsString(g, "g_discription");
        int price  = getAsNumber(g, "g_price").intValue();
        int stock  = getAsNumber(g, "g_stock").intValue();
        String brand = getAsString(g, "g_brand");

        String main = mapper.findMainImage(gId);

        Map<String, Object> stats = mapper.findReviewStats(gId);
        Double avgStar = stats != null && stats.get("avgStar") != null ? ((Number) stats.get("avgStar")).doubleValue() : null;
        int cnt = stats != null && stats.get("cnt") != null ? ((Number) stats.get("cnt")).intValue() : 0;

        StringBuilder sb = new StringBuilder();
        sb.append("상품 ‘").append(name).append("’ (브랜드: ").append(brand).append(")\n")
          .append("- 가격: ").append(price).append("원, 재고: ").append(stock).append("개\n");
        if (avgStar != null) sb.append(String.format("- 리뷰 평점: ★%.1f (%d건)\n", avgStar, cnt));
        if (desc != null && !desc.isBlank()) {
            sb.append("- 설명: ").append(desc.length() > 200 ? desc.substring(0, 200) + "..." : desc);
        }
        return Optional.of(sb.toString());
    }

    private static Number getAsNumber(Map<String, Object> m, String key) {
        Object v = m.get(key);
        if (v instanceof Number) return (Number) v;
        if (v == null) return 0;
        try { return Double.valueOf(String.valueOf(v)); } catch (Exception e) { return 0; }
    }

    private static String getAsString(Map<String, Object> m, String key) {
        Object v = m.get(key);
        return v == null ? "" : String.valueOf(v);
    }



    private static List<String> extractTokens(String s) {
        if (s == null) return List.of();
        // 기호 제거 + 소문자화
        String norm = s.replaceAll("[^0-9A-Za-z가-힣\\s]", " ")
                       .replaceAll("\\s+", " ").trim().toLowerCase();
        if (norm.isEmpty()) return List.of();
        String[] arr = norm.split("\\s+");
        List<String> out = new ArrayList<>();
        for (String t : arr) {
            if (t.length() < 2) continue;
            out.add(t);
        }
        // 키워드가 너무 많으면 상위 3~4개만
        return out.size() > 4 ? out.subList(0, 4) : out;
    }
}
