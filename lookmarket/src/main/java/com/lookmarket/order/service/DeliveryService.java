package com.lookmarket.order.service;

import java.util.List;

import com.lookmarket.order.vo.DeliveryVO;

public interface DeliveryService {

	public int updateStatusByOrderId(int orderId, int dStatus) throws Exception;

	public Object NewDelivery(DeliveryVO deliVO) throws Exception;
	
	public List<DeliveryVO> getDeliveryList(String d_m_id) throws Exception;

}
