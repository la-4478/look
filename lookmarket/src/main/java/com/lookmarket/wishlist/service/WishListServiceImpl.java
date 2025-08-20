package com.lookmarket.wishlist.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lookmarket.wishlist.dao.WishListDAO;
import com.lookmarket.wishlist.vo.WishListVO;

@Service("wishListService")
@Transactional
public class WishListServiceImpl implements WishListService  {

    @Autowired
    private WishListDAO wishListDAO;

    @Override
    public List<WishListVO> getWishListByMember(String mId) throws Exception {
        return wishListDAO.selectWishListByMember(mId);
    }
    
    @Override
    public List<Integer> getWishlistIdsByMember(String mId) throws Exception {
        return wishListDAO.selectWishlistIdsByMember(mId);
    }

    @Override
    public int removeWishList(int wId) throws Exception {
        return wishListDAO.deleteWishList(wId);
    }
    
    @Override
    public boolean isWished(String mId, int gId) throws Exception {
        return wishListDAO.isWished(mId, gId) > 0;
    }

    @Override
    public void addWish(String mId, int gId) throws Exception {
        wishListDAO.insertWish(mId, gId);
    }

    @Override
    public void removeWish(String mId, int gId) throws Exception {
        wishListDAO.deleteWish(mId, gId);
    }
}
