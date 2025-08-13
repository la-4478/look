package com.lookmarket.event.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import com.lookmarket.event.vo.EventPostVO;

@Repository("eventDAO")
public class EventDAOImpl implements EventDAO {

    @Autowired
    private SqlSession sqlSession;

    @Override
    public void insertPromotionPost(EventPostVO eventPostVO) throws DataAccessException {
        sqlSession.insert("com.lookmarket.event.dao.EventDAO.insertPromotionPost", eventPostVO);
    }

    @Override
    public List<EventPostVO> selectPromotionPostList() throws DataAccessException {
        return sqlSession.selectList("com.lookmarket.event.dao.EventDAO.selectPromotionPostList");
    }

    @Override
    public EventPostVO selectPromotionPostById(int postId) throws DataAccessException {
        return sqlSession.selectOne("com.lookmarket.event.dao.EventDAO.selectPromotionPostById", postId);
    }

    @Override
    public void updatePromotionPost(EventPostVO eventPostVO) throws DataAccessException {
        sqlSession.update("com.lookmarket.event.dao.EventDAO.updatePromotionPost", eventPostVO);
    }

    @Override
    public void deletePromotionPost(int postId) throws DataAccessException {
        sqlSession.delete("com.lookmarket.event.dao.EventDAO.deletePromotionPost", postId);
    }
}
