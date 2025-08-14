package com.lookmarket.chatbot.vo;

import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

@Component("ChatMessageVO")
public class ChatMessageVO {
  private Long messageId;
  private Long sessionId;
  private String role;      // "system" | "user" | "assistant"
  private String content;
  private String metaJson;  // optional
  private LocalDateTime createdAt;
	  
	  
	public Long getMessageId() {
		return messageId;
	}
	public void setMessageId(Long messageId) {
		this.messageId = messageId;
	}
	public Long getSessionId() {
		return sessionId;
	}
	public void setSessionId(Long sessionId) {
		this.sessionId = sessionId;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getMetaJson() {
		return metaJson;
	}
	public void setMetaJson(String metaJson) {
		this.metaJson = metaJson;
	}
	public LocalDateTime getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}
	  
	  
	}