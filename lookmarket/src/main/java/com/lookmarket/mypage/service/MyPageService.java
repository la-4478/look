package com.lookmarket.mypage.service;

import java.util.List;

import com.lookmarket.community.vo.ReviewVO;
import com.lookmarket.mypage.vo.MyPageVO;

public interface MyPageService {
	public MyPageVO getMyPageInfo(String current_id) throws Exception;
	public int updateMyInfo(MyPageVO myPageVO) throws Exception;
	void deleteMember(String m_id) throws Exception;
	public List<ReviewVO> selectMyCommunityList(String m_id) throws Exception;
}
