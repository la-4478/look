package com.lookmarket.chatbot.vo;

import java.sql.Timestamp;
import lombok.Data;

@Data
public class AppUserVO {
	  private Long userId;
	  private String username;
	  private String displayName;
	  private Timestamp createdAt;
  
  
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getDisplayName() {
		return displayName;
	}
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
	public Timestamp getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(Timestamp createdAt) {
		this.createdAt = createdAt;
	}
	  
}