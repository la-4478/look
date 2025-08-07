package com.lookmarket.cart.service;

import java.util.List;

public interface CartService {
	public List myCartList(String current_id) throws Exception;
	public void updateQty(int c_id, int c_qty) throws Exception;
	public void deleteCartItem(int c_id) throws Exception;
}
