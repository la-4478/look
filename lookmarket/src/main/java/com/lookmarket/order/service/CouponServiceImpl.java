package com.lookmarket.order.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lookmarket.event.vo.CouponVO;
import com.lookmarket.order.dao.CouponDAO;

@Service
public class CouponServiceImpl implements CouponService {
	@Autowired
	private CouponDAO couponDAO;
	@Override
	public List<Integer> findmycoupon(String memberId) throws Exception {
		return couponDAO.findmycoupon(memberId);
	}
	@Override
	public List<CouponVO> couponList(List<Integer> promo_id) throws Exception {
		return couponDAO.couponList(promo_id);
	}
	@Override
	public List<CouponVO> getCouponsForMember(String memberId) throws Exception {
		return couponDAO.getCouponsForMember(memberId);
	}
	@Override
	public void useCoupon(String couponId, String member_id) throws Exception {
		couponDAO.useCoupon(couponId, member_id);
	}

}
