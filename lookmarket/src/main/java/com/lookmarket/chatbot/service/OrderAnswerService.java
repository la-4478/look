package com.lookmarket.chatbot.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lookmarket.chatbot.mapper.OrderBotMapper;

@Service
public class OrderAnswerService {
    private final OrderBotMapper mapper;
    public OrderAnswerService(OrderBotMapper mapper) { this.mapper = mapper; }
    private static final ObjectMapper mapper1 = new ObjectMapper();
    
    private static final Pattern RX_TYPE_CARD      = Pattern.compile("\\bPaymentMethodCard\\b", Pattern.CASE_INSENSITIVE);
    private static final Pattern RX_TYPE_EASYPAY   = Pattern.compile("\\bPaymentMethodEasyPay\\b", Pattern.CASE_INSENSITIVE);
    private static final Pattern RX_PROVIDER       = Pattern.compile("\\bprovider\\s*=\\s*([^,}\\s]+)", Pattern.CASE_INSENSITIVE);
    private static final Pattern RX_CARD_BLOCK     = Pattern.compile("\\bcard\\s*=\\s*\\{([^}]*)\\}", Pattern.CASE_INSENSITIVE);
    private static final Pattern RX_KEYVAL_NAME    = Pattern.compile("\\bname\\s*=\\s*([^,}]+)");
    private static final Pattern RX_KEYVAL_PUB     = Pattern.compile("\\bpublisher\\s*=\\s*([^,}]+)");
    private static final Pattern RX_KEYVAL_ISSUER  = Pattern.compile("\\bissuer\\s*=\\s*([^,}]+)");
    private static final Pattern RX_KEYVAL_BRAND   = Pattern.compile("\\bbrand\\s*=\\s*([^,}]+)");
    
    private static String won(int v) { return String.format("%,d", v); }
    
    
    public Optional<String> answerLatest(String mId) {
        Map<String,Object> oi = mapper.findLatestOrder(mId);
        if (oi == null || oi.isEmpty()) return Optional.empty();

        int oId = getAsNumber(oi, "o_id").intValue();

        List<Map<String,Object>> items = mapper.findOrderItems(oId);
        Map<String,Object> deli  = mapper.findLatestDeliveryByOrder(oId);
        Map<String,Object> pay   = mapper.findPaymentByOrder(oId);
        
        System.out.println("[PAY] row = " + pay); // Map 전체 찍기
        System.out.println("[PAY] keys = " + (pay != null ? pay.keySet() : "null"));
        System.out.println("[PAY] p_method raw = " + (pay != null ? pay.get("p_method") : "null"));

        // 날짜 예쁘게: 'T' → ' '
        String oiDateRaw = getAsString(oi, "oi_date");
        String oiDate = oiDateRaw.replace('T', ' ');

        int total     = getAsNumber(oi, "oi_total_goods_price").intValue();
        int dfee      = getAsNumber(oi, "oi_delivery_price").intValue();
        int salePrice = getAsNumber(oi, "oi_sale_price").intValue();

        StringBuilder line = new StringBuilder();
        line.append("가장 최근 주문(주문ID ").append(oId).append(", ").append(oiDate).append(") \n요약\n");

        if (items != null && !items.isEmpty()) {
            line.append("- 품목:\n");
            items.stream().limit(3).forEach(it -> {
                String nm = getAsString(it, "ot_goods_name");   // ★ 키 그대로
                int qty   = getAsNumber(it, "ot_goods_qty").intValue();
                line.append("  · ").append(nm).append(" × ").append(qty).append("\n");
            });
            if (items.size() > 3) line.append("  외 ").append(items.size()-3).append("건\n");
        } else {
            line.append("- 품목 없음\n");
        }

        // ✅ String.format 제거, 안전하게 붙이기
        line.append("- 금액: 상품합계 ")
            .append(won(total)).append("원, 할인 ")
            .append(won(salePrice)).append("원, 배송비 ")
            .append(won(dfee)).append("원\n");

        if (deli != null && !deli.isEmpty()) {
            int st = getAsNumber(deli, "d_status").intValue();
            String stTxt = switch (st) {
                case 1 -> "배송준비중";
                case 2 -> "배송중";
                case 3 -> "배송완료";
                case 4 -> "주문취소";
                default -> "알수없음";
            };
            line.append("- 배송상태: ").append(stTxt)
            .append(" (택배사 ").append(getAsString(deli, "d_company"))
            .append(", 운송장 ").append(getAsString(deli, "d_transport_num"))
            .append(", 출고 ").append(getAsString(deli, "d_shipped_date"))
            .append(", 예정/완료 ").append(getAsString(deli, "d_delivery_date"))
            .append(")\n");
        } else {
            line.append("- 배송 정보 없음\n");
        }

        if (pay != null && !pay.isEmpty()) {
        	Object pMethod = pay.get("p_method");
        	String methodStr = normalizePaymentMethod(pMethod);
        	line.append("- 결제: ").append(methodStr)
        	    .append(", 결제시각 ").append(getAsString(pay, "p_order_time"))
        	    .append(", 최종금액 ").append(won(getAsNumber(pay, "p_final_total_price").intValue())).append("원")
        	    .append("\n");
        } else {
            line.append("- 결제 정보 없음\n");
        }
        	
        return Optional.of(line.toString());
    }

    private static String normalizePaymentMethod(Object pMethodRaw) {
        if (pMethodRaw == null) {System.out.println("pMethodRaw"); return "알수없음";}
        String s = String.valueOf(pMethodRaw).trim();
        if (s.isEmpty()) { System.out.println("s.isEmpty()"); return "알수없음";}

        // 1) 간편결제: provider=... 우선
        if (RX_TYPE_EASYPAY.matcher(s).find()) {
            String prov = extractFirst(RX_PROVIDER, s);
            return (prov != null && !prov.isBlank()) ? prov : "간편결제";
        }

        // 2) 카드결제: card={...} 블록 안에서 name / publisher / issuer / brand 순으로
        if (RX_TYPE_CARD.matcher(s).find()) {
            String cardBlock = extractFirstGroup(RX_CARD_BLOCK, s); // 중괄호 안
            if (cardBlock != null) {
                String name = extractFirst(RX_KEYVAL_NAME, cardBlock);
                if (nonEmpty(name)) return name;
                String pub = extractFirst(RX_KEYVAL_PUB, cardBlock);
                if (nonEmpty(pub)) return pub;
                String iss = extractFirst(RX_KEYVAL_ISSUER, cardBlock);
                if (nonEmpty(iss)) return iss;
                String br  = extractFirst(RX_KEYVAL_BRAND, cardBlock);
                if (nonEmpty(br))  return br;
            }
            return "카드";
        }

        // 3) 타입 문자열이 아예 없을 때: provider 우선, 그다음 card 블록 name 시도
        String prov = extractFirst(RX_PROVIDER, s);
        if (nonEmpty(prov)) return prov;

        String cardBlock = extractFirstGroup(RX_CARD_BLOCK, s);
        if (cardBlock != null) {
            String name = extractFirst(RX_KEYVAL_NAME, cardBlock);
            if (nonEmpty(name)) return name;
        }

        // 4) 그래도 못 찾으면 원문
        return s;
    }

    private static boolean nonEmpty(String x) { return x != null && !x.isBlank(); }

    private static String extractFirst(Pattern p, String text) {
        Matcher m = p.matcher(text);
        return m.find() ? m.group(1).trim() : null;
    }

    private static String extractFirstGroup(Pattern p, String text) {
        Matcher m = p.matcher(text);
        return m.find() ? m.group(1) : null;
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
