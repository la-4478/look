package com.lookmarket.chatbot.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface BoardBotMapper {
	// 정확 일치로 리뷰/통계
    List<Map<String,Object>> findLatestReviewsByGoodsName(
        @Param("gName") String gName,
        @Param("limit") int limit,
        @Param("offset") int offset
    );
    Map<String,Object> findReviewStatsByGoodsName(@Param("gName") String gName);

    // LIKE 폴백 (정확 일치 없을 때)
    List<Map<String,Object>> findLatestReviewsByGoodsNameLike(
        @Param("gNameLike") String gNameLike,
        @Param("limit") int limit,
        @Param("offset") int offset
    );
    Map<String,Object> findReviewStatsByGoodsNameLike(@Param("gNameLike") String gNameLike);

    // (옵션) 배너/가격 등 상품 정보 합쳐서 보여주고 싶으면 goods join도 추가 가능
    Map<String,Object> findGoodsInfoByName(@Param("gName") String gName);
}
