package com.lookmarket.chatbot.service;

import com.lookmarket.chatbot.mapper.PromoBotMapper;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class PromoAnswerService {
    private final PromoBotMapper mapper;
    public PromoAnswerService(PromoBotMapper mapper) { this.mapper = mapper; }

    public Optional<String> answerPromos() {
        var promos = mapper.findActivePromos(3);
        if (promos == null || promos.isEmpty()) return Optional.empty();
        StringBuilder sb = new StringBuilder("진행 중인 이벤트:\n");
        for (var p : promos) {
            sb.append(String.format("- %s (%s ~ %s)\n",
                p.get("promo_title"), p.get("promo_start_date"), p.get("promo_end_date")));
        }
        return Optional.of(sb.toString());
    }

    public Optional<String> answerCoupons(String mId) {
        var rows = mapper.findAvailableCouponsForUser(Map.of("mId", mId, "limit", 5));
        if (rows == null || rows.isEmpty()) return Optional.empty();
        StringBuilder sb = new StringBuilder("사용 가능한 쿠폰:\n");
        for (var r : rows) {
            int typ = ((Number) r.get("promo_discount_type")).intValue();
            int val = ((Number) r.get("promo_discount_value")).intValue();
            String human = (typ == 1) ? (val + "%") : (val + "원");
            sb.append(String.format("- %s: %s 할인 (최소구매 %s원) 유효기간 %s~%s\n",
                    r.get("promo_code"), human, r.get("promo_min_purchase"),
                    r.get("promo_start_date"), r.get("promo_end_date")));
        }
        return Optional.of(sb.toString());
    }
}
