package com.lookmarket.sijangbajo.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service("sijangSearchService")
public class SijangSearchService {
	
	public List<Map<String, String>> fetchAllDataFromApi(String apiUrl) {
		List<Map<String, String>> dataList  = new ArrayList<>();
		
	    try {
	        URL url = new URL(apiUrl);
	        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
	        conn.setRequestMethod("GET");

	        if (conn.getResponseCode() == 200) {
	            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
	            StringBuilder sb = new StringBuilder();
	            String line;
	            while ((line = in.readLine()) != null) {
	                sb.append(line);
	            }
	            in.close();

	            JSONObject json = new JSONObject(sb.toString());
	            JSONArray dataArray = json.getJSONArray("data");

	            for (int i = 0; i < dataArray.length(); i++) {
	                JSONObject item = dataArray.getJSONObject(i);
	                Map<String, String> map = new LinkedHashMap<>(); // 순서 보장

	                Iterator<String> keys = item.keys();
	                while (keys.hasNext()) {
	                    String key = keys.next();
	                    map.put(key, item.optString(key, ""));
	                }

	                dataList.add(map);
	            }
	        } else {
	            System.out.println("API 응답 오류: " + conn.getResponseCode());
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	    }

	    return dataList;
	}
	
	
	public List<Map<String, Object>> fetchFestivals(String startDate) {
	    List<Map<String, Object>> festivalList = new ArrayList<>();

	    try {
	        String rawServiceKey = "2jgkuxtnmXwkyNhBItGVEgjMOV8IATXuwlZLJsbjbELR1bhnG0pCi7GH4eJlWLhuC1sohQgeOlCeX1WwrhWLSA==";
	        String encodedKey = URLEncoder.encode(rawServiceKey, StandardCharsets.UTF_8);

	        String apiUrl = "https://apis.data.go.kr/B551011/KorService2/searchFestival2"
	                + "?serviceKey=" + encodedKey
	                + "&MobileOS=ETC"
	                + "&MobileApp=SijangBajo"
	                + "&eventStartDate=" + startDate
	                + "&_type=json"
	                + "&numOfRows=100"
	                + "&pageNo=1";

	        URL url = new URL(apiUrl);
	        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
	        conn.setRequestMethod("GET");

	        if (conn.getResponseCode() == 200) {
	            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
	            StringBuilder sb = new StringBuilder();
	            String line;
	            while ((line = in.readLine()) != null) {
	                sb.append(line);
	            }
	            in.close();

	            JSONObject json = new JSONObject(sb.toString());
	            JSONArray items = json.getJSONObject("response")
	                                  .getJSONObject("body")
	                                  .getJSONObject("items")
	                                  .getJSONArray("item");

	            for (int i = 0; i < items.length(); i++) {
	                JSONObject item = items.getJSONObject(i);
	                Map<String, Object> festival = new HashMap<>();
	                festival.put("title", item.optString("title", "제목 없음"));
	                festival.put("address", item.optString("addr1", ""));
	                festival.put("image", item.optString("firstimage", ""));
	                festival.put("startDate", item.optString("eventstartdate", ""));
	                festival.put("endDate", item.optString("eventenddate", ""));
	                festivalList.add(festival);
	            }
	        } else {
	            System.out.println("축제 API 오류 코드: " + conn.getResponseCode());
	        }

	    } catch (Exception e) {
	        e.printStackTrace();
	    }

	    return festivalList;
	}
	
	public List<Map<String, Object>> fetchTourCourses(String areaCode) {
	    List<Map<String, Object>> courseList = new ArrayList<>();
	    try {
	    	String rawServiceKey = "2jgkuxtnmXwkyNhBItGVEgjMOV8IATXuwlZLJsbjbELR1bhnG0pCi7GH4eJlWLhuC1sohQgeOlCeX1WwrhWLSA==";
	    	String encodedServiceKey = URLEncoder.encode(rawServiceKey, StandardCharsets.UTF_8);

	    	String apiUrl = "https://apis.data.go.kr/B551011/KorService2/areaBasedList2"
	    	    + "?serviceKey=" + encodedServiceKey
	    	    + "&MobileOS=ETC"
	    	    + "&MobileApp=SijangBajo"
	    	    + "&contentTypeId=39"
	    	    + "&_type=json"
	    	    + "&numOfRows=20"
	    	    + "&pageNo=1";

	    	// areaCode가 있을 때만 붙임
	        if (areaCode != null && !areaCode.trim().isEmpty()) {
	            apiUrl += "&areaCode=" + areaCode;
	        }
	        
	        URL url = new URL(apiUrl);
	        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
	        conn.setRequestMethod("GET");

	        if (conn.getResponseCode() == 200) {
	            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
	            StringBuilder sb = new StringBuilder();
	            String line;
	            while ((line = in.readLine()) != null) {
	                sb.append(line);
	            }
	            in.close();

	            JSONObject json = new JSONObject(sb.toString());
	            JSONObject response = json.getJSONObject("response");
	            JSONObject body = response.getJSONObject("body");
	            JSONObject items = body.getJSONObject("items");
	            JSONArray itemArray = items.getJSONArray("item");

	            for (int i = 0; i < itemArray.length(); i++) {
	                JSONObject item = itemArray.getJSONObject(i);
	                Map<String, Object> course = new HashMap<>();

	                course.put("title", item.optString("title", "제목 없음"));
	                course.put("image", item.optString("firstimage", ""));
	                course.put("address", item.optString("addr1", "") + " " + item.optString("addr2", ""));
	                course.put("latitude", item.optString("mapy", ""));
	                course.put("longitude", item.optString("mapx", ""));


	                courseList.add(course);
	            }
	        } else {
	            System.out.println("관광 API 오류 코드: " + conn.getResponseCode());
	        }

	    } catch (Exception e) {
	        e.printStackTrace();
	    }

	    return courseList;
	}
	public List<Map<String, Object>> fetchFestivalListByRegionName(String regionName) {
	    String normalizedRegionName = normalizeRegionName(regionName); // ← 정규화 적용
	    String areaCode = areaCodeMap.get(normalizedRegionName);       // 정규화된 지역명으로 areaCode 검색

	    System.out.println("입력: " + regionName + " → 정규화: " + normalizedRegionName + " → areaCode: " + areaCode); // 디버깅 로그

	    return fetchFestivalList(areaCode);  // 기존 메서드 활용
	}



	public double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
	    double earthRadius = 6371.0; // km 단위
	    double dLat = Math.toRadians(lat2 - lat1);
	    double dLon = Math.toRadians(lon2 - lon1);
	    double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
	               Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
	               Math.sin(dLon / 2) * Math.sin(dLon / 2);
	    double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
	    return earthRadius * c;
	}
	
