package com.lookmarket.chatbot.vo;

import java.sql.Timestamp;
import java.math.BigDecimal;
import lombok.Data;

@Data
public class ChatCallLogVO {
	  private Long callId;
	  private Long sessionId;
	  private String model;
	  private int promptTokens;
	  private int completionTokens;
	  private int totalTokens;
	  private BigDecimal costUsd;
	  private String requestId;
	  private Timestamp createdAt;
	  
	  
	public Long getCallId() {
		return callId;
	}
	public void setCallId(Long callId) {
		this.callId = callId;
	}
	public Long getSessionId() {
		return sessionId;
	}
	public void setSessionId(Long sessionId) {
		this.sessionId = sessionId;
	}
	public String getModel() {
		return model;
	}
	public void setModel(String model) {
		this.model = model;
	}
	public int getPromptTokens() {
		return promptTokens;
	}
	public void setPromptTokens(int promptTokens) {
		this.promptTokens = promptTokens;
	}
	public int getCompletionTokens() {
		return completionTokens;
	}
	public void setCompletionTokens(int completionTokens) {
		this.completionTokens = completionTokens;
	}
	public int getTotalTokens() {
		return totalTokens;
	}
	public void setTotalTokens(int totalTokens) {
		this.totalTokens = totalTokens;
	}
	public BigDecimal getCostUsd() {
		return costUsd;
	}
	public void setCostUsd(BigDecimal costUsd) {
		this.costUsd = costUsd;
	}
	public String getRequestId() {
		return requestId;
	}
	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}
	public Timestamp getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(Timestamp createdAt) {
		this.createdAt = createdAt;
	}
}