package com.lookmarket.order.dao;

import org.springframework.dao.DataAccessException;

import com.lookmarket.order.vo.PayVO;

public interface PayDAO {
	public void insertPay(PayVO payVO) throws DataAccessException;
}
