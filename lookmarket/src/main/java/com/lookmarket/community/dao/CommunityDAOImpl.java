package com.lookmarket.community.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;
import org.springframework.web.servlet.ModelAndView;

import com.lookmarket.community.vo.BlackBoardVO;
import com.lookmarket.community.vo.ReviewVO;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Repository("community")
public class CommunityDAOImpl implements CommunityDAO{
	@Autowired
	private SqlSession sqlSession;
	@Autowired
	private ReviewVO reviewVO;
	
	@Override
	public List<BlackBoardVO> blackBoardList() throws DataAccessException{
		return (ArrayList)sqlSession.selectList("mapper.community.blackBoardList");
	}
	
	@Override
	public List<ReviewVO> communityList() throws DataAccessException{
		return (ArrayList)sqlSession.selectList("mapper.community.communityList");
	}
	
	@Override
	public ReviewVO communityDetail(int r_id) throws DataAccessException{
		return sqlSession.selectOne("mapper.community.communityDetail", r_id);
	}
	
	@Override
	public void upHit(int r_id, int hit) throws DataAccessException{
		reviewVO.setR_id(r_id);
		reviewVO.setR_hit(hit);
		
		sqlSession.update("mapper.community.upHit", reviewVO);
	}
}
