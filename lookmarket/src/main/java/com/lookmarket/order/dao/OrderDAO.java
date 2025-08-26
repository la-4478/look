package com.lookmarket.order.dao;

import java.util.List;

import org.springframework.dao.DataAccessException;

import com.lookmarket.cart.vo.CartVO;
import com.lookmarket.order.vo.OrderItemVO;
import com.lookmarket.order.vo.OrderListRowVO;
import com.lookmarket.order.vo.OrderVO;
import com.lookmarket.order.vo.PayVO;

public interface OrderDAO {
	public List<OrderVO> listMyOrderGoods(OrderVO orderBean) throws DataAccessException;
	public void insertNewOrder(List<OrderVO> myOrderList) throws DataAccessException;
	public OrderVO findMyOrder(String order_id) throws DataAccessException;
    public void removeGoodsFromCart(List<OrderVO> myOrderList)throws DataAccessException;
    public Integer selectCartIdByMemberAndGoods(CartVO cartVO) throws DataAccessException;
    public void deleteCartGoods(int cart_id) throws DataAccessException;
	public void addOrderItem(OrderItemVO itemVO);
	public void insertOrder(String memberId, List<CartVO> cartList) throws DataAccessException;
	List<OrderItemVO> getCartItemsByMemberId(String m_id) throws DataAccessException;
	public void insertPay(PayVO payVO) throws DataAccessException;
	public Integer removeCartItem(CartVO cartVO) throws DataAccessException;
	public List<OrderVO> allOrderList() throws DataAccessException;
	public List<OrderItemVO> allItemList() throws DataAccessException;
}