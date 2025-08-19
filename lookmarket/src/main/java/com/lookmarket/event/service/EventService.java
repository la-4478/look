package com.lookmarket.event.service;

import java.util.List;

import com.lookmarket.event.vo.CouponVO;
import com.lookmarket.event.vo.EventPostVO;

public interface EventService {
	public void insertPromotionPost(EventPostVO eventPostVO) throws Exception;
    public List<EventPostVO> selectPromotionPostList() throws Exception;
    public EventPostVO selectPromotionPostById(Integer postId) throws Exception;
    public void updatePromotionPost(EventPostVO eventPostVO) throws Exception;
    public void deletePromotionPost(Integer postId) throws Exception;
    
    // 기존 이벤트 메서드 외에 아래 추가
    List<CouponVO> selectCouponListByPostId(int postId) throws Exception;
    CouponVO selectCouponById(int promoId) throws Exception;
    void insertCoupon(CouponVO couponVO) throws Exception;
    void updateCoupon(CouponVO couponVO) throws Exception;
    void deleteCoupon(int promoId) throws Exception;
}
