package com.lookmarket.chatbot.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class RouterAnswerService {

    private final GoodsAnswerService goodsSvc;
    private final OrderAnswerService orderSvc;
    private final PromoAnswerService promoSvc;
    private final BoardAnswerService boardSvc;
    private final ChatbotService openAiSvc; // 네 OpenAI 서비스

    private final boolean fallbackEnabled;

    public RouterAnswerService(
            GoodsAnswerService goodsSvc,
            OrderAnswerService orderSvc,
            PromoAnswerService promoSvc,
            BoardAnswerService boardSvc,
            ChatbotService openAiSvc,
            @Value("${chatbot.fallback.enabled:true}") boolean fallbackEnabled
    ) {
        this.goodsSvc = goodsSvc;
        this.orderSvc = orderSvc;
        this.promoSvc = promoSvc;
        this.boardSvc = boardSvc;
        this.openAiSvc = openAiSvc;
        this.fallbackEnabled = fallbackEnabled;
    }

    private static void L(String s){ System.out.println("[ROUTER] " + s); }

    public String route(String userText, String loginUserId) {
      String q = userText == null ? "" : userText.trim();
      L("q=" + q + ", user=" + loginUserId);

      if (loginUserId != null && q.matches(".*(주문|배송|운송장|결제|취소|환불|언제|어디).*")) {
        var ans = orderSvc.answerLatest(loginUserId);
        L("order -> " + ans.isPresent());
        if (ans.isPresent()) return ans.get();
      }
      if (q.matches(".*(이벤트|프로모션|행사).*")) {
        var ans = promoSvc.answerPromos(); L("promo -> " + ans.isPresent());
        if (ans.isPresent()) return ans.get();
      }
      if (q.matches(".*(쿠폰|할인코드).*")) {
        var ans = promoSvc.answerCoupons(loginUserId); L("coupon -> " + ans.isPresent());
        if (ans.isPresent()) return ans.get();
      }
      if (q.matches(".*(상품|가격|얼마|재고|스펙|사양|리뷰|평점).*")) {
        var ans = goodsSvc.answer(q); L("goods -> " + ans.isPresent());
        if (ans.isPresent()) return ans.get();
      }
      var bd = boardSvc.answerBoard(q); L("board -> " + bd.isPresent());
      if (bd.isPresent()) return bd.get();

      L("fallback LLM");
      return openAiSvc.getChatbotResponse(userText);
    }

    private static String normalize(String s) {
        if (s == null) return "";
        String x = s.trim();
        // 필요시 불용어 제거, 소문자화 등 추가 가능
        return x;
    }
}
