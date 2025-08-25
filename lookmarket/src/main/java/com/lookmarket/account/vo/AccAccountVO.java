package com.lookmarket.account.vo;

import org.springframework.stereotype.Component;

@Component
public class AccAccountVO {
	 private Long accountId;   // BIGINT PK
	 private String name;      // 계정명
	 private String kind;      // 'CASH','BANK','PG','ETC' (문자열로 받는 걸 추천)
	 private Integer isActive; // TINYINT(1) -> 0/1
	
	 
	 public Long getAccountId() {
		return accountId;
	}
	public void setAccountId(Long accountId) {
		this.accountId = accountId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getKind() {
		return kind;
	}
	public void setKind(String kind) {
		this.kind = kind;
	}
	public Integer getIsActive() {
		return isActive;
	}
	public void setIsActive(Integer isActive) {
		this.isActive = isActive;
	}
	 
}
