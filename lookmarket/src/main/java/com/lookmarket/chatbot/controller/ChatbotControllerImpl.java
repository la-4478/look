package com.lookmarket.chatbot.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.lookmarket.Vector.AiAnswerService;
import com.lookmarket.chatbot.dto.ChatDTO.ChatRequest;
import com.lookmarket.chatbot.dto.ChatDTO.ChatResponse;
import com.lookmarket.chatbot.service.ChatbotService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
@Controller
@RequestMapping("/chatbot")
public class ChatbotControllerImpl implements ChatbotController {

	@Autowired
    private ChatbotService chatbotService;
	@Autowired
	private AiAnswerService aiAnswerService;
    
    @Override
    @ResponseBody
    public ResponseEntity<ChatResponse> getChatResponse(@RequestBody ChatRequest body) {
        try {
            // NPE 방지: body가 null이거나 message가 비어 있으면 안내
            if (body == null || body.getMessage() == null || body.getMessage().isBlank()) {
                return ResponseEntity.badRequest().body(new ChatResponse("메시지가 비었습니다."));
            }

            String result = chatbotService.getChatbotResponse(body.getMessage());
            return ResponseEntity.ok(new ChatResponse(result));
        } catch (Exception e) {
            // 서버 콘솔 확인용 스택트레이스
            e.printStackTrace();
            // 프론트는 .content 만 읽으므로 동일 포맷 유지
            return ResponseEntity.status(500).body(new ChatResponse("서버 오류: " + e.getMessage()));
        }
    }
    
    @PostMapping(
    		  value = "/message.do",
    		  consumes = MediaType.APPLICATION_JSON_VALUE,
    		  produces = MediaType.APPLICATION_JSON_VALUE
    		)
    		public ResponseEntity<ChatResponse> message(@RequestBody ChatRequest body) {
    		    if (body == null || body.getMessage() == null || body.getMessage().isBlank()) {
    		        return ResponseEntity.badRequest().body(new ChatResponse("메시지가 비었습니다."));
    		    }

    		    boolean useRag = "rag".equalsIgnoreCase(body.getMessage()); // ChatRequest에 mode 추가 가정
    		    if (useRag) {
    		        var ans = aiAnswerService.answer(body.getMessage());
    		        return ResponseEntity.ok(new ChatResponse(ans.content())); // 필요시 sources 추가
    		    } else {
    		        String result = chatbotService.getChatbotResponse(body.getMessage());
    		        return ResponseEntity.ok(new ChatResponse(result));
    		    }
    		}

    /**
     * 레이아웃 엔진으로 JSP 진입. 기존 프로젝트 컨벤션 그대로 맞춰 둠.
     */
    @RequestMapping(value="/chatbot.do", method= {RequestMethod.POST, RequestMethod.GET})
    public ModelAndView chatbot(HttpServletRequest request, HttpServletResponse response) throws Exception{
        HttpSession session = request.getSession();
        ModelAndView mav = new ModelAndView();
        String layout = "common/layout";     // 공통 레이아웃
        mav.setViewName(layout);
        mav.addObject("viewName", "/chatbot/chatbot"); // 내부에서 include할 본문 JSP

        // 페이지 진입 시 사이드 메뉴 숨김 처리 (프로젝트 컨벤션)
        session.setAttribute("sideMenu", "hidden");
        return mav;
    }

}
