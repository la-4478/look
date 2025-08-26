package com.lookmarket.notify.service;

import java.util.List;

import com.lookmarket.notify.vo.NotifyVO;

public interface NotifyService {
    int countUnread(String receiverId);
    List<NotifyVO> listUnread(String receiverId);
    void markRead(long nId, String receiverId);
    void markAllRead(String receiverId);
}
