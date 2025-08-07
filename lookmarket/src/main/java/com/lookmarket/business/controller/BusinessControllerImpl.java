package com.lookmarket.business.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Controller("business")
@RequestMapping(value="/business")
public class BusinessControllerImpl implements BusinessController{

	//viewName 수정 필요
	@Override
	@RequestMapping(value="/mypage/mypageBusinessInfo.do", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView mypageBusinessInfo(HttpServletRequest request, HttpServletResponse response) throws Exception{
		//사업자 정보
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
	@RequestMapping(value="/jangbogo/businessGoodsList.do", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView businessGoodsList(HttpServletRequest request, HttpServletResponse response) throws Exception{
		//사업자 상품 리스트
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
	@RequestMapping(value="/order/businessOrderList.do", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView businessOrderList(HttpServletRequest request, HttpServletResponse response) throws Exception{
		//주문 리스트
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
	@RequestMapping(value="/member/myMemberList.do", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView myMemberList(HttpServletRequest request, HttpServletResponse response) throws Exception{
		//회원 리스트
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
	@RequestMapping(value="/mypage/myCommunityList.do", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView myCommunityList(HttpServletRequest request, HttpServletResponse response) throws Exception{
		//커뮤니티
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
	@RequestMapping(value="/mypage/myBlackBoardList.do", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView myBlackBoardList(HttpServletRequest request, HttpServletResponse response) throws Exception{
		//사장님 고충방
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
}
