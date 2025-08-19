package com.lookmarket.wishlist.dao;

import java.util.List;
import org.springframework.dao.DataAccessException;
import com.lookmarket.wishlist.vo.WishListVO;

public interface WishListDAO {
    List<WishListVO> selectWishListByMember(String mId) throws DataAccessException;
    int deleteWishList(int wId) throws DataAccessException;
}
