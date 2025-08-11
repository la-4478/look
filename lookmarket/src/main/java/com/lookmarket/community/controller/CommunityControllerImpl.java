package com.lookmarket.community.controller;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
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
		
		ModelAndView mav = new ModelAndView();
		String layout = "common/layout";
		mav.setViewName(layout);
		String viewName = (String)request.getAttribute("viewName");
		mav.addObject("viewName", viewName);
		
		reviewVO = communityService.communityDetail(r_id);
		int hit = Integer.parseInt(reviewVO.getR_hit()) + 1;
		
		if("1".equals(reviewVO.getR_secret())) {
			//공개
			mav.addObject("review", reviewVO);
			communityService.upHit(r_id, String.valueOf(hit));
		}else {
			//비공개
			if(reviewVO.getM_id().equals(current_id)) {
				mav.addObject("review", reviewVO);
				communityService.upHit(r_id, String.valueOf(hit));
			}else {
		    	redirectAttributes.addFlashAttribute("message", "비공개 리뷰 입니다.");
		    	mav.setViewName("redirect:/community/communityList.do");		    	
			}
		}
		
		session.setAttribute("sideMenu", "reveal");
		session.setAttribute("sideMenu_option", "community");
		
		return mav;
	}

	@Override
	@RequestMapping(value="/communityUpdateForm.do", method=RequestMethod.GET)
	public ModelAndView communityUpdateForm(HttpServletRequest request, HttpServletResponse response)  throws Exception{
		//커뮤니티 수정
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
	@RequestMapping(value="/insertReview.do", method = RequestMethod.POST)
	public ModelAndView insertReview(
	    @RequestParam("r_filename1") MultipartFile uploadFile,
	    @ModelAttribute ReviewVO reviewVO,
	    HttpServletRequest request
	) throws Exception {

	    HttpSession session = request.getSession();
	    String m_id = (String) session.getAttribute("current_id");
	    
	    if(m_id == null) {
	    	throw new IllegalStateException("로그인 세션이 만료되었거나 로그인 정보가 없습니다");
	    }
	    reviewVO.setM_id(m_id);
	    
	    if (uploadFile != null && !uploadFile.isEmpty()) {
	        String fileName = uploadFile.getOriginalFilename();
	        String fileType = uploadFile.getContentType();

	        String uploadDir = "C:\\lookmarket_resources\\board";
	        File dir = new File(uploadDir);
	        if (!dir.exists()) {
	            dir.mkdirs();
	        }

	        File file = new File(uploadDir, fileName);
	        uploadFile.transferTo(file);

	        reviewVO.setR_filename1(fileName);
	        reviewVO.setR_filetype(fileType);
	    }

	    String secretValue = request.getParameter("r_secret");
	    reviewVO.setR_secret("private".equals(secretValue) ? "0" : "1");

	    String currentDate = new SimpleDateFormat("yyyy-MM-dd").format(new java.util.Date());
	   //reviewVO.setM_id(m_id);
	    reviewVO.setR_date(currentDate);
	    reviewVO.setR_hit("0");

	    communityService.insertReview(reviewVO);

	    return new ModelAndView("redirect:/community/communityList.do");
	}

}
