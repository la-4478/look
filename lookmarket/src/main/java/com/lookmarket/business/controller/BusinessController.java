package com.lookmarket.business.controller;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface BusinessController {
	public ModelAndView mypageBusinessInfo(HttpServletRequest request, HttpServletResponse response) throws Exception;
	public ModelAndView businessGoodsList(@RequestParam("category") String category, HttpServletRequest request, HttpServletResponse response) throws Exception;
	public ModelAndView businessOrderList(HttpServletRequest request, HttpServletResponse response) throws Exception;
	public ModelAndView myMemberList(HttpServletRequest request, HttpServletResponse response) throws Exception;
	public ModelAndView myCommunityList(HttpServletRequest request, HttpServletResponse response) throws Exception;
	public ModelAndView myBlackBoardList(HttpServletRequest request, HttpServletResponse response) throws Exception;
	public ModelAndView BusinessMain(HttpServletRequest request, HttpServletResponse response) throws Exception;
}
