package com.lookmarket.sijangbajo.controller;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.lookmarket.sijangbajo.service.SijangSearchService3;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Controller("sijangbajoController")
@RequestMapping(value="/sijangbajo")
public class SijangbajoControllerImpl3 implements SijangbajoController{
	
	@Autowired
	private SijangSearchService3 sijangService;
	
	@Override
	@RequestMapping(value="/sijangSearch/search.do", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView search(HttpServletRequest request, HttpServletResponse response) throws Exception {
	    ModelAndView mav = new ModelAndView();
	    String layout = "common/layout";
	    mav.setViewName(layout);
	    String viewName = (String) request.getAttribute("viewName");
	    mav.addObject("viewName", viewName);

	    List<Map<String, String>> seoulSijangList = new ArrayList<>();

	    String apiUrl1 = "https://api.odcloud.kr/api/15052837/v1/uddi:8e90c34b-c086-422f-882a-d3c15efd101f?page=1&perPage=1000&serviceKey=YU6e42LEcBk0HxFjOvjJmeT93M%2FftIc3HK8kXmgMnh%2Fen2s2q2dPNQKL2ifT5WQd5LnY4a2J9KmhwBMECJDMuQ%3D%3D";
	    List<Map<String, String>> sijangList1 = (List<Map<String, String>>) sijangService.fetchAllDataFromApi(apiUrl1);

	    String apiUrl2 = "https://api.odcloud.kr/api/15052836/v1/uddi:2253111c-b6f3-45ad-9d66-924fd92dabd7?page=1&perPage=1000&serviceKey=YU6e42LEcBk0HxFjOvjJmeT93M%2FftIc3HK8kXmgMnh%2Fen2s2q2dPNQKL2ifT5WQd5LnY4a2J9KmhwBMECJDMuQ%3D%3D";
	    List<Map<String, String>> sijangList2 = (List<Map<String, String>>) sijangService.fetchAllDataFromApi(apiUrl2);

	    List<Map<String, String>> allSijangList = new ArrayList<>();
	    if (sijangList1 != null) allSijangList.addAll(sijangList1);
	    if (sijangList2 != null) allSijangList.addAll(sijangList2);

	    for (Map<String, String> item : allSijangList) {
	        String addr = item.get("지번주소");
	        if (addr != null && addr.contains("서울")) {
	            seoulSijangList.add(item);
	        }
	    }

	    mav.addObject("seoulSijangList", seoulSijangList);

	    mav.addObject("sijangList1", sijangList1);
	    mav.addObject("sijangList2", sijangList2);

	    HttpSession session = request.getSession();
	    session.setAttribute("sideMenu", "reveal");
	    session.setAttribute("sideMenu_option", "search");

	    return mav;
	}
	
	@ResponseBody
	@RequestMapping(value="/sijangSearch/searchAjax.do", method = RequestMethod.GET, produces = "application/json; charset=UTF-8")
	public List<Map<String, String>> searchAjax(HttpServletRequest request) throws Exception {
		String sido = request.getParameter("sido");
		String sigungu = request.getParameter("sigungu");
		String marketName = request.getParameter("marketName");

		String apiUrl1 = "https://api.odcloud.kr/api/15052837/v1/uddi:8e90c34b-c086-422f-882a-d3c15efd101f?page=1&perPage=1000&serviceKey=YU6e42LEcBk0HxFjOvjJmeT93M%2FftIc3HK8kXmgMnh%2Fen2s2q2dPNQKL2ifT5WQd5LnY4a2J9KmhwBMECJDMuQ%3D%3D";
		String apiUrl2 = "https://api.odcloud.kr/api/15052836/v1/uddi:2253111c-b6f3-45ad-9d66-924fd92dabd7?page=1&perPage=1000&serviceKey=YU6e42LEcBk0HxFjOvjJmeT93M%2FftIc3HK8kXmgMnh%2Fen2s2q2dPNQKL2ifT5WQd5LnY4a2J9KmhwBMECJDMuQ%3D%3D";

		Map<String, String> list1 = sijangService.fetchAllDataFromApi(apiUrl1);
		Map<String, String> list2 = sijangService.fetchAllDataFromApi(apiUrl2);

		// 두 리스트 병합
		List<Map<String, String>> merged = new ArrayList<>();
		merged.add(list1);
		merged.add(list2);

		// 조건 필터링
		return merged.stream()
				.filter(item -> (sido == null || sido.isEmpty() || item.getOrDefault("시도", "").contains(sido)) &&
			               (sigungu == null || sigungu.equals("전체") || item.getOrDefault("시군구", "").contains(sigungu)) &&
			               (marketName == null || marketName.isEmpty() || item.getOrDefault("시장명", "").contains(marketName)))
				  .collect(Collectors.toList());
	}
	
	@Override
	@RequestMapping(value="/nearby/nearby3.do", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView nearby(HttpServletRequest request, HttpServletResponse response) throws Exception{
		//주변상권
		HttpSession session;
		ModelAndView mav = new ModelAndView();
		String layout = "common/layout";
		mav.setViewName(layout);
		String viewName = (String)request.getAttribute("viewName");
		mav.addObject("viewName", viewName);
		
		mav.addObject("pageType", "sijangbajo");
		
		session = request.getSession();
		session.setAttribute("sideMenu", "reveal");
		session.setAttribute("sideMenu_option", "nearby");
		
		return mav;
	}
	
	@ResponseBody
	@RequestMapping(value="/nearby/searchApi.do", method = RequestMethod.GET, produces="application/json; charset=UTF-8")
	public List<Map<String,String>> searchNearbyAjax(HttpServletRequest request) throws Exception {

	    String divId    = request.getParameter("divId");   // ctprvnCd, signguCd, adongCd, indsLclsCd...
	    String key      = request.getParameter("key");     // 선택한 코드값
	    String indsLclsCd = request.getParameter("indsLclsCd"); // 업종 대분류
	    String indsMclsCd = request.getParameter("indsMclsCd"); // 업종 중분류
	    String indsSclsCd = request.getParameter("indsSclsCd"); // 업종 소분류
	    String keyword  = request.getParameter("keyword"); // 키워드

	    String serviceKey = "%2F2vcOHGzNGP%2F8zjFlX1i9QWj9IrvLSYBpKso2R%2FKt8pWEBSSykLBTybHIdCCsK1hS0bQaT8QjWoV11vZLxECMg%3D%3D";

	 // key 값 URL 인코딩
        String encodedKey = key != null ? URLEncoder.encode(key,"UTF-8") : "";

        String apiUrl = "https://apis.data.go.kr/B553077/api/open/sdsc2/storeListInDong"
                + "?ServiceKey=" + serviceKey
                + "&pageNo=1&numOfRows=10"
                + (divId != null && key != null ? "&divId=" + divId + "&key=" + key : "")
                + (indsLclsCd != null && !indsLclsCd.isEmpty() ? "&indsLclsCd=" + indsLclsCd : "")
                + (indsMclsCd != null && !indsMclsCd.isEmpty() ? "&indsMclsCd=" + indsMclsCd : "")
                + (indsSclsCd != null && !indsSclsCd.isEmpty() ? "&indsSclsCd=" + indsSclsCd : "")
                + "&type=json";

        // API 호출 및 응답 처리
        String jsonResponse = sijangService.fetchDataFromApi(apiUrl);
        JSONObject jsonObj = new JSONObject(jsonResponse);
        JSONArray items = jsonObj.getJSONObject("body").getJSONArray("items");

        List<Map<String,String>> storeList = new ArrayList<>();
        for (int i = 0; i < items.length(); i++) {
            JSONObject obj = items.getJSONObject(i);
            Map<String,String> map = new HashMap<>();
            map.put("bizesNm", obj.optString("bizesNm"));
            map.put("indsMclsNm", obj.optString("indsMclsNm"));
            map.put("rdnmAdr", obj.optString("rdnmAdr"));
            map.put("latitude", obj.optString("latitude"));
            map.put("longitude", obj.optString("longitude"));
            storeList.add(map);
        }

        // 키워드 필터링
        if (keyword != null && !keyword.isEmpty()) {
            storeList = storeList.stream()
                                 .filter(item -> item.getOrDefault("bizesNm", "").contains(keyword))
                                 .collect(Collectors.toList());
        }

        return storeList;
    }

	
	@Override
	@RequestMapping(value="/nearby/nearCourse.do", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView nearCourse(HttpServletRequest request, HttpServletResponse response) throws Exception{
		//추천 코스
		HttpSession session;
		ModelAndView mav = new ModelAndView();
		String layout = "common/layout";
		mav.setViewName(layout);
		String viewName = (String)request.getAttribute("viewName");
		mav.addObject("viewName", viewName);

		session = request.getSession();
		session.setAttribute("sideMenu", "reveal");
		session.setAttribute("sideMenu_option", "nearby");
		
		return mav;
	}
	
	@Override
	@RequestMapping(value="/clean/clean.do", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView clean(HttpServletRequest request, HttpServletResponse response) throws Exception{
		//클린업체
		HttpSession session;
		ModelAndView mav = new ModelAndView();
		String layout = "common/layout";
		mav.setViewName(layout);
		String viewName = (String)request.getAttribute("viewName");
		mav.addObject("viewName", viewName);

		session = request.getSession();
		session.setAttribute("sideMenu", "reveal");
		session.setAttribute("sideMenu_option", "clean");
		
		return mav;
	}
	
}
