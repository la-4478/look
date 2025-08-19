package com.lookmarket.kb.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.lookmarket.kb.vo.KbChunkVO;
import com.lookmarket.kb.vo.KbDocumentVO;

/**
 * KB(문서/청크) CRUD 매퍼
 * - 문서 등록/조회/삭제
 * - 청크 배치/조회
 */
@Mapper
public interface KbDAO {
  // ===== Document =====
  /** 문서 1건 등록 (AUTO_INCREMENT docId 반환) */
  int insertDocument(KbDocumentVO doc);

  /** PK로 문서 조회 */
  KbDocumentVO findDocumentById(@Param("docId") long docId);

  /** content_hash로 중복 체크 */
  KbDocumentVO findDocumentByHash(@Param("contentHash") String contentHash);

  /** 문서 목록 (최근 수정 순) */
  List<KbDocumentVO> listDocuments();

  /** 문서 삭제 (청크는 ON DELETE CASCADE로 함께 제거) */
  int deleteDocument(@Param("docId") long docId);

  // ===== Chunk =====
  /** 청크 1건 등록 (AUTO_INCREMENT chunkId) */
  int insertChunk(KbChunkVO chunk);

  /** 청크 여러 건 배치 등록 */
  int insertChunksBatch(@Param("list") List<KbChunkVO> list);

  /** 전체 청크 (운영에선 페이징/필터 추천) */
  List<KbChunkVO> selectAllChunks();

  /** 특정 문서의 청크를 순서대로 조회 */
  List<KbChunkVO> selectChunksByDoc(@Param("docId") long docId);

  /** 특정 문서의 청크 일괄 삭제 */
  int deleteChunksByDoc(@Param("docId") long docId);
}