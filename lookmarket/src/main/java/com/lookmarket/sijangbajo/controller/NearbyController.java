package com.lookmarket.sijangbajo.controller;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lookmarket.sijangbajo.vo.StoreVO;

@Controller
@RequestMapping("/sijangbajo/nearby")
public class NearbyController {
	
	// 중분류 (DB 연동 가능)
	private static final java.util.Map<String, List<String[]>> middleCategoryMap = new java.util.HashMap<>();
	static {
	    // 도소매 (G)
	    List<String[]> gList = new ArrayList<>();
	    gList.add(new String[]{"G202", "자동차 부품 및 내장품 소매업"});
	    gList.add(new String[]{"G203", "모터사이클 및 부품 소매업"});
	    gList.add(new String[]{"G204", "종합 소매업"});
	    gList.add(new String[]{"G205", "식료품 소매업"});
	    gList.add(new String[]{"G206", "음료 소매업"});
	    gList.add(new String[]{"G207", "담배 소매업"});
	    gList.add(new String[]{"G208", "가전제품 및 정보 통신장비 소매업"});
	    gList.add(new String[]{"G209", "섬유, 의복, 신발 및 가죽제품 소매업"});
	    gList.add(new String[]{"G210", "철물, 공구, 창호 및 건설자재 소매업"});
	    gList.add(new String[]{"G211", "가구 소매업"});
	    gList.add(new String[]{"G212", "기타 생활용품 소매업"});
	    gList.add(new String[]{"G213", "문화, 오락 및 여가 용품 소매업"});
	    gList.add(new String[]{"G214", "연료소매업"});
	    gList.add(new String[]{"G215", "의약품, 의료용 기구, 화장품 및 방향제 소매업"});
	    gList.add(new String[]{"G216", "사무용 기기, 안경, 사진장비 및 정밀기기 소매업"});
	    gList.add(new String[]{"G217", "시계 및 귀금속 소매업"});
	    gList.add(new String[]{"G218", "예술품, 기념품 및 장식용품 소매업"});
	    gList.add(new String[]{"G219", "화초 및 식물 소매업"});
	    gList.add(new String[]{"G220", "애완용 동물 및 관련용품 소매업"});
	    gList.add(new String[]{"G221", "기타 상품 전문 소매업"});
	    gList.add(new String[]{"G222", "중고 상품 소매업"});
	    middleCategoryMap.put("G2", gList);

	    // 숙박 (I1)
	    List<String[]> i1List = new ArrayList<>();
	    i1List.add(new String[]{"I101", "일반 및 생활 숙박시설 운영업"});
	    i1List.add(new String[]{"I102", "기타 숙박업"});
	    middleCategoryMap.put("I1", i1List);
	    
	    // 음식점 (I2)
	    List<String[]> i2List = new ArrayList<>();
	    i2List.add(new String[]{"I201", "한식 음식점업"});
	    i2List.add(new String[]{"I202", "중식 음식점업"});
	    i2List.add(new String[]{"I203", "일식 음식점업"});
	    i2List.add(new String[]{"I204", "서양식 음식점업"});
	    i2List.add(new String[]{"I205", "동남아시아 음식점업"});
	    i2List.add(new String[]{"I206", "기타 외국식 음식점업"});
	    i2List.add(new String[]{"I207", "구내식당 및 뷔페"});
	    i2List.add(new String[]{"I210", "기타 간이 음식점업"});
	    i2List.add(new String[]{"I211", "주점업"});
	    i2List.add(new String[]{"I212", "비알코올 음료점업"});
	    middleCategoryMap.put("I2", i2List);

	    // 부동산업 (L)
	    List<String[]> lList = new ArrayList<>();
	    lList.add(new String[]{"L102", "부동산관련 서비스업"});
	    middleCategoryMap.put("L1", lList);

	    // 전문, 과학 및 기술 서비스업 (M)
	    List<String[]> mList = new ArrayList<>();
	    mList.add(new String[]{"M103", "법무관련 서비스업"});
	    mList.add(new String[]{"M104", "회계 및 세무관련 서비스업"});
	    mList.add(new String[]{"M105", "광고업"});
	    mList.add(new String[]{"M106", "시장 조사 및 여론 조사업"});
	    mList.add(new String[]{"M107", "회사 본부 및 경영 컨설팅 서비스업"});
	    mList.add(new String[]{"M109", "건축 기술, 엔지니어링 및 관련 기술 서비스업"});
	    mList.add(new String[]{"M111", "수의업"});
	    mList.add(new String[]{"M112", "전문 디자인업"});
	    mList.add(new String[]{"M113", "사진 촬영 및 처리업"});
	    mList.add(new String[]{"M114", "인쇄 및 제품 제작업"});
	    mList.add(new String[]{"M115", "그 외 기타 전문, 과학 및 기술 서비스업"});
	    middleCategoryMap.put("M1", mList);

	    // 사업지원 서비스업 (N)
	    List<String[]> nList = new ArrayList<>();
	    nList.add(new String[]{"N101", "사업시설 유지ㆍ관리 서비스업"});
	    nList.add(new String[]{"N102", "건물ㆍ산업설비 청소 및 방제 서비스업"});
	    nList.add(new String[]{"N103", "조경관리 및 유지 서비스업"});
	    nList.add(new String[]{"N104", "고용 알선 및 인력 공급업"});
	    nList.add(new String[]{"N105", "여행사 및 기타 여행 보조 서비스업"});
	    nList.add(new String[]{"N107", "사무 지원 서비스업"});
	    nList.add(new String[]{"N108", "기타 사업 지원 서비스업"});
	    nList.add(new String[]{"N109", "운송장비 대여업"});
	    nList.add(new String[]{"N110", "개인 및 가정용품 대여업"});
	    nList.add(new String[]{"N111", "산업용 기계 및 장비 대여업"});
	    middleCategoryMap.put("N1", nList);

	    // 교육서비스업 (P)
	    List<String[]> pList = new ArrayList<>();
	    pList.add(new String[]{"P105", "일반 교육기관"});
	    pList.add(new String[]{"P106", "기타 교육기관"});
	    pList.add(new String[]{"P107", "교육 지원 서비스업"});
	    middleCategoryMap.put("P1", pList);

	    // 보건업 (Q)
	    List<String[]> qList = new ArrayList<>();
	    qList.add(new String[]{"Q101", "병원"});
	    qList.add(new String[]{"Q102", "의원"});
	    qList.add(new String[]{"Q104", "기타 보건업"});
	    middleCategoryMap.put("Q1", qList);

	    // 예술, 스포츠 및 여가 관련 서비스업 (R)
	    List<String[]> rList = new ArrayList<>();
	    rList.add(new String[]{"R102", "도서관, 사적지 및 유사 여가관련 서비스업"});
	    rList.add(new String[]{"R103", "스포츠 서비스업"});
	    rList.add(new String[]{"R104", "유원지 및 기타 오락관련 서비스업"});
	    middleCategoryMap.put("R1", rList);

	    // 수리 및 기타 개인 서비스업 (S)
	    List<String[]> sList = new ArrayList<>();
	    sList.add(new String[]{"S201", "컴퓨터 및 주변 기기 수리업"});
	    sList.add(new String[]{"S202", "통신장비 수리업"});
	    sList.add(new String[]{"S203", "자동차 수리 및 세차업"});
	    sList.add(new String[]{"S204", "모터사이클 수리업"});
	    sList.add(new String[]{"S205", "가전제품 수리업"});
	    sList.add(new String[]{"S206", "기타 개인 및 가정용품 수리업"});
	    sList.add(new String[]{"S207", "이용 및 미용업"});
	    sList.add(new String[]{"S208", "욕탕, 마사지 및 기타 신체 관리 서비스업"});
	    sList.add(new String[]{"S209", "세탁업"});
	    sList.add(new String[]{"S210", "장례식장 및 관련 서비스업"});
	    sList.add(new String[]{"S211", "기타 개인서비스"});
	    middleCategoryMap.put("S2", sList);
	}


