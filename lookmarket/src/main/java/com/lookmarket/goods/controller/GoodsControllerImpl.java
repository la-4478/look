package com.lookmarket.goods.controller;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
@RequestMapping(value = "/jangbogo")
public class GoodsControllerImpl implements GoodsController {
	private static final String CURR_IMAGE_REPO_PATH = "C:\\lookmarket_resources\\file_repo";
	@Autowired
	private GoodsService goodsService;
	@Autowired
	private MemberService memberService;
	@Autowired
	private WishListService wishListService;

	@Override
	@RequestMapping(value = "/goodsList.do", method = RequestMethod.GET)
	public ModelAndView goodsList(@RequestParam("category") String category,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		// 장보고 상품화면(사용자)
		List<GoodsVO> goodsList = new ArrayList<>();
		if (category.equals("all")) {
			goodsList = goodsService.getAllGoods();
		} else if (category.equals("fresh")) {
			goodsList = goodsService.getFreshGoods(1);
		} else if (category.equals("processed")) {
			goodsList = goodsService.getProcessed(2);
		} else if (category.equals("living")) {
			goodsList = goodsService.getLiving(3);
		} else if (category.equals("fashion")) {
			goodsList = goodsService.getFashion(4);
		} else if (category.equals("local")) {
			goodsList = goodsService.getLocal(5);
		}
		List<GoodsVO> goods = goodsService.getAllGoods();
		ModelAndView mav = new ModelAndView();
		String layout = "common/layout";
		mav.setViewName(layout);
		String viewName = (String) request.getAttribute("viewName");
		mav.addObject("viewName", viewName);

		mav.addObject("goods", goods);
		mav.addObject("goodsList", goodsList);

		// 찜목록 유지(새로고침 후에도)
		HttpSession session = request.getSession();
		String mId = (String) session.getAttribute("loginUserId");

		if (mId != null) {
			List<Integer> myWishList = wishListService
					.getWishlistIdsByMember(mId);

			mav.addObject("myWishList", myWishList);
		}

		return mav;
	}

	@Override
	@GetMapping("/goodsDetail.do")
	public ModelAndView goodsDetail(@RequestParam("g_id") int gId,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

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
		List<ImageFileVO> detailImageList = goodsService.goodsSubImage(gId);

		// 3) 레이아웃 + 바디 뷰 세팅 (request attribute에 의존 X)
		ModelAndView mav = new ModelAndView("/common/layout"); // ← 슬래시 붙이자
		// 타일즈 안 쓰고 include 방식이면 보통 이런 식으로 body 경로 내려줌
		mav.addObject("body", "/WEB-INF/views/goods/goodsDetail.jsp");

		// 4) 모델
		mav.addObject("goods", goods);
		mav.addObject("detailImageList", detailImageList);
		mav.addObject("Mainimage", mainimage);

		// 찜목록 유지(새로고침 후에도)
		HttpSession session = request.getSession();
		String mId = (String) session.getAttribute("loginUserId");

		if (mId != null) {
			List<Integer> myWishList = wishListService
					.getWishlistIdsByMember(mId);

			mav.addObject("myWishList", myWishList);
		}

		return mav;
	}

	@Override
	@RequestMapping(value = "/goodsAddForm.do", method = RequestMethod.GET)
	public ModelAndView goodsAddForm(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		ModelAndView mav = new ModelAndView();
		String layout = "common/layout";
		mav.setViewName(layout);
		String viewName = (String) request.getAttribute("viewName");
		mav.addObject("viewName", viewName);
		return mav;
	}

	@RequestMapping(value = "/busigoodsAddForm.do", method = {RequestMethod.GET,
			RequestMethod.POST})
	public ModelAndView busigoodsAddForm(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		ModelAndView mav = new ModelAndView();
		String layout = "common/layout";
		mav.setViewName(layout);
		String viewName = (String) request.getAttribute("viewName");
		mav.addObject("viewName", viewName);
		return mav;
	}

