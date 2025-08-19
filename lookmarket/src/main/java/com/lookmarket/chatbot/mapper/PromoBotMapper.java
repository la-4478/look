package com.lookmarket.chatbot.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
@Mapper
public interface PromoBotMapper {
	 // ① 진행중 프로모션
    List<Map<String,Object>> findActivePromotions(
        @Param("today") String today,   // 'YYYY-MM-DD'
        @Param("limit") int limit,
        @Param("offset") int offset
    );

    // ② 키워드 검색(진행중만)
    List<Map<String,Object>> searchActivePromotions(
        @Param("q") String q,
        @Param("today") String today,
        @Param("limit") int limit,
        @Param("offset") int offset
    );

    // ③ 단건 조회
    Map<String,Object> findPromotionById(@Param("postId") int postId);

    // ④ 배너만 (상위 N)
    List<Map<String,Object>> findActiveBanners(
        @Param("today") String today,
        @Param("limit") int limit
    );
}
