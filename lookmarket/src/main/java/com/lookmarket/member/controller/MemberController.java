package com.lookmarket.member.controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.lookmarket.member.vo.MemberVO;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public interface MemberController {
	public ModelAndView login(@RequestParam("m_id") String m_id, @RequestParam("m_pw") String m_pw, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) throws Exception;
	public String naverCallback(@RequestParam("code") String code, @RequestParam("state") String state, HttpSession session, Model model) throws Exception;
	public ModelAndView logout(HttpServletRequest request, HttpServletResponse response) throws Exception;
	public String overlapped(@RequestParam("m_id") String m_id, HttpServletRequest request, HttpServletResponse response) throws Exception;
	public String mailCheck(@RequestParam("email") String email) throws Exception;
	public String addMember(@ModelAttribute("memberVO") MemberVO _memberVO, RedirectAttributes redirectAttributes, HttpServletRequest request, HttpServletResponse response) throws Exception;
	public ModelAndView memberList(HttpServletRequest request, HttpServletResponse response) throws Exception;
	public ModelAndView memberDetail(HttpServletRequest request, HttpServletResponse response) throws Exception;
	public ModelAndView businessMemberList(HttpServletRequest request, HttpServletResponse response) throws Exception;
	public ModelAndView businessMemberDetail(HttpServletRequest request, HttpServletResponse response) throws Exception;
	public ModelAndView memberForm(HttpServletRequest request, HttpServletResponse response) throws Exception;
	public String findId(@RequestParam("m_name") String m_name, @RequestParam("m_email") String m_email, HttpServletRequest request, HttpServletResponse response) throws Exception;
	public String findPw(@RequestParam("m_id") String m_id, @RequestParam("m_name") String m_name, HttpServletRequest request, HttpServletResponse response) throws Exception;
}
