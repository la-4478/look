package com.lookmarket.order.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lookmarket.order.dao.DeliveryDAO;
import com.lookmarket.order.vo.DeliveryVO;

@Service("deliverService")
public class DeliveryServiceImpl implements DeliveryService {
	@Autowired
	private DeliveryDAO deliveryDAO;

	@Override
	public int updateStatusByOrderId(int orderId, int dStatus) throws Exception {
		DeliveryVO deliVO = new DeliveryVO();
		deliVO.setO_id(orderId);
		deliVO.setD_status(dStatus);
		return deliveryDAO.updateStatusByOrderId(deliVO);
	}

	@Override
	public Object NewDelivery(DeliveryVO deliVO) throws Exception {
		return deliveryDAO.newDelivery(deliVO);
		
	}
	

}
