package com.lookmarket.notify.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import com.lookmarket.notify.vo.NotifyVO;

public interface NotifyDAO {
    int countUnread(String receiverId) throws DataAccessException;
    List<NotifyVO> listUnread(String receiverId) throws DataAccessException;
    int markRead(@Param("nId") long nId, @Param("receiverId") String receiverId) throws DataAccessException;
    int markAllRead(String receiverId) throws DataAccessException;
	int insert(NotifyVO n) throws DataAccessException;
}