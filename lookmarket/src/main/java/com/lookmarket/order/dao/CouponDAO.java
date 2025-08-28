package com.lookmarket.order.dao;

import java.util.List;

import org.springframework.dao.DataAccessException;

import com.lookmarket.order.vo.CouponVO;

public interface CouponDAO {

	List<CouponVO> findAvailableCoupons(String memberId) throws DataAccessException;

}
