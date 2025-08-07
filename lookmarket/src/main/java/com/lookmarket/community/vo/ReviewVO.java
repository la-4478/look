package com.lookmarket.community.vo;

import org.springframework.stereotype.Component;

@Component("reviewVO")
public class ReviewVO {
	int r_id;
	String m_id;
	String r_title;
	int g_id;
	int r_star;
	String r_content;
	String r_filename;
	String r_filetype;
	int r_secret;
	String r_date;
	int r_hit;
	
	public int getR_id() {
		return r_id;
	}
	public void setR_id(int r_id) {
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
	public int getG_id() {
		return g_id;
	}
	public void setG_id(int g_id) {
		this.g_id = g_id;
	}
	public int getR_star() {
		return r_star;
	}
	public void setR_star(int r_star) {
		this.r_star = r_star;
	}
	public String getR_content() {
		return r_content;
	}
	public void setR_content(String r_content) {
		this.r_content = r_content;
	}
	public String getR_filename() {
		return r_filename;
	}
	public void setR_filename(String r_filename) {
		this.r_filename = r_filename;
	}
	public String getR_filetype() {
		return r_filetype;
	}
	public void setR_filetype(String r_filetype) {
		this.r_filetype = r_filetype;
	}
	public int getR_secret() {
		return r_secret;
	}
	public void setR_secret(int r_secret) {
		this.r_secret = r_secret;
	}
	public String getR_date() {
		return r_date;
	}
	public void setR_date(String r_date) {
		this.r_date = r_date;
	}
	public int getR_hit() {
		return r_hit;
	}
	public void setR_hit(int r_hit) {
		this.r_hit = r_hit;
	}
}
