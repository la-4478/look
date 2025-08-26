package com.lookmarket.order.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lookmarket.cart.dao.CartDAO;
import com.lookmarket.cart.vo.CartVO;
import com.lookmarket.order.dao.OrderDAO;
import com.lookmarket.order.vo.OrderItemVO;
import com.lookmarket.order.vo.OrderVO;
import com.lookmarket.order.vo.PayVO;

@Service("orderService")
public class OrderServiceImpl implements OrderService {
	@Autowired
	private OrderDAO orderDAO;
	@Autowired
    private CartDAO cartDAO;
	
	
	public List<OrderVO> listMyOrderGoods(OrderVO orderVO) throws Exception{
		List<OrderVO> orderGoodsList;
		orderGoodsList=orderDAO.listMyOrderGoods(orderVO);
		return orderGoodsList;
	}
	
	public void addNewOrder(List<OrderVO> myOrderList) throws Exception{
		orderDAO.insertNewOrder(myOrderList);
	}	
    @Override
    public void addNewpay(PayVO payVO) throws Exception{
        orderDAO.insertPay(payVO);
        
    }
    
    public OrderVO findMyOrder(String order_id) throws Exception{
        return orderDAO.findMyOrder(order_id);
    }
    
    @Override
    public void removeCartItem(String m_id, int G_id) throws Exception {
        CartVO cartVO = new CartVO();
        cartVO.setM_id(m_id);
        cartVO.setG_id(G_id);

        Integer cart_id = orderDAO.removeCartItem(cartVO);
        if (cart_id != null) {
            orderDAO.deleteCartGoods(cart_id);
        } else {
            System.out.println("삭제할 장바구니 항목이 없습니다.");
        }
    }

	@Override
	public void addOrderItem(OrderItemVO itemVO) throws Exception {
		orderDAO.addOrderItem(itemVO);
		
	}
	
	@Override
    @Transactional
    public void processOrder(String m_id) throws Exception {
        // 1. 회원 장바구니 상품 조회
        List<CartVO> cartList = cartDAO.selectCartByMemberId(m_id);
        
        if (cartList == null || cartList.isEmpty()) {
            throw new Exception("장바구니가 비어있습니다.");
        }
        
        // 2. 주문(헤더+아이템) 저장
        orderDAO.insertOrder(m_id, cartList);
        
        // 3. 장바구니 비우기
        cartDAO.clearCart(m_id);
    }
	
	@Override
	public List<OrderItemVO> getCartItemsByMemberId(String m_id) throws Exception {
	    return orderDAO.getCartItemsByMemberId(m_id);
	}

	@Override
	public List<OrderVO> allOrderList() throws Exception {
		return orderDAO.allOrderList();
	}

	@Override
	public List<OrderItemVO> allItemList() throws Exception {
		return orderDAO.allItemList();
	}

	@Override
	public String reviewgoodsname(int o_id) throws Exception {
		return orderDAO.reviewgoodsname(o_id);
	}

	@Override
	public Integer whomid(String m_id) throws Exception {
		Integer oId = orderDAO.whomid(m_id);
	if (oId == null) {
		throw new EmptyResultDataAccessException("주문없음: " + m_id, 1);
    }
	return oId;
	}
	
}
