package com.lookmarket.cart.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface CartController {
	public ModelAndView myCartList(HttpServletRequest request, HttpServletResponse response)  throws Exception;
	public ResponseEntity<String> updateCartQty(@RequestParam("c_id") int c_id, @RequestParam("c_qty") int c_qty, HttpServletRequest request, HttpServletResponse response)  throws Exception;
}
