package com.lookmarket.wishlist.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import com.lookmarket.wishlist.vo.WishListVO;

@Repository
public class WishListDAOImpl implements WishListDAO {

    @Autowired
    private SqlSession sqlSession;

    @Override
    public List<WishListVO> selectWishListByMember(String mId) {
        return sqlSession.selectList("mapper.wishlist.selectWishListByMember", mId);
    }
    @Override
    public List<Integer> selectWishlistIdsByMember(String mId) {
    	return sqlSession.selectList("mapper.wishlist.selectWishlistIdsByMember", mId);
    }

    @Override
    public int deleteWishList(int wId) {
        return sqlSession.delete("mapper.wishlist.deleteWishList", wId);
    }

    @Override
    public int isWished(WishListVO wishVO) throws DataAccessException {
        return sqlSession.selectOne("mapper.wishlist.isWished", wishVO);
    }

    @Override
    public void insertWish(WishListVO wishVO) throws DataAccessException {
        sqlSession.insert("mapper.wishlist.insertWish", wishVO);
    }

    @Override
    public void deleteWish(String mId, int gId) throws DataAccessException {
        Map<String, Object> map = new HashMap<>();
        map.put("mId", mId);
        map.put("gId", gId);
        sqlSession.delete("mapper.wishlist.deleteWish", map);
    }
}
