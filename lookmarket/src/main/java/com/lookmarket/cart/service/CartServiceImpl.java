package com.lookmarket.cart.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lookmarket.cart.dao.CartDAO;

@Service("cartService")
public class CartServiceImpl implements CartService{
	@Autowired
	private CartDAO cartDAO;
	
	@Override
	public List myCartList(String current_id) throws Exception{
		return cartDAO.myCartList(current_id);
	}
	
	@Override
	public void updateQty(int c_id, int c_qty) throws Exception{
		cartDAO.updateQty(c_id, c_qty);
	}
	
	@Override
	public void deleteCartItem(int c_id) throws Exception{
		cartDAO.deleteCartItem(c_id);
	}
}
