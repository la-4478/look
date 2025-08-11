package com.lookmarket.cart.dao;

import java.util.List;

import org.springframework.dao.DataAccessException;

import com.lookmarket.cart.vo.CartVO;

public interface CartDAO {
	public List<CartVO> myCartList(String current_id) throws DataAccessException;
	public void addCartItem(CartVO cartVO) throws Exception;	
	public void updateQty(int c_id, int c_qty) throws DataAccessException;
	public void deleteCartItem(int c_id) throws DataAccessException;
	
    // 추가 메서드
    List<CartVO> selectCartByMemberId(String m_id) throws Exception;
    void insertOrder(String m_id) throws Exception;
    int selectLastOrderId() throws Exception;
    void insertOrderItem(int o_id, CartVO cartItem) throws Exception;
    void updateGoodsStock(int g_id, int qty) throws Exception;
    void clearCart(String m_id) throws Exception;
}
