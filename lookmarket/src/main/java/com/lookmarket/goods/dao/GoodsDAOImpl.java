package com.lookmarket.goods.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.lookmarket.goods.vo.GoodsVO;
import com.lookmarket.goods.vo.ImageFileVO;

@Repository("goodsDAO")
public class GoodsDAOImpl implements GoodsDAO{
	@Autowired
	private SqlSession sqlSession;
	
	@Override
	public List<GoodsVO> selectAllGoodsList() throws Exception{
		return sqlSession.selectList("mapper.goods.selectAllGoodsList");
	}
	
	@Override
	public GoodsVO selectGoodsDetail(int g_id) throws Exception{
		return sqlSession.selectOne("mapper.goods.selectGoodsDetail", g_id);
	}

	@Override
	public int addNewGoods(Map<String, Object> newGoodsMap) {
	    sqlSession.insert("mapper.goods.insertGoods", newGoodsMap);
	    Object pk = newGoodsMap.get("g_id");
	    if (pk == null) throw new IllegalStateException("생성된 g_id를 못 받았습니다. keyProperty/keyColumn/paramType 확인!");
	    return ((Number) pk).intValue();
	}
	

	@Override
	public void insertGoodsImageFile(ArrayList<ImageFileVO> imageFileList) {
	    for (ImageFileVO imageFileVO : imageFileList) {
	        sqlSession.insert("mapper.goods.insertGoodsImageFile", imageFileVO);
	    }
	}
	
	@Override
	public ImageFileVO selectGoodsmainImage(int g_id) throws Exception {
	    return sqlSession.selectOne("mapper.goods.selectMainimagefile", g_id);
	}
}



