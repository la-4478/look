package com.lookmarket.community.vo;

import org.springframework.stereotype.Component;

@Component("blackBoardVO")
public class BlackBoardVO {
	String b_id;
	String m_id;
	String b_title;
	String b_content;
	String b_date;
	String b_hit;
	public String getB_id() {
		return b_id;
	}
	public void setB_id(String b_id) {
		this.b_id = b_id;
	}
	public String getM_id() {
		return m_id;
	}
	public void setM_id(String m_id) {
		this.m_id = m_id;
	}
	public String getB_title() {
		return b_title;
	}
	public void setB_title(String b_title) {
		this.b_title = b_title;
	}
	public String getB_content() {
		return b_content;
	}
	public void setB_content(String b_content) {
		this.b_content = b_content;
	}
	public String getB_date() {
		return b_date;
	}
	public void setB_date(String b_date) {
		this.b_date = b_date;
	}
	public String getB_hit() {
		return b_hit;
	}
	public void setB_hit(String b_hit) {
		this.b_hit = b_hit;
	}
	
	
}
