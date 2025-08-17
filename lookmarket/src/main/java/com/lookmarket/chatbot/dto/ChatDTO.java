package com.lookmarket.chatbot.dto;

/**
 * ✨ Chatbot JSON 요청/응답을 운반하는 단순 DTO 모음.
 * - ChatRequest : 클라이언트 → 서버 로 들어오는 JSON { "message": "..." }
 * - ChatResponse: 서버 → 클라이언트 로 나가는 JSON { "content": "..." }
 * 컨트롤러가 @RequestBody / @ResponseBody 와 함께 사용한다.
 */
public class ChatDTO {
    // 클라이언트가 보내는 페이로드. {"message":"..."} 형태만 받으면 충분
    public static class ChatRequest {
        // 필드명은 JSON 키와 매칭되므로 "message"로 유지할 것
        private String message;
        public String getMessage() { return message; }
        public void setMessage(String message) { this.message = message; }
    }

    // 서버가 돌려줄 응답 페이로드. {"content":"..."} 만으로 프론트에서 출력 가능
    public static class ChatResponse {
        private String content;
        public ChatResponse(String content) { this.content = content; }
        public String getContent() { return content; }
    }
}
