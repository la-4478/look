package com.lookmarket.cart.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lookmarket.cart.dao.CartDAO;
import com.lookmarket.cart.vo.CartVO;

@Service("cartService")
public class CartServiceImpl implements CartService{
	@Autowired
	private CartDAO cartDAO;
	
	@Override
	public List<CartVO> myCartList(String current_id) throws Exception{
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
	
	@Override
	public void addCartItem(CartVO cartVO) throws Exception{
		cartDAO.addCartItem(cartVO);
	}
	
	@Override
	public void placeOrder(String m_id) throws Exception {
	    // 1. 장바구니 내역 가져오기
	    List<CartVO> cartList = cartDAO.selectCartByMemberId(m_id);
	    
	    if (cartList == null || cartList.isEmpty()) {
	        throw new Exception("장바구니가 비어있습니다.");
	    }

	    // 2. 주문 테이블에 주문 생성
	    cartDAO.insertOrder(m_id);

	    // 3. 방금 insert한 주문 ID 가져오기
	    int o_id = cartDAO.selectLastOrderId();

	    // 4. 주문 상세 테이블에 insert + 재고 차감
	    for (CartVO cartItem : cartList) {
	        cartDAO.insertOrderItem(o_id, cartItem);
	        cartDAO.updateGoodsStock(cartItem.getG_id(), cartItem.getC_qty());
	    }

	    // 5. 장바구니 비우기
	    cartDAO.clearCart(m_id);
	}

}
