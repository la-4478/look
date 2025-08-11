package com.lookmarket.member.controller;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.lookmarket.mail.service.MailService;
import com.lookmarket.member.service.MemberService;
import com.lookmarket.member.service.NaverLoginService;
import com.lookmarket.member.vo.BusinessVO;
import com.lookmarket.member.vo.MemberVO;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Controller("memberController")
@RequestMapping(value="/member")
public class MemberControllerImpl implements MemberController {
	@Autowired
	private MemberService memberService;
	@Autowired
	private MemberVO memberVO;
	@Autowired
	private MailService mailService;
	@Autowired
	private NaverLoginService naverLoginService;
	
	@Override
	@RequestMapping(value="/login.do", method=RequestMethod.POST)
	public ModelAndView login(@RequestParam("m_id") String m_id, @RequestParam("m_pw") String m_pw, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) throws Exception{
		//로그인
		ModelAndView mav = new ModelAndView();
		String check_id = memberService.overlapped(m_id);
		
		if(check_id.equals("true")) {
			memberVO = memberService.login(m_id, m_pw);		
			
			if(memberVO != null && memberVO.getM_outdate() != null) {
				//탈퇴회원일 경우
	            redirectAttributes.addFlashAttribute("message", "계정을 복구합니다.");
	            
	            memberService.reSignUp(m_id);
				mav.setViewName("redirect:/member/loginForm.do");
				return mav;
			}			
			
			if(memberVO != null && memberVO.getM_id() != null) {
				HttpSession session = request.getSession();
				session = request.getSession();
				session.setAttribute("current_id", m_id);
				session.setAttribute("isLogOn", true);
				session.setAttribute("memberInfo",memberVO);
				
				String action = (String)session.getAttribute("action");
				if(action != null && action.equals("/order/orderEachGoods.do")) {
					//추가 필요
					mav.setViewName("forward:" + action);
				}else {
					mav.setViewName("redirect:/main/sijangbajoMain.do");
				}
				
			}else {
	            redirectAttributes.addFlashAttribute("message", "비밀번호가 틀렸습니다. 다시 로그인해주세요.");
				mav.setViewName("redirect:/member/loginForm.do");
			}
	
		}else {
	        redirectAttributes.addFlashAttribute("message", "존재하지 않는 아이디 입니다. 다시 로그인해주세요.");
			mav.setViewName("redirect:/member/loginForm.do");
		}
		
		return mav;
	}
	
	@Override
	@RequestMapping(value = "/naverCallback.do", method = { RequestMethod.GET, RequestMethod.POST })
	public String naverCallback(@RequestParam("code") String code, @RequestParam("state") String state, HttpSession session, Model model) throws Exception {
		MemberVO naverMember = naverLoginService.getNaverUserInfo(code, state);
		
        if (naverMember != null) {
        	if(naverMember.getM_outdate() != null) {
        		//재가입
        		memberService.reSignUp(naverMember.getM_id());
                model.addAttribute("message", "계정을 복구합니다.");
                return "forward:/member/loginForm.do";

        	}else {
        	
        		session.setAttribute("isLogOn", true);
        		session.setAttribute("memberInfo", naverMember);
        		session.setAttribute("current_id", naverMember.getM_id());
        		
        		return "redirect:/main/sijangbajoMain.do";
        	}
        } else {
            model.addAttribute("message", "네이버 로그인에 실패했습니다.");
            return "forward:/member/loginForm.do";
        }
	}
	
	@Override
	@RequestMapping(value="/logout.do", method={RequestMethod.POST, RequestMethod.GET})
	public ModelAndView logout(HttpServletRequest request, HttpServletResponse response) throws Exception{
		//로그아웃
		ModelAndView mav = new ModelAndView();
		HttpSession session = request.getSession();
		session.setAttribute("isLogOn", false);
		session.removeAttribute("memberInfo");
		mav.setViewName("redirect:/main/sijangbajoMain.do");
		session.invalidate();
		
		return mav;
	}
	
