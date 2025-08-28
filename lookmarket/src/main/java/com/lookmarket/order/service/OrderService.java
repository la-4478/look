package com.lookmarket.order.service;

import java.util.List;

import com.lookmarket.order.vo.OrderItemVO;
import com.lookmarket.order.vo.OrderListRowVO;
import com.lookmarket.order.vo.OrderVO;
import com.lookmarket.order.vo.PayVO;

public interface OrderService {
    // 내 주문 목록 조회
    List<OrderVO> listMyOrderGoods(OrderVO orderVO) throws Exception;
    
    // 새 주문 저장 (복수 주문 리스트)
    void addNewOrder(List<OrderVO> myOrderList) throws Exception;
    
    // 특정 주문 조회
    OrderVO findMyOrder(String o_id) throws Exception;
    
    // 결제정보 저장
    void addNewpay(PayVO payVO) throws Exception;
    
    // 장바구니 항목 삭제
    void removeCartItem(String m_id, int goods_num) throws Exception;
    
    // 주문 아이템 저장
    void addOrderItem(OrderItemVO itemVO) throws Exception;
    
    // 장바구니 기반 주문 처리 (헤더+아이템 저장 및 장바구니 비우기)
    void processOrder(String m_id) throws Exception;
    
    List<OrderItemVO> getCartItemsByMemberId(String m_id) throws Exception;

	public List<OrderVO> allOrderList() throws Exception;

	public List<OrderItemVO> allItemList() throws Exception;

	public String reviewgoodsname(int o_id)throws Exception;
	
	public Integer whomid(String m_id) throws Exception;
	
	public void confirmPaymentAndRecordAccounting(String paymentId, String paymentKey, int generatedOrderId, long paidTotal, String m_id, Object object, String cardCompany, int i) throws Exception;
}
