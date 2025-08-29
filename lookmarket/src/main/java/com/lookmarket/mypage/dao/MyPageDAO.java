package com.lookmarket.mypage.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.dao.DataAccessException;

import com.lookmarket.community.vo.ReviewVO;
import com.lookmarket.mypage.vo.MyPageVO;
import com.lookmarket.order.vo.OrderItemVO;
import com.lookmarket.order.vo.OrderVO;
import com.lookmarket.wishlist.vo.WishListVO;

public interface MyPageDAO {
	public MyPageVO getMyPageInfo(String current_id) throws DataAccessException;
	public int updateMyInfo(MyPageVO myPageVO) throws DataAccessException;
	int deleteMember(String m_id) throws Exception;
	public List<ReviewVO> selectMyCommunityList(String m_id) throws Exception;

	public List<OrderVO> getOrdersByMemberId(@Param("m_id") String mId) throws DataAccessException;
	public OrderVO getOrderById(int oId) throws DataAccessException;
	public List<OrderItemVO> getOrderItemsByOrderId(int oId) throws DataAccessException;
	public List<WishListVO> getMyWishList(String m_id) throws DataAccessException;
	public int issuePromotionCoupon(int promoId, String memberId) throws DataAccessException;
	boolean isCouponAlreadyIssued(int promoId, String memberId) throws DataAccessException;

}