	@Override
	@RequestMapping(value="/findId.do", method=RequestMethod.POST)
	@ResponseBody
	public String findId(@RequestParam("m_name") String m_name, @RequestParam("m_email") String m_email, HttpServletRequest request, HttpServletResponse response) throws Exception{
		//아이디 찾기
		//AJAX 사용
		String result = memberService.findId(m_name, m_email);
	    if (result == null || result.trim().isEmpty() || result.equals("null")) {
	        return "NOT_FOUND";
	    }
	    
		return result;
	}
	
	@Override
	@RequestMapping(value="/findPw.do", method=RequestMethod.POST)
	@ResponseBody
	public String findPw(@RequestParam("m_id") String m_id, @RequestParam("m_name") String m_name, HttpServletRequest request, HttpServletResponse response) throws Exception{
		//비밀번호 찾기
		//AJAX 사용
		String result = memberService.findPw(m_id, m_name);
	    if (result == null || result.trim().isEmpty()) {
	        return "NOT_FOUND";
	    }
	    
		return result;
	}
	
	@Override
	@RequestMapping(value="/overlapped.do", method=RequestMethod.POST)
	@ResponseBody
	public String overlapped(@RequestParam("m_id") String m_id, HttpServletRequest request, HttpServletResponse response) throws Exception{
		//아이디 중복검사
		//AJAX 사용
		String result = memberService.overlapped(m_id);
		return result;
	}
	
	@Override
	@RequestMapping(value="/mailCheck.do", method=RequestMethod.POST)
	@ResponseBody
	public String mailCheck(@RequestParam("email") String email) throws Exception{
		//이메일 인증
		
		String overlappedByEmail = memberService.overlappedByEmail(email);
		if(overlappedByEmail.equals("true")) {
			return overlappedByEmail;
		}else {
			String authCode = mailService.joinEmail(email);
			return authCode;
		}
	}
	
	@Override
	@RequestMapping(value="/addMember.do", method=RequestMethod.POST)
	public String addMember(@ModelAttribute("memberVO") MemberVO _memberVO, RedirectAttributes redirectAttributes, HttpServletRequest request, HttpServletResponse response) throws Exception{
		//회원가입
		response.setContentType("text/html; charset=UTF-8");
		request.setCharacterEncoding("UTF-8");
		
		String message = null;
		ResponseEntity resEntity = null;
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "text/html; charset=UTF-8");
		
		//성별
		int gender = _memberVO.getM_gender();
		if(gender == 1 || gender == 3) {
			_memberVO.setM_gender(1);
		}else {
			_memberVO.setM_gender(2);
		}
		
		//회원등급
		_memberVO.setM_role(1);
		
		//가입일
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date now = new Date();
		String m_joindate = sdf.format(now);
		
		_memberVO.setM_joindate(m_joindate);
		
