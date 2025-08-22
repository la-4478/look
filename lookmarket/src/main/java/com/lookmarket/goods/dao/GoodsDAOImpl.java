package com.lookmarket.goods.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import com.lookmarket.chatbot.mapper.GoodsBotMapper;
import com.lookmarket.goods.vo.GoodsVO;
import com.lookmarket.goods.vo.ImageFileVO;
import com.lookmarket.order.vo.OrderItemVO;

@Repository("goodsDAO")
public class GoodsDAOImpl implements GoodsDAO{
	@Autowired
	private SqlSession sqlSession;
	private GoodsBotMapper goodsMapper;
    public void GoodsDAO(GoodsBotMapper goodsMapper) {
        this.goodsMapper = goodsMapper;
    }
    public List<GoodsVO> findByKeyword(String keyword) {
        return goodsMapper.selectByKeyword(keyword);
    }
	
	@Override
	public List<GoodsVO> selectAllGoodsList() throws DataAccessException{
		return sqlSession.selectList("mapper.goods.selectAllGoodsList");
	}
	@Override
	public List<GoodsVO> getFreshGoods(int category) throws DataAccessException{
		System.out.println("DAO카테고리 값 : " + category);
		return sqlSession.selectList("mapper.goods.getFreshGoods", category);
	}
	@Override
	public List<GoodsVO> getProcessed(int category) throws DataAccessException{
		System.out.println("DAO카테고리 값 : " + category);
		return sqlSession.selectList("mapper.goods.getProcessed", category);
	}
	@Override
	public List<GoodsVO> getLiving(int category) throws DataAccessException{
		System.out.println("DAO카테고리 값 : " + category);
		return sqlSession.selectList("mapper.goods.getLiving", category);
	}
	@Override
	public List<GoodsVO> getFashion(int category) throws DataAccessException{
		System.out.println("DAO카테고리 값 : " + category);
		return sqlSession.selectList("mapper.goods.getFashion", category);
	}
	@Override
	public List<GoodsVO> getLocal(int category) throws DataAccessException{
		System.out.println("DAO카테고리 값 : " + category);
		return sqlSession.selectList("mapper.goods.getLocal", category);
	}
	
	
	@Override
	public GoodsVO selectGoodsDetail(int g_id) throws DataAccessException{
		return sqlSession.selectOne("mapper.goods.selectGoodsDetail", g_id);
	}

	@Override
	public int addNewGoods(Map<String, Object> newGoodsMap) throws DataAccessException{
	    sqlSession.insert("mapper.goods.insertGoods", newGoodsMap);
	    Object pk = newGoodsMap.get("g_id");
	    if (pk == null) throw new IllegalStateException("생성된 g_id를 못 받았습니다. keyProperty/keyColumn/paramType 확인!");
	    return ((Number) pk).intValue();
	}
	

	@Override
	public void insertGoodsImageFile(ArrayList<ImageFileVO> imageFileList) throws DataAccessException{
	    for (ImageFileVO imageFileVO : imageFileList) {
	        sqlSession.insert("mapper.goods.insertGoodsImageFile", imageFileVO);
	    }
	}
	
    @Override
    public List<ImageFileVO> selectGoodsImages(int g_id) throws DataAccessException {
        return sqlSession.selectList("mapper.goods.selectMainimagefile", g_id);
    }

	@Override
	public int updateGoods(GoodsVO goods)throws DataAccessException {
		return sqlSession.update("mapper.goods.updateGoods", goods);
	}
	
	@Override
	public int deleteGoodsImages(int g_id)throws DataAccessException {
	    return sqlSession.delete("mapper.goods.deleteGoodsImages", g_id);
	}
	
	@Override
	public int deleteGoods(int g_id)throws DataAccessException {
	    return sqlSession.delete("mapper.goods.deleteGoods", g_id);
	}

	@Override
	public List<GoodsVO> myGoodsList(String m_id) throws DataAccessException {
		return sqlSession.selectList("mapper.goods.myGoodsList", m_id);
	}

	@Override
	public List<GoodsVO> selectAllMyGoodsList(String category, String m_id) {
	    System.out.println("DAO 진입");
	    System.out.println("받아온 카테고리 값 : " + category);
	    System.out.println("받아온 아이디 값 : " + m_id);

	    Map<String,Object> params = new HashMap<>();
	    params.put("mId", m_id);

	    Integer catCode = toCategoryCode(category); // fresh→1, processed→2…
	    params.put("category", catCode);            // null이면 전체

	    return sqlSession.selectList("mapper.goods.selectAllMyGoodsList", params);
	}

	private Integer toCategoryCode(String category) {
	    if (category == null || category.isBlank() || "all".equalsIgnoreCase(category)) return null;
	    switch (category) {
	    	case "all" : return 0; 
	        case "fresh":     return 1;
	        case "processed": return 2;
	        case "living":    return 3;
	        case "fashion":   return 4;
	        case "local":     return 5;
	        default:
	            try { return Integer.valueOf(category); } catch (NumberFormatException e) { return null; }
	    }
	}


	@Override
	public List<OrderItemVO> selectBizOrderItems(Map<String, Object> params) throws DataAccessException {
		return sqlSession.selectList("mapper.order.selectBizOrderItems", params);
	}


	@Override
	public int countBizOrderItems(String mId) throws DataAccessException {
		return sqlSession.selectOne("mapper.order.countBizOrderItems", mId);
		

	}
	@Override
	public List<ImageFileVO> goodsSubImage(int g_id) {
		return sqlSession.selectList("mapper.goods.selectSubimage", g_id);
	}
}
