package com.lookmarket.account.vo;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

@Component
public class AccTxnVO {
    private Long txnId;             // BIGINT PK
    private LocalDate txnDate;      // DATE
    private Long accountId;         // FK -> acc_account
    private Long categoryId;        // FK -> acc_category (TRANSFER 시 null 가능)
    private BigDecimal amount;      // DECIMAL(15,2)  (수입=+, 지출=-)
    private String memo;            // 비고
    private String partnerName;     // 거래처명 텍스트(옵션)
    private int orderId;           // 주문번호(옵션)
    private String paymentId;         // 결제로그 키(옵션)
    private String transferKey;     // UUID 문자열(이체쌍 식별)
    private LocalDateTime createdAt;// TIMESTAMP
	
    
    public Long getTxnId() {
		return txnId;
	}
	public void setTxnId(Long txnId) {
		this.txnId = txnId;
	}
	public LocalDate getTxnDate() {
		return txnDate;
	}
	public void setTxnDate(LocalDate txnDate) {
		this.txnDate = txnDate;
	}
	public Long getAccountId() {
		return accountId;
	}
	public void setAccountId(Long accountId) {
		this.accountId = accountId;
	}
	public Long getCategoryId() {
		return categoryId;
	}
	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
	public String getPartnerName() {
		return partnerName;
	}
	public void setPartnerName(String partnerName) {
		this.partnerName = partnerName;
	}
	public int getOrderId() {
		return orderId;
	}
	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}
	public String getPaymentId() {
		return paymentId;
	}
	public void setPaymentId(String paymentId) {
		this.paymentId = paymentId;
	}
	public String getTransferKey() {
		return transferKey;
	}
	public void setTransferKey(String transferKey) {
		this.transferKey = transferKey;
	}
	public LocalDateTime getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}
}
