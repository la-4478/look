package com.lookmarket.event.controller;

import org.springframework.web.servlet.ModelAndView;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface EventController {
	public ModelAndView promotionList(HttpServletRequest request, HttpServletResponse response) throws Exception;
	public ModelAndView promotionDetail(HttpServletRequest request, HttpServletResponse response) throws Exception;
	public ModelAndView todayPick(HttpServletRequest request, HttpServletResponse response) throws Exception;
}
