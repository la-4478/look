package com.lookmarket.community.vo;

import org.springframework.stereotype.Component;

@Component("reviewVO")
public class ReviewVO {
	String r_id;
	String m_id;
	String r_title;
	String g_id;
	String r_star;
	String r_content;
	String r_filename1;
	String r_filetype;
	String r_secret;
	String r_date;
	String r_hit;
	public String getR_id() {
		return r_id;
	}
	public void setR_id(String r_id) {
		this.r_id = r_id;
	}
	public String getM_id() {
		return m_id;
	}
	public void setM_id(String m_id) {
		this.m_id = m_id;
	}
	public String getR_title() {
		return r_title;
	}
	public void setR_title(String r_title) {
		this.r_title = r_title;
	}
	public String getG_id() {
		return g_id;
	}
	public void setG_id(String g_id) {
		this.g_id = g_id;
	}
	public String getR_star() {
		return r_star;
	}
	public void setR_star(String r_star) {
		this.r_star = r_star;
	}
	public String getR_content() {
		return r_content;
	}
	public void setR_content(String r_content) {
		this.r_content = r_content;
	}
	public String getR_filename1() {
		return r_filename1;
	}
	public void setR_filename1(String r_filename1) {
		this.r_filename1 = r_filename1;
	}
	public String getR_filetype() {
		return r_filetype;
	}
	public void setR_filetype(String r_filetype) {
		this.r_filetype = r_filetype;
	}
	public String getR_secret() {
		return r_secret;
	}
	public void setR_secret(String r_secret) {
		this.r_secret = r_secret;
	}
	public String getR_date() {
		return r_date;
	}
	public void setR_date(String r_date) {
		this.r_date = r_date;
	}
	public String getR_hit() {
		return r_hit;
	}
	public void setR_hit(String r_hit) {
		this.r_hit = r_hit;
	}
	
	
}
