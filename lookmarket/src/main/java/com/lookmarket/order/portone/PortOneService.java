package com.lookmarket.order.portone;

public interface PortOneService {
	public abstract boolean verifyPayment(String impUid, int expectAmount) throws Exception;
}
