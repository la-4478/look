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
import java.util.stream.Collectors;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.lookmarket.goods.service.GoodsService;
import com.lookmarket.goods.vo.GoodsVO;
import com.lookmarket.goods.vo.ImageFileVO;
import com.lookmarket.member.service.MemberService;
import com.lookmarket.member.vo.MemberVO;
import com.lookmarket.wishlist.service.WishListService;

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
	private MemberService memberService;
	@Autowired
	private WishListService wishListService;
	
	@Override
	@RequestMapping(value="/goodsList.do", method=RequestMethod.GET)
	public ModelAndView goodsList(@RequestParam("category") String category, HttpServletRequest request, HttpServletResponse response) throws Exception{
		//장보고 상품화면(사용자)
		List<GoodsVO> goodsList = new ArrayList<>();
		if(category.equals("all")) {
			goodsList = goodsService.getAllGoods();
		} else if(category.equals("fresh")) {
			goodsList = goodsService.getFreshGoods(1);
		} else if(category.equals("processed")) {
				goodsList = goodsService.getProcessed(2);
			} else if(category.equals("living")) {
				goodsList = goodsService.getLiving(3);
			} else if(category.equals("fashion")) {
				goodsList = goodsService.getFashion(4);
			} else if(category.equals("local")) {
				goodsList = goodsService.getLocal(5);			
		}
		List<GoodsVO> goods = goodsService.getAllGoods();
		ModelAndView mav = new ModelAndView();
		String layout = "common/layout";
		mav.setViewName(layout);
		String viewName = (String)request.getAttribute("viewName");
		mav.addObject("viewName", viewName);
		
		mav.addObject("goods", goods);
		mav.addObject("goodsList", goodsList);
		
		//찜목록 유지(새로고침 후에도)
	    HttpSession session = request.getSession();
	    String mId = (String) session.getAttribute("loginUserId");

	    if (mId != null) {
	        List<Integer> myWishList = wishListService.getWishlistIdsByMember(mId); 
	        
	        mav.addObject("myWishList", myWishList);
	    }

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
	    List<ImageFileVO> mainimage = goodsService.goodsMainImage(gId);

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
	    
	  //찜목록 유지(새로고침 후에도)
	    HttpSession session = request.getSession();
	    String mId = (String) session.getAttribute("loginUserId");

	    if (mId != null) {
	        List<Integer> myWishList = wishListService.getWishlistIdsByMember(mId); 
	        
	        mav.addObject("myWishList", myWishList);
	    }

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

	@RequestMapping(value="/busigoodsAddForm.do", method= {RequestMethod.GET,RequestMethod.POST})
	public ModelAndView busigoodsAddForm(HttpServletRequest request, HttpServletResponse response) throws Exception {
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

	        File destFile = new File(tempDirFile, originalName);
	        multipartFile.transferTo(destFile); // 실제 파일 저장

	        ImageFileVO imageFileVO = new ImageFileVO();
	        imageFileVO.setI_filename(originalName);
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
	
	//상품 등록 로직

	@RequestMapping(value="/busigoodsAdd.do", method= {RequestMethod.POST,RequestMethod.GET})
	public ResponseEntity<String> buisgoodsAdd(MultipartHttpServletRequest multipartRequest,
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

	        File destFile = new File(tempDirFile, originalName);
	        multipartFile.transferTo(destFile); // 실제 파일 저장

	        ImageFileVO imageFileVO = new ImageFileVO();
	        imageFileVO.setI_filename(originalName);
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
	        message += "location.href='" + multipartRequest.getContextPath() + "/business/businessGoodsList.do?category=all';";
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
	@RequestMapping(value="/goodsUpdate.do", method=RequestMethod.POST)
	public ResponseEntity<String> goodsUpdate(MultipartHttpServletRequest multipartRequest,
	                                          HttpServletResponse response) throws Exception {

	    multipartRequest.setCharacterEncoding("utf-8");
	    response.setContentType("text/html; charset=UTF-8");

	    HttpHeaders headers = new HttpHeaders();
	    headers.add("Content-Type", "text/html; charset=utf-8");

	    Map<String, Object> goodsMap = new HashMap<>();

	    // 1) 숫자 파라미터 목록 (프로젝트 스키마에 맞춰 조정)
	    Set<String> intParams = Set.of("g_id","g_category","g_price","g_stock","g_status","g_delivery_price");

	    // 2) 파라미터 수집
	    Enumeration<?> enu = multipartRequest.getParameterNames();
	    while (enu.hasMoreElements()) {
	        String name = (String) enu.nextElement();
	        String value = multipartRequest.getParameter(name);
	        if (value == null || value.isBlank()) {
	            goodsMap.put(name, null);
	            continue;
	        }
	        if (intParams.contains(name)) {
	            goodsMap.put(name, Integer.parseInt(value));
	        } else {
	            goodsMap.put(name, value);
	        }
	    }

	    // 필수: g_id
	    Object gidObj = goodsMap.get("g_id");
	    if (gidObj == null) {
	        String msg = "<script>alert('필수 값 g_id가 없습니다.'); history.back();</script>";
	        return new ResponseEntity<>(msg, headers, HttpStatus.OK);
	    }
	    int g_id = (gidObj instanceof Number) ? ((Number) gidObj).intValue()
	                                          : Integer.parseInt(String.valueOf(gidObj));

	    // 기존 파일명(유지용)
	    String oldFileName = String.valueOf(goodsMap.getOrDefault("old_i_filename", ""));

	    // 3) 로그인 사용자(수정자) 기록(선택)
	    HttpSession session = multipartRequest.getSession(false);
	    if (session != null) {
	        MemberVO memberVO = (MemberVO) session.getAttribute("memberInfo");
	        if (memberVO != null) {
	            goodsMap.put("upd_id", memberVO.getM_id()); // 매퍼/컬럼에 맞춰 쓰거나 빼도 됨
	        }
	    }

	    // 4) 새 파일 업로드 여부 확인 → temp 저장
	    boolean hasNewImage = false;
	    String newFileName = null;

	    MultipartFile file = multipartRequest.getFile("i_filename");
	    if (file != null && !file.isEmpty() && file.getOriginalFilename() != null) {
	        String original = file.getOriginalFilename();
	        String ext = "";
	        int dot = original.lastIndexOf('.');
	        if (dot >= 0) ext = original.substring(dot).toLowerCase(); // .jpg ...

	        newFileName = original+ ext;
	        hasNewImage = true;

	        // 임시 저장: {CURR_IMAGE_REPO_PATH}/temp
	        java.nio.file.Path tempDir = java.nio.file.Paths.get(CURR_IMAGE_REPO_PATH, "temp");
	        java.io.File tempDirFile = tempDir.toFile();
	        if (!tempDirFile.exists()) tempDirFile.mkdirs();

	        java.io.File dest = new java.io.File(tempDirFile, newFileName);
	        file.transferTo(dest);

	        // DB에 반영할 새 파일명 키
	        goodsMap.put("i_filename", newFileName);
	    } else {
	        // 새 파일 없으면 기존 파일 유지
	        if (oldFileName != null && !oldFileName.isBlank()) {
	            goodsMap.put("i_filename", oldFileName);
	        }
	    }

	    String message;
	    java.io.File movedNewFile = null;
	    try {
	        // 5) DB 업데이트 (서비스/매퍼 명은 프로젝트에 맞게 수정)
	        // 예시 A) goodsService.updateGoods(goodsMap);  // void
	        // 예시 B) int updated = goodsService.updateGoods(goodsMap); // 1 기대
	        int updated = goodsService.updateGoods(goodsMap); // 너 서비스 시그니처에 맞게 바꿔도 됨
	        if (updated <= 0) throw new RuntimeException("업데이트 대상이 없습니다.");

	        // 6) 파일 이동: temp → {CURR_IMAGE_REPO_PATH}/{g_id}/
	        if (hasNewImage && newFileName != null) {
	            java.io.File goodsDir = java.nio.file.Paths
	                    .get(CURR_IMAGE_REPO_PATH, String.valueOf(g_id)).toFile();
	            if (!goodsDir.exists()) goodsDir.mkdirs();

	            java.io.File src = new java.io.File(
	                    java.nio.file.Paths.get(CURR_IMAGE_REPO_PATH, "temp").toFile(),
	                    newFileName
	            );
	            if (src.exists()) {
	                org.apache.commons.io.FileUtils.moveFileToDirectory(src, goodsDir, true);
	                movedNewFile = new java.io.File(goodsDir, newFileName);
	            }

	            // 7) 기존 파일 삭제 (파일명이 다를 때만)
	            if (oldFileName != null && !oldFileName.isBlank() && !oldFileName.equals(newFileName)) {
	                java.io.File old = new java.io.File(goodsDir, oldFileName);
	                if (old.exists()) old.delete();
	            }
	        }

	        message  = "<script>";
	        message += "alert('수정 완료되었습니다.');";
	        message += "location.href='" + multipartRequest.getContextPath() + "/business/businessGoodsList.do?category=all';";
	        message += "</script>";

	    } catch (Exception e) {
	        e.printStackTrace();

	        // 실패 시 temp에 남은 새 파일 제거
	        if (hasNewImage && newFileName != null) {
	            java.io.File temp = new java.io.File(
	                    java.nio.file.Paths.get(CURR_IMAGE_REPO_PATH, "temp").toFile(),
	                    newFileName
	            );
	            if (temp.exists()) temp.delete();
	            // 혹시 이미 이동했다면 롤백 겸 삭제
	            if (movedNewFile != null && movedNewFile.exists()) movedNewFile.delete();
	        }

	        message  = "<script>";
	        message += "alert('수정 실패. 다시 시도해주세요.');";
	        message += "history.back();";
	        message += "</script>";
	    }

	    return new ResponseEntity<>(message, headers, HttpStatus.OK);
	}

	@PostMapping("/goodsDelete.do")
	public String deleteGoodsForm(@RequestParam("g_id") int gId, RedirectAttributes ra,
	                              HttpSession session) throws Exception {
		System.out.println("컨트롤러 진입");
		
		 // 0) 로그인 사용자 확인
	    MemberVO login = (MemberVO) session.getAttribute("memberInfo");
	    if (login == null) {
	    	System.out.println("로그인이 필요합니다.");
	        ra.addFlashAttribute("msg", "로그인이 필요합니다.");
	        return "redirect:/member/loginForm.do";
	    }
	    
	    String mId = login.getM_id();

	    // 1) DB에서 권한 조회 (세션 신뢰 X)
	    Integer role = memberService.getRoleById(mId); // null 가능
	    if (role == null || role.intValue() != 2) {
	    	System.out.println("권한이 없습니다.");
	        ra.addFlashAttribute("msg", "권한이 없습니다.");
	        return "redirect:/business/businessgoodsList.do?category=all";
	    }

	    int deleted = goodsService.deleteGoods(gId); // 하드삭제: DELETE FROM goods WHERE g_id = ?
	    if (deleted > 0) {
	        ra.addFlashAttribute("msg", "상품이 삭제되었습니다.");
	    } else {
	        ra.addFlashAttribute("msg", "삭제 대상이 없거나 이미 삭제되었습니다.");
	    }
	    return "redirect:/business/businessgoodsList.do?category=all";
	}
}


