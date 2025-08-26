package com.lookmarket.inquiry.dao;

import java.util.List;

import org.springframework.dao.DataAccessException;

import com.lookmarket.inquiry.vo.CommentVO;
import com.lookmarket.inquiry.vo.InquiryVO;

public interface InquiryDAO {
	public List<InquiryVO> selectInquiryById(String loginId) throws DataAccessException;
	public void insertInquiry(InquiryVO vo) throws DataAccessException;
	public void updateAnswer(InquiryVO vo) throws DataAccessException;
	public List<InquiryVO> selectInquiriesForAdmin() throws DataAccessException;
	public InquiryVO getInquiryDetail(int inquiryId)throws DataAccessException;
	public int getInquiryNum(int inquiryId) throws DataAccessException;
	public int insertComment(CommentVO vo) throws DataAccessException;
	public List<CommentVO> getcomment(int i_b_id) throws DataAccessException;
}