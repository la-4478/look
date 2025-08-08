package com.lookmarket.community.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lookmarket.community.dao.CommunityDAO;
import com.lookmarket.community.vo.BlackBoardVO;
import com.lookmarket.community.vo.ReviewVO;

@Service("communityService")
public class CommunityServiceImpl implements CommunityService{
	@Autowired
	private CommunityDAO communityDAO;
	
	@Override
	public List<BlackBoardVO> blackBoardList() throws Exception{
		return communityDAO.blackBoardList();
	}
	
	@Override
	public List<ReviewVO> communityList() throws Exception{
		return communityDAO.communityList();
	}
	
	@Override
	public ReviewVO communityDetail(String r_id) throws Exception{
		return communityDAO.communityDetail(Integer.parseInt(r_id));
	}
	
	@Override
	public void upHit(String r_id, String hit) throws Exception{
		communityDAO.upHit(Integer.parseInt(r_id), Integer.parseInt(hit));
	}
	@Override
	public void insertReview(ReviewVO reviewVO) throws Exception{
		communityDAO.insertReview(reviewVO);
	}
}
