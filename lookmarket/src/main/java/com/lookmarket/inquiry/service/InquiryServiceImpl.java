package com.lookmarket.inquiry.service;

import java.nio.file.AccessDeniedException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lookmarket.community.dao.CommunityDAO;
import com.lookmarket.inquiry.dao.InquiryDAO;
import com.lookmarket.inquiry.vo.CommentVO;
import com.lookmarket.inquiry.vo.InquiryVO;
import com.lookmarket.notify.dao.NotifyDAO;
import com.lookmarket.notify.vo.NotifyVO;

@Service("inquiryService")
public class InquiryServiceImpl implements InquiryService {
	@Autowired
	private InquiryDAO inquirydao; // MyBatis 매퍼 인터페이스
	@Autowired
	private CommunityDAO communityDAO;
	@Autowired
	private NotifyDAO notifyDAO;

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
	        return list;
	    }
	    throw new AccessDeniedException("허용되지 않은 역할: " + role);
	}

	@Override
	public InquiryVO getInquiryDetail(int inquiryId) throws Exception {
		return inquirydao.getInquiryDetail(inquiryId);
	}
	
	

	
	@Override
	public void answerInquiry(long id, String adminId, int loginRole, String answer) throws Exception {
        if (loginRole != 3) throw new AccessDeniedException("관리자 전용");
        InquiryVO vo = new InquiryVO();
        vo.setInquiryId(id);
        vo.setAnsweredBy(adminId);
        vo.setAnswer(answer);
        inquirydao.updateAnswer(vo);
	}

	@Override
	public int getInquiryNum(int inquiryId) throws Exception {
		return inquirydao.getInquiryNum(inquiryId);
	}

	@Transactional
	@Override
	public int insertComment(CommentVO vo, String sessionMemberId) throws Exception {
	    // 0) 신뢰원 천거: 작성자 ID는 세션으로 강제
	    if (sessionMemberId == null || sessionMemberId.isBlank()) {
	        throw new IllegalStateException("로그인 필요");
	    }
	    if (vo.getB_id() == null) {
	        throw new IllegalArgumentException("b_id 누락");
	    }
	    vo.setC_m_id(sessionMemberId);

	    // 1) 댓글 저장
	    int r = inquirydao.insertComment(vo); // useGeneratedKeys로 c_id 채움 가정
	    if (r != 1) {
	        throw new IllegalStateException("댓글 저장 실패");
	    }

	    // 2) 글쓴이 알림
	    String postOwnerId = communityDAO.findWriterByBoardId(vo.getB_id());
	    if (isValidUser(postOwnerId) && !postOwnerId.equals(sessionMemberId)) {
	        NotifyVO n = NotifyVO.commentOnMyPost(
	            postOwnerId,
	            Integer.parseInt(vo.getB_id()),
	            "내 글에 새 댓글",
	            sanitizeSnippet(vo.getC_content(), 60),
	            "/business/blackBoardDetail.do?b_id=" + vo.getB_id()
	        );
	        int nr = notifyDAO.insert(n);
	        if (nr != 1) throw new IllegalStateException("알림 저장 실패(COMMENT)");
	        // notifyPublisher.publish(n); // SSE/WebSocket 쓰면 여기에 신호 (선택)
	    }

	    // 3) 대댓글 알림
	    if (vo.getC_id() != null) {
	        String parentWriter = communityDAO.findWriterByCommentId(vo.getC_id());
	        if (isValidUser(parentWriter)
	            && !parentWriter.equals(sessionMemberId)
	            && !parentWriter.equals(postOwnerId)) {

	            NotifyVO n2 = NotifyVO.replyToMyComment(
	                parentWriter,
	                Integer.parseInt(vo.getC_id()),
	                "내 댓글에 답글",
	                sanitizeSnippet(vo.getC_content(), 60),
	                "/community/detail.do?b_id=" + vo.getB_id() + "#c" + vo.getC_id()
	            );
	            int nr2 = notifyDAO.insert(n2);
	            if (nr2 != 1) throw new IllegalStateException("알림 저장 실패(REPLY)");
	            // notifyPublisher.publish(n2); // (선택)
	        }
	    }

	    return r;
	}

	private boolean isValidUser(String id) {
	    return id != null && !id.isBlank();
	}

	private String sanitizeSnippet(String s, int maxLen) {
	    if (s == null) return "";
	    String one = s.replaceAll("\\s+", " ").trim();
	    String cut = one.length() <= maxLen ? one : one.substring(0, maxLen) + "…";
	    // 필요시 HtmlUtils.htmlEscape(cut) 도입
	    return cut;
	}


	@Override
	public List<CommentVO> getcomment(String b_id) throws Exception {
		int i_b_id = Integer.parseInt(b_id);
		
		return inquirydao.getcomment(i_b_id);
	}

}
