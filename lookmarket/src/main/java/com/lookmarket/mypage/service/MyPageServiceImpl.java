package com.lookmarket.mypage.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lookmarket.community.vo.ReviewVO;
import com.lookmarket.mypage.dao.MyPageDAO;
import com.lookmarket.mypage.vo.MyPageVO;

@Service("myPageService")
public class MyPageServiceImpl implements MyPageService{
	@Autowired
	private MyPageDAO myPageDAO;
	
	@Override
	public MyPageVO getMyPageInfo(String current_id) throws Exception{
		return myPageDAO.getMyPageInfo(current_id);
	}
	
	@Override
	public int updateMyInfo(MyPageVO myPageVO) throws Exception{
		return myPageDAO.updateMyInfo(myPageVO);
	}
	
	@Override
	public void deleteMember(String m_id) throws Exception {
	    myPageDAO.deleteMember(m_id);
	}
	@Override
	public List<ReviewVO> selectMyCommunityList(String m_id) throws Exception{
		return myPageDAO.selectMyCommunityList(m_id);
	}
}
