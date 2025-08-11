package com.lookmarket.goods.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lookmarket.goods.dao.GoodsDAO;
import com.lookmarket.goods.vo.GoodsVO;
import com.lookmarket.goods.vo.ImageFileVO;

@Service("goodsService")
public class GoodsServiceImpl implements GoodsService {
	@Autowired
	private GoodsDAO goodsDAO;
	
	@Override
	public List<GoodsVO> getAllGoods() throws Exception{
		return goodsDAO.selectAllGoodsList();
	}
	
	@Override
	public List<GoodsVO> getFreshGoods() throws Exception{
		return goodsDAO.selectAllGoodsList();
	}
	
	@Override
	public List<GoodsVO> getProcessed() throws Exception{
		return goodsDAO.selectAllGoodsList();
	}
	
	@Override
	public List<GoodsVO> getLiving() throws Exception{
		return goodsDAO.selectAllGoodsList();
	}
	
	@Override
	public List<GoodsVO> getFashion() throws Exception{
		return goodsDAO.selectAllGoodsList();
	}
	
	@Override
	public List<GoodsVO> getLocal() throws Exception{
		return goodsDAO.selectAllGoodsList();
	}
	
	@Override
	public GoodsVO getGoodsDetail(int g_id) throws Exception{
		return goodsDAO.selectGoodsDetail(g_id);
	}

	@Override
	public int addNewGoods(Map<String, Object> newGoodsMap) throws Exception {
	    int goodsId = goodsDAO.addNewGoods(newGoodsMap); // ✅ 이제 7 같은 실제 PK

	    @SuppressWarnings("unchecked")
	    ArrayList<ImageFileVO> imageFileList = (ArrayList<ImageFileVO>) newGoodsMap.get("imageFileList");
	    if (imageFileList != null && !imageFileList.isEmpty()) {
	        for (ImageFileVO imageFileVO : imageFileList) {
	            imageFileVO.setG_id(goodsId); // FK 세팅
	        }
	        goodsDAO.insertGoodsImageFile(imageFileList);
	    }
	    return goodsId;
	}

	@Override
	public ImageFileVO goodsMainImage(int g_id) throws Exception {
	    return goodsDAO.selectGoodsmainImage(g_id);
	}

}


