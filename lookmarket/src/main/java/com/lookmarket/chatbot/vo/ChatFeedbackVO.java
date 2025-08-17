package com.lookmarket.chatbot.vo;

import java.sql.Timestamp;
import lombok.Data;

@Data
public class ChatFeedbackVO {
	  private Long feedbackId;
	  private Long messageId;
	  private byte rating;
	  private String note;
	  private Timestamp createdAt;
	  
	  
	public Long getFeedbackId() {
		return feedbackId;
	}
	public void setFeedbackId(Long feedbackId) {
		this.feedbackId = feedbackId;
	}
	public Long getMessageId() {
		return messageId;
	}
	public void setMessageId(Long messageId) {
		this.messageId = messageId;
	}
	public byte getRating() {
		return rating;
	}
	public void setRating(byte rating) {
		this.rating = rating;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	public Timestamp getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(Timestamp createdAt) {
		this.createdAt = createdAt;
	}

}