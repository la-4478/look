package com.lookmarket.event.dao;

import java.util.List;

import org.springframework.dao.DataAccessException;

import com.lookmarket.event.vo.EventPostVO;

public interface EventDAO {
    void insertPromotionPost(EventPostVO eventPostVO) throws DataAccessException;
    List<EventPostVO> selectPromotionPostList() throws DataAccessException;
    EventPostVO selectPromotionPostById(int postId) throws DataAccessException;
    void updatePromotionPost(EventPostVO eventPostVO) throws DataAccessException;
    void deletePromotionPost(int postId) throws DataAccessException;
}
