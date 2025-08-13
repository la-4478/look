package com.lookmarket.order.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import com.lookmarket.cart.vo.CartVO;
import com.lookmarket.order.vo.OrderItemVO;
import com.lookmarket.order.vo.OrderVO;
import com.lookmarket.order.vo.PayVO;

@Repository("orderDAO")
public class OrderDAOImpl implements OrderDAO {
	@Autowired
	private SqlSession sqlSession;
	
	public List<OrderVO> listMyOrderGoods(OrderVO orderVO) throws DataAccessException{
		List<OrderVO> orderGoodsList=new ArrayList<OrderVO>();
		orderGoodsList=(ArrayList)sqlSession.selectList("mapper.order.selectMyOrderList",orderVO);
		return orderGoodsList;
	}
	
	public void insertNewOrder(List<OrderVO> myOrderList) throws DataAccessException{
	//	int order_id=selectOrderID();
		for(int i=0; i<myOrderList.size();i++){
			OrderVO orderVO =(OrderVO)myOrderList.get(i);
		//	orderVO.setOrder_id(order_id);
			sqlSession.insert("mapper.order.addNewOrder",orderVO);
		}
		
	}	
	
	public OrderVO findMyOrder(String order_id) throws DataAccessException{
		OrderVO orderVO=(OrderVO)sqlSession.selectOne("mapper.order.selectMyOrder",order_id);		
		return orderVO;
	}
	
	public void removeGoodsFromCart(OrderVO orderVO)throws DataAccessException{
		sqlSession.delete("mapper.order.deleteGoodsFromCart",orderVO);
	}
	
	public void removeGoodsFromCart(List<OrderVO> myOrderList)throws DataAccessException{
		for(int i=0; i<myOrderList.size();i++){
			OrderVO orderVO =(OrderVO)myOrderList.get(i);
			sqlSession.delete("mapper.order.deleteGoodsFromCart",orderVO);		
		}
	}	
    @Override
    public Integer selectCartIdByMemberAndGoods(CartVO cartVO) throws DataAccessException {
        return sqlSession.selectOne("mapper.cart.removeCartItem", cartVO);
    }

    @Override
    public void deleteCartGoods(int cart_id) throws DataAccessException {
        sqlSession.delete("mapper.cart.removeCartItem", cart_id);
    }

	@Override
	public void addOrderItem(OrderItemVO itemVO) {
		sqlSession.insert("mapper.order.addOrderItem", itemVO);
	}
	
	@Override
	public void insertOrder(String memberId, List<CartVO> cartList) throws DataAccessException {
	    // 1. 주문 헤더 저장 (orders 테이블)
	    OrderVO orderVO = new OrderVO();
	    orderVO.setMId(memberId);
	    // 주문 상세 정보는 필요에 따라 세팅
	    sqlSession.insert("mapper.order.addNewOrder", orderVO);
	    int orderId = orderVO.getOId(); // useGeneratedKeys로 생성된 주문번호 받기

	    // 2. 주문 아이템 저장 (order_item 테이블)
	    for (CartVO cart : cartList) {
	        OrderItemVO itemVO = new OrderItemVO();
	        itemVO.setOId(orderId);
	        itemVO.setOtGId(cart.getG_id());
	        itemVO.setOtGoodsPrice(cart.getG_price());
	        itemVO.setOtSalePrice(0);  // 할인 없으면 0 또는 적절한 값 설정
	        itemVO.setOtGoodsName(cart.getG_name());  // 필수: 상품명 세팅
	        itemVO.setOtGoodsQty(cart.getC_qty());
	        sqlSession.insert("mapper.order.addOrderItem", itemVO);
	    }
	}
	
	@Override
	public List<OrderItemVO> getCartItemsByMemberId(String m_id) throws DataAccessException {
	    return sqlSession.selectList("mapper.order.selectCartItemsByMemberId", m_id);
	}

	@Override
	public void insertPay(PayVO payVO) throws DataAccessException {
		sqlSession.insert("mapper.order.addNewpay", payVO);
		
	}

	@Override
	public Integer removeCartItem(CartVO cartVO) throws DataAccessException {
		return sqlSession.selectOne("mapper.order.removeCartItem", cartVO);
	}

}

