package com.lookmarket.inquiry.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.lookmarket.inquiry.service.InquiryService;
import com.lookmarket.inquiry.vo.InquiryVO;
import com.lookmarket.member.service.MemberService;
import com.lookmarket.member.vo.MemberVO;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Controller("inquiryController")
@RequestMapping("/inquiry")
public class InquiryControllerImpl implements InquiryController {
	@Autowired
	private InquiryService inquiryService;
	@Autowired
	private MemberService memberService;
	
	 // 1. 문의 작성 페이지 이동
    @GetMapping("/inquiryAddForm.do")
    public ModelAndView newInquiryForm(HttpSession session,HttpServletRequest request, HttpServletResponse response) throws Exception {
		ModelAndView mav = new ModelAndView();
		String layout = "common/layout";
		mav.setViewName(layout);
		String viewName = (String)request.getAttribute("viewName");
		MemberVO memberInfo = (MemberVO)session.getAttribute("memberInfo");
		mav.addObject("viewName", viewName);
		mav.addObject("memberInfo", memberInfo);
        return mav;
    }

    // 2. 문의 등록 처리
    @PostMapping("/insert.do")
    public ModelAndView insertInquiry(@ModelAttribute InquiryVO vo, HttpSession session, HttpServletRequest request) throws Exception {
        String loginId = (String) session.getAttribute("loginUserId");
        
        MemberVO membervo = memberService.findMemberById(loginId);
        
        int role = membervo.getM_role();

        inquiryService.createInquiry(loginId, role, vo);

        ModelAndView mav = new ModelAndView("redirect:/inquiry/inquiryList.do"); 
        return mav;
    }

    // 3. 내 문의 목록 페이지
    @GetMapping("/inquiryList.do")
    public ModelAndView myInquiries(HttpSession session, HttpServletRequest request, HttpServletResponse reseponse) throws Exception {
        String loginId = (String) session.getAttribute("loginUserId");
        
        MemberVO vo = memberService.findMemberById(loginId);
        
        int role = vo.getM_role();
        
        List<InquiryVO> list = inquiryService.getInquiry(loginId, role);
        System.out.println(list);
		ModelAndView mav = new ModelAndView();
		String layout = "common/layout";
		mav.setViewName(layout);
		String viewName = (String)request.getAttribute("viewName");
		mav.addObject("viewName", viewName);
		if(role == 3) {
			session.setAttribute("sideMenu", "reveal");
			session.setAttribute("sideMenu_option", "myPage_admin");
		}else if(role == 1) {
			session.setAttribute("sideMenu", "reveal");
			session.setAttribute("sideMenu_option", "myPage");
		}


        mav.addObject("inquiryList", list);
        return mav;
    }


    // 4. 문의 상세 조회
    @GetMapping("/detail.do")
    public ModelAndView detailInquiry(@RequestParam("inquiryId") int inquiryId, HttpSession session, HttpServletRequest request) throws Exception {
        String loginId = (String) session.getAttribute("loginUserId");
        MemberVO memvo = memberService.findMemberById(loginId);
        
        int role = memvo.getM_role();
        
        System.out.println("권한 번호 : " + role);

        InquiryVO vo = inquiryService.getInquiryDetail(inquiryId);

		ModelAndView mav = new ModelAndView();
		String layout = "common/layout";
		mav.setViewName(layout);
		String viewName = (String)request.getAttribute("viewName");
		mav.addObject("viewName", viewName);
        mav.addObject("inquiry", vo);
        mav.addObject("role", role);
        return mav;
    }

    // 5. 관리자 답변 등록
    @PostMapping("/answer.do")
    public ModelAndView answerInquiry(@RequestParam("inquiryId") long inquiryId, @RequestParam("answer") String answer,HttpSession session)throws Exception {
        String adminId = (String) session.getAttribute("loginUserId");
        int role = (int) session.getAttribute("loginUserRole");

        inquiryService.answerInquiry(inquiryId, adminId, role, answer);

        return new ModelAndView("redirect:/inquiry/" + inquiryId);
    }
}

