package com.lookmarket.account.vo;

import org.springframework.stereotype.Component;

@Component
public class AccCategoryVO {
    private Long categoryId;  // BIGINT PK
    private String kind;      // 'INCOME','EXPENSE','TRANSFER'
    private String name;      // 카테고리명
    private Integer isActive; // 0/1
	
    
    public Long getCategoryId() {
		return categoryId;
	}
	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}
	public String getKind() {
		return kind;
	}
	public void setKind(String kind) {
		this.kind = kind;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getIsActive() {
		return isActive;
	}
	public void setIsActive(Integer isActive) {
		this.isActive = isActive;
	}

}
