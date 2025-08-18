package com.lookmarket.wishlist.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.lookmarket.wishlist.dao.WishListDAO;
import com.lookmarket.wishlist.vo.WishListVO;

@Service("wishListService")
@Transactional
public class WishListServiceImpl implements WishListService {

    @Autowired
    private WishListDAO wishListDAO;

    @Override
    public List<WishListVO> getWishListByMember(String mId) {
        return wishListDAO.selectWishListByMember(mId);
    }

    @Override
    public int removeWishList(int wId) {
        return wishListDAO.deleteWishList(wId);
    }
}
