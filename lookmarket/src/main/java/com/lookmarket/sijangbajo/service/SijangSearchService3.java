package com.lookmarket.sijangbajo.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

import org.springframework.stereotype.Service;

@Service("sijangSearchService")
public class SijangSearchService3 {
	
	public Map<String, String> fetchAllDataFromApi(String apiUrl) {
	    StringBuilder response = new StringBuilder();
	    try {
	        URL url = new URL(apiUrl);
	        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
	        conn.setRequestMethod("GET");
	        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
	        String line;
	        while ((line = br.readLine()) != null) {
	            response.append(line);
	        }
	        br.close();
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    return (Map<String, String>)response;
	}

	public String fetchDataFromApi(String apiUrl) {
		// TODO Auto-generated method stub
		return null;
	}


}
