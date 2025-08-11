package com.lookmarket.cart.service;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.lookmarket.cart.vo.CartVO;

import jakarta.servlet.http.HttpServletRequest;

public interface CartService {
	public List<CartVO> myCartList(String current_id) throws Exception;
	public void addCartItem(CartVO cartVO) throws Exception;
	public void updateQty(int c_id, int c_qty) throws Exception;
	public void deleteCartItem(int c_id) throws Exception;
	public void placeOrder(String m_id) throws Exception;
}
