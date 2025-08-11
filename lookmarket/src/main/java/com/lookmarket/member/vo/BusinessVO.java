package com.lookmarket.member.vo;

import org.springframework.stereotype.Component;

@Component("BusinessVO")
public class BusinessVO {
	private String m_id;
	private String bm_name;
	private String bm_reg_num;
	private String bm_type;
	private String bm_status;
	
	
	public String getM_id() {
		return m_id;
	}
	public void setM_id(String m_id) {
		this.m_id = m_id;
	}
	public String getBm_name() {
		return bm_name;
	}
	public void setBm_name(String bm_name) {
		this.bm_name = bm_name;
	}
	public String getBm_reg_num() {
		return bm_reg_num;
	}
	public void setBm_reg_num(String bm_reg_num) {
		this.bm_reg_num = bm_reg_num;
	}
	public String getBm_type() {
		return bm_type;
	}
	public void setBm_type(String bm_type) {
		this.bm_type = bm_type;
	}
	public String getBm_status() {
		return bm_status;
	}
	public void setBm_status(String bm_status) {
		this.bm_status = bm_status;
	}
}
