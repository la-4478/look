package com.lookmarket.event.dao;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import com.lookmarket.event.vo.CouponVO;
import com.lookmarket.event.vo.EventPostVO;

@Repository("eventDAO")
public class EventDAOImpl implements EventDAO {

    @Autowired
    private SqlSession sqlSession;

    @Override
    public void insertPromotionPost(EventPostVO eventPostVO) throws DataAccessException {
        sqlSession.insert("com.lookmarket.event.dao.EventDAO.insertPromotionPost", eventPostVO);
    }

    @Override
    public List<EventPostVO> selectPromotionPostList() throws DataAccessException {
        return sqlSession.selectList("com.lookmarket.event.dao.EventDAO.selectPromotionPostList");
    }

    @Override
    public EventPostVO selectPromotionPostById(Integer postId) throws DataAccessException {
        return sqlSession.selectOne("com.lookmarket.event.dao.EventDAO.selectPromotionPostById", postId);
    }

    @Override
    public void updatePromotionPost(EventPostVO eventPostVO) throws DataAccessException {
        sqlSession.update("com.lookmarket.event.dao.EventDAO.updatePromotionPost", eventPostVO);
    }

    @Override
    public void deletePromotionPost(Integer postId) throws DataAccessException {
        sqlSession.delete("com.lookmarket.event.dao.EventDAO.deletePromotionPost", postId);
    }
    
    @Override
    public List<CouponVO> selectCouponListByPostId(int postId) throws Exception {
        return sqlSession.selectList("com.lookmarket.event.dao.EventDAO.selectCouponListByPostId", postId);
    }

    @Override
    public CouponVO selectCouponById(int promoId) throws Exception {
        return sqlSession.selectOne("com.lookmarket.event.dao.EventDAO.selectCouponById", promoId);
    }

    @Override
    public void insertCoupon(CouponVO couponVO) throws Exception {
        sqlSession.insert("com.lookmarket.event.dao.EventDAO.insertCoupon", couponVO);
    }

    @Override
    public void updateCoupon(CouponVO couponVO) throws Exception {
        sqlSession.update("com.lookmarket.event.dao.EventDAO.updateCoupon", couponVO);
    }

    @Override
    public void deleteCoupon(int promoId) throws Exception {
        sqlSession.delete("com.lookmarket.event.dao.EventDAO.deleteCoupon", promoId);
    }

	@Override
	public List<CouponVO> selectAllCoupons() throws Exception{
		return sqlSession.selectList("com.lookmarket.event.dao.EventDAO.selectAllCoupons");
	}
	
	@Override
	public List<CouponVO> selectCouponsIssuedToMember(String memberId) throws DataAccessException {
		return sqlSession.selectList("com.lookmarket.event.dao.EventDAO.selectCouponsIssuedToMember", memberId);

	}

}
