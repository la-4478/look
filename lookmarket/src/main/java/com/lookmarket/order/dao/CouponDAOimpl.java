package com.lookmarket.order.dao;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import com.lookmarket.order.vo.CouponVO;

@Repository
public class CouponDAOimpl implements CouponDAO{
	@Autowired
	private SqlSession sqlSession;
	@Override
	public List<CouponVO> findAvailableCoupons(String memberId) throws DataAccessException {
		return sqlSession.selectList("mapper.mypage.couponList", memberId);
	}

}
