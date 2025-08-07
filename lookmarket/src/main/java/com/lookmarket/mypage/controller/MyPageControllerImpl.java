package com.lookmarket.mypage.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.lookmarket.common.base.BaseController;
import com.lookmarket.community.vo.ReviewVO;
import com.lookmarket.mypage.service.MyPageService;
import com.lookmarket.mypage.vo.MyPageVO;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Controller("myPageController")
@RequestMapping(value="/mypage")
public class MyPageControllerImpl extends BaseController implements MyPageController{
	@Autowired
	private MyPageService myPageService;
	@Autowired
	private MyPageVO myPageVO;
	
	//사용자	
	@Override
	@RequestMapping(value="/mypageInfo.do", method=RequestMethod.GET)
	public ModelAndView myPageInfo(HttpServletRequest request, HttpServletResponse response)  throws Exception{
		//마이페이지 첫화면(사용자)
		HttpSession session;
		ModelAndView mav = new ModelAndView();
		String layout = "common/layout";
		mav.setViewName(layout);
		String viewName = (String)request.getAttribute("viewName");
		mav.addObject("viewName", viewName);
		
		session = request.getSession();
		String current_id = (String)session.getAttribute("current_id");
		
		myPageVO = myPageService.getMyPageInfo(current_id);
		
		String m_email = myPageVO.getM_email();
		
		if (m_email != null && m_email.contains("@")) {
		    String[] parts = m_email.split("@", 2);
		    String m_email_id = parts[0];
		    String m_email_domain = parts[1];
		    myPageVO.setM_email_id(m_email_id);
		    myPageVO.setM_email_domain(m_email_domain);
		} else {
		    // 이메일 형식이 올바르지 않을 경우 처리 (필요하면)
		    myPageVO.setM_email_id("");
		    myPageVO.setM_email_domain("");
		}
		
		mav.addObject("myPageInfo", myPageVO);
		session.setAttribute("sideMenu", "reveal");
		session.setAttribute("sideMenu_option", "myPage");
		
		return mav;
	}
	
	@Override
	@RequestMapping(value="/listMyOrderHistory.do", method=RequestMethod.GET)
	public ModelAndView listMyOrderHistory(HttpServletRequest request, HttpServletResponse response)  throws Exception{
		//주문내역(사용자)
		HttpSession session;
		ModelAndView mav = new ModelAndView();
		String layout = "common/layout";
		mav.setViewName(layout);
		String viewName = (String)request.getAttribute("viewName");
		mav.addObject("viewName", viewName);
		
		session = request.getSession();
		session.setAttribute("sideMenu", "reveal");
		session.setAttribute("sideMenu_option", "myPage");
		
		return mav;
	}
	
	@Override
	@RequestMapping(value="/myOrderDetail.do", method=RequestMethod.GET)
	public ModelAndView myOrderDetail(HttpServletRequest request, HttpServletResponse response)  throws Exception{
		//주문상세내역(사용자)
		HttpSession session;
		ModelAndView mav = new ModelAndView();
		String layout = "common/layout";
		mav.setViewName(layout);
		String viewName = (String)request.getAttribute("viewName");
		mav.addObject("viewName", viewName);
		
		session = request.getSession();
		session.setAttribute("sideMenu", "reveal");
		session.setAttribute("sideMenu_option", "myPage");
		
		return mav;
	}
	
//	@Override
//	@RequestMapping(value="/.do", method=RequestMethod.GET)
//	public ModelAndView myPageInfo(HttpServletRequest request, HttpServletResponse response)  throws Exception{
//		//배송조회(일단 보류)
//		
//	}
	
	@Override
	@RequestMapping(value="/myWishList.do", method=RequestMethod.GET)
	public ModelAndView myWishList(HttpServletRequest request, HttpServletResponse response)  throws Exception{
		//찜(사용자)
		HttpSession session;
		ModelAndView mav = new ModelAndView();
		String layout = "common/layout";
		mav.setViewName(layout);
		String viewName = (String)request.getAttribute("viewName");
		mav.addObject("viewName", viewName);
		
		session = request.getSession();
		session.setAttribute("sideMenu", "reveal");
		session.setAttribute("sideMenu_option", "myPage");
		
		return mav;
	}
	
	@Override
	@RequestMapping(value="/myCommunity.do", method=RequestMethod.GET)
	public ModelAndView myCommunity(HttpServletRequest request, HttpServletResponse response)  throws Exception{
		//커뮤니티(사용자)
		HttpSession session = request.getSession();
		ModelAndView mav = new ModelAndView();
		String layout = "common/layout";
		
		String m_id = (String)session.getAttribute("current_id");
		List<ReviewVO> communityList = myPageService.selectMyCommunityList(m_id);
		
		mav.setViewName(layout);
		String viewName = (String)request.getAttribute("viewName");
		mav.addObject("viewName", viewName);
		mav.addObject("communityList", communityList);
		
		session = request.getSession();
		session.setAttribute("sideMenu", "reveal");
		session.setAttribute("sideMenu_option", "myPage");
		
		return mav;
	}
	
	@Override
	@RequestMapping(value="/updateMyInfo.do", method=RequestMethod.POST)
	public ModelAndView updateMyInfo(@ModelAttribute MyPageVO myPageVO, HttpSession session, HttpServletResponse response, RedirectAttributes redirectAttributes) throws Exception {
		ModelAndView mav = new ModelAndView();
		
		String m_email_id = myPageVO.getM_email_id();
		String m_email_domain =myPageVO.getM_email_domain();
		myPageVO.setM_email(m_email_id + "@" + m_email_domain);

	    int result = myPageService.updateMyInfo(myPageVO);

	    if(result == 1) {
	    	redirectAttributes.addFlashAttribute("message", "정보가 수정되었습니다.");
	    	mav.setViewName("redirect:/mypage/mypageInfo.do");
	    }else {
	    	redirectAttributes.addFlashAttribute("message", "정보가 수정에 오류가 발생하였습니다. 다시시도해주세요.");
	    	mav.setViewName("redirect:/mypage/mypageInfo.do");
	    }
	    return mav;
	}
	
	@RequestMapping(value="/deleteMyInfo.do", method=RequestMethod.POST)
	public String deleteMember(@RequestParam("m_id") String m_id, HttpSession session, RedirectAttributes rttr) {
	    try {
	    	myPageService.deleteMember(m_id);
	    	
	    	session.invalidate();
	        rttr.addFlashAttribute("message", "회원 탈퇴가 완료되었습니다.\n일주일 후 계정 정보가 완전히 삭제됩니다.");
	        return "redirect:/main/sijangbajoMain.do";  // 메인 페이지로 이동
	    } catch (Exception e) {
	    	e.printStackTrace(); // 어떤 예외인지 로그에 자세히 출력
	        rttr.addFlashAttribute("message", "탈퇴 처리 중 오류가 발생했습니다.");
	        return "redirect:/mypage/mypageInfo.do";
	    }
	}

}
