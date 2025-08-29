package com.lookmarket.order.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.lookmarket.event.vo.CouponVO;
import com.lookmarket.order.service.CouponService;

import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/coupon")
public class CouponController {
	
	@Autowired
	private CouponService couponService;
	
	@GetMapping(value="/list.do", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<CouponVO>> list(
            HttpSession session,
            @RequestParam(value="memberId", required=false) String memberIdParam) {

        String memberId = (String) session.getAttribute("current_id");
        if (memberId == null) memberId = memberIdParam; // DEV ONLY

        try {
            if (memberId == null) {
                return ResponseEntity.ok(java.util.Collections.emptyList());
            }
            List<CouponVO> items = couponService.getCouponsForMember(memberId);
            return ResponseEntity.ok(items == null ? java.util.Collections.emptyList() : items);

        } catch (Exception e) {
            // 500 HTMl 대신 서버 로그로만 상세 남기고, 프론트에는 [] 반환
            return ResponseEntity.ok(java.util.Collections.emptyList());
        }
    }
}
