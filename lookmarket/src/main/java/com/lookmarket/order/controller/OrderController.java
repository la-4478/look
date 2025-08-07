package com.lookmarket.order.controller;

import org.springframework.web.servlet.ModelAndView;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface OrderController {
	public ModelAndView orderResult(HttpServletRequest request, HttpServletResponse response) throws Exception;
	public ModelAndView form(HttpServletRequest request, HttpServletResponse response) throws Exception;
}
