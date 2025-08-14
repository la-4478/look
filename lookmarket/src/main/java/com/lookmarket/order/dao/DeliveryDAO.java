package com.lookmarket.order.dao;

import org.springframework.dao.DataAccessException;

import com.lookmarket.order.vo.DeliveryVO;

public interface DeliveryDAO {
 
	public int updateStatusByOrderId(DeliveryVO deliVO) throws DataAccessException;
	public Object newDelivery(DeliveryVO deliVO) throws DataAccessException;

}
