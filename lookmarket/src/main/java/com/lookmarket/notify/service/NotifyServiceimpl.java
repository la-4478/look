package com.lookmarket.notify.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lookmarket.notify.dao.NotifyDAO;
import com.lookmarket.notify.vo.NotifyVO;

@Service
public class NotifyServiceimpl implements NotifyService {
	@Autowired
	private NotifyDAO notifyDAO;

    @Override
    public int countUnread(String receiverId) {
        return notifyDAO.countUnread(receiverId);
    }

    @Override
    public List<NotifyVO> listUnread(String receiverId) {
        return notifyDAO.listUnread(receiverId);
    }

    @Override
    public void markRead(long nId, String receiverId) {
        // 소유권 확인 로직 추가
        notifyDAO.markRead(nId, receiverId);
    }

    @Override
    public void markAllRead(String receiverId) {
        notifyDAO.markAllRead(receiverId);
    }

}