	// 상권 검색
    @GetMapping("/searchApi1.do")
    @ResponseBody
    public List<StoreVO> searchNearby(
            @RequestParam("divId") String divId,
            @RequestParam("key") String key,
            @RequestParam(value="indsLclsCd", required=false) String indsLclsCd,
            @RequestParam(value="indsMclsCd", required=false) String indsMclsCd,
            @RequestParam(value="indsSclsCd", required=false) String indsSclsCd,  // ✅ 소분류 파라미터 추가
            @RequestParam(value="keyword", required=false) String keyword
    ) throws Exception {
    	System.out.println("📥 받은 대분류 코드: " + indsLclsCd);
        System.out.println("📥 받은 중분류 코드: " + indsMclsCd);

        String encodedKey = "%2F2vcOHGzNGP%2F8zjFlX1i9QWj9IrvLSYBpKso2R%2FKt8pWEBSSykLBTybHIdCCsK1hS0bQaT8QjWoV11vZLxECMg%3D%3D";

        StringBuilder urlBuilder = new StringBuilder("https://apis.data.go.kr/B553077/api/open/sdsc2/storeListInDong");
        urlBuilder.append("?serviceKey=").append(encodedKey);
        urlBuilder.append("&divId=").append(divId);
        urlBuilder.append("&key=").append(key);
        urlBuilder.append("&numOfRows=50&type=json");

        if (indsLclsCd != null && !indsLclsCd.isEmpty()) urlBuilder.append("&indsLclsCd=").append(indsLclsCd);
        if (indsMclsCd != null && !indsMclsCd.isEmpty()) urlBuilder.append("&indsMclsCd=").append(indsMclsCd);
        if (indsSclsCd != null && !indsSclsCd.isEmpty()) urlBuilder.append("&indsSclsCd=").append(indsSclsCd);

        URL url = new URL(urlBuilder.toString());
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Content-type", "application/json");

        BufferedReader rd;
        if (conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
            rd = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
        } else {
            rd = new BufferedReader(new InputStreamReader(conn.getErrorStream(), "UTF-8"));
        }

        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = rd.readLine()) != null) sb.append(line);
        rd.close();
        conn.disconnect();

        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(sb.toString());
        JsonNode itemsNode = root.path("body").path("items");

        List<StoreVO> storeList = new ArrayList<>();
        if (itemsNode.isArray()) {
            for (JsonNode item : itemsNode) {
                StoreVO store = new StoreVO();

                store.setBizesNm(item.has("bizesNm") ? item.get("bizesNm").asText() : null);
                store.setRdnmAdr(item.has("rdnmAdr") ? item.get("rdnmAdr").asText() : null);
                store.setIndsLclsNm(item.hasNonNull("indsLclsNm") ? item.get("indsLclsNm").asText() : "");
                store.setIndsMclsNm(item.hasNonNull("indsMclsNm") ? item.get("indsMclsNm").asText() : "");
                store.setIndsSclsNm(item.hasNonNull("indsSclsNm") ? item.get("indsSclsNm").asText() : "");
                store.setLatitude(item.has("lat") ? item.get("lat").asText() : null);
                store.setLongitude(item.has("lon") ? item.get("lon").asText() : null);

                if (keyword == null || keyword.isEmpty() || (store.getBizesNm() != null && store.getBizesNm().contains(keyword))) {
                    storeList.add(store);
                }
                
            }
        }
        return storeList;
    }

    
    // 중분류 조회
    @GetMapping(value="/getMiddleCategory.do", produces="application/json; charset=UTF-8")
    @ResponseBody
    public List<String[]> getMiddleCategory(@RequestParam("indsLclsCd") String indsLclsCd){
        System.out.println("middle category 호출됨: " + indsLclsCd);
        return middleCategoryMap.getOrDefault(indsLclsCd, new ArrayList<>());
    }

}
