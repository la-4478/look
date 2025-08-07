package com.lookmarket.event.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Controller("eventController")
@RequestMapping(value="/event")
public class EventControllerImpl implements EventController{
	
	@Override
	@RequestMapping(value="/promotionList.do", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView promotionList(HttpServletRequest request, HttpServletResponse response) throws Exception{
		//이벤트 리스트 출력
		HttpSession session;
		ModelAndView mav = new ModelAndView();
		String layout = "common/layout";
		mav.setViewName(layout);
		String viewName = (String)request.getAttribute("viewName");
		mav.addObject("viewName", viewName);
		
		session = request.getSession();

		session.setAttribute("sideMenu", "reveal");
		session.setAttribute("sideMenu_option", "event");
		
		return mav;
	}
	
	@Override
	@RequestMapping(value="/promotionDetail.do", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView promotionDetail(HttpServletRequest request, HttpServletResponse response) throws Exception{
		//이벤트 상세정보
		HttpSession session;
		ModelAndView mav = new ModelAndView();
		String layout = "common/layout";
		mav.setViewName(layout);
		String viewName = (String)request.getAttribute("viewName");
		mav.addObject("viewName", viewName);
		
		session = request.getSession();
		session.setAttribute("sideMenu", "reveal");
		session.setAttribute("sideMenu_option", "event");
		
		return mav;
	}
	
	@Override
	@RequestMapping(value="/todayPick.do", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView todayPick(HttpServletRequest request, HttpServletResponse response) throws Exception{
		//오늘의 추천
		HttpSession session;
		ModelAndView mav = new ModelAndView();
		String layout = "common/layout";
		mav.setViewName(layout);
		String viewName = (String)request.getAttribute("viewName");
		mav.addObject("viewName", viewName);
		
		session = request.getSession();
		session.setAttribute("sideMenu", "reveal");
		session.setAttribute("sideMenu_option", "event");
		
		return mav;
	}
	
	@RequestMapping(value="/promotionAddForm.do", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView promotionAddForm(HttpServletRequest request, HttpServletResponse response) throws Exception{
		//이벤트 리스트 출력
		HttpSession session;
		ModelAndView mav = new ModelAndView();
		String layout = "common/layout";
		mav.setViewName(layout);
		String viewName = (String)request.getAttribute("viewName");
		mav.addObject("viewName", viewName);
		
		session = request.getSession();

		session.setAttribute("sideMenu", "reveal");
		session.setAttribute("sideMenu_option", "event");
		
		return mav;
	}
}