	@Override
	@RequestMapping(value = "/goodsUpdateForm.do", method = RequestMethod.GET)
	public ModelAndView goodsUpdateForm(@RequestParam("g_id") int g_id,HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		GoodsVO goods = goodsService.getGoodsDetail(g_id);
		List<ImageFileVO> subImage = goodsService.goodsSubImage(g_id);
		String layout = "common/layout";
		ModelAndView mav = new ModelAndView();
		mav.setViewName(layout);
		String viewName = (String) request.getAttribute("viewName");
		mav.addObject("viewName", viewName);
		mav.addObject("goods", goods);
		mav.addObject("detailImageList", subImage);
		return mav;
	}

	@RequestMapping(value = "/busigoodsUpdateForm.do", method = RequestMethod.GET)
	public ModelAndView busigoodsUpdateForm(@RequestParam("g_id") int g_id, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		GoodsVO goods = goodsService.getGoodsDetail(g_id);
		List<ImageFileVO> subImage = goodsService.goodsSubImage(g_id);
		
		String layout = "common/layout";
		ModelAndView mav = new ModelAndView();
		mav.setViewName(layout);
		String viewName = (String) request.getAttribute("viewName");
		mav.addObject("viewName", viewName);
		mav.addObject("goods", goods);
		mav.addObject("detailImageList", subImage);
		return mav;
	}

	// 상품 등록 로직
	@Override
	@RequestMapping(value = "/goodsAdd.do", method = RequestMethod.POST)
	public ResponseEntity<String> goodsAdd(
			MultipartHttpServletRequest multipartRequest,
			HttpServletResponse response) throws Exception {
		// 0) 인코딩/응답 헤더
		multipartRequest.setCharacterEncoding("utf-8");
		response.setContentType("text/html; charset=UTF-8");

		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "text/html; charset=utf-8");

		Map<String, Object> newGoodsMap = new HashMap<>();

		// 1) 파라미터 수집
		Enumeration<?> enu = multipartRequest.getParameterNames();
		Set<String> intParams = Set.of("g_category", "g_price", "g_stock",
				"g_status", "g_delivery_price");

		while (enu.hasMoreElements()) {
			String name = (String) enu.nextElement();
			String value = multipartRequest.getParameter(name);

			if (value == null || value.isBlank()) {
				newGoodsMap.put(name, null);
				continue;
			}

			if (intParams.contains(name)) {
				try {
					newGoodsMap.put(name, Integer.parseInt(value.trim()));
				} catch (NumberFormatException nfe) {
					newGoodsMap.put(name, null);
				}
			} else {
				newGoodsMap.put(name, value.trim());
			}
		}

		// 2) 등록자 ID
		HttpSession session = multipartRequest.getSession();
		MemberVO memberVO = (MemberVO) session.getAttribute("memberInfo");
		if (memberVO == null) {
			String msg = "<script>alert('로그인이 필요합니다.'); history.back();</script>";
			return new ResponseEntity<>(msg, responseHeaders, HttpStatus.OK);
		}
		String reg_id = memberVO.getM_id();
		newGoodsMap.put("reg_id", reg_id);

		// 3) 파일 임시 저장 (C:/upload/temp)
		Path tempDir = Paths.get(CURR_IMAGE_REPO_PATH, "temp");
		File tempDirFile = tempDir.toFile();
		if (!tempDirFile.exists())
			tempDirFile.mkdirs();

		// 메인 이미지
		MultipartFile mainFile = multipartRequest.getFile("i_filename");
		String mainSavedName = null;

		if (mainFile != null && !mainFile.isEmpty()) {
			String orig = mainFile.getOriginalFilename();
			if (orig != null) {
				mainSavedName = orig; // 원본 파일명 그대로
				mainFile.transferTo(new File(tempDirFile, mainSavedName));
				newGoodsMap.put("i_filename", mainSavedName); // goods 테이블용
			}
		}

		// 상세 이미지 (다중 업로드)
		List<ImageFileVO> subImageList = new ArrayList<>();
		List<MultipartFile> subs = multipartRequest.getFiles("sub_image");

