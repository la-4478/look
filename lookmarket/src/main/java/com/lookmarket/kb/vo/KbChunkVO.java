package com.lookmarket.kb.vo;

import java.sql.Timestamp;

import lombok.Data;

/**
 * kb_chunk 테이블 매핑
 * - 모델에 넣을 컨텍스트 단위(문단/슬라이스)
 * - embedding: float[] (TypeHandler로 BLOB 매핑)
 */
@Data
public class KbChunkVO {
	  private Long chunkId;       // PK
	  private Long docId;         // FK -> kb_document.doc_id
	  private String title;       // 문서/섹션 제목
	  private String text;        // 컨텍스트 텍스트(수백~천자 권장)
	  private float[] embedding;  // 임베딩 벡터
	  private Integer tokenCount; // 토큰 수(옵션)
	  private Integer orderInDoc; // 문서 내 순서(정렬용)
	  private Timestamp createdAt;
	  private Timestamp updatedAt;
	  
	  
	public Long getChunkId() {
		return chunkId;
	}
	public void setChunkId(Long chunkId) {
		this.chunkId = chunkId;
	}
	public Long getDocId() {
		return docId;
	}
	public void setDocId(Long docId) {
		this.docId = docId;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public float[] getEmbedding() {
		return embedding;
	}
	public void setEmbedding(float[] embedding) {
		this.embedding = embedding;
	}
	public Integer getTokenCount() {
		return tokenCount;
	}
	public void setTokenCount(Integer tokenCount) {
		this.tokenCount = tokenCount;
	}
	public Integer getOrderInDoc() {
		return orderInDoc;
	}
	public void setOrderInDoc(Integer orderInDoc) {
		this.orderInDoc = orderInDoc;
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
