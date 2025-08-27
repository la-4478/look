package com.lookmarket.mypage.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;

import com.lookmarket.community.vo.ReviewVO;
import com.lookmarket.mypage.dao.MyPageDAO;
import com.lookmarket.mypage.vo.MyPageVO;
import com.lookmarket.order.vo.OrderItemVO;
import com.lookmarket.order.vo.OrderVO;
import com.lookmarket.wishlist.vo.WishListVO;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Service("myPageService")
public class MyPageServiceImpl implements MyPageService{
	@Autowired
	private MyPageDAO myPageDAO;
	
	@Override
	public MyPageVO getMyPageInfo(String current_id) throws Exception{
		return myPageDAO.getMyPageInfo(current_id);
	}
	
	@Override
	public int updateMyInfo(MyPageVO myPageVO) throws Exception{
		return myPageDAO.updateMyInfo(myPageVO);
	}
	
	@Override
	public void deleteMember(String m_id) throws Exception {
	    myPageDAO.deleteMember(m_id);
	}
	@Override
	public List<ReviewVO> selectMyCommunityList(String m_id) throws Exception{
		return myPageDAO.selectMyCommunityList(m_id);
	}
	@Override
	public List<OrderVO> getOrdersByMemberId(String mId) throws Exception {
	    return myPageDAO.getOrdersByMemberId(mId);
	}

	@Override
	public OrderVO getOrderById(int oId) throws Exception {
	    return myPageDAO.getOrderById(oId);
	}

	@Override
	public List<OrderItemVO> getOrderItemsByOrderId(int oId) throws Exception {
	    return myPageDAO.getOrderItemsByOrderId(oId);
	}
	
	@Override
	public List<WishListVO> getMyWishList(String m_id) throws Exception {
		return myPageDAO.getMyWishList(m_id);
	}
	@Override
	public int issuePromotionCoupon(int promoId, String memberId) throws Exception {
	    boolean alreadyIssued = myPageDAO.isCouponAlreadyIssued(promoId, memberId);
	    
	    if (alreadyIssued) {
	        return 0; // 중복
	    }

	    return myPageDAO.issuePromotionCoupon(promoId, memberId);
	}

}
