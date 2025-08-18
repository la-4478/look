package com.lookmarket.wishlist.vo;

public class WishListVO {
    private int wId;      // 찜 아이디
    private int gId;      // 상품 번호
    private String mId;   // 회원 아이디

    // 상품 관련 추가 정보 (옵션)
    private String gName;       // 상품명
    private int gPrice;         // 상품 가격
    private String gThumbnail;  // 상품 대표 이미지

    // Getter / Setter
    public int getWId() { return wId; }
    public void setWId(int wId) { this.wId = wId; }

    public int getGId() { return gId; }
    public void setGId(int gId) { this.gId = gId; }

    public String getMId() { return mId; }
    public void setMId(String mId) { this.mId = mId; }

    public String getGName() { return gName; }
    public void setGName(String gName) { this.gName = gName; }

    public int getGPrice() { return gPrice; }
    public void setGPrice(int gPrice) { this.gPrice = gPrice; }

    public String getGThumbnail() { return gThumbnail; }
    public void setGThumbnail(String gThumbnail) { this.gThumbnail = gThumbnail; }
}
