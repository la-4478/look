package com.lookmarket.event.controller;

import java.io.File;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.lookmarket.event.service.EventService;
import com.lookmarket.event.vo.EventPostVO;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Controller("eventController")
@RequestMapping(value="/event")
public class EventControllerImpl implements EventController{
	@Autowired
    private EventService eventService;

	@Override
	@RequestMapping(value="/promotionList.do", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView promotionList(HttpServletRequest request, HttpServletResponse response) throws Exception{
		//이벤트 리스트 출력
		HttpSession session;
		ModelAndView mav = new ModelAndView();
		String layout = "common/layout";
		mav.setViewName(layout);
		// 이벤트 리스트 조회해서 모델에 담기
	    List<EventPostVO> promotionList = eventService.selectPromotionPostList(); // 이벤트 리스트 조회 서비스 호출
	    mav.addObject("promotionList", promotionList); // JSP에서 이 이름으로 사용

		String viewName = (String)request.getAttribute("viewName");
		mav.addObject("viewName", "event/promotionList");
		
		session = request.getSession();
		session.setAttribute("sideMenu", "reveal");
		session.setAttribute("sideMenu_option", "event");
		
		return mav;
	}
	@Override
	@RequestMapping(value="/promotionDetail.do", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView promotionDetail(HttpServletRequest request, HttpServletResponse response) throws Exception {
	    HttpSession session = request.getSession();
	    ModelAndView mav = new ModelAndView("common/layout");

	    // 🔹 상세 정보 조회용 ID 받기
	    String postId = request.getParameter("id");

	    // 🔹 Service 통해 해당 이벤트 정보 가져오기
	    EventPostVO promo = eventService.selectPromotionPostById(postId);

	    // 🔹 promo 객체를 모델에 담아서 JSP로 전달
	    mav.addObject("promo", promo);
	    mav.addObject("viewName", "event/promotionDetail");

	    session.setAttribute("sideMenu", "reveal");
	    session.setAttribute("sideMenu_option", "event");

	    return mav;
	}
	
	@Override
	@RequestMapping(value="/promotionAddForm.do", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView promotionAddForm(HttpServletRequest request, HttpServletResponse response) throws Exception{
		//이벤트 리스트 출력
		HttpSession session;
		ModelAndView mav = new ModelAndView();
		String layout = "common/layout";
		mav.setViewName(layout);
		mav.addObject("viewName", "event/promotionAddForm");
		String viewName = (String)request.getAttribute("viewName");
		
		session = request.getSession();

		session.setAttribute("sideMenu", "reveal");
		session.setAttribute("sideMenu_option", "event");
		
		return mav;
	}
	@Override
	@RequestMapping(value="/insertPromotionPost.do", method = RequestMethod.POST)
	public ModelAndView insertPromotionPost(HttpServletRequest request, HttpServletResponse response,
	                                       @ModelAttribute EventPostVO eventPostVO,
	                                       @RequestParam("imageFile") MultipartFile imageFile) throws Exception {
	    request.setCharacterEncoding("utf-8");

	    if (imageFile != null && !imageFile.isEmpty()) {
	        // 저장할 서버 경로 (예: C:/lookmarket_resources/event_banners)
	        String uploadDir = "C:/lookmarket_resources/event_banners/";
	        File uploadPath = new File(uploadDir);
	        if (!uploadPath.exists()) uploadPath.mkdirs();

	        // 원본 파일명과 확장자 분리
	        String originalFilename = imageFile.getOriginalFilename();
	        String extension = "";
	        int dotIndex = originalFilename.lastIndexOf(".");
	        if (dotIndex >= 0) {
	            extension = originalFilename.substring(dotIndex).toLowerCase();
	        }

	        // UUID 기반 고유 파일명 생성
	        String storedFileName = UUID.randomUUID().toString() + extension;

	        // 저장할 파일 객체 생성
	        File destFile = new File(uploadPath, storedFileName);

	        // 파일 저장
	        imageFile.transferTo(destFile);

	        // VO에 저장된 파일명 세팅
	        eventPostVO.setPromoBannerImg(storedFileName);
	    }

	    // 서비스 호출 (DB insert)
	    eventService.insertPromotionPost(eventPostVO);

	    return new ModelAndView("redirect:/event/promotionList.do");
	}

}
