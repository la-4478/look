package com.lookmarket.mypage.dao;

import java.util.List;

import org.springframework.dao.DataAccessException;

import com.lookmarket.community.vo.ReviewVO;
import com.lookmarket.mypage.vo.MyPageVO;

public interface MyPageDAO {
	public MyPageVO getMyPageInfo(String current_id) throws DataAccessException;
	public int updateMyInfo(MyPageVO myPageVO) throws DataAccessException;
	int deleteMember(String m_id) throws Exception;
	List<ReviewVO> selectMyCommunityList(String m_id) throws Exception;
}
