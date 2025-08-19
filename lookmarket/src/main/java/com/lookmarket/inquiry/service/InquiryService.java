package com.lookmarket.inquiry.service;

import java.util.List;

import com.lookmarket.inquiry.vo.InquiryVO;

public interface InquiryService {
	// 문의 작성(ROLE: 1,2)
	public long createInquiry(String loginId, int loginRole, InquiryVO vo) throws Exception;
	// 단건 조회(본인 or 관리자)
    public List<InquiryVO> getInquiry(String loginId, int role) throws Exception;
//    // 답변 등록(관리자만)
//    public void answerInquiry(long id, String adminId, int loginRole, String answer) throws Exception;
    
    public InquiryVO getInquiryDetail(int inquiryId) throws Exception;
    
}
