package com.lookmarket.chatbot.controller;

import java.util.Map;

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
import com.lookmarket.chatbot.dto.ChatRequest;
import com.lookmarket.chatbot.dto.ChatResponse;
import com.lookmarket.chatbot.service.ChatbotService;
import com.lookmarket.chatbot.service.RouterAnswerService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

//com.lookmarket.chatbot.controller.ChatbotControllerImpl
@Controller
@RequestMapping("/chatbot")
public class ChatbotControllerImpl implements ChatbotController {
	
	 @Autowired 
	 private ChatbotService chatbotService;
	 @Autowired 
	 private AiAnswerService aiAnswerService;
	 @Autowired 
	 private RouterAnswerService routerAnswerService;
	 
	 @PostMapping(
			  value = "/message.do",
			  consumes = MediaType.APPLICATION_JSON_VALUE,
			  produces = MediaType.APPLICATION_JSON_VALUE
			)
			@Override
			@ResponseBody
			public ResponseEntity<ChatResponse> getChatResponse(
			        @RequestBody ChatRequest body, HttpServletRequest request) {
			    try {
			        if (body == null || body.getMessage() == null || body.getMessage().isBlank()) {
			            return ResponseEntity.badRequest().body(new ChatResponse("메시지가 비었습니다.", null, "BAD_REQUEST"));
			        }
			        String userId = (String) request.getSession().getAttribute("loginUserId"); // null이면 비로그인
			        String answer = routerAnswerService.route(body.getMessage(), userId);
			        return ResponseEntity.ok(new ChatResponse(answer, userId, null));
			    } catch (Exception e) {
			        e.printStackTrace();
			        String sid = (body != null ? body.getSessionId() : null);
			        return ResponseEntity.status(500).body(new ChatResponse("서버 오류: " + e.getMessage(), sid, "SERVER_ERROR"));
			    }
	 }
	
	 @PostMapping(
	     value = "/message-rag.do",
	     consumes = MediaType.APPLICATION_JSON_VALUE,
	     produces = MediaType.APPLICATION_JSON_VALUE
	 )
	 @ResponseBody
	 public ResponseEntity<ChatResponse> messageRag(@RequestBody Map<String, String> body) {
	     String q = body.getOrDefault("message", "").trim();
	     if (q.isEmpty()) {
	         return ResponseEntity.ok(new ChatResponse("질문을 입력해 주세요.", null, "EMPTY_QUESTION"));
	     }
	     var ans = aiAnswerService.answer(q); // RAG 엔진의 결과
	     return ResponseEntity.ok(new ChatResponse(ans.content(), null, null));
	 }
	
	 @RequestMapping(value="/chatbot.do", method={RequestMethod.POST, RequestMethod.GET})
	 public ModelAndView chatbot(HttpServletRequest request, HttpServletResponse response) {
	     HttpSession session = request.getSession();
	     ModelAndView mav = new ModelAndView();
	     String layout = "common/layout";
	     mav.setViewName(layout);
	     mav.addObject("viewName", "/chatbot/chatbot");
	     session.setAttribute("sideMenu", "hidden");
	     return mav;
	 }
}
