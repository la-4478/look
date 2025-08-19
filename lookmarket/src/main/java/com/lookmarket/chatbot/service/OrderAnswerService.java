package com.lookmarket.chatbot.service;

import com.lookmarket.chatbot.mapper.OrderBotMapper;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class OrderAnswerService {
    private final OrderBotMapper mapper;
    public OrderAnswerService(OrderBotMapper mapper) { this.mapper = mapper; }

    public Optional<String> answerLatest(String mId) {
        Map<String,Object> oi = mapper.findLatestOrder(mId);
        if (oi == null || oi.isEmpty()) return Optional.empty();

        int oId = getAsNumber(oi, "o_id").intValue();

        // ✅ 명확한 제네릭
        List<Map<String,Object>> items = mapper.findOrderItems(oId);
        Map<String,Object> deli  = mapper.findLatestDeliveryByOrder(oId);
        Map<String,Object> pay   = mapper.findPaymentByOrder(oId);

        String oiDate = getAsString(oi, "oi_date");
        int total     = getAsNumber(oi, "oi_total_goods_price").intValue();
        int dfee      = getAsNumber(oi, "oi_delivery_price").intValue();
        int salePrice = getAsNumber(oi, "oi_sale_price").intValue();

        StringBuilder line = new StringBuilder();
        line.append("가장 최근 주문(주문ID ").append(oId).append(", ").append(oiDate).append(") 요약\n");

        if (items != null && !items.isEmpty()) {
            line.append("- 품목: ");
            items.stream().limit(3).forEach(it -> {
                String nm = getAsString(it, "ot_goods_name<br>");
                int qty   = getAsNumber(it, "ot_goods_qty").intValue();
                line.append(nm).append("×").append(qty).append(", ");
            });
            if (items.size() > 3) line.append("외 ").append(items.size()-3).append("건");
            line.append("\n");
        } else {
            line.append("- 품목 없음\n");
        }

        line.append(String.format("- 금액: 상품합계 %,d원, 할인 %,d원, 배송비 %,d원\n", total, salePrice, dfee));

        if (deli != null && !deli.isEmpty()) {
            int st = getAsNumber(deli, "d_status").intValue();
            String stTxt = switch (st) {
                case 1 -> "배송준비중";
                case 2 -> "배송중";
                case 3 -> "배송완료";
                case 4 -> "주문취소";
                default -> "알수없음";
            };
            line.append(String.format("- 배송상태: %s (택배사 %s, 운송장 %s, 출고 %s, 예정/완료 %s)\n",
                    getNonEmpty(stTxt),
                    getAsString(deli, "d_company"),
                    getAsString(deli, "d_transport_num"),
                    getAsString(deli, "d_shipped_date"),
                    getAsString(deli, "d_delivery_date")));
        } else {
            line.append("- 배송 정보 없음\n");
        }

        if (pay != null && !pay.isEmpty()) {
            line.append(String.format("- 결제: %s, 결제시각 %s, 최종금액 %,d원, 거래번호 %s\n",
                    getAsString(pay, "p_method"),
                    getAsString(pay, "p_order_time"),
                    getAsNumber(pay, "p_final_total_price").intValue(),
                    getAsString(pay, "p_transaction_id")));
        } else {
            line.append("- 결제 정보 없음\n");
        }
        return Optional.of(line.toString());
    }

    // ===== 안전 캐스팅 헬퍼 =====
    private static Number getAsNumber(Map<String, Object> m, String key) {
        Object v = m.get(key);
        if (v == null) return 0;
        if (v instanceof Number) return (Number) v;
        try { return new java.math.BigDecimal(String.valueOf(v)); } catch (Exception e) { return 0; }
    }
    private static String getAsString(Map<String, Object> m, String key) {
        Object v = m.get(key);
        return v == null ? "" : String.valueOf(v);
    }
    private static String getNonEmpty(String s) { return (s == null || s.isBlank()) ? "없음" : s; }
}
