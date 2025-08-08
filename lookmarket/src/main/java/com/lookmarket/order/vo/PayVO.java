package com.lookmarket.order.vo;

import org.springframework.stereotype.Component;

@Component("PayVO")
public class PayVO {
    private int oId;                   // 주문 id
    private String pMethod;             // 결제 방법
    private String pCardName;           // 결제 카드 회사 이름
    private int pPayMonth;               // 할부 개월 수 (0: 일시불)
    private String pOrdererPhone;       // 주문자 휴대폰 번호
    private String pOrderTime;          // 결제 시간
    private int pFinalTotalPrice;       // 최종 가격
    private String pTransactionId;      // PG사 거래번호

    // Getter & Setter
    public int getOId() {
        return oId;
    }
    public void setOId(int oId) {
        this.oId = oId;
    }

    public String getPMethod() {
        return pMethod;
    }
    public void setPMethod(String pMethod) {
        this.pMethod = pMethod;
    }

    public String getPCardName() {
        return pCardName;
    }
    public void setPCardName(String pCardName) {
        this.pCardName = pCardName;
    }

    public int getPPayMonth() {
        return pPayMonth;
    }
    public void setPPayMonth(int pPayMonth) {
        this.pPayMonth = pPayMonth;
    }

    public String getPOrdererPhone() {
        return pOrdererPhone;
    }
    public void setPOrdererPhone(String pOrdererPhone) {
        this.pOrdererPhone = pOrdererPhone;
    }

    public String getPOrderTime() {
        return pOrderTime;
    }
    public void setPOrderTime(String pOrderTime) {
        this.pOrderTime = pOrderTime;
    }

    public int getPFinalTotalPrice() {
        return pFinalTotalPrice;
    }
    public void setPFinalTotalPrice(int pFinalTotalPrice) {
        this.pFinalTotalPrice = pFinalTotalPrice;
    }

    public String getPTransactionId() {
        return pTransactionId;
    }
    public void setPTransactionId(String pTransactionId) {
        this.pTransactionId = pTransactionId;
    }
}
