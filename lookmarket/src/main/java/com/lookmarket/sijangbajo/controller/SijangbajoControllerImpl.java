package com.lookmarket.sijangbajo.controller;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lookmarket.common.kakao.KakaoLocalClient;
import com.lookmarket.sijangbajo.service.SijangSearchService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Controller("sijangbajoController")
@RequestMapping(value="/sijangbajo")
public class SijangbajoControllerImpl implements SijangbajoController {

    @Autowired
    private SijangSearchService sijangService;

    @Autowired
    KakaoLocalClient kakaoLocalClient;

    @Autowired
    ObjectMapper om;

    @Value("${kakao.js-key:}")
    private String kakaoJsKey;
    @Value("${kakao.rest-key:}")
    private String kakaoRestKey;

    // ================== ✅ 시/도 정규화 맵 & 유틸 ==================
    private static final Map<String, String> SIDO_MAP = new HashMap<>();
    static {
        SIDO_MAP.put("서울", "서울특별시");
        SIDO_MAP.put("부산", "부산광역시");
        SIDO_MAP.put("대구", "대구광역시");
        SIDO_MAP.put("인천", "인천광역시");
        SIDO_MAP.put("광주", "광주광역시");
        SIDO_MAP.put("대전", "대전광역시");
        SIDO_MAP.put("울산", "울산광역시");
        SIDO_MAP.put("세종", "세종특별자치시");
        SIDO_MAP.put("경기", "경기도");
        SIDO_MAP.put("강원", "강원특별자치도");   // 변경됨
        SIDO_MAP.put("충북", "충청북도");
        SIDO_MAP.put("충남", "충청남도");
        SIDO_MAP.put("전북", "전북특별자치도");  // 변경됨
        SIDO_MAP.put("전남", "전라남도");
        SIDO_MAP.put("경북", "경상북도");
        SIDO_MAP.put("경남", "경상남도");
        SIDO_MAP.put("제주", "제주특별자치도");
    }

    private static String normalizeSido(String input) {
        if (isEmpty(input)) return null;
        String key = input.trim();
        // 이미 풀네임이면 그대로
        if (SIDO_MAP.containsValue(key)) return key;
        // 축약형이면 풀네임으로
        return SIDO_MAP.getOrDefault(key, key);
    }
    // =====================================================

    @Override
    @RequestMapping(value="/sijangSearch/search.do", method = { RequestMethod.GET, RequestMethod.POST })
    public ModelAndView search(HttpServletRequest request, HttpServletResponse response) throws Exception {
        ModelAndView mav = new ModelAndView("common/layout");

        String viewName = (String) request.getAttribute("viewName");
        mav.addObject("viewName", viewName);


        String apiUrl2 = "https://api.odcloud.kr/api/15052836/v1/uddi:2253111c-b6f3-45ad-9d66-924fd92dabd7?page=1&perPage=1000&serviceKey=YU6e42LEcBk0HxFjOvjJmeT93M%2FftIc3HK8kXmgMnh%2Fen2s2q2dPNQKL2ifT5WQd5LnY4a2J9KmhwBMECJDMuQ%3D%3D";

        List<Map<String, String>> sijangList2 = sijangService.fetchAllDataFromApi(apiUrl2);

        List<Map<String, String>> allSijangList = new ArrayList<>();

        if (sijangList2 != null) allSijangList.addAll(sijangList2);
        
        // 서울만 좌표 부여 테스트 (그대로 유지)
        List<Map<String, Object>> seoulWithCoord = new ArrayList<>();
        for (Map<String, String> item : allSijangList) {
            String addr = item.get("지번주소");
            if (addr != null && addr.contains("서울")) {
                Map<String, Object> row = new HashMap<>(item); // 원본 복사
                kakaoLocalClient.geocode(addr).ifPresent(coord -> {
                    row.put("lat", coord.lat);
                    row.put("lng", coord.lng);
                });
                seoulWithCoord.add(row);
            }
        }

        mav.addObject("seoulSijangList", seoulWithCoord);
        mav.addObject("seoulSijangListJson", om.writeValueAsString(seoulWithCoord));


        mav.addObject("sijangList2", sijangList2);

        mav.addObject("kakaoJsKey", kakaoJsKey);

        HttpSession session = request.getSession();
        session.setAttribute("sideMenu", "reveal");
        session.setAttribute("sideMenu_option", "search");
        return mav;
    }

