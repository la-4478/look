package com.lookmarket.order.service;

import java.util.List;

import com.lookmarket.order.vo.CouponVO;

public interface CouponService {
	public List<CouponVO> findAvailableCoupons(String memberId) throws Exception;	
}
