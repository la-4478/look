package com.lookmarket.inquiry.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.lookmarket.inquiry.service.InquiryService;
import com.lookmarket.inquiry.vo.CommentVO;
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
        int inquiry_num = 0;
        int role = memvo.getM_role();
        
        System.out.println("권한 번호 : " + role);

        InquiryVO vo = inquiryService.getInquiryDetail(inquiryId);
        System.out.println("문의번호 : " +vo.getInquiryId());
        System.out.println("답변 : " + vo.getAnswer());
        
        if(role == 3) {
        	inquiry_num = inquiryService.getInquiryNum(inquiryId);
        	System.out.println("관리자측 문의번호 : " + inquiry_num);
        }

		ModelAndView mav = new ModelAndView();
		String layout = "common/layout";
		mav.setViewName(layout);
		String viewName = (String)request.getAttribute("viewName");
		mav.addObject("viewName", viewName);
        mav.addObject("inquiry", vo);
        mav.addObject("number", inquiry_num);
        mav.addObject("role", role);
        return mav;
    }

    // 5. 관리자 답변 등록
    @PostMapping("/answer.do")
    public ModelAndView answerInquiry(@RequestParam("inquiryId") int inquiryId, @RequestParam("answer") String answer,HttpSession session)throws Exception {
        String adminId = (String) session.getAttribute("loginUserId");
        String loginId = (String) session.getAttribute("loginUserId");
        MemberVO memvo = memberService.findMemberById(loginId);
        int role = memvo.getM_role();

        inquiryService.answerInquiry(inquiryId, adminId, role, answer);

        return new ModelAndView("redirect:/inquiry/detail.do?inquiryid=" + inquiryId);
    }

	@Override
	@PostMapping("/insertcomment.do")
	public String insertComment(@ModelAttribute("CommentVO")CommentVO vo, HttpSession session, RedirectAttributes redirectAttributes) throws Exception {
		String loginId = (String) session.getAttribute("current_id"); // 프로젝트 키에 맞춰 통일
	    if (loginId == null || loginId.isBlank()) {
	        redirectAttributes.addFlashAttribute("message", "로그인 후 이용해주세요.");
	        return "redirect:/member/loginForm.do";
	    }
	    MemberVO memberVO = (MemberVO) session.getAttribute("memberInfo");

	    // 1) 필수값 검증
	    if (vo.getB_id() == null) {
	        redirectAttributes.addFlashAttribute("message", "잘못된 접근입니다.(게시글 ID 없음)");
	        return "redirect:/community/list.do";
	    }
	    if (vo.getC_content() == null || vo.getC_content().isBlank()) {
	        redirectAttributes.addFlashAttribute("message", "내용을 입력해주세요.");
	        return "redirect:/community/detail.do?b_id=" + vo.getB_id();
	    }
	    vo.setC_m_id(loginId);

	    // 3) 저장
	    int inserted = inquiryService.insertComment(vo, loginId);
	    if (inserted != 1) {
	        redirectAttributes.addFlashAttribute("message", "댓글 저장 중 오류가 발생했습니다.");
	    } else {
	        redirectAttributes.addFlashAttribute("message", "댓글이 등록되었습니다.");
	    }

	    // 4) PRG 패턴으로 상세로 복귀
	    
	    if(memberVO.getM_role() == 3) {
	    return "redirect:/admin/community/blackBoardDetail.do?b_id=" + vo.getB_id();
	    }
	    return "redirect:/business/blackBoardDetail.do?b_id=" + vo.getB_id();
		}
    
}

