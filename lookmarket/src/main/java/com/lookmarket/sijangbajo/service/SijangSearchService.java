package com.lookmarket.sijangbajo.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
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
}
