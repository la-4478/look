package com.lookmarket.event.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lookmarket.event.dao.EventDAO;
import com.lookmarket.event.vo.CouponVO;
import com.lookmarket.event.vo.EventPostVO;

@Service("eventService")
public class EventServiceImpl implements EventService {

    @Autowired
    private EventDAO eventDAO;

    @Override
    public void insertPromotionPost(EventPostVO eventPostVO) throws Exception {
        // DB에 저장
    	eventDAO.insertPromotionPost(eventPostVO);
    }

    @Override
    public List<EventPostVO> selectPromotionPostList() throws Exception {
        return eventDAO.selectPromotionPostList();
    }

    @Override
    public EventPostVO selectPromotionPostById(Integer postId) throws Exception {
        return eventDAO.selectPromotionPostById(postId);
    }

    @Override
    public void updatePromotionPost(EventPostVO eventPostVO) throws Exception {
        eventDAO.updatePromotionPost(eventPostVO);
    }

    @Override
    public void deletePromotionPost(Integer postId) throws Exception {
        // DB에서 삭제 처리 로직
        eventDAO.deletePromotionPost(postId);
    }

    @Override
    public CouponVO selectCouponById(int promoId) throws Exception {
        return eventDAO.selectCouponById(promoId);
    }

    @Override
    public void insertCoupon(CouponVO couponVO) throws Exception {
        eventDAO.insertCoupon(couponVO);
    }

    @Override
    public void updateCoupon(CouponVO couponVO) throws Exception {
        eventDAO.updateCoupon(couponVO);
    }

    @Override
    public void deleteCoupon(int promoId) throws Exception {
        eventDAO.deleteCoupon(promoId);
    }

    @Override
    public List<CouponVO> selectCouponListByPostId(int postId) throws Exception {
    	List<CouponVO> coupons = eventDAO.selectCouponListByPostId(postId);
        for (CouponVO coupon : coupons) {
            coupon.setPromoCouponActive(!coupon.isExpired());
        }
        return coupons;
    }
    
	@Override
	public List<CouponVO> selectAllCoupons() throws Exception {
		List<CouponVO> coupons = eventDAO.selectAllCoupons();
	    for (CouponVO coupon : coupons) {
	        coupon.setPromoCouponActive(!coupon.isExpired());
	    }
	    return coupons;
	}
	
	@Override
	public List<CouponVO> selectCouponsIssuedToMember(String memberId) throws Exception {
		List<CouponVO> coupons = eventDAO.selectCouponsIssuedToMember(memberId);
	    for (CouponVO coupon : coupons) {
	        coupon.setPromoCouponActive(!coupon.isExpired());
	    }
	    return coupons;
	}
	@Override
	public CouponVO selectCouponById1(int promoId) throws Exception {
	    CouponVO coupon = eventDAO.selectCouponById(promoId);
	    if (coupon != null) {
	        coupon.setPromoCouponActive(!coupon.isExpired());
	    }
	    return coupon;
	}
}
