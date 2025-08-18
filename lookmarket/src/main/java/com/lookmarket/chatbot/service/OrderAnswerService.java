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
        if (oi == null) return Optional.empty();

        int oId = ((Number) oi.get("o_id")).intValue();
        var items = mapper.findOrderItems(oId);
        var deli  = mapper.findLatestDeliveryByOrder(oId);
        var pay   = mapper.findPaymentByOrder(oId);

        String oiDate = String.valueOf(oi.get("oi_date"));
        int total = ((Number) oi.get("oi_total_goods_price")).intValue();
        int dfee  = ((Number) oi.get("oi_delivery_price")).intValue();
        Object sale = oi.get("oi_sale_price");
        int salePrice = (sale == null) ? 0 : ((Number) sale).intValue();

        StringBuilder line = new StringBuilder();
        line.append("가장 최근 주문(주문ID ").append(oId).append(", ").append(oiDate).append(") 요약\n");

        if (items != null && !items.isEmpty()) {
            line.append("- 품목: ");
            items.stream().limit(3).forEach(it -> {
                String nm = String.valueOf(it.get("ot_goods_name"));
                int qty = ((Number) it.get("ot_goods_qty")).intValue();
                line.append(nm).append("×").append(qty).append(", ");
            });
            if (items.size() > 3) line.append("외 ").append(items.size()-3).append("건");
            line.append("\n");
        }

        line.append(String.format("- 금액: 상품합계 %d원, 할인 %d원, 배송비 %d원\n", total, salePrice, dfee));

        if (deli != null) {
            int st = ((Number) deli.get("d_status")).intValue();
            String stTxt = switch (st) {
                case 1 -> "배송준비중";
                case 2 -> "배송중";
                case 3 -> "배송완료";
                case 4 -> "주문취소";
                default -> "알수없음";
            };
            line.append(String.format("- 배송상태: %s (택배사 %s, 운송장 %s, 출고 %s, 예정/완료 %s)\n",
                    stTxt,
                    String.valueOf(deli.get("d_company")),
                    String.valueOf(deli.get("d_transport_num")),
                    String.valueOf(deli.get("d_shipped_date")),
                    String.valueOf(deli.get("d_delivery_date"))));
        } else {
            line.append("- 배송 정보 없음\n");
        }

        if (pay != null) {
            line.append(String.format("- 결제: %s, 결제시각 %s, 최종금액 %s원, 거래번호 %s\n",
                    String.valueOf(pay.get("p_method")),
                    String.valueOf(pay.get("p_order_time")),
                    String.valueOf(pay.get("p_final_total_price")),
                    String.valueOf(pay.get("p_transaction_id"))));
        } else {
            line.append("- 결제 정보 없음\n");
        }
        return Optional.of(line.toString());
    }
}
