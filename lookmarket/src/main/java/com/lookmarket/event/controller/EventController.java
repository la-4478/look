package com.lookmarket.event.controller;

import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.lookmarket.event.vo.EventPostVO;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface EventController {
	public ModelAndView promotionList(HttpServletRequest request, HttpServletResponse response) throws Exception;
	public ModelAndView promotionDetail(@RequestParam(value="postId", required=true) Integer postId,
            HttpServletRequest request, HttpServletResponse response) throws Exception;
	public ModelAndView promotionAddForm(HttpServletRequest request, HttpServletResponse response) throws Exception;
	public ModelAndView insertPromotionPost(HttpServletRequest request, HttpServletResponse response,
            @ModelAttribute EventPostVO eventPostVO,
            @RequestParam("imageFile") MultipartFile imageFile) throws Exception;
	public ModelAndView promotionUpdateForm(@RequestParam(value="postId", required=true) Integer postId,
            HttpServletRequest request, HttpServletResponse response) throws Exception;

	public ModelAndView updatePromotionPost(HttpServletRequest request, HttpServletResponse response,
	                                       @ModelAttribute EventPostVO eventPostVO,
	                                       @RequestParam(value="imageFile", required=false) MultipartFile imageFile) throws Exception;

	public ModelAndView deletePromotionPost(@RequestParam("postId") int postId,
            HttpServletRequest request,
            HttpServletResponse response) throws Exception;
	public ModelAndView couponList(@RequestParam(value="postId", required=false) Integer postId,
            HttpServletRequest request,
            HttpServletResponse response) throws Exception;

}
