package com.lookmarket.chatbot.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.lookmarket.goods.vo.GoodsVO;

public interface GoodsBotMapper {
    List<Map<String,Object>> searchGoodsLike(Map<String,Object> p);
    String findMainImage(int gId);
    List<String> findSubImages(int gId);
    Map<String,Object> findReviewStats(int gId);
    List<Map<String,Object>> findLatestReviews(int gId);
    List<Map<String, Object>> searchGoodsByTokens(@Param("tokens") List<String> tokens,@Param("limit") int limit);
	List<GoodsVO> selectByKeyword(@Param("q") String keyword);
	// 전체 상품명 (운영에서는 최근 N일/판매중만 등 조건 걸어도 OK)
    List<String> listAllGoodsNames();

    // LIKE 탐색(폴백용)
    List<String> findGoodsNamesLike(@Param("pat") String pat);
}
