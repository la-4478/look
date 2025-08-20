package com.lookmarket.sijangbajo.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

@Service("sijangSearchService")
public class SijangSearchService2 {
	
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
	public List<Map<String, Object>> fetchTourCourses2(String areaCode) {
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
	    	    + "&numOfRows=10"
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

	public List<Map<String, Object>> fetchFestivals(String startDate) {
	    List<Map<String, Object>> festivalList = new ArrayList<>();

	    try {
	        String rawServiceKey = "발급받은인증키";
	        String encodedKey = URLEncoder.encode(rawServiceKey, StandardCharsets.UTF_8);

	        String apiUrl = "https://apis.data.go.kr/B551011/KorService2/searchFestival2"
	                + "?serviceKey=" + encodedKey
	                + "&MobileOS=ETC"
	                + "&MobileApp=SijangBajo"
	                + "&eventStartDate=" + startDate
	                + "&_type=json"
	                + "&numOfRows=20"
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

}
