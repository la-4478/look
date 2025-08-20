package com.lookmarket.wishlist.dao;

import java.util.List;

import org.springframework.dao.DataAccessException;

import com.lookmarket.wishlist.vo.WishListVO;

public interface WishListDAO {
    
    // 회원별 위시리스트 조회
    List<WishListVO> selectWishListByMember(String mId) throws DataAccessException;
    
    public List<Integer> selectWishlistIdsByMember(String mId) throws DataAccessException;
    
    // 특정 위시리스트 항목 삭제
    int deleteWishList(int wId) throws DataAccessException;
    
    // 이미 찜했는지 확인
    int isWished(WishListVO wishVO) throws DataAccessException;
    
    // 찜 추가
    void insertWish(WishListVO wishVO) throws DataAccessException;
    
    // 찜 해제
    void deleteWish(String mId, int gId) throws DataAccessException;
}
