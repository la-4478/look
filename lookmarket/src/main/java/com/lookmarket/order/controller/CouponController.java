package com.lookmarket.order.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lookmarket.order.service.CouponService;
import com.lookmarket.order.vo.CouponVO;

import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/coupon")
public class CouponController {
	
	@Autowired
	private CouponService couponService;
	
	@RequestMapping(value="/list.do", produces = MediaType.APPLICATION_JSON_VALUE)
	public List<CouponVO> list(HttpSession session)throws Exception{
		String memberId = (String)session.getAttribute("current_id");
		if(memberId == null) {
			throw new org.springframework.web.server.ResponseStatusException(org.springframework.http.HttpStatus.UNAUTHORIZED, "LOGIN_REQUIRED");
		}
		return couponService.findAvailableCoupons(memberId);
		
	}
}
