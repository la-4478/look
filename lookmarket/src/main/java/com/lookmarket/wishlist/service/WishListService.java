package com.lookmarket.wishlist.service;

import java.util.List;
import org.springframework.dao.DataAccessException;
import com.lookmarket.wishlist.vo.WishListVO;

public interface WishListService {
    List<WishListVO> getWishListByMember(String mId) throws Exception;
    public List<Integer> getWishlistIdsByMember(String mId) throws Exception;
    int removeWishList(int wId) throws Exception;
    boolean isWished(WishListVO wishVO) throws Exception;
    void addWish(WishListVO wishVO) throws Exception;
    void removeWish(String m_id, int gId) throws Exception;

}
