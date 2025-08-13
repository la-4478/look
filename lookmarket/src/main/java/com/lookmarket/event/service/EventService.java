package com.lookmarket.event.service;

import java.util.List;

import com.lookmarket.event.vo.EventPostVO;

public interface EventService {
	public void insertPromotionPost(EventPostVO eventPostVO) throws Exception;
    public List<EventPostVO> selectPromotionPostList() throws Exception;
    public EventPostVO selectPromotionPostById(String postId) throws Exception;
    public void updatePromotionPost(EventPostVO eventPostVO) throws Exception;
    public void deletePromotionPost(String postId) throws Exception;
}
