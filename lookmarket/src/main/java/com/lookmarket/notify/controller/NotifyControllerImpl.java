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
public class NotifyControllerImpl implements NotifyController {
    
	@Autowired
	private NotifyService notifyService;

	@Override
    @GetMapping("/unread-count.do")
    @ResponseBody
    public int unreadCount(HttpSession session)throws Exception{
        String mId = (String) session.getAttribute("current_id");
        if (mId == null) return 0;
        return notifyService.countUnread(mId);
    }
	@Override
	@GetMapping("/unread-list.do")
	@ResponseBody
	public List<NotifyVO> unreadList(HttpSession session){
	    String mId = (String) session.getAttribute("current_id");
	    System.out.println("[NOTIFY] unread-list mId=" + mId);
	    List<NotifyVO> list = notifyService.listUnread(mId);
	    System.out.println("[NOTIFY] size=" + (list==null?0:list.size()) + " -> " + list);
	    return list == null ? List.of() : list;
    }
	@Override
    @PostMapping("/read.do")
    @ResponseBody
    public void markRead(@RequestParam("n_id") long nId, HttpSession session)throws Exception{
        String mId = (String) session.getAttribute("current_id");
        notifyService.markRead(nId, mId); // ✅ 두 개 인자 전달
    }
	@Override
    @PostMapping("/read-all.do")
    @ResponseBody
    public void markAllRead(HttpSession session)throws Exception{
        String mId = (String) session.getAttribute("current_id");
        if (mId != null) notifyService.markAllRead(mId);
    }
}
