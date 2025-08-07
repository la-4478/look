package com.lookmarket.member.service;

import com.lookmarket.member.vo.MemberVO;

public interface NaverLoginService {
	public MemberVO getNaverUserInfo(String code,String state) throws Exception;
}
