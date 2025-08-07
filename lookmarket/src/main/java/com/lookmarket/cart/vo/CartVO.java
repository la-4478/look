package com.lookmarket.cart.vo;

import org.springframework.stereotype.Component;

@Component("cartVO")
public class CartVO {
	int c_id;
	int g_id;
	String m_id;
	int c_qty;
	String g_name;
	int g_price;
	int g_sale_price;
	int g_delivery_price;
	int g_stock;
	
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
	public int getG_sale_price() {
		return g_sale_price;
	}
	public void setG_sale_price(int g_sale_price) {
		this.g_sale_price = g_sale_price;
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
	
}
