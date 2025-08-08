package com.lookmarket.goods.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import com.lookmarket.goods.service.GoodsService;
import com.lookmarket.goods.vo.GoodsVO;
import com.lookmarket.goods.vo.ImageFileVO;
import com.lookmarket.member.vo.MemberVO;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Controller("goodsController")
@RequestMapping(value="/jangbogo")
public class GoodsControllerImpl implements GoodsController{
	private static final String CURR_IMAGE_REPO_PATH = "C:\\lookmarket_resources\\file_repo";
	@Autowired
	private GoodsService goodsService;
	@Autowired
	private GoodsVO goodsVO;
	
	@Override
	@RequestMapping(value="/goodsList.do", method=RequestMethod.GET)
	public ModelAndView goodsList(@RequestParam("category") String category, HttpServletRequest request, HttpServletResponse response) throws Exception{
		//장보고 상품화면(사용자)
		List<GoodsVO> goodsList = new ArrayList<>();
		if(category.equals("all")) {
			goodsList = goodsService.getAllGoods();
		} else if(category.equals("fresh")) {
			goodsList = goodsService.getFreshGoods();
		} else if(category.equals("processed")) {
				goodsList = goodsService.getProcessed();
			} else if(category.equals("living")) {
				goodsList = goodsService.getLiving();
			} else if(category.equals("fashion")) {
				goodsList = goodsService.getFashion();
			} else if(category.equals("local")) {
				goodsList = goodsService.getLocal();			
		}
		List<GoodsVO> goods = goodsService.getAllGoods();
//		ImageFileVO mainimage = goodsService.goodsMainImage();
		ModelAndView mav = new ModelAndView();
		String layout = "common/layout";
		mav.setViewName(layout);
		String viewName = (String)request.getAttribute("viewName");
		mav.addObject("viewName", viewName);
		
		mav.addObject("goodsList", goodsList);
		return mav;
	}
	
	@Override
	@GetMapping("/goodsDetail.do")
	public ModelAndView goodsDetail(@RequestParam("g_id") int gId,
	                                HttpServletRequest request,
	                                HttpServletResponse response) throws Exception {

	    // 1) 상품 로드 & 존재 체크
	    GoodsVO goods = goodsService.getGoodsDetail(gId);
	    if (goods == null) {
	        // 필요에 따라 404 페이지로 보내거나 목록으로 리다이렉트
	        return new ModelAndView("redirect:/goods/list.do");
	        // 혹은: return new ModelAndView("/error/404");
	    }
	    // 상품 메인이미지 불러오기
	    ImageFileVO mainimage = goodsService.goodsMainImage(gId);

	    // 2) 상세 이미지 목록 파싱 (첫 번째는 대표 이미지이므로 skip)
	    List<String> detailImageList = Collections.emptyList();
	    String filenames = goods.getI_file_name();
	    if (filenames != null && !filenames.isBlank()) {
	        detailImageList = Arrays.stream(filenames.split(",", -1)) // 빈 토큰 보존
	                .map(String::trim)
	                .filter(s -> !s.isEmpty())
	                .skip(1) // 대표 이미지 제외
	                .collect(Collectors.toList());
	    }

	    // 3) 레이아웃 + 바디 뷰 세팅 (request attribute에 의존 X)
	    ModelAndView mav = new ModelAndView("/common/layout"); // ← 슬래시 붙이자
	    // 타일즈 안 쓰고 include 방식이면 보통 이런 식으로 body 경로 내려줌
	    mav.addObject("body", "/WEB-INF/views/goods/goodsDetail.jsp");

	    // 4) 모델
	    mav.addObject("goods", goods);
	    mav.addObject("detailImageList", detailImageList);
	    mav.addObject("Mainimage", mainimage);

	    return mav;
	}

	
	@Override
	@RequestMapping(value="/goodsAddForm.do", method=RequestMethod.GET)
	public ModelAndView goodsAddForm(HttpServletRequest request, HttpServletResponse response) throws Exception {
		ModelAndView mav = new ModelAndView();
		String layout = "common/layout";
		mav.setViewName(layout);
		String viewName = (String)request.getAttribute("viewName");
		mav.addObject("viewName", viewName);
	    return mav;
	}
	
	@Override
	@RequestMapping(value="/goodsUpdateForm.do", method=RequestMethod.GET)
	public ModelAndView goodsUpdateForm(HttpServletRequest request, HttpServletResponse response) throws Exception {
	    int g_id = 1;
	    GoodsVO goods = goodsService.getGoodsDetail(g_id);
	    ModelAndView mav = new ModelAndView("jangbogo/goodsUpdateForm");
	    mav.addObject("goods", goods);
	    return mav;
	}
	