    @ResponseBody
    @RequestMapping(
        value = "/sijangSearch/searchDetail.do",
        method = RequestMethod.GET,
        produces = "application/json; charset=UTF-8"
    )
    public List<Map<String, String>> searchAjax(HttpServletRequest request) throws Exception {
        String sido = trim(request.getParameter("sido"));          // "부산" / "부산광역시" / null
        String sigungu = trim(request.getParameter("sigungu"));    // "부산진구" / "전체" / null
        String marketName = trim(request.getParameter("marketName"));
        

        // ✅ 입력 정규화
        String sidoNorm = normalizeSido(sido);
        String sigunguNorm = "전체".equals(sigungu) ? null : sigungu;

        String apiUrl1 = "https://api.odcloud.kr/api/15052837/v1/uddi:8e90c34b-c086-422f-882a-d3c15efd101f?page=1&perPage=1000&serviceKey=YU6e42LEcBk0HxFjOvjJmeT93M%2FftIc3HK8kXmgMnh%2Fen2s2q2dPNQKL2ifT5WQd5LnY4a2J9KmhwBMECJDMuQ%3D%3D";
        String apiUrl2 = "https://api.odcloud.kr/api/15052836/v1/uddi:2253111c-b6f3-45ad-9d66-924fd92dabd7?page=1&perPage=1000&serviceKey=YU6e42LEcBk0HxFjOvjJmeT93M%2FftIc3HK8kXmgMnh%2Fen2s2q2dPNQKL2ifT5WQd5LnY4a2J9KmhwBMECJDMuQ%3D%3D";
        var ping1 = rawGet(apiUrl1);
        var ping2 = rawGet(apiUrl2);
        System.out.println("[PING1] code=" + ping1.get("code") + " body.head=" + String.valueOf(ping1.get("body")).substring(0, Math.min(200, String.valueOf(ping1.get("body")).length())));
        System.out.println("[PING2] code=" + ping2.get("code") + " body.head=" + String.valueOf(ping2.get("body")).substring(0, Math.min(200, String.valueOf(ping2.get("body")).length())));
        List<Map<String, String>> list1 = sijangService.fetchAllDataFromApi(apiUrl1);
        List<Map<String, String>> list2 = sijangService.fetchAllDataFromApi(apiUrl2);

        List<Map<String, String>> merged = new ArrayList<>();
        if (list1 != null) merged.addAll(list1);
        if (list2 != null) merged.addAll(list2);

        System.out.println("[searchDetail] total=" + merged.size()
                + " sidoIn=" + sido + " => norm=" + sidoNorm
                + " sigunguIn=" + sigungu + " => norm=" + sigunguNorm
                + " marketName=" + marketName);

        // ✅ 컬럼 키 후보들 한 번에 정의
        final String[] SIDO_KEYS = { "시도", "시도명", "소재지 시도", "소재지(시도)" };
        final String[] SIGUNGU_KEYS = { "시군구", "시군구명", "소재지 시군구", "소재지(시군/구)" };
        final String[] ADDR_JIBUN_KEYS = { "지번주소", "소재지지번주소", "소재지 주소", "주소", "소재지주소" };
        final String[] ADDR_ROAD_KEYS  = { "도로명주소", "소재지도로명주소" };
        final String[] MARKET_KEYS     = { "시장명", "시장명칭", "전통시장명" };

        // ✅ 필터링
        return merged.stream().filter(item -> {
            String addrJibun = firstNonEmptyFrom(item, ADDR_JIBUN_KEYS);
            String addrRoad  = firstNonEmptyFrom(item, ADDR_ROAD_KEYS);
            String addrAny   = firstNonEmpty(addrRoad, addrJibun);

            String itemSidoRaw    = firstNonEmptyFrom(item, SIDO_KEYS);
            String itemSigunguRaw = firstNonEmptyFrom(item, SIGUNGU_KEYS);
            String itemMarket     = firstNonEmptyFrom(item, MARKET_KEYS);

            String itemSidoNorm    = normalizeSido(itemSidoRaw);
            String itemSigunguNorm = itemSigunguRaw;

            // 주소 폴백 파싱
            if (isEmpty(itemSidoNorm)) {
                itemSidoNorm = normalizeSido(extractSidoFromAddress(addrAny));
            }
            if (isEmpty(itemSigunguNorm)) {
                itemSigunguNorm = extractSigunguFromAddress(addrAny);
            }

            // 조건
            boolean passSido =
                isEmpty(sidoNorm) ||
                containsIgnoreCase(itemSidoNorm, sidoNorm) ||
                containsIgnoreCase(addrAny, sidoNorm);

            boolean passSigungu =
                isEmpty(sigunguNorm) ||
                containsIgnoreCase(itemSigunguNorm, sigunguNorm) ||
                containsIgnoreCase(addrAny, sigunguNorm);

            boolean passMarket =
                isEmpty(marketName) ||
                containsIgnoreCase(itemMarket, marketName);

            // 디버그 찍고 싶으면 주석 해제
            // System.out.println("DBG :: " + itemMarket + " | " + itemSidoNorm + " | " + itemSigunguNorm + " | " + addrAny);

            return passSido && passSigungu && passMarket;
        }).collect(Collectors.toList());
    }

