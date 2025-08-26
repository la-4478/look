package com.lookmarket.inquiry.vo;

import org.springframework.stereotype.Component;

@Component
public class CommentVO {
	private String c_id;
	private String b_id;
	private String c_content;
	private String c_m_id;
	private String c_credate;
	
	
	public String getC_id() {
		return c_id;
	}
	public void setC_id(String c_id) {
		this.c_id = c_id;
	}
	public String getB_id() {
		return b_id;
	}
	public void setB_id(String b_id) {
		this.b_id = b_id;
	}
	public String getC_content() {
		return c_content;
	}
	public void setC_content(String c_content) {
		this.c_content = c_content;
	}
	public String getC_m_id() {
		return c_m_id;
	}
	public void setC_m_id(String c_m_id) {
		this.c_m_id = c_m_id;
	}
	public String getC_credate() {
		return c_credate;
	}
	public void setC_credate(String c_credate) {
		this.c_credate = c_credate;
	}
}
