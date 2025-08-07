package com.lookmarket.admin.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Controller("adminController")
@RequestMapping("/admin")
public class AdminControllerImpl implements AdminController{
	
	//viewName 수정 필요
	@Override
	@RequestMapping(value="/mypage/mypageAdminInfo.do", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView mypageAdminInfo(HttpServletRequest request, HttpServletResponse response) throws Exception{
		//내정보
		HttpSession session;
		ModelAndView mav = new ModelAndView();
		String layout = "common/layout";
		mav.setViewName(layout);
		String viewName = (String)request.getAttribute("viewName");
		mav.addObject("viewName", viewName);
		
		session = request.getSession();
		session.setAttribute("sideMenu", "reveal");
		session.setAttribute("sideMenu_option", "myPage_admin");
		
		return mav;
	}
	
	@Override
	@RequestMapping(value="/jangbogo/allGoodsList.do", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView allGoodsList(HttpServletRequest request, HttpServletResponse response) throws Exception{
		//상품정보
		HttpSession session;
		ModelAndView mav = new ModelAndView();
		String layout = "common/layout";
		mav.setViewName(layout);
		String viewName = (String)request.getAttribute("viewName");
		mav.addObject("viewName", viewName);
		
		session = request.getSession();
		session.setAttribute("sideMenu", "reveal");
		session.setAttribute("sideMenu_option", "myPage_admin");
		
		return mav;
	}
	
	@Override
	@RequestMapping(value="/order/allOrderList.do", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView allOrderList(HttpServletRequest request, HttpServletResponse response) throws Exception{
		//주문정보
		HttpSession session;
		ModelAndView mav = new ModelAndView();
		String layout = "common/layout";
		mav.setViewName(layout);
		String viewName = (String)request.getAttribute("viewName");
		mav.addObject("viewName", viewName);
		
		session = request.getSession();
		session.setAttribute("sideMenu", "reveal");
		session.setAttribute("sideMenu_option", "myPage_admin");
		
		return mav;
	}
	
	@Override
	@RequestMapping(value="/member/allMemberList.do", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView allMemberList(HttpServletRequest request, HttpServletResponse response) throws Exception{
		//회원정보
		HttpSession session;
		ModelAndView mav = new ModelAndView();
		String layout = "common/layout";
		mav.setViewName(layout);
		String viewName = (String)request.getAttribute("viewName");
		mav.addObject("viewName", viewName);
		
		session = request.getSession();
		session.setAttribute("sideMenu", "reveal");
		session.setAttribute("sideMenu_option", "myPage_admin");
		
		return mav;
	}
	
	
	@Override
	@RequestMapping(value="/mypage/allBlackBoardList.do", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView allBlackBoardList(HttpServletRequest request, HttpServletResponse response) throws Exception{
		//사장님고충방 리스트
		HttpSession session;
		ModelAndView mav = new ModelAndView();
		String layout = "common/layout";
		mav.setViewName(layout);
		String viewName = (String)request.getAttribute("viewName");
		mav.addObject("viewName", viewName);
		
		session = request.getSession();
		session.setAttribute("sideMenu", "reveal");
		session.setAttribute("sideMenu_option", "myPage_admin");
		
		return mav;
	}	
	
	@Override
	@RequestMapping(value="/accountList.do", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView accountList(HttpServletRequest request, HttpServletResponse response) throws Exception{
		//회계리스트
		HttpSession session;
		ModelAndView mav = new ModelAndView();
		String layout = "common/layout";
		mav.setViewName(layout);
		String viewName = (String)request.getAttribute("viewName");
		mav.addObject("viewName", viewName);
		
		session = request.getSession();
		session.setAttribute("sideMenu", "reveal");
		session.setAttribute("sideMenu_option", "myPage_admin");
		
		return mav;
	}
	
	@Override
	@RequestMapping(value="/accountDetail.do", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView accountDetail(HttpServletRequest request, HttpServletResponse response) throws Exception{
		//회계상세정보
		HttpSession session;
		ModelAndView mav = new ModelAndView();
		String layout = "common/layout";
		mav.setViewName(layout);
		String viewName = (String)request.getAttribute("viewName");
		mav.addObject("viewName", viewName);
		
		session = request.getSession();
		session.setAttribute("sideMenu", "reveal");
		session.setAttribute("sideMenu_option", "myPage_admin");
		
		return mav;
	}
}
