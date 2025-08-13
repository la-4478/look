package com.lookmarket.event.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lookmarket.event.dao.EventDAO;
import com.lookmarket.event.vo.EventPostVO;

@Service("eventService")
public class EventServiceImpl implements EventService {

    @Autowired
    private EventDAO eventDAO;

    @Override
    public void insertPromotionPost(EventPostVO eventPostVO) throws Exception {
        // DB에 저장
    	eventDAO.insertPromotionPost(eventPostVO);
    }

    @Override
    public List<EventPostVO> selectPromotionPostList() throws Exception {
        return eventDAO.selectPromotionPostList();
    }

    @Override
    public EventPostVO selectPromotionPostById(String postId) throws Exception {
        return eventDAO.selectPromotionPostById(Integer.parseInt(postId));
    }

    @Override
    public void updatePromotionPost(EventPostVO eventPostVO) throws Exception {
        eventDAO.updatePromotionPost(eventPostVO);
    }

    @Override
    public void deletePromotionPost(String postId) throws Exception {
        eventDAO.deletePromotionPost(Integer.parseInt(postId));
    }
}
