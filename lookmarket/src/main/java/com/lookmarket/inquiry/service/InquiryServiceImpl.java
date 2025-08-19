package com.lookmarket.inquiry.service;

import java.nio.file.AccessDeniedException;
import java.util.List;

import org.apache.ibatis.javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lookmarket.inquiry.dao.InquiryDAO;
import com.lookmarket.inquiry.vo.InquiryVO;

@Service("inquiryService")
public class InquiryServiceImpl implements InquiryService {
	@Autowired
	private InquiryDAO inquirydao; // MyBatis 매퍼 인터페이스


	@Override
	public long createInquiry(String loginId, int loginRole, InquiryVO vo) throws Exception {
	       if (loginRole < 1 || loginRole > 2) throw new AccessDeniedException("문의 작성 권한 없음");
	        vo.setmId(loginId);
	        inquirydao.insertInquiry(vo);
	        return vo.getInquiryId();
	}

	@Override
	public List<InquiryVO> getInquiry(String loginId, int role) throws Exception {
	    if (role == 3) {
	        // 관리자: 전체
	        return inquirydao.selectInquiriesForAdmin(); // 네가 만든 관리자 전체 조회
	    }

	    if (role == 1) {
	        // 회원: 본인 것만
	        List<InquiryVO> list = inquirydao.selectInquiryById(loginId); // 메서드명/쿼리 변경 권장
	        // MyBatis selectList는 거의 null 아님. 안전하게 비어있음만 검사.
	        if (list == null || list.isEmpty()) {
	        	throw new NotFoundException(loginId);
	        }
	        return list;
	    }
	    throw new AccessDeniedException("허용되지 않은 역할: " + role);
	}

	@Override
	public InquiryVO getInquiryDetail(int inquiryId) throws Exception {
		return inquirydao.getInquiryDetail(inquiryId);
	}
	
	

	
//	@Override
//	public void answerInquiry(long id, String adminId, int loginRole, String answer) throws Exception {
//        if (loginRole != 3) throw new AccessDeniedException("관리자 전용");
//        InquiryVO vo = new InquiryVO();
//        vo.setInquiryId(id);
//        vo.setAnsweredBy(adminId);
//        vo.setAnswer(answer);
//        inquirydao.updateAnswer(vo);
//	}

}
