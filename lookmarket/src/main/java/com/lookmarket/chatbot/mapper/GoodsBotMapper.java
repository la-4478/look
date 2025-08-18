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
	Object searchGoodsByTokens(Map<String, Object> of);
	List<GoodsVO> selectByKeyword(@Param("q") String keyword);
}
