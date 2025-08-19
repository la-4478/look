package com.lookmarket.chatbot.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.lookmarket.chatbot.mapper.PromoBotMapper;

@Service
public class PromoAnswerService {
    private final PromoBotMapper mapper;

    public PromoAnswerService(PromoBotMapper mapper) { this.mapper = mapper; }

    private static final String NO_ACTIVE =
        "현재 진행 중인 프로모션이 없습니다.\n새로운 이벤트가 열리면 바로 알려드릴게요!";

    private static final String NO_RESULT =
        "검색하신 조건에 맞는 프로모션이 없습니다.";

    public Optional<String> answerActivePromotions() {
        String today = LocalDate.now().toString(); // 'YYYY-MM-DD'
        List<Map<String,Object>> list = mapper.findActivePromotions(today, 5, 0);

        // ✅ 비었을 때도 도메인 고정 답변 (폴백 금지)
        if (list == null || list.isEmpty()) {
            return Optional.of(NO_ACTIVE);
        }

        StringBuilder sb = new StringBuilder("진행 중인 프로모션 상위 5건\n\n");
        for (Map<String,Object> p : list) {
            sb.append("· ")
              .append(String.valueOf(p.get("promo_title")))
              .append(" (")
              .append(String.valueOf(p.get("promo_start_date")))
              .append(" ~ ")
              .append(String.valueOf(p.get("promo_end_date")))
              .append(")\n");
        }
        return Optional.of(sb.toString());
    }

    public Optional<String> answerSearchPromotions(String q) {
        String today = LocalDate.now().toString();
        List<Map<String,Object>> list = mapper.searchActivePromotions(q, today, 5, 0);

        // ✅ 검색 결과 없을 때도 안내
        if (list == null || list.isEmpty()) {
            return Optional.of(NO_RESULT);
        }

        StringBuilder sb = new StringBuilder("프로모션 검색 결과 상위 5건\n\n");
        for (Map<String,Object> p : list) {
            sb.append("· ")
              .append(String.valueOf(p.get("promo_title")))
              .append(" (")
              .append(String.valueOf(p.get("promo_start_date")))
              .append(" ~ ")
              .append(String.valueOf(p.get("promo_end_date")))
              .append(")\n");
        }
        return Optional.of(sb.toString());
    }
}
