package com.lookmarket.member.vo;

import java.util.List;

public class MemberApprovalDTO {
    private MemberVO member;
    private List<BusinessVO> pendingList;
    
    
	public MemberVO getMember() {
		return member;
	}
	public void setMember(MemberVO member) {
		this.member = member;
	}
	public List<BusinessVO> getPendingList() {
		return pendingList;
	}
	public void setPendingList(List<BusinessVO> pendingList) {
		this.pendingList = pendingList;
	}
}
    
