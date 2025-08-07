package com.lookmarket.jangbogo.controller;

import org.springframework.web.servlet.ModelAndView;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface JangbogoController {
	public ModelAndView goodsList(HttpServletRequest request, HttpServletResponse response) throws Exception;
	public ModelAndView goodsDetail(HttpServletRequest request, HttpServletResponse response) throws Exception;
	public ModelAndView goodsUpdateForm(HttpServletRequest request, HttpServletResponse response) throws Exception;
	public ModelAndView form(HttpServletRequest request, HttpServletResponse response) throws Exception;
}
