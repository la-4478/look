package com.lookmarket.event.vo;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

public class CouponVO {
	private Integer promoId;
	private String promoCode;
	private Integer promoDiscountType;
	private Double promoDiscountValue;
	private Integer promoMaxDiscount;
	private Integer promoMinPurchase;
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date promoStartDate;
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date promoEndDate;
	private boolean promoCouponActive;
	private String mId; // nullable
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date promoUsedDate;
	private Integer postId;
	public Integer getPromoId() {
		return promoId;
	}
	public void setPromoId(Integer promoId) {
		this.promoId = promoId;
	}
	public String getPromoCode() {
		return promoCode;
	}
	public void setPromoCode(String promoCode) {
		this.promoCode = promoCode;
	}
	public Integer getPromoDiscountType() {
		return promoDiscountType;
	}
	public void setPromoDiscountType(Integer promoDiscountType) {
		this.promoDiscountType = promoDiscountType;
	}
	public Double getPromoDiscountValue() {
		return promoDiscountValue;
	}
	public void setPromoDiscountValue(Double promoDiscountValue) {
		this.promoDiscountValue = promoDiscountValue;
	}
	public Integer getPromoMaxDiscount() {
		return promoMaxDiscount;
	}
	public void setPromoMaxDiscount(Integer promoMaxDiscount) {
		this.promoMaxDiscount = promoMaxDiscount;
	}
	public Integer getPromoMinPurchase() {
		return promoMinPurchase;
	}
	public void setPromoMinPurchase(Integer promoMinPurchase) {
		this.promoMinPurchase = promoMinPurchase;
	}
	public Date getPromoStartDate() {
		return promoStartDate;
	}
	public void setPromoStartDate(Date promoStartDate) {
		this.promoStartDate = promoStartDate;
	}
	public Date getPromoEndDate() {
		return promoEndDate;
	}
	public void setPromoEndDate(Date promoEndDate) {
		this.promoEndDate = promoEndDate;
	}
    public Boolean getPromoCouponActive() {
        return promoCouponActive;
    }
    public void setPromoCouponActive(Boolean promoCouponActive) {
        this.promoCouponActive = promoCouponActive;
    }
	public String getMId() {
		return mId;
	}
	public void setMId(String mId) {
		this.mId = mId;
	}
	public Date getPromoUsedDate() {
		return promoUsedDate;
	}
	public void setPromoUsedDate(Date promoUsedDate) {
		this.promoUsedDate = promoUsedDate;
	}
	public Integer getPostId() {
		return postId;
	}
	public void setPostId(Integer postId) {
		this.postId = postId;
	}
}