    /* ================== 컨트롤러 내부 private 헬퍼 ================== */

    private static String trim(String s) {
        return s == null ? null : s.trim();
    }
    private static boolean isEmpty(String s) {
        return s == null || s.isEmpty();
    }
    private static boolean containsIgnoreCase(String haystack, String needle) {
        if (isEmpty(haystack) || isEmpty(needle)) return false;
        return haystack.toLowerCase().contains(needle.toLowerCase());
    }
    @SafeVarargs
    private static <T> T firstNonEmpty(T... vals) {
        for (T v : vals) {
            if (v instanceof String) {
                if (!isEmpty((String) v)) return v;
            } else if (v != null) {
                return v;
            }
        }
        return null;
    }
    // ✅ 맵에서 여러 후보 키 중 첫 값
    private static String firstNonEmptyFrom(Map<String, String> map, String... keys) {
        if (map == null) return null;
        for (String k : keys) {
            String v = trim(map.get(k));
            if (!isEmpty(v)) return v;
        }
        return null;
    }

    /** 주소에서 시/도를 추정 (예: "서울특별시 강남구 …" → "서울특별시") */
    private static String extractSidoFromAddress(String addr) {
        if (isEmpty(addr)) return null;
        String[] tok = addr.split("\\s+");
        if (tok.length == 0) return null;
        return tok[0];
    }

    /** 주소에서 시/군/구를 추정 (예: "경기도 수원시 장안구 …" → "장안구" 우선, 없으면 "수원시") */
    private static String extractSigunguFromAddress(String addr) {
        if (isEmpty(addr)) return null;
        String[] tok = addr.split("\\s+");
        if (tok.length < 2) return null;

        String t1 = tok[1]; // 보통 시/군/구 or 시
        if (tok[0].contains("세종")) return t1; // 세종시 특례

        // 2단계(시 다음 구/군)까지 있을 때는 구/군을 우선 반환
        if (tok.length >= 3 && (tok[2].endsWith("구") || tok[2].endsWith("군"))) {
            return tok[2];
        }
        return t1;
    }
	
