package com.lookmarket.notify.vo;

import org.springframework.stereotype.Component;

@Component
public class NotifyVO {
    private Long n_id;
    private String receiver_id;
    private String n_type;
    private int ref_id;
    private String title;
    private String message;
    private String link_url;
    private boolean is_read;
    private java.time.LocalDateTime created_at;

    // 팩토리 메서드
    public static NotifyVO commentOnMyPost(String receiver, int bId, String title, String msg, String url){
        NotifyVO n = new NotifyVO();
        n.receiver_id = receiver;
        n.n_type = "COMMENT";
        n.ref_id = bId;
        n.title = title;
        n.message = msg;
        n.link_url = url;
        return n;
    }
    public static NotifyVO replyToMyComment(String receiver, int cId, String title, String msg, String url){
        NotifyVO n = new NotifyVO();
        n.receiver_id = receiver;
        n.n_type = "REPLY";
        n.ref_id = cId;
        n.title = title;
        n.message = msg;
        n.link_url = url;
        return n;
    }
	public Long getN_id() {
		return n_id;
	}
	public void setN_id(Long n_id) {
		this.n_id = n_id;
	}
	public String getReceiver_id() {
		return receiver_id;
	}
	public void setReceiver_id(String receiver_id) {
		this.receiver_id = receiver_id;
	}
	public String getN_type() {
		return n_type;
	}
	public void setN_type(String n_type) {
		this.n_type = n_type;
	}
	public int getRef_id() {
		return ref_id;
	}
	public void setRef_id(int ref_id) {
		this.ref_id = ref_id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getLink_url() {
		return link_url;
	}
	public void setLink_url(String link_url) {
		this.link_url = link_url;
	}
	public boolean isIs_read() {
		return is_read;
	}
	public void setIs_read(boolean is_read) {
		this.is_read = is_read;
	}
	public java.time.LocalDateTime getCreated_at() {
		return created_at;
	}
	public void setCreated_at(java.time.LocalDateTime created_at) {
		this.created_at = created_at;
	}

}
