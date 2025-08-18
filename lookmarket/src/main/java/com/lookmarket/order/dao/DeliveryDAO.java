package com.lookmarket.order.dao;

import java.util.List;

import org.springframework.dao.DataAccessException;

import com.lookmarket.order.vo.DeliveryVO;

public interface DeliveryDAO {
 
	public int updateStatusByOrderId(DeliveryVO deliVO) throws DataAccessException;
	public Object newDelivery(DeliveryVO deliVO) throws DataAccessException;
	public List<DeliveryVO> selectDeliveryListByMember(String d_m_id) throws DataAccessException;

}
