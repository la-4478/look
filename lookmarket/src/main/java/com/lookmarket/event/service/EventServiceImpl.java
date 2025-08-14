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
    public EventPostVO selectPromotionPostById(Integer postId) throws Exception {
        return eventDAO.selectPromotionPostById(postId);
    }

    @Override
    public void updatePromotionPost(EventPostVO eventPostVO) throws Exception {
        eventDAO.updatePromotionPost(eventPostVO);
    }

    @Override
    public void deletePromotionPost(Integer postId) throws Exception {
        // DB에서 삭제 처리 로직
        eventDAO.deletePromotionPost(postId);
    }

}
