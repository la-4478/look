package com.lookmarket.event.controller;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.lookmarket.event.service.EventService;
import com.lookmarket.event.vo.CouponVO;
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
	public ModelAndView promotionDetail(@RequestParam(value="postId", required=true) Integer postId,
	                                    HttpServletRequest request, HttpServletResponse response) throws Exception {
	    ModelAndView mav = new ModelAndView("common/layout");

	    EventPostVO promo = eventService.selectPromotionPostById(postId);
	    mav.addObject("promo", promo);
	    mav.addObject("viewName", "event/promotionDetail");

	    HttpSession session = request.getSession();
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


	        // 저장할 파일 객체 생성
	        File destFile = new File(uploadPath, originalFilename);

	        // 파일 저장
	        imageFile.transferTo(destFile);

	        // VO에 저장된 파일명 세팅
	        eventPostVO.setPromoBannerImg(originalFilename);
	    }

	    // 서비스 호출 (DB insert)
	    eventService.insertPromotionPost(eventPostVO);

	    return new ModelAndView("redirect:/event/promotionList.do");
	}

	// 수정폼 띄우기
	@Override
	@RequestMapping(value="/promotionUpdateForm.do", method = {RequestMethod.GET, RequestMethod.POST})
	public ModelAndView promotionUpdateForm(@RequestParam(value="postId", required=true) Integer postId,
	                                        HttpServletRequest request, HttpServletResponse response) throws Exception {
	    ModelAndView mav = new ModelAndView("common/layout");

	    EventPostVO promo = eventService.selectPromotionPostById(postId);
	    mav.addObject("promo", promo);
	    mav.addObject("viewName", "event/promotionUpdateForm");

	    HttpSession session = request.getSession();
	    session.setAttribute("sideMenu", "reveal");
	    session.setAttribute("sideMenu_option", "event");

	    return mav;
	}


	// 수정 처리
	@Override
	@RequestMapping(value="/updatePromotionPost.do", method = RequestMethod.POST)
	public ModelAndView updatePromotionPost(HttpServletRequest request, HttpServletResponse response,
	                                        @ModelAttribute EventPostVO eventPostVO,
	                                        @RequestParam(value="imageFile", required=false) MultipartFile imageFile) throws Exception {
	    request.setCharacterEncoding("utf-8");

	    // 이미지 파일 업데이트 처리 (업로드 되어 있으면 새 이미지로 변경)
	    if (imageFile != null && !imageFile.isEmpty()) {
	        String uploadDir = "C:/lookmarket_resources/event_banners/";
	        File uploadPath = new File(uploadDir);
	        if (!uploadPath.exists()) uploadPath.mkdirs();

	        String originalFilename = imageFile.getOriginalFilename();
	        String extension = "";
	        int dotIndex = originalFilename.lastIndexOf(".");
	        if (dotIndex >= 0) {
	            extension = originalFilename.substring(dotIndex).toLowerCase();
	        }

	        File destFile = new File(uploadPath, originalFilename);
	        imageFile.transferTo(destFile);

	        eventPostVO.setPromoBannerImg(originalFilename);
	    } else {
	        // 이미지 변경 없으면 기존 이미지 유지 필요 → DB에서 기존값 조회 후 세팅하거나 폼에서 hidden으로 받기
	        EventPostVO existing = eventService.selectPromotionPostById(eventPostVO.getPostId());
	        eventPostVO.setPromoBannerImg(existing.getPromoBannerImg());
	    }

	    eventService.updatePromotionPost(eventPostVO);

	    return new ModelAndView("redirect:/event/promotionDetail.do?postId=" + eventPostVO.getPostId());
	}

	// 삭제 처리 메서드 추가 (GET 방식 허용)
	@RequestMapping(value = "/deletePromotionPost.do", method = RequestMethod.GET)
	public ModelAndView deletePromotionPost(@RequestParam("postId") int postId,
	                                        HttpServletRequest request,
	                                        HttpServletResponse response) throws Exception {
	    // 프로모션 삭제 서비스 호출
	    eventService.deletePromotionPost(postId);

	    // 삭제 후 목록 페이지로 리다이렉트
	    return new ModelAndView("redirect:/event/promotionList.do");
	}
	
	// 쿠폰 목록 조회
	@Override
	@RequestMapping(value="/couponList.do", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView couponList(@RequestParam(value="postId", required=false) Integer postId,
	                               HttpServletRequest request,
	                               HttpServletResponse response) throws Exception {
	    HttpSession session = request.getSession();
	    ModelAndView mav = new ModelAndView("common/layout");

	    List<CouponVO> couponList = eventService.selectCouponListByPostId(postId);
	    mav.addObject("couponList", couponList);
	    mav.addObject("postId", postId);
	    mav.addObject("viewName", "event/couponList");

	    // 사이드 메뉴 세팅 (너가 다른 메서드들에서 일관적으로 쓰는 세션 세팅 포함)
	    session.setAttribute("sideMenu", "reveal");
	    session.setAttribute("sideMenu_option", "event");

	    return mav;
	}


	// 쿠폰 등록 폼
	@RequestMapping(value="/couponAddForm.do", method = RequestMethod.GET)
	public ModelAndView couponAddForm(HttpServletRequest request,
	                                  HttpServletResponse response) throws Exception {
	    ModelAndView mav = new ModelAndView("common/layout");

	    mav.addObject("viewName", "event/couponAddForm");

	    return mav;
	}

	// 쿠폰 등록 처리
	@RequestMapping(value="/insertCoupon.do", method = RequestMethod.POST)
	public ModelAndView insertCoupon(@ModelAttribute CouponVO couponVO,
	                                 HttpServletRequest request,
	                                 HttpServletResponse response) throws Exception {
	    request.setCharacterEncoding("utf-8");

	    eventService.insertCoupon(couponVO);

	    return new ModelAndView("redirect:/event/couponList.do?postId=" + couponVO.getPostId());
	}

	// 쿠폰 상세 보기
	@RequestMapping(value="/couponDetail.do", method = RequestMethod.GET)
	public ModelAndView couponDetail(@RequestParam("promoId") int promoId,
	                                 HttpServletRequest request,
	                                 HttpServletResponse response) throws Exception {
	    ModelAndView mav = new ModelAndView("common/layout");

	    CouponVO coupon = eventService.selectCouponById(promoId); // 상세 조회
	    mav.addObject("coupon", coupon);
	    mav.addObject("viewName", "event/couponDetail");

	    return mav;
	}

	// 쿠폰 수정 폼
	@RequestMapping(value="/couponUpdateForm.do", method = RequestMethod.GET)
	public ModelAndView couponUpdateForm(@RequestParam("promoId") int promoId,
	                                     HttpServletRequest request,
	                                     HttpServletResponse response) throws Exception {
	    ModelAndView mav = new ModelAndView("common/layout");

	    CouponVO coupon = eventService.selectCouponById(promoId);
	    mav.addObject("coupon", coupon);
	    mav.addObject("viewName", "event/couponUpdateForm");

	    return mav;
	}

	// 쿠폰 수정 처리
	@RequestMapping(value="/updateCoupon.do", method = RequestMethod.POST)
	public ModelAndView updateCoupon(@ModelAttribute CouponVO couponVO,
	                                 HttpServletRequest request,
	                                 HttpServletResponse response) throws Exception {
	    request.setCharacterEncoding("utf-8");

	    eventService.updateCoupon(couponVO);

	    return new ModelAndView("redirect:/event/couponList.do?postId=" + couponVO.getPostId());
	}

	// 쿠폰 삭제
	@RequestMapping(value="/deleteCoupon.do", method = RequestMethod.GET)
	public ModelAndView deleteCoupon(@RequestParam("promoId") int promoId,
	                                 @RequestParam("postId") int postId,
	                                 HttpServletRequest request,
	                                 HttpServletResponse response) throws Exception {

	    eventService.deleteCoupon(promoId);

	    return new ModelAndView("redirect:/event/couponList.do?postId=" + postId);
	}


	@InitBinder
	public void initBinder(WebDataBinder binder) {
	    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	    dateFormat.setLenient(false);
	    binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
	}
}
