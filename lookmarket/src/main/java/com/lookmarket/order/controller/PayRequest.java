package com.lookmarket.order.controller;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;

public class PayRequest {
    @JsonProperty("portone_paymentKey")
    private String portonePaymentKey;

    @JsonProperty("or_idx")
    private Integer orIdx;

    @JsonProperty("pd_name")
    private String pdName;

    @JsonProperty("price")
    private Integer price;

    @JsonProperty("receiver_name")
    private String receiverName;

    // 수령자 번호 분할 (선택적)
    private String receiverPhone;

    @JsonProperty("goods_num")
    private List<Integer> goodsNum;

    @JsonProperty("goods_name")
    private List<String> goodsName;

    @JsonProperty("goods_sales_price")
    private List<Integer> goodsSalesPrice;

    @JsonProperty("order_name")
    private String orderName;

    @JsonProperty("order_num")
    private String orderNum;

    private String zipcode;

    @JsonProperty("roadAddress")
    private String roadAddress;

    @JsonProperty("jibunAddress")
    private String jibunAddress;

    @JsonProperty("namujiAddress")
    private String namujiAddress;

    @JsonProperty("delivery_message")
    private String deliveryMessage;

    @JsonProperty("delivery_method")
    private String deliveryMethod;   // "parcel" 등

    @JsonProperty("pay_method")
    private String payMethod;        // "card" | "kakao" | "naver" | "transfer"

    @JsonProperty("card_com_name")
    private String cardComName;      // null 가능

    @JsonProperty("card_pay_month")
    private Integer cardPayMonth;    // 0,3,6...

    @JsonProperty("pay_order_tel")
    private String payOrderTel;      // 전체 번호(선택)

	public String getPortonePaymentKey() {
		return portonePaymentKey;
	}

	public void setPortonePaymentKey(String portonePaymentKey) {
		this.portonePaymentKey = portonePaymentKey;
	}

	public Integer getOrIdx() {
		return orIdx;
	}

	public void setOrIdx(Integer orIdx) {
		this.orIdx = orIdx;
	}

	public String getPdName() {
		return pdName;
	}

	public void setPdName(String pdName) {
		this.pdName = pdName;
	}

	public Integer getPrice() {
		return price;
	}

	public void setPrice(Integer price) {
		this.price = price;
	}

	public String getReceiverName() {
		return receiverName;
	}

	public void setReceiverName(String receiverName) {
		this.receiverName = receiverName;
	}

	public String getReceiverPhone() {
		return receiverPhone;
	}

	public void setReceiverPhone(String receiverPhone) {
		this.receiverPhone = receiverPhone;
	}

	public List<Integer> getGoodsNum() {
		return goodsNum;
	}

	public void setGoodsNum(List<Integer> goodsNum) {
		this.goodsNum = goodsNum;
	}

	public List<String> getGoodsName() {
		return goodsName;
	}

	public void setGoodsName(List<String> goodsName) {
		this.goodsName = goodsName;
	}

	public List<Integer> getGoodsSalesPrice() {
		return goodsSalesPrice;
	}

	public void setGoodsSalesPrice(List<Integer> goodsSalesPrice) {
		this.goodsSalesPrice = goodsSalesPrice;
	}

	public String getOrderName() {
		return orderName;
	}

	public void setOrderName(String orderName) {
		this.orderName = orderName;
	}

	public String getOrderNum() {
		return orderNum;
	}

	public void setOrderNum(String orderNum) {
		this.orderNum = orderNum;
	}

	public String getZipcode() {
		return zipcode;
	}

	public void setZipcode(String zipcode) {
		this.zipcode = zipcode;
	}

	public String getRoadAddress() {
		return roadAddress;
	}

	public void setRoadAddress(String roadAddress) {
		this.roadAddress = roadAddress;
	}

	public String getJibunAddress() {
		return jibunAddress;
	}

	public void setJibunAddress(String jibunAddress) {
		this.jibunAddress = jibunAddress;
	}

	public String getNamujiAddress() {
		return namujiAddress;
	}

	public void setNamujiAddress(String namujiAddress) {
		this.namujiAddress = namujiAddress;
	}

	public String getDeliveryMessage() {
		return deliveryMessage;
	}

	public void setDeliveryMessage(String deliveryMessage) {
		this.deliveryMessage = deliveryMessage;
	}

	public String getDeliveryMethod() {
		return deliveryMethod;
	}

	public void setDeliveryMethod(String deliveryMethod) {
		this.deliveryMethod = deliveryMethod;
	}

	public String getPayMethod() {
		return payMethod;
	}

	public void setPayMethod(String payMethod) {
		this.payMethod = payMethod;
	}

	public String getCardComName() {
		return cardComName;
	}

	public void setCardComName(String cardComName) {
		this.cardComName = cardComName;
	}

	public Integer getCardPayMonth() {
		return cardPayMonth;
	}

	public void setCardPayMonth(Integer cardPayMonth) {
		this.cardPayMonth = cardPayMonth;
	}

	public String getPayOrderTel() {
		return payOrderTel;
	}

	public void setPayOrderTel(String payOrderTel) {
		this.payOrderTel = payOrderTel;
	}
    

}
