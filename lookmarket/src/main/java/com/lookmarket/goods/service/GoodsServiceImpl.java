package com.lookmarket.goods.service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
	public List<GoodsVO> getMyGoodsByCategory(String category, String m_id) throws Exception {
	    System.out.println("서비스 진입 / 카테고리: " + category + " / 아이디: " + m_id);
	    return goodsDAO.selectAllMyGoodsList(category, m_id);
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
	public List<ImageFileVO> goodsMainImage(int g_id) throws Exception {
	    return goodsDAO.selectGoodsImages(g_id);
	}

	@Override
    @Transactional(rollbackFor = Exception.class)
    public int updateGoods(Map<String, Object> goodsMap) throws Exception {
        GoodsVO vo = mapToGoodsVO(goodsMap);

        if (vo.getG_id() == 0) {
            throw new IllegalArgumentException("g_id 누락");
        }

        // 이미지 교체 안 하면 i_filename 건드리지 않도록 null/빈으로 유지
        String newFile = asStr(goodsMap.get("i_filename"));
        String oldFile = asStr(goodsMap.get("old_i_filename"));
        if (newFile != null && !newFile.isBlank()) {
            vo.setI_filename(newFile);   // 교체
        } else {
            vo.setI_filename(null);      // 동적 SQL로 무시
            // 참고: old_i_filename은 DB에 이미 있으므로 굳이 set하지 않음
        }

        return goodsDAO.updateGoods(vo);
    }

    // ---------------- helpers ----------------
    private GoodsVO mapToGoodsVO(Map<String, Object> m) {
        GoodsVO vo = new GoodsVO();
        vo.setG_id(asInt(m.get("g_id")));
        vo.setG_category(asInt(m.get("g_category")));
        vo.setG_name(asStr(m.get("g_name")));
        vo.setG_brand(asStr(m.get("g_brand")));
        vo.setG_discription(asStr(m.get("g_discription")));
        vo.setG_price(asInt(m.get("g_price")));
        vo.setG_manufactured_date(asStr(m.get("g_manufactured_date")));
        vo.setG_expiration_date(asStr(m.get("g_expiration_date")));
        vo.setG_delivery_price(asInt(m.get("g_delivery_price")));
        vo.setG_status(asInt(m.get("g_status")));
        vo.setG_stock(asInt(m.get("g_stock")));
        // i_filename은 위에서 따로 처리
        return vo;
    }

    private String asStr(Object o) {
        return (o == null) ? null : String.valueOf(o).trim();
    }

    private int asInt(Object o) {
        if (o == null) return 0;
        if (o instanceof Number) return ((Number) o).intValue();
        String s = String.valueOf(o).trim();
        if (s.isEmpty()) return 0;
        return Integer.parseInt(s);
    }
    

	@Override
	@Transactional
	public int deleteGoods(int gId) throws Exception {
		try {
            GoodsVO found = goodsDAO.selectGoodsDetail(gId);
            if (found == null) return 0;
        } catch (Exception e) {
            // 상세조회 예외는 삭제 실패 처리
            return 0;
        }
		
		goodsDAO.deleteGoodsImages(gId);
		
		int deleted = goodsDAO.deleteGoods(gId);
		
		return deleted;
	}
	
	@Override
	public List<GoodsVO>myGoodsList(String m_id) throws Exception{
		return goodsDAO.myGoodsList(m_id);
		
	}
}



