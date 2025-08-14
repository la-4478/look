package com.lookmarket.mypage.service;

import java.util.List;

import com.lookmarket.community.vo.ReviewVO;
import com.lookmarket.mypage.vo.MyPageVO;
import com.lookmarket.order.vo.OrderItemVO;
import com.lookmarket.order.vo.OrderVO;

public interface MyPageService {
	public MyPageVO getMyPageInfo(String current_id) throws Exception;
	public int updateMyInfo(MyPageVO myPageVO) throws Exception;
	void deleteMember(String m_id) throws Exception;
	public List<ReviewVO> selectMyCommunityList(String m_id) throws Exception;
	// 회원 주문 내역 전체 조회
	public List<OrderVO> getOrdersByMemberId(String mId) throws Exception;
	// 특정 주문 상세 조회
	public OrderVO getOrderById(int oId) throws Exception;
	// 특정 주문의 상품 목록 조회
	public List<OrderItemVO> getOrderItemsByOrderId(int oId) throws Exception;
	
}
