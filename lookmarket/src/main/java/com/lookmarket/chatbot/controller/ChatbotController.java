package com.lookmarket.chatbot.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.servlet.ModelAndView;

import com.lookmarket.chatbot.dto.ChatDTO.ChatRequest;
import com.lookmarket.chatbot.dto.ChatDTO.ChatResponse;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


public interface ChatbotController {

    public ResponseEntity<ChatResponse> getChatResponse(@RequestBody ChatRequest body) throws Exception;

    public ModelAndView chatbot(HttpServletRequest request, HttpServletResponse response) throws Exception;
}