		if (subs != null) {
			for (MultipartFile f : subs) {
				if (f == null || f.isEmpty())
					continue;
				String orig = f.getOriginalFilename();
				if (orig == null)
					continue;

				// 원본 파일명 그대로 사용
				f.transferTo(new File(tempDirFile, orig));

				ImageFileVO vo = new ImageFileVO();
				vo.setI_filename(orig);
				vo.setI_filetype("subimage");
				subImageList.add(vo);
			}
		}

		if (!subImageList.isEmpty()) {
			newGoodsMap.put("detailImageList", subImageList);
		}

		// 4) 서비스 호출 → DB insert + PK(goods_num) 획득 → 파일 이동
		String message;
		try {
			int goods_num = goodsService.addNewGoods(newGoodsMap);

			File goodsDir = Paths
					.get(CURR_IMAGE_REPO_PATH, String.valueOf(goods_num))
					.toFile();
			if (!goodsDir.exists())
				goodsDir.mkdirs();

			// 메인
			if (mainSavedName != null) {
				File src = new File(tempDirFile, mainSavedName);
				if (src.exists())
					FileUtils.moveFileToDirectory(src, goodsDir, true);
			}

			// 상세
			for (ImageFileVO img : subImageList) {
				File src = new File(tempDirFile, img.getI_filename());
				if (src.exists())
					FileUtils.moveFileToDirectory(src, goodsDir, true);
			}

			message = "<script>";
			message += "alert('등록성공.');";
			message += "location.href='" + multipartRequest.getContextPath()
					+ "/jangbogo/goodsList.do?category=all';";
			message += "</script>";

		} catch (Exception e) {
			// 실패 시 temp 정리
			if (mainSavedName != null) {
				File f = new File(tempDirFile, mainSavedName);
				if (f.exists())
					f.delete();
			}
			for (ImageFileVO img : subImageList) {
				File f = new File(tempDirFile, img.getI_filename());
				if (f.exists())
					f.delete();
			}
			e.printStackTrace();

			message = "<script>";
			message += "alert('등록실패');";
			message += "location.href='" + multipartRequest.getContextPath()
					+ "/jangbogo/goodsAddForm.do';";
			message += "</script>";
		}

