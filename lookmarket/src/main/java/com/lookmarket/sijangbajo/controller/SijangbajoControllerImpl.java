package com.lookmarket.sijangbajo.controller;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

    // ✅ 카카오 JS 키를 JSP로 내려주기 위해 주입 (도메인 제한 필수)
    @Value("${kakao.js-key:}")
    private String kakaoJsKey;
    @Value("${kakao.rest-key:}")
    private String kakaoRestKey;

    @Override
    @RequestMapping(value="/sijangSearch/search.do", method = { RequestMethod.GET, RequestMethod.POST })
    public ModelAndView search(HttpServletRequest request, HttpServletResponse response) throws Exception {
        ModelAndView mav = new ModelAndView("common/layout");

        String viewName = (String) request.getAttribute("viewName");
        mav.addObject("viewName", viewName);

        String apiUrl1 = "https://api.odcloud.kr/api/15052837/v1/uddi:8e90c34b-c086-422f-882a-d3c15efd101f?page=1&perPage=1000&serviceKey=YU6e42LEcBk0HxFjOvjJmeT93M%2FftIc3HK8kXmgMnh%2Fen2s2q2dPNQKL2ifT5WQd5LnY4a2J9KmhwBMECJDMuQ%3D%3D";
        String apiUrl2 = "https://api.odcloud.kr/api/15052836/v1/uddi:2253111c-b6f3-45ad-9d66-924fd92dabd7?page=1&perPage=1000&serviceKey=YU6e42LEcBk0HxFjOvjJmeT93M%2FftIc3HK8kXmgMnh%2Fen2s2q2dPNQKL2ifT5WQd5LnY4a2J9KmhwBMECJDMuQ%3D%3D";

        List<Map<String, String>> sijangList1 = sijangService.fetchAllDataFromApi(apiUrl1);
        List<Map<String, String>> sijangList2 = sijangService.fetchAllDataFromApi(apiUrl2);

        List<Map<String, String>> allSijangList = new ArrayList<>();
        if (sijangList1 != null) allSijangList.addAll(sijangList1);
        if (sijangList2 != null) allSijangList.addAll(sijangList2);

        // 2) 서울 필터 + 좌표 부여
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

        // 3) 모델에 전달 (리스트 + JSON)
        mav.addObject("seoulSijangList", seoulWithCoord);
        mav.addObject("seoulSijangListJson", om.writeValueAsString(seoulWithCoord));

        mav.addObject("sijangList1", sijangList1);
        mav.addObject("sijangList2", sijangList2);

        // ✅ 카카오 JS 키 JSP로 전달 (스크립트 로딩에 사용)
        mav.addObject("kakaoJsKey", kakaoJsKey);

        // 사이드메뉴 유지
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
        String sido = request.getParameter("sido");          // 예: "서울"
        String sigungu = request.getParameter("sigungu");    // 예: "강남구" 또는 "전체"
        String marketName = request.getParameter("marketName");

        String apiUrl1 = "https://api.odcloud.kr/api/15052837/v1/uddi:8e90c34b-c086-422f-882a-d3c15efd101f?page=1&perPage=1000&serviceKey=YU6e42LEcBk0HxFjOvjJmeT93M%2FftIc3HK8kXmgMnh%2Fen2s2q2dPNQKL2ifT5WQd5LnY4a2J9KmhwBMECJDMuQ%3D%3D";
        String apiUrl2 = "https://api.odcloud.kr/api/15052836/v1/uddi:2253111c-b6f3-45ad-9d66-924fd92dabd7?page=1&perPage=1000&serviceKey=YU6e42LEcBk0HxFjOvjJmeT93M%2FftIc3HK8kXmgMnh%2Fen2s2q2dPNQKL2ifT5WQd5LnY4a2J9KmhwBMECJDMuQ%3D%3D";

        List<Map<String, String>> list1 = sijangService.fetchAllDataFromApi(apiUrl1);
        List<Map<String, String>> list2 = sijangService.fetchAllDataFromApi(apiUrl2);

        // 두 리스트 병합 (null-safe)
        List<Map<String, String>> merged = new ArrayList<>();
        if (list1 != null) merged.addAll(list1);
        if (list2 != null) merged.addAll(list2);

        // 디버그: 사이즈/샘플 확인
        System.out.println("[searchDetail] total=" + merged.size()
                + " sido=" + sido + " sigungu=" + sigungu + " marketName=" + marketName);

        // 조건 필터링 (여러 키명 지원 + 주소 파싱 폴백 + contains 대소문자 무시)
        return merged.stream().filter(item -> {
            String addr = trim(item.get("지번주소"));

            // 시/도 후보 키들
            String itemSido = firstNonEmpty(
                    trim(item.get("시도")),
                    trim(item.get("시도명")),
                    trim(item.get("소재지 시도")),
                    trim(item.get("소재지(시도)")),
                    extractSidoFromAddress(addr)
            );

            // 시/군/구 후보 키들
            String itemSigungu = firstNonEmpty(
                    trim(item.get("시군구")),
                    trim(item.get("시군구명")),
                    trim(item.get("소재지 시군구")),
                    trim(item.get("소재지(시군/구)")),
                    extractSigunguFromAddress(addr)
            );

            String itemMarket = trim(item.get("시장명"));

            boolean passSido =
                    isEmpty(sido) ||
                    containsIgnoreCase(itemSido, sido) ||
                    containsIgnoreCase(addr, sido); // 주소로도 매칭

            boolean passSigungu =
                    isEmpty(sigungu) || "전체".equals(sigungu) ||
                    containsIgnoreCase(itemSigungu, sigungu) ||
                    containsIgnoreCase(addr, sigungu);

            boolean passMarket =
                    isEmpty(marketName) || containsIgnoreCase(itemMarket, marketName);

            return passSido && passSigungu && passMarket;
        }).collect(Collectors.toList());
    }

    /* ================== 아래는 컨트롤러 내부 private 헬퍼 ================== */

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

    /** 주소에서 시/도를 추정 (예: "서울특별시 강남구 …" → "서울특별시") */
    private static String extractSidoFromAddress(String addr) {
        if (isEmpty(addr)) return null;
        String[] tok = addr.split("\\s+");
        if (tok.length == 0) return null;
        // 첫 토큰이 보통 시/도 ("서울특별시","경기도","부산광역시","세종특별자치시" 등)
        return tok[0];
    }

    /** 주소에서 시/군/구를 추정 (예: "경기도 수원시 장안구 …" → "수원시 장안구" 또는 "장안구") */
    private static String extractSigunguFromAddress(String addr) {
        if (isEmpty(addr)) return null;
        String[] tok = addr.split("\\s+");
        if (tok.length < 2) return null;

        // 보편: 두 번째 토큰이 "강남구/중구/수원시" 등
        String t1 = tok[1];
        // 세종특별자치시는 구/군 없이 단일 도시이므로 그대로 반환
        if (tok[0].contains("세종")) return t1;

        // "수원시 장안구" 같은 2단계일 때는 2번째 또는 3번째가 구/군
        if (t1.endsWith("구") || t1.endsWith("군") || t1.endsWith("시")) {
            // 가능하면 세 번째 토큰이 구/군이면 그걸 사용
            if (tok.length >= 3 && (tok[2].endsWith("구") || tok[2].endsWith("군"))) {
                return tok[2];
            }
            return t1;
        }
        // 그 외엔 3번째를 시도
        if (tok.length >= 3) return tok[2];
        return null;
    }


    @Override
    @RequestMapping(value="/nearby/nearby.do", method = { RequestMethod.GET, RequestMethod.POST })
    public ModelAndView nearby(HttpServletRequest request, HttpServletResponse response) throws Exception {
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

    @Override
    @RequestMapping(value="/nearby/nearCourse.do", method = { RequestMethod.GET, RequestMethod.POST })
    public ModelAndView nearCourse(HttpServletRequest request, HttpServletResponse response) throws Exception {
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
    public ModelAndView clean(HttpServletRequest request, HttpServletResponse response) throws Exception {
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

    // =========================
    // ✅ 추가: 시장명 → 좌표 조회 (JSP의 viewMarketDetail()이 호출)
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
            // 1) 주소 우선 쿼리
            String query = (addr != null && !addr.isBlank()) ? addr : marketName;
            String url = "https://dapi.kakao.com/v2/local/search/address.json?query=" 
                         + URLEncoder.encode(query, StandardCharsets.UTF_8.name());

            System.out.println("[getMarketCoords] marketName=" + marketName + " / addr=" + addr);

            conn = (HttpURLConnection) new URL(url).openConnection();
            conn.setRequestMethod("GET");
            // ✅ 줄바꿈 없이 정확히 세팅
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
                // ✅ 프론트가 기대하는 키명으로 내려줌
                result.put("longitude", Double.parseDouble(first.getString("x"))); // 경도
                result.put("latitude",  Double.parseDouble(first.getString("y"))); // 위도
                result.put("marketName", marketName);
                return result;
            }

            // 2) 주소검색 실패 시: 키워드 검색으로 폴백 (시장명 + 시군구 일부)
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

    // 유틸
    private static void closeQuietly(Closeable c) { try { if (c != null) c.close(); } catch (IOException ignore) {} }



    // =========================
    // ✅ 추가: 주변 상권 카테고리 검색 (FD6 음식점, CE7 카페 등)
    // 프론트: /sijangSearch/nearby.json?x={lng}&y={lat}&code=FD6&radius=500&size=15
    // =========================
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
}
