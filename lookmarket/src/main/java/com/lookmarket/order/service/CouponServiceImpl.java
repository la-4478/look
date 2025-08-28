package com.lookmarket.order.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lookmarket.order.dao.CouponDAO;
import com.lookmarket.order.vo.CouponVO;

@Service
public class CouponServiceImpl implements CouponService {
	@Autowired
	private CouponDAO couponDAO;
	@Override
	public List<CouponVO> findAvailableCoupons(String memberId) throws Exception {
		return couponDAO.findAvailableCoupons(memberId);
	}

}