	//상품 등록 로직
	@Override
	@RequestMapping(value="/goodsAdd.do", method=RequestMethod.POST)
	public ResponseEntity goodsAdd(MultipartHttpServletRequest multipartRequest, HttpServletResponse response)
			throws Exception {
		multipartRequest.setCharacterEncoding("utf-8");
	    response.setContentType("text/html; charset=UTF-8");
	    String imageFileName = null;

	    Map<String, Object> newGoodsMap = new HashMap<>();
	    Enumeration<?> enu = multipartRequest.getParameterNames();
	    Set<String> intParams = Set.of("g_category", "g_price", "g_stock", "g_status", "g_delivery_price"); // 필요에 따라 추가

	    while (enu.hasMoreElements()) {
	        String name = (String) enu.nextElement();
	        String value = multipartRequest.getParameter(name);

	        if (intParams.contains(name) && value != null && !value.equals("")) {
	            newGoodsMap.put(name, Integer.parseInt(value));
	        } else {
	            newGoodsMap.put(name, value);
	        }
	    }
	    HttpSession session = multipartRequest.getSession();
	    MemberVO memberVO = (MemberVO) session.getAttribute("memberInfo");
	    String reg_id = memberVO.getM_id();

	    // ✅ 이미지 업로드 처리
	    List<ImageFileVO> imageFileList = new ArrayList<>();
	    Iterator<String> fileNames = multipartRequest.getFileNames();

	    while (fileNames.hasNext()) {
	        MultipartFile multipartFile = multipartRequest.getFile(fileNames.next());

	        if (multipartFile != null && !multipartFile.isEmpty()) {
	            String originalName = multipartFile.getOriginalFilename();
	            String ext = originalName.substring(originalName.lastIndexOf("."));
	            String newFileName = UUID.randomUUID().toString() + ext;

	            File tempDir = new File(CURR_IMAGE_REPO_PATH + File.separator + "temp");
	            if (!tempDir.exists()) {
	                tempDir.mkdirs();
	            }

	            File destFile = new File(tempDir, newFileName);
	            multipartFile.transferTo(destFile); // ✅ 실제 파일 저장

	            ImageFileVO imageFileVO = new ImageFileVO();
	            imageFileVO.setI_filename(newFileName);

	            String extension = ext.toLowerCase();
	            if (extension.matches(".jpg|.jpeg|.png|.gif")) {
	                imageFileVO.setI_filetype("image");
	            } else {
	                imageFileVO.setI_filetype("etc");
	            }

	            imageFileList.add(imageFileVO);
	        }
	    }

	    if (!imageFileList.isEmpty()) {
	        String mainImageFileName = imageFileList.get(0).getI_filename();
	        newGoodsMap.put("goods_fileName", mainImageFileName);
	        newGoodsMap.put("imageFileList", imageFileList);
	    }

	    String message = null;
	    ResponseEntity resEntity = null;
	    HttpHeaders responseHeaders = new HttpHeaders();
	    responseHeaders.add("Content-Type", "text/html; charset=utf-8");

	    try {
	        int goods_num = goodsService.addNewGoods(newGoodsMap);

	        if (!imageFileList.isEmpty()) {
	            for (ImageFileVO imageFileVO : imageFileList) {
	                imageFileName = imageFileVO.getI_filename();
	                File srcFile = new File(CURR_IMAGE_REPO_PATH + File.separator + "temp" + File.separator + imageFileName);
	                File destDir = new File(CURR_IMAGE_REPO_PATH + File.separator + goods_num);
	                FileUtils.moveFileToDirectory(srcFile, destDir, true);
	            }
	        }

	        message = "<script>";
	        message += " alert('등록성공.');";
	        message += " location.href='" + multipartRequest.getContextPath() + "/jangbogo/goodsList.do?category=all';";
	        message += "</script>";

	    } catch (Exception e) {
	        if (!imageFileList.isEmpty()) {
	            for (ImageFileVO imageFileVO : imageFileList) {
	                imageFileName = imageFileVO.getI_filename();
	                File srcFile = new File(CURR_IMAGE_REPO_PATH + File.separator + "temp" + File.separator + imageFileName);
	                if (srcFile.exists()) srcFile.delete();
	            }
	        }

	        message = "<script>";
	        message += " alert('등록실패');";
	        message += " location.href='" + multipartRequest.getContextPath() + "/jangbogo/goodsAddForm.do';";
	        message += "</script>";

	        e.printStackTrace();
	    }

	    resEntity = new ResponseEntity(message, responseHeaders, HttpStatus.OK);
	    return resEntity;
	}


}
