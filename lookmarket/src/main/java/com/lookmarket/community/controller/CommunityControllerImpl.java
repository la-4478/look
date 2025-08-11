package com.lookmarket.community.controller;

import java.io.File;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.lookmarket.community.Service.CommunityService;
import com.lookmarket.community.vo.BlackBoardVO;
import com.lookmarket.community.vo.ReviewVO;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

	@Controller("communityController")
	@RequestMapping(value="/community")
	public class CommunityControllerImpl implements CommunityController{
		@Autowired
		private CommunityService communityService;
		@Autowired
		private BlackBoardVO blackBoardVO;
		@Autowired
		private ReviewVO reviewVO;
		
		@Override
		@RequestMapping(value="/communityList.do", method=RequestMethod.GET)
		public ModelAndView communityList(HttpServletRequest request, HttpServletResponse response)  throws Exception{
			//커뮤니티리스트
			HttpSession session;
			ModelAndView mav = new ModelAndView();
			String layout = "common/layout";
			mav.setViewName(layout);
			String viewName = "/community/communityList";
			mav.addObject("viewName", viewName);

			List<ReviewVO> reviewList = communityService.communityList();
			mav.addObject("communityList", reviewList);
			
			session = request.getSession();
			session.setAttribute("sideMenu", "reveal");
			session.setAttribute("sideMenu_option", "community");
			
			return mav;
		}
		@Override
		@RequestMapping(value="/communityDetail.do", method=RequestMethod.GET)
		public ModelAndView communityDetail(@RequestParam("r_id") String r_id, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes)  throws Exception{
			//커뮤니티 상세정보
			HttpSession session = request.getSession();
			String current_id = (String) session.getAttribute("current_id");
			session.setAttribute("userId", current_id);
			
			ModelAndView mav = new ModelAndView();
			String layout = "common/layout";
			mav.setViewName(layout);
			String viewName = (String)request.getAttribute("viewName");
			mav.addObject("viewName", "/community/communityDetail");
			
			reviewVO = communityService.communityDetail(r_id);
			int hit = Integer.parseInt(reviewVO.getR_hit()) + 1;
			
			if("1".equals(reviewVO.getR_secret())) {
				//공개
				mav.addObject("review", reviewVO);
				communityService.upHit(Integer.parseInt(r_id), hit);
			}else {
				//비공개
				if(reviewVO.getM_id().equals(current_id)) {
					mav.addObject("review", reviewVO);
				communityService.upHit(Integer.parseInt(r_id), hit); 

				}else {
			    	redirectAttributes.addFlashAttribute("message", "비공개 리뷰 입니다.");
			    	mav.setViewName("redirect:/community/communityList.do");		    	
				}
			}
			
			session.setAttribute("sideMenu", "reveal");
			session.setAttribute("sideMenu_option", "community");
			
			return mav;
		}

		@RequestMapping(value="/communityUpdateForm.do", method=RequestMethod.GET)
		public ModelAndView communityUpdateForm(@RequestParam("r_id") String r_id, HttpServletRequest request, HttpServletResponse response) throws Exception {
		    HttpSession session = request.getSession();

		    ModelAndView mav = new ModelAndView();
		    String layout = "common/layout";
		    mav.setViewName(layout);

		    // 기존 리뷰 데이터 가져오기
		    ReviewVO review = communityService.communityDetail(r_id);
		    mav.addObject("review", review);

		    // 로그인한 사용자 아이디
		    String current_id = (String) session.getAttribute("current_id");
		    mav.addObject("current_id", current_id);

		    // 뷰 이름 지정
		    mav.addObject("viewName", "/community/communityUpdateForm");

		    session.setAttribute("sideMenu", "reveal");
		    session.setAttribute("sideMenu_option", "community");

		    return mav;
		}

		
		@Override
		@RequestMapping(value="/blackBoardList.do", method=RequestMethod.GET)
		public ModelAndView blackBoardList(HttpServletRequest request, HttpServletResponse response)  throws Exception{
			//고충방 리스트
			HttpSession session;
			ModelAndView mav = new ModelAndView();
			String layout = "common/layout";
			mav.setViewName(layout);
			String viewName = (String)request.getAttribute("viewName");
			mav.addObject("viewName", viewName);
			
			List<BlackBoardVO> blackBoardList = communityService.blackBoardList();
			mav.addObject("blackBoardList", blackBoardList);
			
			session = request.getSession();
			session.setAttribute("sideMenu", "reveal");
			session.setAttribute("sideMenu_option", "community_admin");
			
			return mav;
		}
		
		@Override
		@RequestMapping(value="/blackBoardDetail.do", method=RequestMethod.GET)
		public ModelAndView blackBoardDetail(HttpServletRequest request, HttpServletResponse response)  throws Exception{
			//고충방 상세
			HttpSession session;
			ModelAndView mav = new ModelAndView();
			String layout = "common/layout";
			mav.setViewName(layout);
			String viewName = (String)request.getAttribute("viewName");
			mav.addObject("viewName", viewName);
			
			session = request.getSession();
			session.setAttribute("sideMenu", "reveal");
			session.setAttribute("sideMenu_option", "community_admin");
			
			return mav;
		}
		
		@Override
		@RequestMapping(value="/blackBoardUpdateForm.do", method=RequestMethod.GET)
		public ModelAndView blackBoardUpdateForm(HttpServletRequest request, HttpServletResponse response) throws Exception{
			//고충방 수정
			HttpSession session;
			ModelAndView mav = new ModelAndView();
			String layout = "common/layout";
			mav.setViewName(layout);
			String viewName = (String)request.getAttribute("viewName");
			mav.addObject("viewName", viewName);
			
			session = request.getSession();
			session.setAttribute("sideMenu", "reveal");
			session.setAttribute("sideMenu_option", "community_admin");
			
			return mav;
		}
		
		@Override
		@RequestMapping(value="/communityAddForm.do", method=RequestMethod.GET)
		public ModelAndView communityAddForm(HttpServletRequest request, HttpServletResponse response) throws Exception{
			HttpSession session;
			ModelAndView mav = new ModelAndView();
			String layout = "common/layout";
			mav.setViewName(layout);
			String viewName = (String)request.getAttribute("viewName");
			mav.addObject("viewName", viewName);
			
			session = request.getSession();
			session.setAttribute("sideMenu", "reveal");
			session.setAttribute("sideMenu_option", "community");
			
			return mav;
		}
		
		@Override
		@RequestMapping(value="/blackBoardAddForm.do", method=RequestMethod.GET)
		public ModelAndView blackBoardAddForm(HttpServletRequest request, HttpServletResponse response) throws Exception{
			HttpSession session;
			ModelAndView mav = new ModelAndView();
			String layout = "common/layout";
			mav.setViewName(layout);
			String viewName = (String)request.getAttribute("viewName");
			mav.addObject("viewName", viewName);
			
			session = request.getSession();
			session.setAttribute("sideMenu", "reveal");
			session.setAttribute("sideMenu_option", "community_admin");
			
			return mav;
		}

		@Override
		@RequestMapping(value="/insertReview.do", method=RequestMethod.POST)
		public ModelAndView insertReview(MultipartHttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) throws Exception {
		    HttpSession session = request.getSession();
		    String current_id = (String) session.getAttribute("current_id");

		    if (current_id == null) {
		        redirectAttributes.addFlashAttribute("message", "로그인이 필요합니다.");
		        return new ModelAndView("redirect:/member/loginForm.do");
		    }

		    String r_title = request.getParameter("r_title");
		    String g_id = request.getParameter("g_id");
		    String r_star = request.getParameter("r_star");
		    String r_content = request.getParameter("r_content");
		    String r_secret = request.getParameter("r_secret");

		    MultipartFile file = request.getFile("r_filename");

		    ReviewVO reviewVO = new ReviewVO();
		    reviewVO.setM_id(current_id);
		    reviewVO.setR_title(r_title);
		    reviewVO.setG_id(g_id);
		    reviewVO.setR_star(r_star);
		    reviewVO.setR_content(r_content);
		    reviewVO.setR_secret("public".equals(r_secret) ? "1" : "0");
		    reviewVO.setR_hit("0");

		    if (file != null && !file.isEmpty()) {
		        String rootPath = request.getServletContext().getRealPath("/");
		        String uploadPath = rootPath + "upload/review/";

		        File dir = new File(uploadPath);
		        if (!dir.exists()) {
		            dir.mkdirs();
		        }

		        String originalFilename = file.getOriginalFilename();
		        String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
		        String storedFilename = UUID.randomUUID().toString() + extension;

		        File saveFile = new File(uploadPath + storedFilename);
		        file.transferTo(saveFile);

		        reviewVO.setR_filename(storedFilename);
		        reviewVO.setR_filetype(file.getContentType());
		    } else {
		        reviewVO.setR_filename(null);
		        reviewVO.setR_filetype(null);
		    }

		    communityService.insertReview(reviewVO);

		    redirectAttributes.addFlashAttribute("message", "리뷰가 등록되었습니다.");
		    return new ModelAndView("redirect:/community/communityList.do");
		}
		@Override
		public ModelAndView communityUpdateForm(HttpServletRequest request, HttpServletResponse response)
				throws Exception {
			// TODO Auto-generated method stub
			return null;
		}
		
		@RequestMapping(value="/communityUpdate.do", method=RequestMethod.POST)
		public ModelAndView communityUpdate(MultipartHttpServletRequest request, RedirectAttributes redirectAttributes) throws Exception {
		    HttpSession session = request.getSession();
		    String current_id = (String) session.getAttribute("current_id");

		    if (current_id == null) {
		        redirectAttributes.addFlashAttribute("message", "로그인이 필요합니다.");
		        return new ModelAndView("redirect:/member/loginForm.do");
		    }

		    String r_id = request.getParameter("r_id");
		    String r_title = request.getParameter("r_title");
		    String g_id = request.getParameter("g_id");
		    String r_star = request.getParameter("r_star");
		    String r_content = request.getParameter("r_content");
		    String r_secret = request.getParameter("r_secret");

		    MultipartFile file = request.getFile("r_filename");

		    ReviewVO reviewVO = new ReviewVO();
		    reviewVO.setR_id(r_id);
		    reviewVO.setM_id(current_id);
		    reviewVO.setR_title(r_title);
		    reviewVO.setG_id(g_id);
		    reviewVO.setR_star(r_star);
		    reviewVO.setR_content(r_content);
		    reviewVO.setR_secret("public".equals(r_secret) ? "1" : "0");

		    if (file != null && !file.isEmpty()) {
		        String rootPath = request.getServletContext().getRealPath("/");
		        String uploadPath = rootPath + "upload/review/";

		        File dir = new File(uploadPath);
		        if (!dir.exists()) {
		            dir.mkdirs();
		        }

		        String originalFilename = file.getOriginalFilename();
		        String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
		        String storedFilename = UUID.randomUUID().toString() + extension;

		        File saveFile = new File(uploadPath + storedFilename);
		        file.transferTo(saveFile);

		        reviewVO.setR_filename(storedFilename);
		        reviewVO.setR_filetype(file.getContentType());
		    } else {
		        reviewVO.setR_filename(null);
		        reviewVO.setR_filetype(null);
		    }

		    communityService.updateReview(reviewVO);

		    redirectAttributes.addFlashAttribute("message", "리뷰가 수정되었습니다.");
		    return new ModelAndView("redirect:/community/communityDetail.do?r_id=" + r_id);
		}
		@RequestMapping(value="/communityDelete.do", method=RequestMethod.POST)
		public ModelAndView communityDelete(@RequestParam("r_id") String r_id, HttpServletRequest request, RedirectAttributes redirectAttributes) throws Exception {
		    HttpSession session = request.getSession();
		    String current_id = (String) session.getAttribute("current_id");
		    
		    if(current_id == null) {
		        redirectAttributes.addFlashAttribute("message", "로그인이 필요합니다.");
		        return new ModelAndView("redirect:/member/loginForm.do");
		    }

		    // 현재 로그인한 유저가 글 작성자인지 확인
		    ReviewVO review = communityService.communityDetail(r_id);
		    if (!current_id.equals(review.getM_id())) {
		        redirectAttributes.addFlashAttribute("message", "삭제 권한이 없습니다.");
		        return new ModelAndView("redirect:/community/communityDetail.do?r_id=" + r_id);
		    }

		    communityService.deleteReview(r_id);
		    redirectAttributes.addFlashAttribute("message", "리뷰가 삭제되었습니다.");
		    return new ModelAndView("redirect:/community/communityList.do");
		}


}
