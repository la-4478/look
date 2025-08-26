package com.lookmarket.community.Service;

import java.util.List;

import org.springframework.web.servlet.ModelAndView;

import com.lookmarket.community.vo.BlackBoardVO;
import com.lookmarket.community.vo.ReviewVO;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface CommunityService {
	public List<BlackBoardVO> blackBoardList() throws Exception;
	public List<ReviewVO> communityList() throws Exception;
	public ReviewVO communityDetail(String r_id) throws Exception;
	public void upHit(int r_id, int hit) throws Exception;
	public void insertReview(ReviewVO reviewVO) throws Exception;
	public void updateReview(ReviewVO reviewVO) throws Exception;
	public void deleteReview(String r_id) throws Exception;
	public void insertBlackBoard(BlackBoardVO blackBoardVO) throws Exception;
	public BlackBoardVO blackBoardDetail(String b_id) throws Exception;
	public void updateBlackBoard(BlackBoardVO blackBoardVO) throws Exception;
	public void upBlackHit(String b_id) throws Exception;
	public void deleteBlackBoard(int b_id) throws Exception;
	public List<BlackBoardVO> myBlackBoard(String m_id) throws Exception;
	public List<BlackBoardVO> allboardList() throws Exception;

}
