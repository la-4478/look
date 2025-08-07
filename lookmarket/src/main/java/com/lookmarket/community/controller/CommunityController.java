package com.lookmarket.community.controller;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface CommunityController {
	public ModelAndView communityList(HttpServletRequest request, HttpServletResponse response)  throws Exception;
	public ModelAndView communityDetail(@RequestParam("r_id") int r_id, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes)  throws Exception;
	public ModelAndView communityUpdateForm(HttpServletRequest request, HttpServletResponse response)  throws Exception;
	public ModelAndView blackBoardList(HttpServletRequest request, HttpServletResponse response)  throws Exception;
	public ModelAndView blackBoardDetail(HttpServletRequest request, HttpServletResponse response)  throws Exception;
	public ModelAndView blackBoardUpdateForm(HttpServletRequest request, HttpServletResponse response)  throws Exception;
	public ModelAndView communityAddForm(HttpServletRequest request, HttpServletResponse response) throws Exception;
	public ModelAndView blackBoardAddForm(HttpServletRequest request, HttpServletResponse response) throws Exception;
}
