package com.lookmarket.goods.service;

import java.util.List;
import java.util.Map;

import com.lookmarket.goods.vo.GoodsVO;
import com.lookmarket.goods.vo.ImageFileVO;

public interface GoodsService {
	public List<GoodsVO> getAllGoods() throws Exception;
	public List<GoodsVO> getFreshGoods() throws Exception;
	public List<GoodsVO> getProcessed() throws Exception;
	public List<GoodsVO> getLiving() throws Exception;
	public List<GoodsVO> getFashion() throws Exception;
	public List<GoodsVO> getLocal() throws Exception;
	public GoodsVO getGoodsDetail(int g_id) throws Exception;
	public int addNewGoods(Map<String, Object> newGoodsMap) throws Exception;
	public ImageFileVO goodsMainImage(int g_id) throws Exception;
}