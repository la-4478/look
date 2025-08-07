package com.lookmarket.goods.vo;

import org.springframework.stereotype.Component;

@Component("goodsVO")
public class GoodsVO {
	int g_id;
	int g_category;
	String g_name;
	String g_brand;
	String g_discription;
	int g_qty;
	int g_price;
	int g_sale_price;
	String g_credate;
	String g_manufactured_date;
	String g_expiration_date;
	int g_delivery_price;
	String g_delivery_date;
	int g_status;
	int g_stock;
	int i_id;
	String i_file_name;
	String i_file_type;
	
	public int getI_id() {
		return i_id;
	}
	public void setI_id(int i_id) {
		this.i_id = i_id;
	}
	public String getI_file_name() {
		return i_file_name;
	}
	public void setI_file_name(String i_file_name) {
		this.i_file_name = i_file_name;
	}
	public String getI_file_type() {
		return i_file_type;
	}
	public void setI_file_type(String i_file_type) {
		this.i_file_type = i_file_type;
	}
	public String getI_credate() {
		return i_credate;
	}
	public void setI_credate(String i_credate) {
		this.i_credate = i_credate;
	}
	String i_credate;
	
	
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
	public int getG_qty() {
		return g_qty;
	}
	public void setG_qty(int g_qty) {
		this.g_qty = g_qty;
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
	public String getG_delivery_date() {
		return g_delivery_date;
	}
	public void setG_delivery_date(String g_delivery_date) {
		this.g_delivery_date = g_delivery_date;
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
}
