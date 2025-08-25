package com.lookmarket.order.vo;

import org.springframework.stereotype.Component;

@Component("OrderVO") // DB orderInfo 테이블
public class OrderVO {
    private int oId;                     // 주문 id
    private String mId;                  // 회원 id
    private int oiDeliveryPrice;          // 배송비
    private int oiTotalGoodsPrice;        // 전체 상품 가격
    private Integer oiSalePrice;          // 상품 할인 가격 (null 허용)
    private String oiName;                // 주문자 이름(회원이름)
    private String oiReceiverName;        // 수령자 이름
    private String oiReceiverPhone;       // 수령자 휴대폰번호
    private String oiDate;                // 주문 일자
    private String oiDeliveryAddress;     // 배송 주소
    private String oi_deli_namuji_address; //나머지 주소
    private String oiDeliveryMessage;     // 배송 메시지

    // Getter & Setter
    public int getOId() {
        return oId;
    }
    public void setOId(int oId) {
        this.oId = oId;
    }

    public String getMId() {
        return mId;
    }
    public void setMId(String mId) {
        this.mId = mId;
    }

    public int getOiDeliveryPrice() {
        return oiDeliveryPrice;
    }
    public void setOiDeliveryPrice(int oiDeliveryPrice) {
        this.oiDeliveryPrice = oiDeliveryPrice;
    }

    public int getOiTotalGoodsPrice() {
        return oiTotalGoodsPrice;
    }
    public void setOiTotalGoodsPrice(int oiTotalGoodsPrice) {
        this.oiTotalGoodsPrice = oiTotalGoodsPrice;
    }

    public Integer getOiSalePrice() {
        return oiSalePrice;
    }
    public void setOiSalePrice(Integer oiSalePrice) {
        this.oiSalePrice = oiSalePrice;
    }

    public String getOiName() {
        return oiName;
    }
    public void setOiName(String oiName) {
        this.oiName = oiName;
    }

    public String getOiReceiverName() {
        return oiReceiverName;
    }
    public void setOiReceiverName(String oiReceiverName) {
        this.oiReceiverName = oiReceiverName;
    }

    public String getOiReceiverPhone() {
        return oiReceiverPhone;
    }
    public void setOiReceiverPhone(String oiReceiverPhone) {
        this.oiReceiverPhone = oiReceiverPhone;
    }

    public String getOiDate() {
        return oiDate;
    }
    public void setOiDate(String oiDate) {
        this.oiDate = oiDate;
    }

    public String getOiDeliveryAddress() {
        return oiDeliveryAddress;
    }
    public void setOiDeliveryAddress(String oiDeliveryAddress) {
        this.oiDeliveryAddress = oiDeliveryAddress;
    }

    public String getOiDeliveryMessage() {
        return oiDeliveryMessage;
    }
    public void setOiDeliveryMessage(String oiDeliveryMessage) {
        this.oiDeliveryMessage = oiDeliveryMessage;
    }
	public int getoId() {
		return oId;
	}
	public void setoId(int oId) {
		this.oId = oId;
	}
	public String getmId() {
		return mId;
	}
	public void setmId(String mId) {
		this.mId = mId;
	}
	public String getOi_deli_namuji_address() {
		return oi_deli_namuji_address;
	}
	public void setOi_deli_namuji_address(String oi_deli_namuji_address) {
		this.oi_deli_namuji_address = oi_deli_namuji_address;
	}
    
}
