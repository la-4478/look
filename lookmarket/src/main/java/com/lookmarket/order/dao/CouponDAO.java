package com.lookmarket.order.dao;

import java.util.List;

import org.springframework.dao.DataAccessException;

import com.lookmarket.event.vo.CouponVO;

public interface CouponDAO {

	public List<Integer> findmycoupon(String memberId) throws DataAccessException;

	public List<CouponVO> couponList(List<Integer> promo_id) throws DataAccessException;

	public List<CouponVO> getCouponsForMember(String memberId) throws DataAccessException;

	public void useCoupon(String couponId, String member_id) throws DataAccessException;

}
