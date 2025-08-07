package com.lookmarket.goods.controller;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface GoodsController {
	public ModelAndView goodsList(@RequestParam("category") String category, HttpServletRequest request, HttpServletResponse response) throws Exception;
	public ModelAndView goodsDetail(@RequestParam("g_id") int g_id, HttpServletRequest request, HttpServletResponse response) throws Exception;

	public ModelAndView goodsAddForm(HttpServletRequest request, HttpServletResponse response) throws Exception;
	public ModelAndView goodsUpdateForm(HttpServletRequest request, HttpServletResponse response) throws Exception;

}
