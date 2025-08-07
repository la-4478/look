package com.lookmarket.main.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.lookmarket.common.base.BaseController;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Controller("mainController")
@RequestMapping(value="/main")
public class MainController extends BaseController{
	
	@RequestMapping(value="/sijangbajoMain.do", method= {RequestMethod.POST, RequestMethod.GET})
	public ModelAndView sijangbajoMain(HttpServletRequest request, HttpServletResponse response) throws Exception{
		HttpSession session;
		ModelAndView mav = new ModelAndView();
		String layout = "common/layout";
		mav.setViewName(layout);
		String viewName = (String)request.getAttribute("viewName");
		mav.addObject("viewName", viewName);
		session = request.getSession();
		session.setAttribute("sideMenu", "hidden");
		session.setAttribute("pageType", "sijangbajo");
		return mav;
	}
	
	@RequestMapping(value="/jangbogoMain.do", method= {RequestMethod.POST, RequestMethod.GET})
	public ModelAndView jangbogoMain(HttpServletRequest request, HttpServletResponse response) throws Exception{
		HttpSession session;
		ModelAndView mav = new ModelAndView();
		String layout = "common/layout";
		mav.setViewName(layout);
		String viewName = (String)request.getAttribute("viewName");
		mav.addObject("viewName", viewName);
		session = request.getSession();
		session.setAttribute("sideMenu", "hidden");
		session.setAttribute("pageType", "jangbogo");
		return mav;
	}
}
