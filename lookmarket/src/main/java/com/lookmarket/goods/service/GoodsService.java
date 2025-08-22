package com.lookmarket.goods.service;

import java.util.List;
import java.util.Map;

import com.lookmarket.goods.vo.GoodsVO;
import com.lookmarket.goods.vo.ImageFileVO;
import com.lookmarket.order.vo.OrderItemVO;

public interface GoodsService {
	public List<GoodsVO> getAllGoods() throws Exception;
	public List<GoodsVO> getFreshGoods(int category) throws Exception;
	public List<GoodsVO> getProcessed(int category) throws Exception;
	public List<GoodsVO> getLiving(int category) throws Exception;
	public List<GoodsVO> getFashion(int category) throws Exception;
	public List<GoodsVO> getLocal(int category) throws Exception;
	public GoodsVO getGoodsDetail(int g_id) throws Exception;
	public int addNewGoods(Map<String, Object> newGoodsMap) throws Exception;
	public int updateGoods(Map<String, Object> goodsMap) throws Exception;
	public List<ImageFileVO> goodsMainImage(int g_id) throws Exception;
	public int deleteGoods(int gId) throws Exception;
	public List<GoodsVO> myGoodsList(String m_id) throws Exception;
	public List<GoodsVO> getMyGoodsByCategory(String category, String m_id) throws Exception;
	public String selectmyGoods(String m_id) throws Exception;
	public List<OrderItemVO> getBizOrderItems(String mId, int page, int size) throws Exception;
	public int countBizOrderItems(String mId) throws Exception;
	public List<ImageFileVO> goodsSubImage(int gId) throws Exception;
}