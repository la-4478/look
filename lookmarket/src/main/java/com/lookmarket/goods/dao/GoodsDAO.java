package com.lookmarket.goods.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.dao.DataAccessException;

import com.lookmarket.goods.vo.GoodsVO;
import com.lookmarket.goods.vo.ImageFileVO;
import com.lookmarket.order.vo.OrderItemVO;

public interface GoodsDAO {
	public List<GoodsVO> selectAllGoodsList() throws DataAccessException;
	public List<GoodsVO> selectAllMyGoodsList(String category, String m_id) throws DataAccessException;
	public GoodsVO selectGoodsDetail(int g_id) throws DataAccessException;
	public int addNewGoods(Map<String, Object> newGoodsMap) throws DataAccessException;
	public void insertGoodsImageFile(ArrayList<ImageFileVO> imageFileList) throws DataAccessException;
	List<ImageFileVO> selectGoodsImages(int g_id) throws DataAccessException;
	public int updateGoods(GoodsVO goods) throws DataAccessException;
	public int deleteGoodsImages(int g_id) throws DataAccessException;
	public int deleteGoods(int g_id) throws DataAccessException;
	public List<GoodsVO> myGoodsList(String m_id) throws DataAccessException;
	public List<OrderItemVO> selectBizOrderItems(Map<String, Object> params) throws DataAccessException;
	public int countBizOrderItems(String mId) throws DataAccessException;
	public List<GoodsVO> getFreshGoods(int category) throws DataAccessException;
	public List<GoodsVO> getProcessed(int category) throws DataAccessException;
	public List<GoodsVO> getLiving(int category) throws DataAccessException;
	public List<GoodsVO> getFashion(int category) throws DataAccessException;
	public List<GoodsVO> getLocal(int category) throws DataAccessException;
}