package com.lookmarket.goods.dao;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.lookmarket.goods.vo.GoodsVO;

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

}



