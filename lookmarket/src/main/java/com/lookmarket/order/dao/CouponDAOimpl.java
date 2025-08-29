package com.lookmarket.order.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import com.lookmarket.event.vo.CouponVO;

@Repository
public class CouponDAOimpl implements CouponDAO{
	@Autowired
	private SqlSession sqlSession;
	@Override
	public List<Integer> findmycoupon(String memberId) throws DataAccessException {
		List<Integer> result = sqlSession.selectList("mapper.mypage.findmycoupon", memberId); 
		return result;
	}
	@Override
	public List<CouponVO> couponList(List<Integer> promo_id) throws DataAccessException {
		return sqlSession.selectList("mapper.mypage.couponList", promo_id);
	}
	@Override
	public List<CouponVO> getCouponsForMember(String memberId) throws DataAccessException {
		return sqlSession.selectList("mapper.mypage.getCouponsForMember", memberId);
	}
	@Override
	public void useCoupon(String couponId, String member_id) throws DataAccessException {
		Map<String, Object> paramMap = new HashMap<>();
	    paramMap.put("promo_id", couponId);
	    paramMap.put("m_id", member_id);

	    sqlSession.update("mapper.mypage.useCoupon", paramMap);
	}

}
