package com.lookmarket.order.vo;

import org.springframework.stereotype.Component;

@Component("deliveryVO")
public class DeliveryVO {
	private int d_id;
	private int o_id;
	private int d_status;
	private String d_company;
	private String d_transport_num;
	private String d_shipped_date;
	private String d_delivery_date;
	
	public int getD_id() {
		return d_id;
	}
	public void setD_id(int d_id) {
		this.d_id = d_id;
	}
	public int getO_id() {
		return o_id;
	}
	public void setO_id(int o_id) {
		this.o_id = o_id;
	}
	public int getD_status() {
		return d_status;
	}
	public void setD_status(int d_status) {
		this.d_status = d_status;
	}
	public String getD_company() {
		return d_company;
	}
	public void setD_company(String d_company) {
		this.d_company = d_company;
	}
	public String getD_transport_num() {
		return d_transport_num;
	}
	public void setD_transport_num(String d_transport_num) {
		this.d_transport_num = d_transport_num;
	}
	public String getD_shipped_date() {
		return d_shipped_date;
	}
	public void setD_shipped_date(String d_shipped_date) {
		this.d_shipped_date = d_shipped_date;
	}
	public String getD_delivery_date() {
		return d_delivery_date;
	}
	public void setD_delivery_date(String d_delivery_date) {
		this.d_delivery_date = d_delivery_date;
	}
	
	
}
