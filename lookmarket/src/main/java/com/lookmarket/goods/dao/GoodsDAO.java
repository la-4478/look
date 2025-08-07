package com.lookmarket.goods.dao;

import java.util.List;

import com.lookmarket.goods.vo.GoodsVO;

public interface GoodsDAO {
	public List<GoodsVO> selectAllGoodsList() throws Exception;
	public GoodsVO selectGoodsDetail(int g_id) throws Exception;

}