	public Map<String, Object> fetchDataFromApi2(String apiUrl) {
	    // 응답 문자열을 담을 StringBuilder
	    StringBuilder sb = new StringBuilder();
	    try {
	        // 1) API URL 객체 생성
	        URL url = new URL(apiUrl);

	        // 2) HttpURLConnection으로 연결
	        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
	        conn.setRequestMethod("GET");                // GET 방식 요청
	        conn.setConnectTimeout(5000);                // 연결 타임아웃 (5초)
	        conn.setReadTimeout(10000);                  // 응답 읽기 타임아웃 (10초)
	        conn.setRequestProperty("Accept", "application/json"); // JSON 응답 기대

	        // 3) HTTP 응답 코드 확인 (200~299: 정상, 그 외는 에러)
	        int code = conn.getResponseCode();

	        // 4) 스트림 열기 (정상: InputStream, 오류: ErrorStream)
	        try (BufferedReader br = new BufferedReader(
	                new InputStreamReader(
	                    code >= 200 && code < 300 ? conn.getInputStream() : conn.getErrorStream(),
	                    "UTF-8"))) {
	            String line;
	            // 한 줄씩 읽어서 StringBuilder에 담기
	            while ((line = br.readLine()) != null) sb.append(line);
	        } finally {
	            // 연결 닫기
	            conn.disconnect();
	        }

	        // 5) HTTP 에러라면 예외 발생
	        if (code < 200 || code >= 300) {
	            throw new RuntimeException("HTTP " + code + " : " + sb);
	        }

	        // 6) JSON 문자열 → Map 변환 (Jackson 라이브러리 사용)
	        ObjectMapper om = new ObjectMapper();
	        return om.readValue(sb.toString(), new TypeReference<Map<String, Object>>() {});
	    } catch (Exception e) {
	        // 모든 예외를 런타임 예외로 감싸서 던짐
	        throw new RuntimeException("API 호출/파싱 실패", e);
	    }
	}
	public List<Map<String, Object>> fetchFestivalList(String areaCode) {
	    List<Map<String, Object>> festivalList = new ArrayList<>();
	    try {
	        String rawServiceKey = "2jgkuxtnmXwkyNhBItGVEgjMOV8IATXuwlZLJsbjbELR1bhnG0pCi7GH4eJlWLhuC1sohQgeOlCeX1WwrhWLSA==";
	        String encodedServiceKey = URLEncoder.encode(rawServiceKey, StandardCharsets.UTF_8);

	        //String today = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));

	        String apiUrl = "https://apis.data.go.kr/B551011/KorService2/searchFestival2"
	            + "?serviceKey=" + encodedServiceKey
	            + "&MobileOS=ETC"
	            + "&MobileApp=SijangBajo"
	            + "&_type=json"
	            + "&numOfRows=20"
	            + "&pageNo=1"
	            + "&areaCode=" + areaCode;

	        URL url = new URL(apiUrl);
	        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
	        conn.setRequestMethod("GET");

	        if (conn.getResponseCode() == 200) {
	            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
	            StringBuilder sb = new StringBuilder();
	            String line;
	            while ((line = in.readLine()) != null) {
	                sb.append(line);
	            }
	            in.close();

	            JSONObject json = new JSONObject(sb.toString());
	            JSONObject response = json.getJSONObject("response");
	            JSONObject body = response.getJSONObject("body");
	            JSONObject items = body.getJSONObject("items");
	            JSONArray itemArray = items.getJSONArray("item");

	            for (int i = 0; i < itemArray.length(); i++) {
	                JSONObject item = itemArray.getJSONObject(i);
	                Map<String, Object> festival = new HashMap<>();

	                festival.put("title", item.optString("title", "제목 없음"));
	                festival.put("image", item.optString("firstimage", ""));
	                festival.put("address", item.optString("addr1", "") + " " + item.optString("addr2", ""));
	                festival.put("latitude", item.optString("mapy", ""));
	                festival.put("longitude", item.optString("mapx", ""));
	                festival.put("eventStartDate", item.optString("eventstartdate", ""));
	                festival.put("eventEndDate", item.optString("eventenddate", ""));

	                festivalList.add(festival);
	            }
	        } else {
	            System.out.println("축제 API 오류 코드: " + conn.getResponseCode());
	        }

	    } catch (Exception e) {
	        e.printStackTrace();
	    }

	    return festivalList;
	}
	private String normalizeRegionName(String input) {
        if (input == null) return null;

        if (input.contains("서울")) return "서울특별시";
        if (input.contains("부산")) return "부산광역시";
        if (input.contains("인천")) return "인천광역시";
        if (input.contains("대전")) return "대전광역시";
        if (input.contains("대구")) return "대구광역시";
        if (input.contains("광주")) return "광주광역시";
        if (input.contains("울산")) return "울산광역시";
        if (input.contains("세종")) return "세종특별자치시";
        if (input.contains("경기")) return "경기도";
        if (input.contains("강원")) return "강원도";
        if (input.contains("충북")) return "충청북도";
        if (input.contains("충남")) return "충청남도";
        if (input.contains("경북")) return "경상북도";
        if (input.contains("경남")) return "경상남도";
        if (input.contains("전북")) return "전라북도";
        if (input.contains("전남")) return "전라남도";
        if (input.contains("제주")) return "제주특별자치도";

        return input;
    }
	// 지역명 → areaCode 매핑
	private static final Map<String, String> areaCodeMap = new HashMap<>();

	static {
	    areaCodeMap.put("서울특별시", "1");
	    areaCodeMap.put("인천광역시", "2");
	    areaCodeMap.put("대전광역시", "3");
	    areaCodeMap.put("대구광역시", "4");
	    areaCodeMap.put("광주광역시", "5");
	    areaCodeMap.put("부산광역시", "6");
	    areaCodeMap.put("울산광역시", "7");
	    areaCodeMap.put("세종특별자치시", "8");
	    areaCodeMap.put("경기도", "31");
	    areaCodeMap.put("강원도", "32");
	    areaCodeMap.put("충청북도", "33");
	    areaCodeMap.put("충청남도", "34");
	    areaCodeMap.put("경상북도", "35");
	    areaCodeMap.put("경상남도", "36");
	    areaCodeMap.put("전라북도", "37");
	    areaCodeMap.put("전라남도", "38");
	    areaCodeMap.put("제주특별자치도", "39");
	}

	public List<Map<String, Object>> fetchAllFestivals() {
		// TODO Auto-generated method stub
		return null;
	}


}
