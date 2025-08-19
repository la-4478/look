package com.lookmarket.mypage.dao;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import com.lookmarket.community.vo.ReviewVO;
import com.lookmarket.mypage.vo.MyPageVO;
import com.lookmarket.order.vo.OrderItemVO;
import com.lookmarket.order.vo.OrderVO;
import com.lookmarket.wishlist.vo.WishListVO;

@Repository("myPageDAO")
public class MyPageDAOImpl implements MyPageDAO{
	@Autowired
	private SqlSession sqlSession;
	@Autowired
	private MyPageVO myPageVO;
	
	@Override
	public MyPageVO getMyPageInfo(String current_id) throws DataAccessException{
		myPageVO = (MyPageVO)sqlSession.selectOne("mapper.mypage.getMyPageInfo", current_id);
		return myPageVO;
	}
	
	@Override
	public int updateMyInfo(MyPageVO myPageVO) throws DataAccessException{
		return sqlSession.update("mapper.mypage.updateMyInfo", myPageVO);
	}
	
	@Override
	public int deleteMember(String m_id) throws DataAccessException {
		return sqlSession.update("mapper.mypage.deleteMyInfo", m_id);
	}
	
	@Override
	public List<ReviewVO> selectMyCommunityList(String m_id) throws DataAccessException{
		return sqlSession.selectList("mapper.mypage.selectMyCommunityList", m_id);
	}
	
	// ---------------- 주문 관련 추가 ----------------
    @Override
    public List<OrderVO> getOrdersByMemberId(String m_id) throws DataAccessException {
        return sqlSession.selectList("mapper.mypage.getOrdersByMemberId", m_id);
    }

    @Override
    public OrderVO getOrderById(int oId) throws DataAccessException {
        return sqlSession.selectOne("mapper.mypage.getOrderById", oId);
    }

    @Override
    public List<OrderItemVO> getOrderItemsByOrderId(int oId) throws DataAccessException {
        return sqlSession.selectList("mapper.mypage.getOrderItemsByOrderId", oId);
    }
    
    @Override
    public List<WishListVO> getMyWishList(String m_id) throws DataAccessException {
		return sqlSession.selectList("mapper.mypage.getMyWishList", m_id);
    }
}