		try {
			memberService.addMember(_memberVO);
			
			redirectAttributes.addFlashAttribute("message", "회원가입이 완료되었습니다. 로그인해주세요.");
			return "redirect:/member/loginForm.do";
		}catch(Exception e) {
			
			redirectAttributes.addFlashAttribute("message", "작업 중 오류가 발생했습니다. 다시 시도해주세요.");
			return "redirect:/member/memberForm.do";
		}
	}
	
	
	@Override
	@RequestMapping(value="/memberList.do", method=RequestMethod.POST)
	public ModelAndView memberList(HttpServletRequest request, HttpServletResponse response) throws Exception{
		ModelAndView mav = new ModelAndView();
		String layout = "common/layout";
		mav.setViewName(layout);
		String viewName = (String)request.getAttribute("viewName");
		mav.addObject("viewName", viewName);
		
		return mav;
	}
	
	@Override
	@RequestMapping(value="/memberDetail.do", method=RequestMethod.POST)
	public ModelAndView memberDetail(HttpServletRequest request, HttpServletResponse response) throws Exception{
		ModelAndView mav = new ModelAndView();
		String layout = "common/layout";
		mav.setViewName(layout);
		String viewName = (String)request.getAttribute("viewName");
		mav.addObject("viewName", viewName);
		
		return mav;		
	}
	
	@Override
	@RequestMapping(value="/businessMemberList.do", method=RequestMethod.POST)
	public ModelAndView businessMemberList(HttpServletRequest request, HttpServletResponse response) throws Exception{
		ModelAndView mav = new ModelAndView();
		String layout = "common/layout";
		mav.setViewName(layout);
		String viewName = (String)request.getAttribute("viewName");
		mav.addObject("viewName", viewName);
		
		return mav;		
	}
	
	@Override
	@RequestMapping(value="/businessMemberDetail.do", method=RequestMethod.POST)
	public ModelAndView businessMemberDetail(HttpServletRequest request, HttpServletResponse response) throws Exception{
		ModelAndView mav = new ModelAndView();
		String layout = "common/layout";
		mav.setViewName(layout);
		String viewName = (String)request.getAttribute("viewName");
		mav.addObject("viewName", viewName);
		
		return mav;		
	}
	
	@Override
	@RequestMapping(value={"/memberForm.do", "/loginForm.do", "/findIdForm.do", "/findPwForm.do", "/memberSelect.do", "/memberList.do", "/businessForm.do" }, method={RequestMethod.POST, RequestMethod.GET})
	public ModelAndView memberForm(HttpServletRequest request, HttpServletResponse response) throws Exception{
		//로그인창, 회원가입창 출력
		HttpSession session;
		ModelAndView mav = new ModelAndView();
		String layout = "common/layout";
		mav.setViewName(layout);
		String viewName = (String)request.getAttribute("viewName");
		mav.addObject("viewName", viewName);
		session = request.getSession();
		session.setAttribute("sideMenu", "hidden");
		
		return mav;
		
	}

	@Override
	@RequestMapping(value="/addBusiness.do", method={RequestMethod.POST,RequestMethod.GET})
	public String addBusiness(@ModelAttribute("MemberVO") MemberVO memberVO, RedirectAttributes rd, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		//회원가입
				response.setContentType("text/html; charset=UTF-8");
				request.setCharacterEncoding("UTF-8");
				
				String message = null;
				ResponseEntity resEntity = null;
				HttpHeaders responseHeaders = new HttpHeaders();
				responseHeaders.add("Content-Type", "text/html; charset=UTF-8");
				
				//성별
				int gender = memberVO.getM_gender();
				if(gender == 1 || gender == 3) {
					memberVO.setM_gender(1);
				}else {
					memberVO.setM_gender(2);
				}
				String m_id = request.getParameter("m_id");
				String bm_name = request.getParameter("bm_name");
				String bm_reg_num = request.getParameter("bm_reg_num");
				String bm_type = request.getParameter("bm_type");
				
				
				//회원등급
				memberVO.setM_role(3);
				
				//가입일
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				Date now = new Date();
				String m_joindate = sdf.format(now);
				
				memberVO.setM_joindate(m_joindate);
				BusinessVO businessVO = new BusinessVO();				
				businessVO.setM_id(m_id);
				businessVO.setBm_name(bm_name);
				businessVO.setBm_reg_num(bm_reg_num);
				businessVO.setBm_type(bm_type);
				
				
				try {
					memberService.addMember(memberVO);
					memberService.addbusinessMember(businessVO);
					rd.addFlashAttribute("message", "회원가입이 완료되었습니다. 로그인해주세요.");
					return "redirect:/member/loginForm.do";
				}catch(Exception e) {
					e.printStackTrace(); // 콘솔에 예외 클래스, 메시지, 발생 위치까지 전부
				    rd.addFlashAttribute("message", "오류 발생: " + e.getMessage());
					return "redirect:/member/businessForm.do";
	}
	}
}
