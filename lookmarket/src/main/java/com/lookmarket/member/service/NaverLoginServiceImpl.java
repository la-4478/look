package com.lookmarket.member.service;

import java.security.SecureRandom;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lookmarket.member.dao.MemberDAO;
import com.lookmarket.member.vo.MemberVO;

@Service("naverLoginService")
public class NaverLoginServiceImpl implements NaverLoginService{
	@Autowired
	private MemberDAO memberDAO;
	
	private final String NAVER_CLIENT_ID = "0Kh9BAPo35nGcE_1Jbvx";
	private final String NAVER_CLIENT_SECRET = "LuaIR_wl_C";
	private final String NAVER_REDIRECT_URI = "http://localhost:8090/lookmarket/member/naverCallback.do";
	
	@Override
	public MemberVO getNaverUserInfo(String code, String state) throws Exception{
		String accessToken = getAccessToken(code, state);
		
		JsonNode profileJson = getUserProfile(accessToken);
		
		MemberVO memberVO = processNaverUser(profileJson);
		
		return memberVO;
	}
	
    private String getAccessToken(String code, String state) throws Exception {
        // HTTP Header 생성
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        // HTTP Body 생성
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", NAVER_CLIENT_ID);
        params.add("client_secret", NAVER_CLIENT_SECRET);
        params.add("redirect_uri", NAVER_REDIRECT_URI);
        params.add("code", code);
        params.add("state", state);

        // HTTP 요청 보내기
        HttpEntity<MultiValueMap<String, String>> naverTokenRequest = new HttpEntity<>(params, headers);
        RestTemplate rt = new RestTemplate();
        ResponseEntity<String> response = rt.exchange(
            "https://nid.naver.com/oauth2.0/token",
            HttpMethod.POST,
            naverTokenRequest,
            String.class
        );

        // JSON 응답에서 Access Token 파싱하기
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(response.getBody());
        return jsonNode.get("access_token").asText();
    }

    private JsonNode getUserProfile(String accessToken) throws Exception {
        // HTTP Header 생성
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        // HTTP 요청 보내기
        HttpEntity<MultiValueMap<String, String>> naverProfileRequest = new HttpEntity<>(headers);
        RestTemplate rt = new RestTemplate();
        ResponseEntity<String> response = rt.exchange(
            "https://openapi.naver.com/v1/nid/me",
            HttpMethod.POST,
            naverProfileRequest,
            String.class
        );

        // JSON 응답에서 사용자 정보 파싱하기
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(response.getBody());
        return jsonNode.get("response");
    }

    private MemberVO processNaverUser(JsonNode profileJson) throws Exception {
        String email = profileJson.get("email").asText();
        
        MemberVO existingMember = memberDAO.selectMemberByEmail(email);
        
        if (existingMember!= null) {        	
            return existingMember;
        } else {
            //신규 회원이면, 자동 회원가입 처리
            MemberVO newMember = new MemberVO();
            newMember.setM_email(email);
            newMember.setM_name(profileJson.get("name").asText());
            
            //이메일 앞부분을 아이디로 사용
            String emailPrefix = email.substring(0, email.indexOf("@"));
            String naverId = "naver_" + emailPrefix;
            newMember.setM_id(naverId);
            newMember.setM_pw(generateSecureRandomPassword(8));
            
            String gender = profileJson.has("gender") ? profileJson.get("gender").asText() : "";
            int mappedGender = 0; // 기본값

            if ("M".equalsIgnoreCase(gender)) {
                mappedGender = 1;
            } else if ("F".equalsIgnoreCase(gender)) {
                mappedGender = 2;
            }

            newMember.setM_gender(mappedGender);

            String birthYear = profileJson.has("birthyear") ? profileJson.get("birthyear").asText() : "";
            String birthday = profileJson.has("birthday") ? profileJson.get("birthday").asText() : "";

            String fullBirth = "";
            if (!birthYear.isEmpty() && !birthday.isEmpty()) {
                fullBirth = birthYear + "-" + birthday;
            }

            newMember.setM_birth(fullBirth);
            
            String phone = profileJson.has("mobile") ? profileJson.get("mobile").asText() : "";
            if (!phone.isEmpty()) {
                phone = phone.replaceAll("[^0-9]", "");
            } else {
                phone = "01000000000";
            }
            newMember.setM_phone(phone);
            
            memberDAO.insertNewMember(newMember);
            return newMember;
        }
    }
    
    public static String generateSecureRandomPassword(int length) {
        if (length < 8) {
            throw new IllegalArgumentException("비밀번호는 최소 8자 이상이어야 합니다.");
        }

        final String UPPER = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        final String LOWER = "abcdefghijklmnopqrstuvwxyz";
        final String DIGITS = "0123456789";
        final String SPECIAL = "!@#$%^&*()-_=+[]{}|;:,.<>?";
        final String ALL = UPPER + LOWER + DIGITS + SPECIAL;

        SecureRandom random = new SecureRandom();
        StringBuilder password = new StringBuilder();

        // 각 문자 유형에서 하나씩 보장
        password.append(UPPER.charAt(random.nextInt(UPPER.length())));
        password.append(LOWER.charAt(random.nextInt(LOWER.length())));
        password.append(DIGITS.charAt(random.nextInt(DIGITS.length())));
        password.append(SPECIAL.charAt(random.nextInt(SPECIAL.length())));

        // 나머지 자릿수는 랜덤하게 채움
        for (int i = 4; i < length; i++) {
            password.append(ALL.charAt(random.nextInt(ALL.length())));
        }

        return shuffleString(password.toString(), random);
    }

    // 문자열 셔플 메소드
    private static String shuffleString(String input, SecureRandom random) {
        char[] chars = input.toCharArray();
        for (int i = chars.length - 1; i > 0; i--) {
            int j = random.nextInt(i + 1);
            // swap
            char temp = chars[i];
            chars[i] = chars[j];
            chars[j] = temp;
        }
        return new String(chars);
    }
}