		return new ResponseEntity<>(message, responseHeaders, HttpStatus.OK);
	}

	// 상품 등록 로직

	@RequestMapping(value = "/busigoodsAdd.do", method = {RequestMethod.POST,
			RequestMethod.GET})
	public ResponseEntity<String> buisgoodsAdd(
			MultipartHttpServletRequest multipartRequest,
			HttpServletResponse response) throws Exception {
		// 0) 인코딩/응답 헤더
		multipartRequest.setCharacterEncoding("utf-8");
		response.setContentType("text/html; charset=UTF-8");

		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "text/html; charset=utf-8");

		Map<String, Object> newGoodsMap = new HashMap<>();

		// 1) 파라미터 수집
		Enumeration<?> enu = multipartRequest.getParameterNames();
		Set<String> intParams = Set.of("g_category", "g_price", "g_stock",
				"g_status", "g_delivery_price");

		while (enu.hasMoreElements()) {
			String name = (String) enu.nextElement();
			String value = multipartRequest.getParameter(name);

			if (value == null || value.isBlank()) {
				newGoodsMap.put(name, null);
				continue;
			}

			if (intParams.contains(name)) {
				try {
					newGoodsMap.put(name, Integer.parseInt(value.trim()));
				} catch (NumberFormatException nfe) {
					newGoodsMap.put(name, null);
				}
			} else {
				newGoodsMap.put(name, value.trim());
			}
		}

		// 2) 등록자 ID
		HttpSession session = multipartRequest.getSession();
		MemberVO memberVO = (MemberVO) session.getAttribute("memberInfo");
		if (memberVO == null) {
			String msg = "<script>alert('로그인이 필요합니다.'); history.back();</script>";
			return new ResponseEntity<>(msg, responseHeaders, HttpStatus.OK);
		}
		String reg_id = memberVO.getM_id();
		newGoodsMap.put("reg_id", reg_id);

		// 3) 파일 임시 저장 (C:/upload/temp)
		Path tempDir = Paths.get(CURR_IMAGE_REPO_PATH, "temp");
		File tempDirFile = tempDir.toFile();
		if (!tempDirFile.exists())
			tempDirFile.mkdirs();

		// 메인 이미지
		MultipartFile mainFile = multipartRequest.getFile("i_filename");
		String mainSavedName = null;

		if (mainFile != null && !mainFile.isEmpty()) {
			String orig = mainFile.getOriginalFilename();
			if (orig != null) {
				mainSavedName = orig; // 원본 파일명 그대로
				
				mainFile.transferTo(new File(tempDirFile, mainSavedName));
				newGoodsMap.put("i_filename", mainSavedName); // goods 테이블용
			}
		}

		// 상세 이미지 (다중 업로드)
		List<ImageFileVO> subImageList = new ArrayList<>();
		List<MultipartFile> subs = multipartRequest.getFiles("sub_image");

		if (subs != null) {
			for (MultipartFile f : subs) {
				if (f == null || f.isEmpty())
					continue;
				String orig = f.getOriginalFilename();
				if (orig == null)
					continue;

				// 원본 파일명 그대로 사용
				f.transferTo(new File(tempDirFile, orig));
				System.out.println("orig : " + orig);

				ImageFileVO vo = new ImageFileVO();
				// 상세 이미지 파일용 set
				vo.setI_filename(orig);
				vo.setI_filetype("subimage");
				subImageList.add(vo);
				System.out.println("subImageList : " + subImageList.listIterator(0));
			}
		}

		if (!subImageList.isEmpty()) {
			newGoodsMap.put("detailImageList", subImageList);
			System.out.println("비어 있지 않습니다");
		}

		// 4) 서비스 호출 → DB insert + PK(goods_num) 획득 → 파일 이동
		String message;
		try {
			int goods_num = goodsService.addNewGoods(newGoodsMap);

			File goodsDir = Paths
					.get(CURR_IMAGE_REPO_PATH, String.valueOf(goods_num))
					.toFile();
			if (!goodsDir.exists())
				goodsDir.mkdirs();

			// 메인
			if (mainSavedName != null) {
				File src = new File(tempDirFile, mainSavedName);
				if (src.exists())
					FileUtils.moveFileToDirectory(src, goodsDir, true);
			}

			// 상세
			for (ImageFileVO img : subImageList) {
				File src = new File(tempDirFile, img.getI_filename());
				if (src.exists())
					FileUtils.moveFileToDirectory(src, goodsDir, true);
			}

			message = "<script>";
			message += "alert('등록성공.');";
			message += "location.href='" + multipartRequest.getContextPath()
					+ "/business/businessGoodsList.do?category=all';";
			message += "</script>";

		} catch (Exception e) {
			// 실패 시 temp 정리
			if (mainSavedName != null) {
				File f = new File(tempDirFile, mainSavedName);
				if (f.exists())
					f.delete();
			}
			for (ImageFileVO img : subImageList) {
				File f = new File(tempDirFile, img.getI_filename());
				if (f.exists())
					f.delete();
			}
			e.printStackTrace();

			message = "<script>";
			message += "alert('등록실패');";
			message += "location.href='" + multipartRequest.getContextPath()
					+ "/jangbogo/businessgoodsAddForm.do';";
			message += "</script>";
		}

		return new ResponseEntity<>(message, responseHeaders, HttpStatus.OK);
	}

	@Override
	@RequestMapping(value = "/goodsUpdate.do", method = RequestMethod.POST)
	public ResponseEntity<String> goodsUpdate(
			MultipartHttpServletRequest multipartRequest,
			HttpServletResponse response) throws Exception {

		multipartRequest.setCharacterEncoding("utf-8");
		response.setContentType("text/html; charset=UTF-8");

		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "text/html; charset=utf-8");

		Map<String, Object> goodsMap = new HashMap<>();

		// 1) 숫자 파라미터 목록 (프로젝트 스키마에 맞춰 조정)
		Set<String> intParams = Set.of("g_id", "g_category", "g_price",
				"g_stock", "g_status", "g_delivery_price");

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
		int g_id = (gidObj instanceof Number)
				? ((Number) gidObj).intValue()
				: Integer.parseInt(String.valueOf(gidObj));

		// 기존 파일명(유지용)
		String oldFileName = String
				.valueOf(goodsMap.getOrDefault("old_i_filename", ""));

		// 3) 로그인 사용자(수정자) 기록(선택)
		HttpSession session = multipartRequest.getSession(false);
		if (session != null) {
			MemberVO memberVO = (MemberVO) session.getAttribute("memberInfo");
			if (memberVO != null) {
				goodsMap.put("upd_id", memberVO.getM_id()); // 매퍼/컬럼에 맞춰 쓰거나 빼도
															// 됨
			}
		}

		// 4) 새 파일 업로드 여부 확인 → temp 저장
		boolean hasNewImage = false;
		String newFileName = null;

		MultipartFile file = multipartRequest.getFile("i_filename");
		if (file != null && !file.isEmpty()
				&& file.getOriginalFilename() != null) {
			String original = file.getOriginalFilename();
			String ext = "";
			int dot = original.lastIndexOf('.');
			if (dot >= 0)
				ext = original.substring(dot).toLowerCase(); // .jpg ...

			newFileName = original + ext;
			hasNewImage = true;

			// 임시 저장: {CURR_IMAGE_REPO_PATH}/temp
			java.nio.file.Path tempDir = java.nio.file.Paths
					.get(CURR_IMAGE_REPO_PATH, "temp");
			java.io.File tempDirFile = tempDir.toFile();
			if (!tempDirFile.exists())
				tempDirFile.mkdirs();

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
			// 예시 A) goodsService.updateGoods(goodsMap); // void
			// 예시 B) int updated = goodsService.updateGoods(goodsMap); // 1 기대
			int updated = goodsService.updateGoods(goodsMap); // 너 서비스 시그니처에 맞게
																// 바꿔도 됨
			if (updated <= 0)
				throw new RuntimeException("업데이트 대상이 없습니다.");

			// 6) 파일 이동: temp → {CURR_IMAGE_REPO_PATH}/{g_id}/
			if (hasNewImage && newFileName != null) {
				java.io.File goodsDir = java.nio.file.Paths
						.get(CURR_IMAGE_REPO_PATH, String.valueOf(g_id))
						.toFile();
				if (!goodsDir.exists())
					goodsDir.mkdirs();

				java.io.File src = new java.io.File(java.nio.file.Paths
						.get(CURR_IMAGE_REPO_PATH, "temp").toFile(),
						newFileName);
				if (src.exists()) {
					org.apache.commons.io.FileUtils.moveFileToDirectory(src,
							goodsDir, true);
					movedNewFile = new java.io.File(goodsDir, newFileName);
				}

				// 7) 기존 파일 삭제 (파일명이 다를 때만)
				if (oldFileName != null && !oldFileName.isBlank()
						&& !oldFileName.equals(newFileName)) {
					java.io.File old = new java.io.File(goodsDir, oldFileName);
					if (old.exists())
						old.delete();
				}
			}

			message = "<script>";
			message += "alert('수정 완료되었습니다.');";
			message += "location.href='" + multipartRequest.getContextPath()
					+ "/admin/allGoodsList.do';";
			message += "</script>";

		} catch (Exception e) {
			e.printStackTrace();

			// 실패 시 temp에 남은 새 파일 제거
			if (hasNewImage && newFileName != null) {
				java.io.File temp = new java.io.File(java.nio.file.Paths
						.get(CURR_IMAGE_REPO_PATH, "temp").toFile(),
						newFileName);
				if (temp.exists())
					temp.delete();
				// 혹시 이미 이동했다면 롤백 겸 삭제
				if (movedNewFile != null && movedNewFile.exists())
					movedNewFile.delete();
			}

			message = "<script>";
			message += "alert('수정 실패. 다시 시도해주세요.');";
			message += "history.back();";
			message += "</script>";
		}

		return new ResponseEntity<>(message, headers, HttpStatus.OK);
	}

	@RequestMapping(value = "/busigoodsUpdate.do", method = RequestMethod.POST)
	public ResponseEntity<String> busigoodsUpdate(
	        MultipartHttpServletRequest multipartRequest,
	        HttpServletResponse response) throws Exception {

	    multipartRequest.setCharacterEncoding("utf-8");
	    response.setContentType("text/html; charset=UTF-8");

	    HttpHeaders headers = new HttpHeaders();
	    headers.add("Content-Type", "text/html; charset=utf-8");

	    Map<String, Object> goodsMap = new HashMap<>();

	    // 1) 숫자 파라미터
	    Set<String> intParams = Set.of("g_id","g_category","g_price","g_stock","g_status","g_delivery_price");

	    // 2) 일반 파라미터 수집
	    Enumeration<?> enu = multipartRequest.getParameterNames();
	    while (enu.hasMoreElements()) {
	        String name = (String) enu.nextElement();
	        String value = multipartRequest.getParameter(name);
	        if (value == null || value.isBlank()) { goodsMap.put(name, null); continue; }
	        if (intParams.contains(name)) goodsMap.put(name, Integer.parseInt(value));
	        else goodsMap.put(name, value);
	    }

	    // 필수 g_id
	    Object gidObj = goodsMap.get("g_id");
	    if (gidObj == null) {
	        String msg = "<script>alert('필수 값 g_id가 없습니다.'); history.back();</script>";
	        return new ResponseEntity<>(msg, headers, HttpStatus.OK);
	    }
	    int g_id = (gidObj instanceof Number) ? ((Number) gidObj).intValue() : Integer.parseInt(String.valueOf(gidObj));

	    // 기존 대표이미지 파일명
	    String oldFileName = String.valueOf(goodsMap.getOrDefault("old_i_filename", ""));

	    // (A) 대표이미지 업로드(단일)
	    boolean hasNewImage = false;
	    String newFileName = null;
	    MultipartFile file = multipartRequest.getFile("i_filename");
	    if (file != null && !file.isEmpty() && file.getOriginalFilename() != null) {
	        newFileName = normalizeFilename(file.getOriginalFilename());
	        hasNewImage = true;

	        Path tempDir = Paths.get(CURR_IMAGE_REPO_PATH, "temp");
	        Files.createDirectories(tempDir);

	        file.transferTo(tempDir.resolve(newFileName).toFile());
	        goodsMap.put("i_filename", newFileName);
	    } else {
	        if (oldFileName != null && !oldFileName.isBlank()) {
	            goodsMap.put("i_filename", oldFileName);
	        }
	    }

	    // (B) 상세이미지(멀티) — form name="detailImages"
	    // 기존 상세이미지: hidden name="old_sub_images" value="a.jpg,b.png,c.webp"
	    String oldSubsCsv = String.valueOf(goodsMap.getOrDefault("old_sub_images", ""));
	    List<String> oldSubImages = new ArrayList<>();
	    if (oldSubsCsv != null && !oldSubsCsv.isBlank()) {
	        for (String s : oldSubsCsv.split(",")) {
	            String t = s.trim();
	            if (!t.isEmpty()) oldSubImages.add(t);
	        }
	    }

	    List<MultipartFile> subFiles = multipartRequest.getFiles("sub_image");
	    List<ImageFileVO> detailImageList = new ArrayList<>();
	    List<String> newSubImageNames = new ArrayList<>();

	    if (subFiles != null && !subFiles.isEmpty()) {
	        Path tempDir = Paths.get(CURR_IMAGE_REPO_PATH, "temp");
	        Files.createDirectories(tempDir);

	        int order = 0;
	        for (MultipartFile mf : subFiles) {
	            if (mf == null || mf.isEmpty() || mf.getOriginalFilename() == null) continue;

	            String original = normalizeFilename(mf.getOriginalFilename());
	            String safeName = System.currentTimeMillis() + "_" + (order++) + "_" + original;

	            mf.transferTo(tempDir.resolve(safeName).toFile());

	            ImageFileVO vo = new ImageFileVO();
	            vo.setG_id(g_id);
	            vo.setI_filename(original);      // DB 칼럼명에 맞춰 사용
	            System.out.println("original : " + original);
	            vo.setI_filetype("subimage");    // 필요 없으면 제거
	            // vo.setSort_order(order - 1);   // 정렬 칼럼 쓰면 활성화

	            detailImageList.add(vo);
	            newSubImageNames.add(safeName);
	        }
	        if (!detailImageList.isEmpty()) {
	            goodsMap.put("detailImageList", detailImageList);
	        }
	    }

	    String message;
	    File movedNewFile = null;
	    List<File> movedSubFiles = new ArrayList<>();

	    try {
	        // 5) DB 업데이트 (대표정보 + 상세이미지 메타)
	        int updated = goodsService.updateGoods(goodsMap);   // 서비스에서 detailImageList가 있으면 전체 교체(DELETE → INSERT)
	        if (updated <= 0) throw new RuntimeException("업데이트 대상이 없습니다.");

	        // 6) 파일 이동: temp -> {CURR_IMAGE_REPO_PATH}/{g_id}/
	        File goodsDir = Paths.get(CURR_IMAGE_REPO_PATH, String.valueOf(g_id)).toFile();
	        if (!goodsDir.exists()) goodsDir.mkdirs();

	        // 대표이미지 이동
	        if (hasNewImage && newFileName != null) {
	            File src = Paths.get(CURR_IMAGE_REPO_PATH, "temp", newFileName).toFile();
	            if (src.exists()) {
	                org.apache.commons.io.FileUtils.moveFileToDirectory(src, goodsDir, true);
	                movedNewFile = new File(goodsDir, newFileName);
	            }
	            // 기존 대표이미지 삭제(파일명이 다를 때)
	            if (oldFileName != null && !oldFileName.isBlank() && !oldFileName.equals(newFileName)) {
	                File old = new File(goodsDir, oldFileName);
	                if (old.exists()) old.delete();
	            }
	        }

	        // 상세이미지 이동 + 기존 상세이미지 정리
	        if (!newSubImageNames.isEmpty()) {
	            File tempDir = Paths.get(CURR_IMAGE_REPO_PATH, "temp").toFile();
	            for (String fname : newSubImageNames) {
	                File src = new File(tempDir, fname);
	                if (src.exists()) {
	                    org.apache.commons.io.FileUtils.moveFileToDirectory(src, goodsDir, true);
	                    movedSubFiles.add(new File(goodsDir, fname));
	                }
	            }
	            // 전체 교체 방식: 새 목록에 없는 기존 파일만 삭제
	            for (String old : oldSubImages) {
	                if (!newSubImageNames.contains(old)) {
	                    File f = new File(goodsDir, old);
	                    if (f.exists()) f.delete();
	                }
	            }
	        }

	        message  = "<script>";
	        message += "alert('수정 완료되었습니다.');";
	        message += "location.href='" + multipartRequest.getContextPath() + "/business/businessGoodsList.do?category=all';";
	        message += "</script>";

	    } catch (Exception e) {
	        e.printStackTrace();

	        // 실패 시 temp에 남은 새 파일 제거 + 이미 이동한 것 롤백
	        if (hasNewImage && newFileName != null) {
	            File temp = Paths.get(CURR_IMAGE_REPO_PATH, "temp", newFileName).toFile();
	            if (temp.exists()) temp.delete();
	            if (movedNewFile != null && movedNewFile.exists()) movedNewFile.delete();
	        }
	        if (!newSubImageNames.isEmpty()) {
	            File tempDir = Paths.get(CURR_IMAGE_REPO_PATH, "temp").toFile();
	            for (String fname : newSubImageNames) {
	                File t = new File(tempDir, fname);
	                if (t.exists()) t.delete();
	            }
	            for (File moved : movedSubFiles) {
	                if (moved.exists()) moved.delete();
	            }
	        }

	        String messageErr  = "<script>";
	        messageErr += "alert('수정 실패. 다시 시도해주세요.');";
	        messageErr += "history.back();";
	        messageErr += "</script>";
	        return new ResponseEntity<>(messageErr, headers, HttpStatus.OK);
	    }

	    return new ResponseEntity<>(message, headers, HttpStatus.OK);
	}

	/** ─────────────────────────────────────────────────────────
	 *  파일명 정규화: 공백 치환, 경로 구분자 제거, 확장자 소문자/중복방지
	 *  예) "A.B.C.JPG"  -> "A.B.C.jpg"
	 *      "cat.jpg.jpg"-> "cat.jpg"
	 *      "../../x.png"-> "x.png"
	 *  ───────────────────────────────────────────────────────── */
	private static String normalizeFilename(String original) {
	    if (original == null) return null;
	    // 경로 구분자 제거 (윈도우/유닉스)
	    String name = original.replace("\\", "/");
	    int slash = name.lastIndexOf('/');
	    if (slash >= 0) name = name.substring(slash + 1);

	    // 공백 -> '_' 로
	    name = name.trim().replaceAll("\\s+", "_");

	    // 마지막 점 기준으로 base/ext 분리
	    int dot = name.lastIndexOf('.');
	    if (dot < 0) return name; // 확장자 없음

	    String base = name.substring(0, dot);
	    String ext  = name.substring(dot).toLowerCase(); // ".JPG" -> ".jpg"

	    if (base.isEmpty()) return name; // 숨김파일(.gitignore 등)은 그대로

	    // base 끝이 ".jpg" 같은 확장자로 끝나 중복된 경우 제거
	    if (base.matches("(?i).+\\.(jpg|jpeg|png|gif|webp|bmp|heic|heif|avif)$")) {
	        base = base.replaceFirst("(?i)\\.(jpg|jpeg|png|gif|webp|bmp|heic|heif|avif)$", "");
	    }
	    return base + ext;
	}


	@RequestMapping(value="/goodsDelete.do",  method = {RequestMethod.POST,RequestMethod.GET})
	public String deleteGoodsForm(@RequestParam("g_id") int gId,
			RedirectAttributes ra, HttpSession session) throws Exception {
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
		if (role == null || role.intValue() != 3) {
			System.out.println("권한이 없습니다.");
			ra.addFlashAttribute("msg", "권한이 없습니다.");
			return "redirect:/admin/allGoodsList";
		}

		int deleted = goodsService.deleteGoods(gId); // 하드삭제: DELETE FROM goods
														// WHERE g_id = ?
		if (deleted > 0) {
			ra.addFlashAttribute("msg", "상품이 삭제되었습니다.");
		} else {
			ra.addFlashAttribute("msg", "삭제 대상이 없거나 이미 삭제되었습니다.");
		}
		return "redirect:/admin/allGoodsList";
	}
	

	@RequestMapping(value="/busigoodsDelete.do",  method = {RequestMethod.POST,RequestMethod.GET})
	public String busideleteGoods(@RequestParam("g_id") int gId,
			RedirectAttributes ra, HttpSession session) throws Exception {
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

		int deleted = goodsService.deleteGoods(gId); // 하드삭제: DELETE FROM goods
														// WHERE g_id = ?
		if (deleted > 0) {
			ra.addFlashAttribute("msg", "상품이 삭제되었습니다.");
		} else {
			ra.addFlashAttribute("msg", "삭제 대상이 없거나 이미 삭제되었습니다.");
		}
		return "redirect:/business/businessgoodsList.do?category=all";
	}
}
