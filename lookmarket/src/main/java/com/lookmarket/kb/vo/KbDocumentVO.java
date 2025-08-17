package com.lookmarket.kb.vo;

import java.sql.Timestamp;

import lombok.Data;

/**
 * kb_document 테이블 매핑
 * - 원문(페이지/문서) 단위
 * - raw_text: 전체 원문, chunking은 별도 테이블(KbChunk)
 */
@Data
public class KbDocumentVO {
	  private Long docId;         // PK
	  private String sourceUrl;   // 원문 URL (있으면)
	  private String title;       // 문서 제목
	  private String rawText;     // 전체 원문
	  private String lang;        // 'ko' 등
	  private String contentHash; // 중복 방지용 sha256
	  private Timestamp createdAt;
	  private Timestamp updatedAt;
  
  
	public Long getDocId() {
		return docId;
	}
	public void setDocId(Long docId) {
		this.docId = docId;
	}
	public String getSourceUrl() {
		return sourceUrl;
	}
	public void setSourceUrl(String sourceUrl) {
		this.sourceUrl = sourceUrl;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getRawText() {
		return rawText;
	}
	public void setRawText(String rawText) {
		this.rawText = rawText;
	}
	public String getLang() {
		return lang;
	}
	public void setLang(String lang) {
		this.lang = lang;
	}
	public String getContentHash() {
		return contentHash;
	}
	public void setContentHash(String contentHash) {
		this.contentHash = contentHash;
	}
	public Timestamp getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(Timestamp createdAt) {
		this.createdAt = createdAt;
	}
	public Timestamp getUpdatedAt() {
		return updatedAt;
	}
	public void setUpdatedAt(Timestamp updatedAt) {
		this.updatedAt = updatedAt;
	}
}
