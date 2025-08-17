package com.lookmarket.chatbot.vo;

import java.sql.Timestamp;
import lombok.Data;

@Data
public class ChatSessionVO {
	  private Long sessionId;
	  private Long userId;
	  private String title;
	  private Timestamp createdAt;
	  private Timestamp updatedAt;
  
	public Long getSessionId() {
		return sessionId;
	}
	public void setSessionId(Long sessionId) {
		this.sessionId = sessionId;
	}
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public Timestamp getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(Timestamp createdAt) {
		this.createdAt = createdAt;
	}
	public Timestamp getUpdatedAt() {
		return updatedAt;
	}
	public void setUpdatedAt(Timestamp updatedAt) {
		this.updatedAt = updatedAt;
	}
  
  
}
