package com.lookmarket.inquiry.dao;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import com.lookmarket.inquiry.vo.CommentVO;
import com.lookmarket.inquiry.vo.InquiryVO;

@Repository
public class InquiryDAOImpl implements InquiryDAO{
	
	private static final String NS = "mapper.Inquiry.";
	@Autowired
	private SqlSession sqlSession;

	@Override
	public List<InquiryVO> selectInquiryById(String loginId) throws DataAccessException {
		return sqlSession.selectList(NS + "selectMyInquiries", loginId);
	}

	@Override
	public void insertInquiry(InquiryVO vo) throws DataAccessException {
        sqlSession.insert(NS + "insertInquiry", vo);
	}

	@Override
	public List<InquiryVO> selectInquiriesForAdmin() throws DataAccessException {
		return sqlSession.selectList(NS + "selectInquiriesForAdmin");
		
	}

	@Override
	public InquiryVO getInquiryDetail(int inquiryId) {
		return sqlSession.selectOne(NS + "selectInquiryById", inquiryId);
	}
	

	@Override
	public void updateAnswer(InquiryVO vo) throws DataAccessException {
        sqlSession.update(NS + "updateAnswer", vo);
	}

	@Override
	public int getInquiryNum(int inquiryId) throws DataAccessException {
		return sqlSession.selectOne(NS + "inquirynumber", inquiryId);
	}

	@Override
	public int insertComment(CommentVO vo) throws DataAccessException {
		return sqlSession.insert(NS + "insertcomment", vo);
	}

	@Override
	public List<CommentVO> getcomment(int i_b_id) throws DataAccessException {
		return sqlSession.selectList(NS + "getcomment", i_b_id);
	}

}
