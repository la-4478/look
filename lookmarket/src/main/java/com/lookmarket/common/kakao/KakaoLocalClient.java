// com.lookmarket.common.kakao.KakaoLocalClient
package com.lookmarket.common.kakao;

import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class KakaoLocalClient {

    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${kakao.rest-key}") 
    private String restKey;

    public static class Coord {
        public final double lat; // y
        public final double lng; // x
        public Coord(double lat, double lng) { this.lat = lat; this.lng = lng; }
    }

    /** 주소 → 좌표(위/경도). 성공 시 Optional<Coord> 반환 */
    public Optional<Coord> geocode(String address) {
        if (address == null || address.isBlank()) return Optional.empty();
        try {
            String q = URLEncoder.encode(address, StandardCharsets.UTF_8);
            String url = "https://dapi.kakao.com/v2/local/search/address.json?query=" + q;

            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "KakaoAK " + restKey);
            HttpEntity<Void> http = new HttpEntity<>(headers);

            ResponseEntity<Map> resp = restTemplate.exchange(URI.create(url), HttpMethod.GET, http, Map.class);
            if (resp.getStatusCode().is2xxSuccessful() && resp.getBody()!=null) {
                List<Map<String,Object>> docs = (List<Map<String,Object>>) resp.getBody().get("documents");
                if (docs!=null && !docs.isEmpty()) {
                    Map<String,Object> first = docs.get(0);
                    double x = Double.parseDouble(String.valueOf(first.get("x")));
                    double y = Double.parseDouble(String.valueOf(first.get("y")));
                    return Optional.of(new Coord(y, x));
                }
            }
        } catch (Exception ignore) {}
        return Optional.empty();
    }

    /** 주변 카테고리 검색 (예: FD6 음식점, CE7 카페, CS2 편의점 등) */
    public List<Map<String,Object>> searchCategory(double lng, double lat, String categoryCode, int radiusMeters, int size) {
        try {
            String url = String.format(
                "https://dapi.kakao.com/v2/local/search/category.json?category_group_code=%s&x=%f&y=%f&radius=%d&size=%d",
                URLEncoder.encode(categoryCode, StandardCharsets.UTF_8), lng, lat, radiusMeters, size);

            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "KakaoAK " + restKey);
            HttpEntity<Void> http = new HttpEntity<>(headers);

            ResponseEntity<Map> resp = restTemplate.exchange(URI.create(url), HttpMethod.GET, http, Map.class);
            if (resp.getStatusCode().is2xxSuccessful() && resp.getBody()!=null) {
                List<Map<String,Object>> docs = (List<Map<String,Object>>) resp.getBody().get("documents");
                return docs == null ? List.of() : docs;
            }
        } catch (Exception ignore) {}
        return List.of();
    }
    
    public Optional<Coord> keywordSearch(String keyword) {
        try {
            String q = URLEncoder.encode(keyword, StandardCharsets.UTF_8);
            String url = "https://dapi.kakao.com/v2/local/search/keyword.json?query=" + q;

            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "KakaoAK " + restKey);
            HttpEntity<Void> http = new HttpEntity<>(headers);

            ResponseEntity<Map> resp = restTemplate.exchange(URI.create(url), HttpMethod.GET, http, Map.class);
            if (resp.getStatusCode().is2xxSuccessful() && resp.getBody()!=null) {
                List<Map<String,Object>> docs = (List<Map<String,Object>>) resp.getBody().get("documents");
                if (docs != null && !docs.isEmpty()) {
                    Map<String,Object> first = docs.get(0);
                    double x = Double.parseDouble(String.valueOf(first.get("x")));
                    double y = Double.parseDouble(String.valueOf(first.get("y")));
                    return Optional.of(new Coord(y, x));
                }
            }
        } catch (Exception ignore) {}
        return Optional.empty();
    }
}
