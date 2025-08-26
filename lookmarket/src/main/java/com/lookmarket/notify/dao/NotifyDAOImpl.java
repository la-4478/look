package com.lookmarket.notify.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;   // <- Template 권장
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import com.lookmarket.notify.vo.NotifyVO;

@Repository("notifyDAO") // 서비스에서 @Autowired NotifyDAO notifyDAO; 로 주입받음
public class NotifyDAOImpl implements NotifyDAO {

    private static final String NS = "mapper.notify."; // 매퍼 namespace 접두사

    private final SqlSessionTemplate sqlSession;

    @Autowired
    public NotifyDAOImpl(SqlSessionTemplate sqlSession) {
        this.sqlSession = sqlSession;
    }

    @Override
    public int countUnread(String receiverId) throws DataAccessException {
        return sqlSession.selectOne(NS + "countUnread", receiverId);
    }

    @Override
    public List<NotifyVO> listUnread(String receiverId) throws DataAccessException {
        return sqlSession.selectList(NS + "listUnread", receiverId);
    }

    @Override
    public int markRead(long nId, String receiverId) throws DataAccessException {
        Map<String, Object> p = new HashMap<>();
        p.put("nId", nId);
        p.put("receiverId", receiverId);
        return sqlSession.update(NS + "markRead", p);
    }

    @Override
    public int markAllRead(String receiverId) throws DataAccessException {
        return sqlSession.update(NS + "markAllRead", receiverId);
    }

    @Override
    public int insert(NotifyVO n) throws DataAccessException {
        return sqlSession.insert(NS + "insert", n);
    }
}
