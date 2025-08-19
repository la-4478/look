package com.lookmarket.chatbot.service;

import java.util.List;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.lookmarket.chatbot.util.GoodsNameExtractor;

@Service
public class RouterAnswerService {

    private final GoodsAnswerService goodsSvc;
    private final OrderAnswerService orderSvc;
    private final PromoAnswerService promoSvc;
    private final BoardAnswerService boardSvc;
    private final ChatbotService openAiSvc;
    private final GoodsNameExtractor goodsNameExtractor;
    private final boolean fallbackEnabled;

    // 🔹 의도별 정규식(사전 컴파일)
    private static final Pattern RX_ORDER  = Pattern.compile("(주문|배송|운송장|결제|취소|환불|언제|어디)");
    private static final Pattern RX_PROMO  = Pattern.compile("(이벤트|프로모션|행사)");
    private static final Pattern RX_COUPON = Pattern.compile("(쿠폰|할인\\s*코드|프로모션\\s*코드|쿠폰코드)");
    private static final Pattern RX_REVIEW = Pattern.compile("(리뷰|후기|평점|평가)");
    private static final Pattern RX_GOODS  = Pattern.compile("(상품|가격|얼마|재고|스펙|사양)");

    public RouterAnswerService(
            GoodsAnswerService goodsSvc,
            OrderAnswerService orderSvc,
            PromoAnswerService promoSvc,
            BoardAnswerService boardSvc,
            ChatbotService openAiSvc,
            GoodsNameExtractor goodsNameExtractor,
            @Value("${chatbot.fallback.enabled:true}") boolean fallbackEnabled
    ) {
        this.goodsSvc = goodsSvc;
        this.orderSvc = orderSvc;
        this.promoSvc = promoSvc;
        this.boardSvc = boardSvc;
        this.openAiSvc = openAiSvc;
        this.goodsNameExtractor = goodsNameExtractor;
        this.fallbackEnabled = fallbackEnabled;
    }

    private static void L(String s){ System.out.println("[ROUTER] " + s); }
    private static boolean find(Pattern p, String q) { return p.matcher(q).find(); }

    public String route(String userText, String loginUserId) {
        String q = userText == null ? "" : userText.trim();
        String nq = normalize(q);
        L("q=" + q + ", user=" + loginUserId);

        // 1) 주문/배송/결제 (로그인 사용자 우선)
        if (loginUserId != null && find(RX_ORDER, nq)) {
            var ans = orderSvc.answerLatest(loginUserId);
            L("intent=ORDER present=" + ans.isPresent());
            if (ans.isPresent()) return ans.get();
        }

        // 2) 쿠폰 (원하면 주석 해제)
        /*
        if (find(RX_COUPON, nq)) {
            var ans = promoSvc.answerCoupons(loginUserId);
            L("intent=COUPON present=" + ans.isPresent());
            if (ans.isPresent()) return ans.get();
        }
        */

        // 3) 프로모션/이벤트
        if (find(RX_PROMO, nq)) {
            var ans = promoSvc.answerActivePromotions();
            L("intent=PROMO present=" + ans.isPresent());
            if (ans.isPresent()) return ans.get();
        }

        // 4) 리뷰/평점 (상품보다 먼저!)
        if (find(RX_REVIEW, nq)) {
            List<String> cands = goodsNameExtractor.extractCandidates(nq);
            if (!cands.isEmpty()) {
                String goodsName = cands.get(0);
                var ans = boardSvc.answerReviewsByGoodsName(goodsName);
                L("intent=REVIEW name=" + goodsName + " present=" + ans.isPresent()
                        + " alt=" + (cands.size() > 1 ? cands.subList(1, Math.min(3, cands.size())) : List.of()));
                if (ans.isPresent()) return ans.get();
            } else {
                L("intent=REVIEW name-candidates=0");
            }
        }

        // 5) 상품 질의(가격/재고/스펙 등)
        if (find(RX_GOODS, nq)) {
            var ans = goodsSvc.answer(nq);
            L("intent=GOODS present=" + ans.isPresent());
            if (ans.isPresent()) return ans.get();
        }

        // 6) 게시판 기타 (공지/문의/FAQ 등 키워드 파악은 서비스 내부에서)
//        var bd = boardSvc.answerBoard(nq);
//        L("intent=BOARD present=" + bd.isPresent());
//        if (bd.isPresent()) return bd.get();

        // 7) 폴백 (LLM)
        L("intent=FALLBACK enabled=" + fallbackEnabled);
        if (fallbackEnabled) {
            return openAiSvc.getChatbotResponse(userText);
        }
        return "요청하신 내용을 이해하지 못했어요. ‘주문 조회’, ‘이벤트’, ‘리뷰’, ‘가격’처럼 다시 한번 말씀해 주세요!";
    }

    private static String normalize(String s) {
        if (s == null) return "";
        String x = s.replaceAll("[\\p{Cntrl}]+", " ");
        x = x.replaceAll("\\s+", " ").trim();
        return x;
    }
}
