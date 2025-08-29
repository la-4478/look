package com.lookmarket.sijangbajo.vo;

import com.fasterxml.jackson.annotation.JsonProperty;

public class StoreVO {
    private String bizesNm;      // 상호명
    private String rdnmAdr;      // 도로명 주소
    private String indsLclsNm;   // 업종 대분류
    private String indsMclsNm;   // 업종 중분류
    private String indsSclsNm;   // 업종 소분류
    private String largeCategory;
    private String middleCategory;
    private String smallCategory;
    

    @JsonProperty("lat")        // JSON 필드 'lat'을 latitude에 매핑
    private String latitude;

    @JsonProperty("lon")        // JSON 필드 'lon'을 longitude에 매핑
    private String longitude;

    // Getter / Setter
    public String getBizesNm() { return bizesNm; }
    public void setBizesNm(String bizesNm) { this.bizesNm = bizesNm; }
    public String getRdnmAdr() { return rdnmAdr; }
    public void setRdnmAdr(String rdnmAdr) { this.rdnmAdr = rdnmAdr; }
    public String getIndsLclsNm() { return indsLclsNm; }
    public void setIndsLclsNm(String indsLclsNm) { this.indsLclsNm = indsLclsNm; }
    public String getIndsMclsNm() { return indsMclsNm; }
    public void setIndsMclsNm(String indsMclsNm) { this.indsMclsNm = indsMclsNm; }
    public String getIndsSclsNm() { return indsSclsNm; }
    public void setIndsSclsNm(String indsSclsNm) { this.indsSclsNm = indsSclsNm; }
    public String getLatitude() { return latitude; }
    public void setLatitude(String latitude) { this.latitude = latitude; }
    public String getLongitude() { return longitude; }
    public void setLongitude(String longitude) { this.longitude = longitude; }
	public String getLargeCategory() {
		return largeCategory;
	}
	public void setLargeCategory(String largeCategory) {
		this.largeCategory = largeCategory;
	}
	public String getMiddleCategory() {
		return middleCategory;
	}
	public void setMiddleCategory(String middleCategory) {
		this.middleCategory = middleCategory;
	}
	public String getSmallCategory() {
		return smallCategory;
	}
	public void setSmallCategory(String smallCategory) {
		this.smallCategory = smallCategory;
	}
    
}
