package com.lookmarket.order.service;

import java.util.List;

import com.lookmarket.event.vo.CouponVO;

public interface CouponService {
	public List<Integer> findmycoupon(String memberId) throws Exception;

	public List<CouponVO> couponList(List<Integer> promo_id) throws Exception;

	public List<CouponVO> getCouponsForMember(String memberId)throws Exception;

	public void useCoupon(String couponId, String member_id) throws Exception;
}
