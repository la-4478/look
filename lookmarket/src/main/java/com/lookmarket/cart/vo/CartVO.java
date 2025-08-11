package com.lookmarket.cart.vo;

import org.springframework.stereotype.Component;

@Component("cartVO")
public class CartVO {
	// 장바구니 정보
	int c_id; //장바구니ID
	int g_id; //상품ID(FK)
	String m_id; //회원ID(FK)
	int c_qty; //장바구니 수량
	// 상품 정보(조인해서 화면에 출력)
	String g_name; //상품명
	int g_price; //상품가격
	int g_delivery_price; //배송비
	int g_stock; //재고
	String i_filename; //이미지 파일명 (추가)
	public int getC_id() {
		return c_id;
	}
	public void setC_id(int c_id) {
		this.c_id = c_id;
	}
	public int getG_id() {
		return g_id;
	}
	public void setG_id(int g_id) {
		this.g_id = g_id;
	}
	public String getM_id() {
		return m_id;
	}
	public void setM_id(String m_id) {
		this.m_id = m_id;
	}
	public int getC_qty() {
		return c_qty;
	}
	public void setC_qty(int c_qty) {
		this.c_qty = c_qty;
	}
	public String getG_name() {
		return g_name;
	}
	public void setG_name(String g_name) {
		this.g_name = g_name;
	}
	public int getG_price() {
		return g_price;
	}
	public void setG_price(int g_price) {
		this.g_price = g_price;
	}
	public int getG_delivery_price() {
		return g_delivery_price;
	}
	public void setG_delivery_price(int g_delivery_price) {
		this.g_delivery_price = g_delivery_price;
	}
	public int getG_stock() {
		return g_stock;
	}
	public void setG_stock(int g_stock) {
		this.g_stock = g_stock;
	}
	public String getI_filename() {
		return i_filename;
	}
	public void setI_filename(String i_filename) {
		this.i_filename = i_filename;
	}
	
	
	
}
