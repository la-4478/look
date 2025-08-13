package com.lookmarket.order.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.servlet.ModelAndView;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public interface OrderController {
	public ModelAndView orderResult(HttpServletRequest request, HttpServletResponse response) throws Exception;
	public ModelAndView form(HttpServletRequest request, HttpServletResponse response) throws Exception;
	public ModelAndView payComplete(HttpServletRequest request, HttpServletResponse response) throws Exception;
	public ResponseEntity<Map<String, Object>> placeOrderAjax(HttpServletRequest request, HttpSession session) throws Exception;
}
