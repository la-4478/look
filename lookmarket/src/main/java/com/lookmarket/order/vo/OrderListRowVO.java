package com.lookmarket.order.vo;

import java.util.Date;
import org.springframework.stereotype.Component;

/**
 * 목록 화면 전용 뷰 VO
 * - JSP에서 참조하는 프로퍼티명을 100% 일치시킴
 *   (예: ${o.OId}, ${o.order_time}, ${o.otGoodsName}, ${o.total_price}, ${o.buyer_name} ...)
 */
@Component("OrderListRowVO")
public class OrderListRowVO {

    // ── 주문/아이템 기본 ─────────────────────────────
    private Integer ONum;            // 아이템 PK (옵션)
    private Integer OId;             // 주문 ID  -> ${o.OId}
    private Integer otGId;           // 상품 ID  -> (필요시)

    private String  otGoodsName;     // 상품명   -> ${o.otGoodsName}
    private Integer otGoodsQty;      // 수량     -> ${o.otGoodsQty}
    private Integer otGoodsPrice;    // 개별가격 -> 계산용
    private Integer otSalePrice;     // 할인가   -> 계산용(null 가능)

    // ── 헤더(주문) 정보 ─────────────────────────────
    private Date    order_time;      // 주문일시 -> ${o.order_time}
    private String  buyer_name;      // 구매자명 -> ${o.buyer_name}
    private String  buyer_id;        // 구매자ID -> ${o.buyer_id}
    private String  o_status;        // 문자열 상태(PENDING/PAID/...) -> ${o.o_status}
    private Integer status;          // 숫자 상태(1/2/3/...)          -> ${o.status}
    private String oi_Receiver_Name;

    // ── 표시 보조 ──────────────────────────────────
    private Integer item_count;      // 같은 주문 내 아이템 개수 -> ${o.item_count}
    private Integer total_price;     // 주문 총액(있으면 JSP가 그대로 씀) -> ${o.total_price}
    private String d_company;
    private String d_transport_num;
    private String d_shipped_date;    // 컬럼 철자에 맞춤
    private String d_delivery_date;
    private Integer d_status;  // 1:준비중, 2:배송중, 3:완료, 4:취소

    public Integer getD_status() { return d_status; }
    public void setD_status(Integer d_status) { this.d_status = d_status; }

    public String getD_company() { return d_company; }
    public void setD_company(String v) { this.d_company = v; }

    public String getD_transport_num() { return d_transport_num; }
    public void setD_transport_num(String v) { this.d_transport_num = v; }

    public String getD_shipped_date() { return d_shipped_date; }
    public void setD_shipped_date(String v) { this.d_shipped_date = v; }

    public String getD_delivery_date() { return d_delivery_date; }
    public void setD_delivery_date(String v) { this.d_delivery_date = v; }

    public Integer getONum() { return ONum; }
    public void setONum(Integer oNum) { ONum = oNum; }

    public Integer getOId() { return OId; }
    public void setOId(Integer oId) { OId = oId; }

    public Integer getOtGId() { return otGId; }
    public void setOtGId(Integer otGId) { this.otGId = otGId; }

    public String getOtGoodsName() { return otGoodsName; }
    public void setOtGoodsName(String otGoodsName) { this.otGoodsName = otGoodsName; }

    public Integer getOtGoodsQty() { return otGoodsQty; }
    public void setOtGoodsQty(Integer otGoodsQty) { this.otGoodsQty = otGoodsQty; }

    public Integer getOtGoodsPrice() { return otGoodsPrice; }
    public void setOtGoodsPrice(Integer otGoodsPrice) { this.otGoodsPrice = otGoodsPrice; }

    public Integer getOtSalePrice() { return otSalePrice; }
    public void setOtSalePrice(Integer otSalePrice) { this.otSalePrice = otSalePrice; }

    public Date getOrder_time() { return order_time; }                // 언더스코어 유지
    public void setOrder_time(Date order_time) { this.order_time = order_time; }

    public String getBuyer_name() { return buyer_name; }              // 언더스코어 유지
    public void setBuyer_name(String buyer_name) { this.buyer_name = buyer_name; }

    public String getBuyer_id() { return buyer_id; }                  // 언더스코어 유지
    public void setBuyer_id(String buyer_id) { this.buyer_id = buyer_id; }

    public String getO_status() { return o_status; }                  // 언더스코어 유지
    public void setO_status(String o_status) { this.o_status = o_status; }

    public Integer getStatus() { return status; }
    public void setStatus(Integer status) { this.status = status; }

    public Integer getItem_count() { return item_count; }             // 언더스코어 유지
    public void setItem_count(Integer item_count) { this.item_count = item_count; }

    public Integer getTotal_price() { return total_price; }           // 언더스코어 유지
    public void setTotal_price(Integer total_price) { this.total_price = total_price; }
    public String getOi_Receiver_Name() {return oi_Receiver_Name;}
	public void setOi_Receiver_Name(String oi_Receiver_Name) {this.oi_Receiver_Name = oi_Receiver_Name;}

	// ── (선택) 편의 게터: JSP 계산 줄이고 싶으면 사용 ─────────
 public Integer getLine_total() {
     int sale = (otSalePrice == null ? 0 : otSalePrice);
     int qty  = (otGoodsQty   == null ? 0 : otGoodsQty);
     int price= (otGoodsPrice == null ? 0 : otGoodsPrice);
     return Math.max(0, price * qty - sale);
}
}
