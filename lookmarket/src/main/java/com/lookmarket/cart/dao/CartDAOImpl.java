package com.lookmarket.cart.dao;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import com.lookmarket.cart.vo.CartVO;

@Repository("cartDAO")
public class CartDAOImpl implements CartDAO{
	@Autowired
	private SqlSession sqlSession;
	@Autowired
	private CartVO cartVO;
	
	@Override
	public List myCartList(String current_id) throws DataAccessException{
		return sqlSession.selectList("mapper.cart.myCartList", current_id);
	}
	
	@Override
	public void updateQty(int c_id, int c_qty) throws DataAccessException{
		cartVO.setC_id(c_id);
		cartVO.setC_qty(c_qty);
		
		sqlSession.update("mapper.cart.updateQty", cartVO);
	}
	
	@Override
	public void deleteCartItem(int c_id) throws DataAccessException{
		sqlSession.delete("mapper.cart.deleteCartItem", c_id);
	}
}
