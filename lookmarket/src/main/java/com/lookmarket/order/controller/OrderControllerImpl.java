package com.lookmarket.order.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Controller("orderController")
@RequestMapping(value="/order")
public class OrderControllerImpl implements OrderController{
	
	@Override
	@RequestMapping(value="/orderResult.do", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView orderResult(HttpServletRequest request, HttpServletResponse response) throws Exception{
		//주문완료
		HttpSession session;
		ModelAndView mav = new ModelAndView();
		String layout = "common/layout";
		mav.setViewName(layout);
		String viewName = (String)request.getAttribute("viewName");
		mav.addObject("viewName", viewName);
		
		session = request.getSession();
		session.setAttribute("sideMenu", "reveal");
		
		return mav;
	}
	
	@Override
	@RequestMapping(value="/orderForm.do", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView form(HttpServletRequest request, HttpServletResponse response) throws Exception{
		//배송정보
		HttpSession session;
		ModelAndView mav = new ModelAndView();
		String layout = "common/layout";
		mav.setViewName(layout);
		String viewName = (String)request.getAttribute("viewName");
		mav.addObject("viewName", viewName);
		
		session = request.getSession();
		session.setAttribute("sideMenu", "reveal");
		
		return mav;
	}

}
