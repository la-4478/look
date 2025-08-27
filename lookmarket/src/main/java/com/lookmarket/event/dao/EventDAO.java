package com.lookmarket.event.dao;

import java.util.List;

import org.springframework.dao.DataAccessException;

import com.lookmarket.event.vo.CouponVO;
import com.lookmarket.event.vo.EventPostVO;

public interface EventDAO {
    void insertPromotionPost(EventPostVO eventPostVO) throws DataAccessException;
    List<EventPostVO> selectPromotionPostList() throws DataAccessException;
    EventPostVO selectPromotionPostById(Integer postId) throws DataAccessException;
    void updatePromotionPost(EventPostVO eventPostVO) throws DataAccessException;
    void deletePromotionPost(Integer postId) throws DataAccessException;
    
    List<CouponVO> selectCouponListByPostId(int postId) throws Exception;
    CouponVO selectCouponById(int promoId) throws Exception;
    void insertCoupon(CouponVO couponVO) throws Exception;
    void updateCoupon(CouponVO couponVO) throws Exception;
    void deleteCoupon(int promoId) throws Exception;
	List<CouponVO> selectAllCoupons() throws Exception;
	List<CouponVO> selectCouponsIssuedToMember(String memberId) throws Exception;
}
