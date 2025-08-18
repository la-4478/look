package com.lookmarket.chatbot.dto;

public class ChatResponse {
	 private String content;    // 봇의 답변(또는 에러 메시지)
     private String sessionId;  // 세션 추적용
     private String error;      // null이면 정상

     public ChatResponse() {}
     public ChatResponse(String content) { this.content = content; }
     public ChatResponse(String content, String sessionId, String error) {
         this.content = content; this.sessionId = sessionId; this.error = error;
     }
     // getters/setters
		public String getContent() {
			return content;
		}
		public void setContent(String content) {
			this.content = content;
		}
		public String getSessionId() {
			return sessionId;
		}
		public void setSessionId(String sessionId) {
			this.sessionId = sessionId;
		}
		public String getError() {
			return error;
		}
		public void setError(String error) {
			this.error = error;
		}
     
}
