package com.lookmarket.sijangbajo.controller;

import org.springframework.web.servlet.ModelAndView;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface SijangbajoController {
	public ModelAndView search(HttpServletRequest request, HttpServletResponse response)  throws Exception;
	public ModelAndView nearby(HttpServletRequest request, HttpServletResponse response)  throws Exception;
	public ModelAndView nearCourse(HttpServletRequest request, HttpServletResponse response)  throws Exception;
	public ModelAndView clean(HttpServletRequest request, HttpServletResponse response)  throws Exception;
}
