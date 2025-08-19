package com.lookmarket.chatbot.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.lookmarket.chatbot.mapper.BoardBotMapper;

@Service
public class BoardAnswerService {
    private final BoardBotMapper mapper;
    public BoardAnswerService(BoardBotMapper mapper) { this.mapper = mapper; }

    public Optional<String> answerReviewsByGoodsName(String goodsName) {
        if (goodsName == null || goodsName.isBlank()) return Optional.empty();

        Map<String,Object> stats = mapper.findReviewStatsByGoodsName(goodsName);
        List<Map<String,Object>> rows = mapper.findLatestReviewsByGoodsName(goodsName, 3, 0);

        boolean hasExact = stats != null && stats.get("cnt") != null
                && ((Number)stats.get("cnt")).intValue() > 0;
        if (!hasExact) {
            String like = "%" + goodsName.trim() + "%";
            stats = mapper.findReviewStatsByGoodsNameLike(like);
            rows  = mapper.findLatestReviewsByGoodsNameLike(like, 3, 0);
        }
        if ((rows == null || rows.isEmpty()) && (stats == null || stats.isEmpty())) {
            return Optional.empty();
        }

        double avg = stats != null && stats.get("avgStar") != null
                ? ((Number)stats.get("avgStar")).doubleValue() : 0.0;
        int cnt = stats != null && stats.get("cnt") != null
                ? ((Number)stats.get("cnt")).intValue() : 0;

        String displayName = (rows != null && !rows.isEmpty())
                ? String.valueOf(rows.get(0).getOrDefault("g_name", goodsName))
                : goodsName;

        StringBuilder sb = new StringBuilder();
        sb.append(String.format("‘%s’ 리뷰 요약: ★%.1f (%d건)\n\n", displayName, avg, cnt));

        if (rows != null) {
            for (Map<String,Object> r : rows) {
                int star = asNum(r.get("r_star")).intValue();
                String content = asStr(r.get("r_content"));
                String date = asStr(r.get("r_date")).replace('T',' ');
                sb.append(String.format("· ★%d | %s | %s\n",
                        star, date, trim(content, 120)));
            }
        }

        // (옵션) 상품정보 한 줄 추가
        Map<String,Object> gi = mapper.findGoodsInfoByName(displayName);
        if (gi != null && !gi.isEmpty()) {
            sb.append(String.format("\n- 참고: %s / 가격 %,d원 / 재고 %s개\n",
                    asStr(gi.get("g_brand")),
                    asNum(gi.get("g_price")).intValue(),
                    asNum(gi.get("g_stock")).intValue()));
        }

        return Optional.of(sb.toString());
    }

    private static Number asNum(Object v) {
        if (v == null) return 0;
        if (v instanceof Number) return (Number) v;
        try { return new java.math.BigDecimal(String.valueOf(v)); } catch (Exception e) { return 0; }
    }
    private static String asStr(Object v) { return v == null ? "" : String.valueOf(v); }
    private static String trim(String s, int n) { return s == null ? "" : (s.length()>n ? s.substring(0,n)+"..." : s); }
}