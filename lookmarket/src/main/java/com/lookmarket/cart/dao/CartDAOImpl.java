package com.lookmarket.cart.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import com.lookmarket.cart.vo.CartVO;

@Repository("cartDAO")
public class CartDAOImpl implements CartDAO{
	@Autowired
	private SqlSession sqlSession;
	
	@Override
	public List<CartVO> myCartList(String current_id) throws DataAccessException{
		return sqlSession.selectList("mapper.cart.myCartList", current_id);
	}
	
	@Override
	public void updateQty(int c_id, int c_qty) throws DataAccessException{
		CartVO paramVO = new CartVO();
		paramVO.setC_id(c_id);
		paramVO.setC_qty(c_qty);
		sqlSession.update("mapper.cart.updateQty", paramVO);
	}
	
	@Override
	public void deleteCartItem(int c_id) throws DataAccessException{
		sqlSession.delete("mapper.cart.deleteCartItem", c_id);
	}
	
	@Override
	public void addCartItem(CartVO cartVO) throws DataAccessException{
		sqlSession.insert("mapper.cart.addCartItem", cartVO);
	}
	
    @Override
    public List<CartVO> selectCartByMemberId(String m_id) throws Exception {
        return sqlSession.selectList("mapper.cart.selectCartByMemberId", m_id);
    }

    @Override
    public void insertOrder(String m_id) throws Exception {
        sqlSession.insert("mapper.cart.insertOrder", m_id);
    }

    @Override
    public int selectLastOrderId() throws Exception {
        return sqlSession.selectOne("mapper.cart.selectLastOrderId");
    }

    @Override
    public void insertOrderItem(int o_id, CartVO cartItem) throws Exception {
        sqlSession.insert("mapper.cart.insertOrderItem", 
            Map.of("o_id", o_id, "g_id", cartItem.getG_id(), "c_qty", cartItem.getC_qty(), "price", cartItem.getG_price()));
    }

    @Override
    public void updateGoodsStock(int g_id, int c_qty) throws Exception {
        sqlSession.update("mapper.cart.updateGoodsStock", Map.of("g_id", g_id, "c_qty", c_qty));
    }

    @Override
    public void clearCart(String m_id) throws Exception {
        sqlSession.delete("mapper.cart.clearCart", m_id);
    }
}
