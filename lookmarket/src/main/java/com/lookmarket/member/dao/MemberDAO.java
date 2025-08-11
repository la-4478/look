package com.lookmarket.member.dao;

import org.springframework.dao.DataAccessException;

import com.lookmarket.member.vo.BusinessVO;
import com.lookmarket.member.vo.MemberVO;

public interface MemberDAO {
	public MemberVO login(String m_id, String m_pw) throws DataAccessException;
	public MemberVO selectMemberByEmail(String email) throws DataAccessException;
	public String selectOverlappedID(String m_id) throws DataAccessException;
	public String overlappedByEmail(String m_email) throws DataAccessException;
	public void insertNewMember(MemberVO memberVO) throws DataAccessException;
	public String findId(String m_name, String m_email) throws DataAccessException;
	public String findPw(String m_id, String m_name) throws DataAccessException;
	public void reSignUp(String r_id) throws DataAccessException;
	public Integer selectRoleById(String mId)throws DataAccessException;
	public void addbusinessMember(BusinessVO businessVO)throws DataAccessException;
}
