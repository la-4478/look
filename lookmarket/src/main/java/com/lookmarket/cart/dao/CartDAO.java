package com.lookmarket.cart.dao;

import java.util.List;

import org.springframework.dao.DataAccessException;

public interface CartDAO {
	public List myCartList(String current_id) throws DataAccessException;
	public void updateQty(int c_id, int c_qty) throws DataAccessException;
	public void deleteCartItem(int c_id) throws DataAccessException;
}
