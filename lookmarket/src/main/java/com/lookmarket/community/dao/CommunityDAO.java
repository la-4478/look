package com.lookmarket.community.dao;

import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.web.servlet.ModelAndView;

import com.lookmarket.community.vo.BlackBoardVO;
import com.lookmarket.community.vo.ReviewVO;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface CommunityDAO {
	public List<BlackBoardVO> blackBoardList() throws DataAccessException;
	public List<ReviewVO> communityList() throws DataAccessException;
	public ReviewVO communityDetail(int r_id) throws DataAccessException;
	public void upHit(int r_id, int hit) throws DataAccessException;
}
