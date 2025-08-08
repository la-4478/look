package com.lookmarket.order.controller;

import com.lookmarket.order.vo.ApiResponse;
import com.lookmarket.order.vo.PayVO;

public interface PayController {
	public ApiResponse insertPay(PayVO payVO) throws Exception;
}
