package com.lookmarket.order.service;

import java.util.List;

import com.lookmarket.order.vo.OrderItemVO;
import com.lookmarket.order.vo.OrderVO;
import com.lookmarket.order.vo.PayVO;

public interface OrderService {
    public List<OrderVO> listMyOrderGoods(OrderVO orderVO) throws Exception;
    public void addNewOrder(List<OrderVO> myOrderList) throws Exception;
    public OrderVO findMyOrder(String order_id) throws Exception;
    public void addNewpay(PayVO payVO)throws Exception;
    public void removeCartItem(String member_id, int goods_num) throws Exception;
	public void addOrderItem(OrderItemVO itemVO) throws Exception;
}