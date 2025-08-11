package com.lookmarket.order.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lookmarket.order.dao.PayDAO;
import com.lookmarket.order.vo.PayVO;

@Service("payService")
public class PayServiceImpl implements PayService {
	@Autowired
	private PayDAO payDAO;

	@Override
	public void insertPay(PayVO payVO) throws Exception {
		payDAO.insertPay(payVO);
	}

}
