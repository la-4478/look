package com.lookmarket.inquiry.controller;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.lookmarket.inquiry.vo.CommentVO;
import com.lookmarket.inquiry.vo.InquiryVO;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public interface InquiryController {
	 public ModelAndView newInquiryForm(HttpSession session,HttpServletRequest request, HttpServletResponse response) throws Exception;
	 public ModelAndView insertInquiry(InquiryVO vo, HttpSession session, HttpServletRequest request) throws Exception;
	 public ModelAndView myInquiries(HttpSession session, HttpServletRequest request, HttpServletResponse reseponse) throws Exception;
	 public ModelAndView detailInquiry(int inquiryId, HttpSession session, HttpServletRequest request) throws Exception;
	 public ModelAndView answerInquiry(int inquiryId, String answer,HttpSession session)throws Exception;
	 public String insertComment(CommentVO vo, HttpSession session, RedirectAttributes redirectAttributes) throws Exception;
}
