package com.lookmarket.goods.vo;

import org.springframework.stereotype.Component;

@Component("goodsVO")
public class GoodsVO {
	private int g_id;
	private  int g_category;
	private String g_name;
	private String g_brand;
	private String g_discription;
	private int g_price;
	private String g_credate;
	private String g_manufactured_date;
	private String g_expiration_date;
	private int g_delivery_price;
	private int g_status;
	private int g_stock;
	private int i_id;
	private String i_filename;
	private String i_filetype;
	private String cre_m_id;
	
	
	public String getCre_m_id() {
		return cre_m_id;
	}
	public void setCre_m_id(String cre_m_id) {
		this.cre_m_id = cre_m_id;
	}
	public int getG_id() {
		return g_id;
	}
	public void setG_id(int g_id) {
		this.g_id = g_id;
	}
	public int getG_category() {
		return g_category;
	}
	public void setG_category(int g_category) {
		this.g_category = g_category;
	}
	public String getG_name() {
		return g_name;
	}
	public void setG_name(String g_name) {
		this.g_name = g_name;
	}
	public String getG_brand() {
		return g_brand;
	}
	public void setG_brand(String g_brand) {
		this.g_brand = g_brand;
	}
	public String getG_discription() {
		return g_discription;
	}
	public void setG_discription(String g_discription) {
		this.g_discription = g_discription;
	}
	public int getG_price() {
		return g_price;
	}
	public void setG_price(int g_price) {
		this.g_price = g_price;
	}
	public String getG_credate() {
		return g_credate;
	}
	public void setG_credate(String g_credate) {
		this.g_credate = g_credate;
	}
	public String getG_manufactured_date() {
		return g_manufactured_date;
	}
	public void setG_manufactured_date(String g_manufactured_date) {
		this.g_manufactured_date = g_manufactured_date;
	}
	public String getG_expiration_date() {
		return g_expiration_date;
	}
	public void setG_expiration_date(String g_expiration_date) {
		this.g_expiration_date = g_expiration_date;
	}
	public int getG_delivery_price() {
		return g_delivery_price;
	}
	public void setG_delivery_price(int g_delivery_price) {
		this.g_delivery_price = g_delivery_price;
	}
	public int getG_status() {
		return g_status;
	}
	public void setG_status(int g_status) {
		this.g_status = g_status;
	}
	public int getG_stock() {
		return g_stock;
	}
	public void setG_stock(int g_stock) {
		this.g_stock = g_stock;
	}
	public int getI_id() {
		return i_id;
	}
	public void setI_id(int i_id) {
		this.i_id = i_id;
	}
	public String getI_filename() {
		return i_filename;
	}
	public void setI_filename(String i_filename) {
		this.i_filename = i_filename;
	}
	public String getI_filetype() {
		return i_filetype;
	}
	public void setI_filetype(String i_filetype) {
		this.i_filetype = i_filetype;
	}

	
	 
}