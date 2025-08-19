package com.lookmarket.mypage.controller;

import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.lookmarket.mypage.vo.MyPageVO;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public interface MyPageController {
	public ModelAndView myPageInfo(HttpServletRequest request, HttpServletResponse response)  throws Exception;
	public ModelAndView listMyOrderHistory(HttpServletRequest request, HttpServletResponse response)  throws Exception;
	public ModelAndView myOrderDetail(@RequestParam("oId") int oId, HttpServletRequest request, HttpServletResponse response)  throws Exception;
	public ModelAndView myWishList(HttpServletRequest request, HttpServletResponse response)  throws Exception;
	public ModelAndView myCommunity(HttpServletRequest request, HttpServletResponse response)  throws Exception;
	public ModelAndView updateMyInfo(@ModelAttribute MyPageVO myPageVO, HttpSession session, HttpServletResponse response, RedirectAttributes redirectAttributes) throws Exception;
	public ModelAndView listMyDelivery(HttpServletRequest request, HttpServletResponse response) throws Exception;
}
