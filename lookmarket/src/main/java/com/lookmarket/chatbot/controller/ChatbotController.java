package com.lookmarket.chatbot.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.lookmarket.chatbot.service.ChatbotService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/chatbot")
public class ChatbotController {

    @Autowired
    private ChatbotService chatbotService;

    @PostMapping("/ask")
    @ResponseBody
    public String ask(@RequestParam("message") String message) {
        return chatbotService.askChatGPT(message);
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