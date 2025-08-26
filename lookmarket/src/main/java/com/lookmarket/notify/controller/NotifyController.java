package com.lookmarket.notify.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lookmarket.notify.service.NotifyService;
import com.lookmarket.notify.vo.NotifyVO;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/notify")
public interface NotifyController {

	public int unreadCount(HttpSession session) throws Exception;
	public List<NotifyVO> unreadList(HttpSession session) throws Exception;
	public void markRead(long nId, HttpSession session) throws Exception;
	public void markAllRead(HttpSession session) throws Exception;

}

