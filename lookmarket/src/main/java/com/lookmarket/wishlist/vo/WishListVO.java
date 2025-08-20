package com.lookmarket.wishlist.vo;

public class WishListVO {
    private int wId;      // 찜 아이디
    private int g_id;      // 상품 번호
    private String mId;   // 회원 아이디

    // 상품 관련 추가 정보 (옵션)
    private String g_name;       // 상품명
    private int g_price;         // 상품 가격
    private String g_image;  // 상품 대표 이미지

    // Getter / Setter
    public int getWId() { return wId; }
    public void setWId(int wId) { this.wId = wId; }

    public int getGId() { return g_id; }
    public void setGId(int g_id) { this.g_id = g_id; }

    public String getMId() { return mId; }
    public void setMId(String mId) { this.mId = mId; }

    public String getG_name() { return g_name; }
    public void setG_name(String g_name) { this.g_name = g_name; }

    public int getG_price() { return g_price; }
    public void setG_price(int g_price) { this.g_price = g_price; }

    public String getG_image() { return g_image; }
    public void setG_image(String g_image) { this.g_image = g_image; }
}
