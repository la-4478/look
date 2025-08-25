package com.lookmarket.member.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.lookmarket.member.dao.MemberDAO;
import com.lookmarket.member.vo.BusinessVO;
import com.lookmarket.member.vo.MemberVO;

@Service("memberService")
@Transactional(propagation = Propagation.REQUIRED)
public class MemberServiceImpl implements MemberService{
	@Autowired
	private MemberDAO memberDAO;
	
	@Override
	public String findId(String m_name, String m_email) throws Exception{
		return memberDAO.findId(m_name, m_email);
	}
	
	@Override
	public String findPw(String m_id, String m_name) throws Exception{
		return memberDAO.findPw(m_id, m_name);
	}
	
	@Override
	public MemberVO login(String m_id, String m_pw) throws Exception{
		return memberDAO.login(m_id, m_pw);
	}
	
	@Override
	public String overlapped(String m_id) throws Exception{
		return memberDAO.selectOverlappedID(m_id);
	}	
	
	@Override
	public String overlappedByEmail(String m_email) throws Exception{
		return memberDAO.overlappedByEmail(m_email);
	}	
	
	@Override
	public void addMember(MemberVO memberVO) throws Exception{
		memberDAO.insertNewMember(memberVO);
	}
	
	@Override
	public void reSignUp(String m_id) throws Exception{
		memberDAO.reSignUp(m_id);
	}

	@Override
	public Integer getRoleById(String mId) {
        return memberDAO.selectRoleById(mId);
	}

	@Override
	public void addbusinessMember(BusinessVO businessVO) {
		memberDAO.addbusinessMember(businessVO);
		
	}

	@Override
	public List<MemberVO> findbusinessMember(int role) throws Exception {
		return memberDAO.findbusinessMember(role);
	}

	@Override
	public List<BusinessVO> findbusinessMember2(String memberId) {
		return memberDAO.findbusinessMember2(memberId);
	}

	@Override
	public void approve(String m_id) throws Exception {
		memberDAO.approve(m_id);
	}

	@Override
	public void reject(String m_id) throws Exception {
		memberDAO.reject(m_id);
		
	}

	@Override
	public void revers(String m_id) throws Exception {
		memberDAO.revers(m_id);
		
	}

	@Override
	public String status(String m_id) throws Exception {
		return memberDAO.status(m_id);
	}

	@Override
	public MemberVO findMemberById(String m_id) throws Exception {
		return memberDAO.findMemberById(m_id);
	}

	@Override
	public BusinessVO findBusinessByMemberId(String m_id) throws Exception {
		return memberDAO.findBusinessByMemberId(m_id);
	}

	@Override
	public List<MemberVO> getMemberList() throws Exception {
		return memberDAO.getMemberList();
	}
}
