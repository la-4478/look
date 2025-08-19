package com.lookmarket.wishlist.dao;

import java.util.List;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
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
    public int deleteWishList(int wId) {
        return sqlSession.delete("mapper.wishlist.deleteWishList", wId);
    }
}
