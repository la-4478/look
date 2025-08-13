package com.lookmarket.event.vo;

import java.sql.Date;
import java.sql.Timestamp;

public class EventPostVO {
    private int postId;
    private String promoTitle;
    private String promoContent;
    private String promoBannerImg;
    private Date promoStartDate;
    private Date promoEndDate;
    private Timestamp promoCreatedAt;
    private int promoStatus;
	public int getPostId() {
		return postId;
	}
	public void setPostId(int postId) {
		this.postId = postId;
	}
	public String getPromoTitle() {
		return promoTitle;
	}
	public void setPromoTitle(String promoTitle) {
		this.promoTitle = promoTitle;
	}
	public String getPromoContent() {
		return promoContent;
	}
	public void setPromoContent(String promoContent) {
		this.promoContent = promoContent;
	}
	public String getPromoBannerImg() {
		return promoBannerImg;
	}
	public void setPromoBannerImg(String promoBannerImg) {
		this.promoBannerImg = promoBannerImg;
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
	public Timestamp getPromoCreatedAt() {
		return promoCreatedAt;
	}
	public void setPromoCreatedAt(Timestamp promoCreatedAt) {
		this.promoCreatedAt = promoCreatedAt;
	}
	public int getPromoStatus() {
		return promoStatus;
	}
	public void setPromoStatus(int promoStatus) {
		this.promoStatus = promoStatus;
	}
}
