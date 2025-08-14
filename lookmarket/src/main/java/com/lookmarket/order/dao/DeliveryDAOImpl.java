package com.lookmarket.order.dao;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import com.lookmarket.order.vo.DeliveryVO;

@Repository("deliveryDAO")
public class DeliveryDAOImpl implements DeliveryDAO {
	@Autowired
	private SqlSession sqlSession;

	@Override
	public int updateStatusByOrderId(DeliveryVO deliVO) {
		int result =  sqlSession.update("mapper.deli.updatedelivery", deliVO); 
		return result;
	}

	@Override
	public Object newDelivery(DeliveryVO deliVO) throws DataAccessException {
		return sqlSession.insert("mapper.deli.insertNewdelivery", deliVO);
	}

}
