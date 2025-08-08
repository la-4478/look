package com.lookmarket.order.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lookmarket.cart.vo.CartVO;
import com.lookmarket.order.dao.OrderDAO;
import com.lookmarket.order.dao.PayDAO;
import com.lookmarket.order.vo.OrderItemVO;
import com.lookmarket.order.vo.OrderVO;
import com.lookmarket.order.vo.PayVO;

@Service("orderService")
public class OrderServiceImpl implements OrderService {
	@Autowired
	private OrderDAO orderDAO;
	@Autowired
	private PayDAO payDAO;
	
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
        payDAO.insertPay(payVO);
        
    }
    
    public OrderVO findMyOrder(String order_id) throws Exception{
        return orderDAO.findMyOrder(order_id);
    }
    
    @Override
    public void removeCartItem(String m_id, int G_id) throws Exception {
        CartVO cartVO = new CartVO();
        cartVO.setM_id(m_id);
        cartVO.setG_id(G_id);

        Integer cart_id = orderDAO.selectCartIdByMemberAndGoods(cartVO);
        if (cart_id != null) {
            orderDAO.deleteCartGoods(cart_id);
        } else {
            System.out.println("🟡 삭제할 장바구니 항목이 없습니다.");
        }
    }

	@Override
	public void addOrderItem(OrderItemVO itemVO) throws Exception {
		orderDAO.addOrderItem(itemVO);
		
	}
}