	@Override
	@RequestMapping(value="/nearby/nearby.do", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView nearby(HttpServletRequest request, HttpServletResponse response) throws Exception{
		//숙박정보
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
        Map<String, Object> jsonResponse = sijangService.fetchDataFromApi2(apiUrl);
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
	@RequestMapping(value = "/nearby/nearCourse.do", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView nearCourse(HttpServletRequest request, HttpServletResponse response) throws Exception {
	    HttpSession session = request.getSession();
	    ModelAndView mav = new ModelAndView();

	    mav.setViewName("common/layout");
	    mav.addObject("viewName", "sijangbajo/nearby/nearCourse");
	    mav.addObject("pageType", "sijangbajo");

	    // 1. 전체 코스 가져오기
	    List<Map<String, Object>> courseList = sijangService.fetchTourCourses(null);

	    // 2. 사용자 입력값 받기
	    String sido = request.getParameter("sido");
	    String sigungu = request.getParameter("sigungu");

	    // 3. 필터링 적용
	    List<Map<String, Object>> filtered = courseList.stream()
	        .filter(item -> {
	            String addr = String.valueOf(item.getOrDefault("address", ""));
	            boolean matchSido = (sido == null || sido.isBlank()) || addr.contains(sido);
	            boolean matchSigungu = (sigungu == null || sigungu.isBlank()) || addr.contains(sigungu);
	            return matchSido && matchSigungu;
	        })
	        .collect(Collectors.toList());

	    // 4. 필터링된 리스트로 전달
	    mav.addObject("courseList", filtered);
	    mav.addObject("sido", sido);       // 선택된 값 유지
	    mav.addObject("sigungu", sigungu); // 선택된 값 유지

	    System.out.println("courseList 원본 크기: " + courseList.size());
	    System.out.println("filtered 결과 크기: " + filtered.size());

	    session.setAttribute("sideMenu", "reveal");
	    session.setAttribute("sideMenu_option", "nearby");

	    return mav;
	}



    // =========================
    // 시장명 → 좌표 조회 (기존 유지)
    // =========================
    @GetMapping(value="/sijangSearch/getMarketCoords.do", produces="application/json;charset=UTF-8")
    @ResponseBody
    public Map<String,Object> getMarketCoords(
            @RequestParam("marketName") String marketName,
            @RequestParam(value="addr", required=false) String addr) {

        Map<String,Object> result = new HashMap<>();
        BufferedReader br = null;
        HttpURLConnection conn = null;

        try {
            String query = (addr != null && !addr.isBlank()) ? addr : marketName;
            String url = "https://dapi.kakao.com/v2/local/search/address.json?query=" 
                         + URLEncoder.encode(query, StandardCharsets.UTF_8.name());

            System.out.println("[getMarketCoords] marketName=" + marketName + " / addr=" + addr);

            conn = (HttpURLConnection) new URL(url).openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Authorization", "KakaoAK " + kakaoRestKey);
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(5000);

            int code = conn.getResponseCode();
            br = new BufferedReader(new InputStreamReader(
                    (code >= 200 && code < 300) ? conn.getInputStream() : conn.getErrorStream(),
                    StandardCharsets.UTF_8));

            StringBuilder sb = new StringBuilder();
            for (String line; (line = br.readLine()) != null; ) sb.append(line);

            JSONObject json = new JSONObject(sb.toString());
            JSONArray docs = json.optJSONArray("documents");

            if (docs != null && docs.length() > 0) {
                JSONObject first = docs.getJSONObject(0);
                result.put("longitude", Double.parseDouble(first.getString("x")));
                result.put("latitude",  Double.parseDouble(first.getString("y")));
                result.put("marketName", marketName);
                return result;
            }

            String keyword = (addr != null && !addr.isBlank()) ? (marketName + " " + addr) : marketName;
            String url2 = "https://dapi.kakao.com/v2/local/search/keyword.json?query=" 
                          + URLEncoder.encode(keyword, StandardCharsets.UTF_8.name());

            closeQuietly(br); conn.disconnect();
            conn = (HttpURLConnection) new URL(url2).openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Authorization", "KakaoAK " + kakaoRestKey);
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(5000);

            code = conn.getResponseCode();
            br = new BufferedReader(new InputStreamReader(
                    (code >= 200 && code < 300) ? conn.getInputStream() : conn.getErrorStream(),
                    StandardCharsets.UTF_8));
            sb.setLength(0);
            for (String line; (line = br.readLine()) != null; ) sb.append(line);

            JSONObject json2 = new JSONObject(sb.toString());
            JSONArray docs2 = json2.optJSONArray("documents");

            if (docs2 != null && docs2.length() > 0) {
                JSONObject first = docs2.getJSONObject(0);
                result.put("longitude", Double.parseDouble(first.getString("x")));
                result.put("latitude",  Double.parseDouble(first.getString("y")));
                result.put("marketName", marketName);
            }

        } catch (Exception e) {
            result.put("error", e.getMessage());
            e.printStackTrace();
        } finally {
            closeQuietly(br);
            if (conn != null) conn.disconnect();
        }
        return result;
    }

    private static void closeQuietly(Closeable c) { try { if (c != null) c.close(); } catch (IOException ignore) {} }

    @GetMapping(value="/sijangSearch/nearby.json", produces = "application/json; charset=UTF-8")
    @ResponseBody
    public List<Map<String, Object>> nearbyCategory(
            @RequestParam("x") double lng,
            @RequestParam("y") double lat,
            @RequestParam("code") String categoryCode,
            @RequestParam(value="radius", defaultValue="500") int radius,
            @RequestParam(value="size", defaultValue="15") int size) {

        return kakaoLocalClient.searchCategory(lng, lat, categoryCode, radius, size);
    }
    
    private static Map<String, Object> rawGet(String urlStr) {
        Map<String, Object> out = new HashMap<>();
        try {
            var url = new URL(urlStr);
            var conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(10000);

            int code = conn.getResponseCode();
            out.put("code", code);

            try (var br = new BufferedReader(new InputStreamReader(
                    (code >= 200 && code < 300) ? conn.getInputStream() : conn.getErrorStream(),
                    StandardCharsets.UTF_8))) {
                var sb = new StringBuilder();
                for (String line; (line = br.readLine()) != null; ) sb.append(line);
                out.put("body", sb.toString());
            }
            conn.disconnect();
        } catch (Exception e) {
            out.put("error", e.toString());
        }
        return out;
    }
    
 // festivalList.do 수정
    @Override
    @RequestMapping(value="/nearby/festivalList.do", method = { RequestMethod.GET, RequestMethod.POST })
    public ModelAndView festivalList(HttpServletRequest request, HttpServletResponse response) throws Exception {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("common/layout");
        mav.addObject("viewName", "sijangbajo/nearby/festivalList");
        mav.addObject("pageType", "sijangbajo");

        String areaCode = request.getParameter("areaCode"); // 선택한 지역코드
        String today = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));

        List<Map<String, Object>> festivalList;

        if (areaCode != null && !areaCode.isEmpty()) {
            // ✅ 지역 코드가 있을 경우 해당 지역의 축제만 가져오기
            festivalList = sijangService.fetchFestivalList(areaCode);
        } else {
            // ✅ 지역 코드 없으면 전체 축제 중 오늘 날짜 기준으로 필터링
            festivalList = sijangService.fetchFestivals(today);
        }

        mav.addObject("festivalList", festivalList);
        mav.addObject("areaCode", areaCode); // 드롭다운 선택 유지용

        HttpSession session = request.getSession();
        session.setAttribute("sideMenu", "reveal");
        session.setAttribute("sideMenu_option", "nearby");

        return mav;
    }

    @GetMapping("/api/festivals.do")
    public ResponseEntity<List<Map<String, Object>>> getFestivalsByRegion(
            @RequestParam(value = "areaCode", required = false) String areaCode) {

        List<Map<String, Object>> festivals;

        if (areaCode == null || areaCode.isEmpty()) {
            // 전체 축제 중 진행 중인 것만 필터링
            List<Map<String, Object>> allFestivals = sijangService.fetchAllFestivals(); // 전체 API 데이터
            LocalDate today = LocalDate.now();

            festivals = allFestivals.stream()
                .filter(f -> {
                    try {
                        String startStr = (String) f.get("eventStartDate");
                        String endStr = (String) f.get("eventEndDate");

                        LocalDate start = LocalDate.parse(startStr, DateTimeFormatter.BASIC_ISO_DATE); // yyyyMMdd
                        LocalDate end = LocalDate.parse(endStr, DateTimeFormatter.BASIC_ISO_DATE);

                        return !today.isBefore(start) && !today.isAfter(end); // 진행 중인지 확인
                    } catch (Exception e) {
                        return false; // 날짜 파싱 실패하면 제외
                    }
                })
                .collect(Collectors.toList());

        } else {
            // 지역별 축제 리스트
            festivals = sijangService.fetchFestivalListByRegionName(areaCode);
        }

        return ResponseEntity.ok(festivals);
    }


//    @GetMapping("/api/festivals/today")
//    @ResponseBody
//    public ResponseEntity<List<Map<String, Object>>> getOngoingFestivalsByRegion(@RequestParam String region) {
//        String fullRegion = normalizeSido(region);
//        List<Map<String, Object>> allFestivals = sijangService.fetchFestivalListByRegionName(fullRegion);
//
//        LocalDate today = LocalDate.now();
//
//        List<Map<String, Object>> ongoingFestivals = allFestivals.stream()
//            .filter(f -> {
//                try {
//                    String startStr = (String) f.get("startDate");
//                    String endStr   = (String) f.get("endDate");
//
//                    LocalDate start = LocalDate.parse(startStr, DateTimeFormatter.BASIC_ISO_DATE);
//                    LocalDate end   = LocalDate.parse(endStr, DateTimeFormatter.BASIC_ISO_DATE);
//
//                    return !today.isBefore(start) && !today.isAfter(end);
//                } catch (Exception e) {
//                    return false;
//                }
//            })
//            .collect(Collectors.toList());
//
//        return ResponseEntity.ok(ongoingFestivals);
//    }

}
