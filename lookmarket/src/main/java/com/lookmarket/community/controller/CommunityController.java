package com.lookmarket.community.controller;

import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface CommunityController {
    ModelAndView communityList(HttpServletRequest request, HttpServletResponse response) throws Exception;
    ModelAndView communityDetail(String r_id, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) throws Exception;
    ModelAndView communityUpdateForm(String r_id, HttpServletRequest request, HttpServletResponse response) throws Exception;
    ModelAndView communityUpdate(MultipartHttpServletRequest request, RedirectAttributes redirectAttributes) throws Exception;
    ModelAndView communityDelete(String r_id, HttpServletRequest request, RedirectAttributes redirectAttributes) throws Exception;

    ModelAndView blackBoardList(HttpServletRequest request, HttpServletResponse response) throws Exception;
    ModelAndView blackBoardDetail(String b_id, HttpServletRequest request, HttpServletResponse response) throws Exception;
    ModelAndView blackBoardUpdateForm(String b_id, HttpServletRequest request, HttpServletResponse response) throws Exception;
    ModelAndView blackBoardUpdate(HttpServletRequest request, RedirectAttributes redirectAttributes) throws Exception;
    ModelAndView blackBoardDelete(String b_id, HttpServletRequest request, RedirectAttributes redirectAttributes) throws Exception;

    ModelAndView communityAddForm(HttpServletRequest request, HttpServletResponse response) throws Exception;
    ModelAndView blackBoardAddForm(HttpServletRequest request, HttpServletResponse response) throws Exception;
    ModelAndView insertReview(MultipartHttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) throws Exception;
    ModelAndView insertBlackBoard(HttpServletRequest request, RedirectAttributes redirectAttributes) throws Exception;
}

