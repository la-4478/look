package com.lookmarket.order.vo;

import java.util.List;

public class OrderDTO {
	private OrderVO order;   	               // 주문 정보
    private List<OrderItemVO> orderItems;   // 주문 상품 리스트
    private DeliveryVO delivery;            // 배송 정보
	
    public OrderVO getOrder() {
		return order;
	}
	public void setOrder(OrderVO order) {
		this.order = order;
	}
	public List<OrderItemVO> getOrderItems() {
		return orderItems;
	}
	public void setOrderItems(List<OrderItemVO> orderItems) {
		this.orderItems = orderItems;
	}
	public DeliveryVO getDelivery() {
		return delivery;
	}
	public void setDelivery(DeliveryVO delivery) {
		this.delivery = delivery;
	}
    
}
