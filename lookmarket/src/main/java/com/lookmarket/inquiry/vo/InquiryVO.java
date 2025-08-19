package com.lookmarket.inquiry.vo;

import java.util.Date;

import org.springframework.stereotype.Component;

@Component
public class InquiryVO {
    /** PK */
    private Long inquiryId;
    /** 작성자(member.m_id) */
    private String mId;
    /** 제목 */
    private String title;
    /** 질문 본문 */
    private String question;
    /** 답변(단답형) */
    private String answer;
    /** 상태: OPEN / ANSWERED / CLOSED */
    private String status;
    /** 답변자(member.m_id, 관리자) */
    private String answeredBy;
    /** 생성 시각 */
    private Date createdAt;
    /** 답변 시각 */
    private Date answeredAt;
    /** 업데이트 시각 */
    private Date updatedAt;
    /** 삭제 시각(소프트 삭제) */
    private Date deletedAt;
    
	public Long getInquiryId() {
		return inquiryId;
	}
	public void setInquiryId(Long inquiryId) {
		this.inquiryId = inquiryId;
	}
	public String getmId() {
		return mId;
	}
	public void setmId(String mId) {
		this.mId = mId;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getQuestion() {
		return question;
	}
	public void setQuestion(String question) {
		this.question = question;
	}
	public String getAnswer() {
		return answer;
	}
	public void setAnswer(String answer) {
		this.answer = answer;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getAnsweredBy() {
		return answeredBy;
	}
	public void setAnsweredBy(String answeredBy) {
		this.answeredBy = answeredBy;
	}
	public Date getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}
	public Date getAnsweredAt() {
		return answeredAt;
	}
	public void setAnsweredAt(Date answeredAt) {
		this.answeredAt = answeredAt;
	}
	public Date getUpdatedAt() {
		return updatedAt;
	}
	public void setUpdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt;
	}
	public Date getDeletedAt() {
		return deletedAt;
	}
	public void setDeletedAt(Date deletedAt) {
		this.deletedAt = deletedAt;
	}
}
