package com.lookmarket.goods.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.lookmarket.goods.vo.GoodsVO;
import com.lookmarket.goods.vo.ImageFileVO;

public interface GoodsDAO {
	public List<GoodsVO> selectAllGoodsList() throws Exception;
	public GoodsVO selectGoodsDetail(int g_id) throws Exception;
	public int addNewGoods(Map<String, Object> newGoodsMap);
	public void insertGoodsImageFile(ArrayList<ImageFileVO> imageFileList);
	List<ImageFileVO> selectGoodsImages(int g_id) throws Exception;
	public int updateGoods(GoodsVO goods);
	public int deleteGoodsImages(int g_id);
	public int deleteGoods(int g_id);
}