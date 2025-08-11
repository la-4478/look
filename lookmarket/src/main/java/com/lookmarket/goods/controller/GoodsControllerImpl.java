package com.lookmarket.goods.controller;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
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
		ModelAndView mav = new ModelAndView();
		String layout = "common/layout";
		mav.setViewName(layout);
		String viewName = (String)request.getAttribute("viewName");
		mav.addObject("viewName", viewName);
		mav.addObject("goods", goods);
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
	    String filenames = goods.getI_filename();
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
	public ResponseEntity<String> goodsAdd(MultipartHttpServletRequest multipartRequest,
	                                       HttpServletResponse response) throws Exception {
	    multipartRequest.setCharacterEncoding("utf-8");
	    response.setContentType("text/html; charset=UTF-8");

	    HttpHeaders responseHeaders = new HttpHeaders();
	    responseHeaders.add("Content-Type", "text/html; charset=utf-8");

	    Map<String, Object> newGoodsMap = new HashMap<>();

	    // 1) 파라미터 수집 (정수/문자 분리)
	    Enumeration<?> enu = multipartRequest.getParameterNames();
	    // 매퍼/테이블과 일치하는 정수형 파라미터들만 여기 넣어줘
	    Set<String> intParams = Set.of("g_category","g_price","g_stock","g_status","g_delivery_price");

	    while (enu.hasMoreElements()) {
	        String name = (String) enu.nextElement();
	        String value = multipartRequest.getParameter(name);
	        if (value == null || value.isBlank()) {
	            newGoodsMap.put(name, null);
	            continue;
	        }
	        if (intParams.contains(name)) {
	            newGoodsMap.put(name, Integer.parseInt(value));
	        } else {
	            newGoodsMap.put(name, value);
	        }
	    }

	    // 2) 등록자 id
	    HttpSession session = multipartRequest.getSession();
	    MemberVO memberVO = (MemberVO) session.getAttribute("memberInfo");
	    if (memberVO == null) {
	        String msg = "<script>alert('로그인이 필요합니다.'); history.back();</script>";
	        return new ResponseEntity<>(msg, responseHeaders, HttpStatus.OK);
	    }
	    String reg_id = memberVO.getM_id();
	    newGoodsMap.put("reg_id", reg_id); // goods 테이블에 reg_id 컬럼이 있으면 매퍼에도 반영되어야 함

	    // 3) 파일 임시 저장 (C:/upload/temp)
	    List<ImageFileVO> imageFileList = new ArrayList<>();
	    Iterator<String> fileNames = multipartRequest.getFileNames();

	    Path tempDir = Paths.get(CURR_IMAGE_REPO_PATH, "temp");
	    File tempDirFile = tempDir.toFile();
	    if (!tempDirFile.exists()) tempDirFile.mkdirs();

	    while (fileNames.hasNext()) {
	        MultipartFile multipartFile = multipartRequest.getFile(fileNames.next());
	        if (multipartFile == null || multipartFile.isEmpty()) continue;

	        String originalName = multipartFile.getOriginalFilename();
	        if (originalName == null) continue;

	        String ext = "";
	        int dot = originalName.lastIndexOf(".");
	        if (dot >= 0) ext = originalName.substring(dot).toLowerCase(); // .jpg …

	        String newFileName = UUID.randomUUID().toString() + ext;
	        File destFile = new File(tempDirFile, newFileName);
	        multipartFile.transferTo(destFile); // 실제 파일 저장

	        ImageFileVO imageFileVO = new ImageFileVO();
	        imageFileVO.setI_filename(newFileName);
	        imageFileVO.setI_filetype(ext.matches("\\.(jpg|jpeg|png|gif|webp|bmp)") ? "image" : "etc");
	        imageFileList.add(imageFileVO);
	    }

	    if (!imageFileList.isEmpty()) {
	        // ✅ 매퍼가 goods.i_filename을 기대하므로 키 이름을 i_filename으로 맞춘다
	        newGoodsMap.put("i_filename", imageFileList.get(0).getI_filename());
	        newGoodsMap.put("imageFileList", imageFileList);
	    }

	    // 4) 서비스 호출 → 생성된 PK로 폴더 이동
	    String message;
	    try {
	        int goods_num = goodsService.addNewGoods(newGoodsMap); // ✅ 생성된 PK(g_id)

	        // 파일 이동: temp → C:/upload/{goods_num}/
	        File goodsDir = Paths.get(CURR_IMAGE_REPO_PATH, String.valueOf(goods_num)).toFile();

	        for (ImageFileVO img : imageFileList) {
	            String fn = img.getI_filename();
	            File src = new File(tempDirFile, fn);
	            if (src.exists()) {
	                FileUtils.moveFileToDirectory(src, goodsDir, true);
	            }
	        }

	        message  = "<script>";
	        message += "alert('등록성공.');";
	        message += "location.href='" + multipartRequest.getContextPath() + "/jangbogo/goodsList.do?category=all';";
	        message += "</script>";

	    } catch (Exception e) {
	        // 실패 시 temp에 남은 파일들 삭제
	        for (ImageFileVO img : imageFileList) {
	            File f = new File(tempDirFile, img.getI_filename());
	            if (f.exists()) f.delete();
	        }
	        e.printStackTrace();

	        message  = "<script>";
	        message += "alert('등록실패');";
	        message += "location.href='" + multipartRequest.getContextPath() + "/jangbogo/goodsAddForm.do';";
	        message += "</script>";
	    }

	    return new ResponseEntity<>(message, responseHeaders, HttpStatus.OK);
	}

	@Override
	public ResponseEntity goodsUpdate(MultipartHttpServletRequest multipartRequest, HttpServletResponse response)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
}


