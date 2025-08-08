package com.lookmarket.order.vo;

import org.springframework.stereotype.Component;

@Component("OrderItemVO")
public class OrderItemVO {
    private int oNum;             // 주문 번호 (PK)
    private int oId;              // 주문 id (FK)
    private int otGId;            // 상품 id
    private int otGoodsPrice;     // 상품 개별 가격
    private Integer otSalePrice;  // 상품 할인 가격 (null 가능)
    private String otGoodsName;   // 상품명
    private int otGoodsQty;       // 주문 수량

    // Getter & Setter
    public int getONum() {
        return oNum;
    }
    public void setONum(int oNum) {
        this.oNum = oNum;
    }

    public int getOId() {
        return oId;
    }
    public void setOId(int oId) {
        this.oId = oId;
    }

    public int getOtGId() {
        return otGId;
    }
    public void setOtGId(int otGId) {
        this.otGId = otGId;
    }

    public int getOtGoodsPrice() {
        return otGoodsPrice;
    }
    public void setOtGoodsPrice(int otGoodsPrice) {
        this.otGoodsPrice = otGoodsPrice;
    }

    public Integer getOtSalePrice() {
        return otSalePrice;
    }
    public void setOtSalePrice(Integer otSalePrice) {
        this.otSalePrice = otSalePrice;
    }

    public String getOtGoodsName() {
        return otGoodsName;
    }
    public void setOtGoodsName(String otGoodsName) {
        this.otGoodsName = otGoodsName;
    }

    public int getOtGoodsQty() {
        return otGoodsQty;
    }
    public void setOtGoodsQty(int otGoodsQty) {
        this.otGoodsQty = otGoodsQty;
    }
}
