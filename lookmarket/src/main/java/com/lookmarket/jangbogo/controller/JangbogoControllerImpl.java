package com.lookmarket.jangbogo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Controller("jangbogoController")
//@RequestMapping(value="/jangbogo")
public class JangbogoControllerImpl implements JangbogoController {
	
	@Override
//	@RequestMapping(value="/goodsList.do", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView goodsList(HttpServletRequest request, HttpServletResponse response) throws Exception{
		//상품 리스트
		HttpSession session;
		ModelAndView mav = new ModelAndView();
		String layout = "common/layout";
		mav.setViewName(layout);
		String viewName = (String)request.getAttribute("viewName");
		mav.addObject("viewName", viewName);
		
		session = request.getSession();
		session.setAttribute("sideMenu", "reveal");
		session.setAttribute("sideMenu_option", "category");
		
		return mav;
	}
	
	@Override
//	@RequestMapping(value="/goodsDetail.do", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView goodsDetail(HttpServletRequest request, HttpServletResponse response) throws Exception{
		//상품상세
		HttpSession session;
		ModelAndView mav = new ModelAndView();
		String layout = "common/layout";
		mav.setViewName(layout);
		String viewName = (String)request.getAttribute("viewName");
		mav.addObject("viewName", viewName);
		
		session = request.getSession();
		session.setAttribute("sideMenu", "reveal");
		session.setAttribute("sideMenu_option", "category");
		
		return mav;
	}
	
	@Override
//	@RequestMapping(value="/goodsUpdateForm.do", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView goodsUpdateForm(HttpServletRequest request, HttpServletResponse response) throws Exception{
		//상품 수정
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
//	@RequestMapping(value="/goodsAddForm.do", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView form(HttpServletRequest request, HttpServletResponse response) throws Exception{
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
