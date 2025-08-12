package com.lookmarket.member.service;

import java.util.List;

import com.lookmarket.member.vo.BusinessVO;
import com.lookmarket.member.vo.MemberVO;

public interface MemberService {
	public MemberVO login(String m_id, String m_pw) throws Exception;
	public String overlapped(String m_id) throws Exception;
	public String overlappedByEmail(String m_email) throws Exception;
	public void addMember(MemberVO memnberVO) throws Exception;
	public String findId(String m_name, String m_email) throws Exception;
	public String findPw(String m_id, String m_name) throws Exception;
	public void reSignUp(String m_id) throws Exception;
	public Integer getRoleById(String mId) throws Exception;
	public void addbusinessMember(BusinessVO businessVO) throws Exception;
	public List<MemberVO> findbusinessMember(int role) throws Exception;
	public List<BusinessVO> findbusinessMember2(String memberId) throws Exception;
	public void approve(String m_id) throws Exception;
	public void reject(String m_id) throws Exception;
	public void revers(String m_id) throws Exception;
	public String status(String m_id) throws Exception;
	public MemberVO findMemberById(String mId) throws Exception;
	public BusinessVO findBusinessByMemberId(String mId) throws Exception;
	
}
