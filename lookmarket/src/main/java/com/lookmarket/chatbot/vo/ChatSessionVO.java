package com.lookmarket.chatbot.vo;

import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

@Component("chatSessionVO")
public class ChatSessionVO {
	  private Long sessionId;
	  private Long userId;
	  private String title;
	  private LocalDateTime createdAt;
	  private LocalDateTime updatedAt;
	  
	  
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
	public LocalDateTime getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}
	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}
	public void setUpdatedAt(LocalDateTime updatedAt) {
		this.updatedAt = updatedAt;
	}
	  
	  
}
