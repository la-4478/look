package com.lookmarket.admin.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.lookmarket.member.service.MemberService;
import com.lookmarket.member.vo.MemberApprovalDTO;
import com.lookmarket.member.vo.MemberVO;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Controller("adminController")
@RequestMapping("/admin")
public class AdminControllerImpl implements AdminController{
	@Autowired
	private MemberService memberService;
	String layout ="common/layout";
	
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

	@Override
	@RequestMapping(value="/ApprovalList.do", method= {RequestMethod.GET, RequestMethod.POST})
	public ModelAndView approvalForm(HttpServletRequest request, HttpServletResponse response) throws Exception {
		System.out.println("ApprovalList.do 컨트롤러 진입");
		HttpSession session = request.getSession();
		String viewName = (String)request.getAttribute("viewName");
		ModelAndView mav = new ModelAndView();
		mav.setViewName(layout);
		mav.addObject("viewName", viewName);
		
		session.setAttribute("sideMenu", "reavel");
		session.setAttribute("sideMenu_option", "myPage_admin");
		
		List<MemberVO> m_role = memberService.findbusinessMember(2);

		List<MemberApprovalDTO> approvalData = new ArrayList<>();
		for (MemberVO member : m_role) {
		    MemberApprovalDTO dto = new MemberApprovalDTO();
		    dto.setMember(member);
		    dto.setPendingList(memberService.findbusinessMember2(member.getM_id()));
		    approvalData.add(dto);
		}

		mav.addObject("approvalList", approvalData);
		
		return mav;
	}	
	
	@Override
	@RequestMapping(value="/approveBusiness.do", method={RequestMethod.GET, RequestMethod.POST})
	public String approveBusiness(@RequestParam("m_id") String m_id, RedirectAttributes ra) throws Exception {
	    memberService.approve(m_id);
	    ra.addFlashAttribute("msg", "사업자 승인 완료: " + m_id);
	    return "redirect:/admin/ApprovalList.do";
	}
	
	@Override
	@RequestMapping(value="/rejectBusiness.do", method={RequestMethod.GET, RequestMethod.POST})
	public String rejectBusiness(@RequestParam("m_id") String m_id, RedirectAttributes ra)throws Exception{
		memberService.reject(m_id);
	    ra.addFlashAttribute("msg", "사업자 승인 거부: " + m_id);
	    return "redirect:/admin/ApprovalList.do";
	}
	
}
