package com.lookmarket.order.vo;

import org.springframework.stereotype.Component;

@Component
public class CouponVO {
	  private Long id;
	  private String name;
	  private String type;      // RATE | AMOUNT
	  private Integer value;    // %
	  private Integer maxDiscount;
	  private Integer minOrder;
	  private String expireAt;  // ISO 문자열
	  private String description;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public Integer getValue() {
		return value;
	}
	public void setValue(Integer value) {
		this.value = value;
	}
	public Integer getMaxDiscount() {
		return maxDiscount;
	}
	public void setMaxDiscount(Integer maxDiscount) {
		this.maxDiscount = maxDiscount;
	}
	public Integer getMinOrder() {
		return minOrder;
	}
	public void setMinOrder(Integer minOrder) {
		this.minOrder = minOrder;
	}
	public String getExpireAt() {
		return expireAt;
	}
	public void setExpireAt(String expireAt) {
		this.expireAt = expireAt;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	  
}
