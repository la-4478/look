package com.lookmarket.member.vo;

import org.springframework.stereotype.Component;

@Component("memberVO")
public class MemberVO {
	String m_id;
	String m_pw;
	String m_name;
	int m_gender;
	String m_birth;
	int m_phone_telecom;
	String m_phone;
	int m_phone_yn;
	String m_email;
	int m_email_yn;
	String m_zipcode;
	String m_road_address;
	String m_jibun_address;
	String m_namuji_address;
	int m_level;
	int m_role;
	String m_joindate;
	String m_outdate;
	
	public String getM_id() {
		return m_id;
	}
	public void setM_id(String m_id) {
		this.m_id = m_id;
	}
	public String getM_pw() {
		return m_pw;
	}
	public void setM_pw(String m_pw) {
		this.m_pw = m_pw;
	}
	public String getM_name() {
		return m_name;
	}
	public void setM_name(String m_name) {
		this.m_name = m_name;
	}
	public int getM_gender() {
		return m_gender;
	}
	public void setM_gender(int m_gender) {
		this.m_gender = m_gender;
	}
	public String getM_birth() {
		return m_birth;
	}
	public void setM_birth(String m_birth) {
		this.m_birth = m_birth;
	}
	public int getM_phone_telecom() {
		return m_phone_telecom;
	}
	public void setM_phone_telecom(int m_phone_telecom) {
		this.m_phone_telecom = m_phone_telecom;
	}
	public String getM_phone() {
		return m_phone;
	}
	public void setM_phone(String m_phone) {
		this.m_phone = m_phone;
	}
	public int getM_phone_yn() {
		return m_phone_yn;
	}
	public void setM_phone_yn(int m_phone_yn) {
		this.m_phone_yn = m_phone_yn;
	}
	public String getM_email() {
		return m_email;
	}
	public void setM_email(String m_email) {
		this.m_email = m_email;
	}
	public int getM_email_yn() {
		return m_email_yn;
	}
	public void setM_email_yn(int m_email_yn) {
		this.m_email_yn = m_email_yn;
	}
	public String getM_zipcode() {
		return m_zipcode;
	}
	public void setM_zipcode(String m_zipcode) {
		this.m_zipcode = m_zipcode;
	}
	public String getM_road_address() {
		return m_road_address;
	}
	public void setM_road_address(String m_road_address) {
		this.m_road_address = m_road_address;
	}
	public String getM_jibun_address() {
		return m_jibun_address;
	}
	public void setM_jibun_address(String m_jibun_address) {
		this.m_jibun_address = m_jibun_address;
	}
	public String getM_namuji_address() {
		return m_namuji_address;
	}
	public void setM_namuji_address(String m_namuji_address) {
		this.m_namuji_address = m_namuji_address;
	}
	public int getM_level() {
		return m_level;
	}
	public void setM_level(int m_level) {
		this.m_level = m_level;
	}
	public int getM_role() {
		return m_role;
	}
	public void setM_role(int m_role) {
		this.m_role = m_role;
	}
	public String getM_joindate() {
		return m_joindate;
	}
	public void setM_joindate(String m_joindate) {
		this.m_joindate = m_joindate;
	}
	public String getM_outdate() {
		return m_outdate;
	}
	public void setM_outdate(String m_outdate) {
		this.m_outdate = m_outdate;
	}
}
