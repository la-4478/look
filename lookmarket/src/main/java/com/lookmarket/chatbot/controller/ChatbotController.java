package com.lookmarket.chatbot.controller;

import com.lookmarket.chatbot.service.ChatbotService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/chatbot")
public class ChatbotController {

    @Autowired
    private ChatbotService chatbotService;

    @PostMapping("/message.do")
    @ResponseBody
    public ResponseEntity<String> getChatResponse(@RequestParam("message") String message) {
        try {
            String result = chatbotService.getChatbotResponse(message);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error occurred: " + e.getMessage());
        }
    }
    
	@RequestMapping(value="/chatbot.do", method= {RequestMethod.POST, RequestMethod.GET})
	public ModelAndView chatbot(HttpServletRequest request, HttpServletResponse response) throws Exception{
		HttpSession session;
		ModelAndView mav = new ModelAndView();
		String layout = "common/layout";
		mav.setViewName(layout);
		mav.addObject("viewName", "/chatbot/chatbot");
		
		session = request.getSession();
		session.setAttribute("sideMenu", "hidden");
		
		return mav;
	}
}