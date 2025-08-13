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
	public void upHit(int r_id, int hit) throws Exception{
		communityDAO.upHit(r_id, hit);
	}
	@Override
	public void insertReview(ReviewVO reviewVO) throws Exception {
	    communityDAO.insertReview(reviewVO);
	}

	@Override
	public void updateReview(ReviewVO reviewVO) throws Exception {
	    communityDAO.updateReview(reviewVO);
	}
	@Override
	public void deleteReview(String r_id) throws Exception {
	    communityDAO.deleteReview(Integer.parseInt(r_id));
	}
	@Override
	public void insertBlackBoard(BlackBoardVO blackBoardVO) throws Exception {
	    communityDAO.insertBlackBoard(blackBoardVO);
	}
	@Override
	public BlackBoardVO blackBoardDetail(String b_id) throws Exception {
	    return communityDAO.blackBoardDetail(Integer.parseInt(b_id));
	}

	@Override
	public void updateBlackBoard(BlackBoardVO blackBoardVO) throws Exception {
	    communityDAO.updateBlackBoard(blackBoardVO);
	}
	@Override
	public void upBlackHit(String b_id) throws Exception {
	    communityDAO.upBlackHit(b_id);
	}
	@Override
	public void deleteBlackBoard(int b_id) throws Exception {
	    communityDAO.deleteBlackBoard(b_id);
	}
}
