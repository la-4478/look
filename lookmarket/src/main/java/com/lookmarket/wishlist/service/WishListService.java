package com.lookmarket.wishlist.service;

import java.util.List;
import org.springframework.dao.DataAccessException;
import com.lookmarket.wishlist.vo.WishListVO;

public interface WishListService {
    List<WishListVO> getWishListByMember(String mId) throws DataAccessException;
    int removeWishList(int wId) throws DataAccessException;
}
