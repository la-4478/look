package com.lookmarket.goods.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lookmarket.goods.dao.GoodsDAO;
import com.lookmarket.goods.vo.GoodsVO;
import com.lookmarket.goods.vo.ImageFileVO;
import com.lookmarket.order.vo.OrderItemVO;

@Service("goodsService")
public class GoodsServiceImpl implements GoodsService {
	@Autowired
	private GoodsDAO goodsDAO;
	
	@Override
	public List<GoodsVO> getAllGoods() throws Exception{
		return goodsDAO.selectAllGoodsList();
	}
	
	@Override
	public List<GoodsVO> getFreshGoods(int category) throws Exception{
		System.out.println("서비스카테고리 값 : " + category);
		return goodsDAO.getFreshGoods(category);
	}
	
	@Override
	public List<GoodsVO> getProcessed(int category) throws Exception{
		System.out.println("서비스카테고리 값 : " + category);
		return goodsDAO.getProcessed(category);
	}
	
	@Override
	public List<GoodsVO> getLiving(int category) throws Exception{
		System.out.println("서비스카테고리 값 : " + category);
		return goodsDAO.getLiving(category);
	}
	
	@Override
	public List<GoodsVO> getFashion(int category) throws Exception{
		System.out.println("서비스카테고리 값 : " + category);
		return goodsDAO.getFashion(category);
	}
	
	@Override
	public List<GoodsVO> getLocal(int category) throws Exception{
		System.out.println("서비스카테고리 값 : " + category);
		return goodsDAO.getLocal(category);
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

	/** goods + goods_image를 한 트랜잭션으로 처리 */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int addNewGoods(Map<String, Object> newGoodsMap) throws Exception {
        // 1) goods insert -> PK 반환
        int goodsId = goodsDAO.addNewGoods(newGoodsMap); // ✅ 실제 PK 반환(아래 매퍼/DAO 참고)
        System.out.println("서비스 실행됨 : int goodsId = goodsDAO.addNewGoods(newGoodsMap)");

        // 2) 상세 이미지 있으면 FK 채워서 batch insert
        @SuppressWarnings("unchecked")
        ArrayList<ImageFileVO> imageFileList = (ArrayList<ImageFileVO>) newGoodsMap.get("detailImageList");
        if (imageFileList != null && !imageFileList.isEmpty()) {
            for (ImageFileVO imageFileVO : imageFileList) {
                System.out.println("imageFileVO : " + imageFileList);
            	imageFileVO.setG_id(goodsId);
            }
            goodsDAO.insertGoodsImageFile(imageFileList);
            System.out.println("서비스 실행됨 : goodsDAO.insertGoodsImageFile(imageFileList);");
        }
        return goodsId;
    }

	@Override
	public List<ImageFileVO> goodsMainImage(int g_id) throws Exception {
	    return goodsDAO.selectGoodsImages(g_id);
	}
	
	public List<ImageFileVO> goodsSubImage(int g_id) throws Exception {
		return goodsDAO.goodsSubImage(g_id);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public int updateGoods(Map<String, Object> goodsMap) throws Exception {
	    GoodsVO vo = mapToGoodsVO(goodsMap);
	    if (vo.getG_id() == 0) throw new IllegalArgumentException("g_id 누락");

	    // 대표이미지: 새 값 없으면 업데이트에서 제외
	    String newFile = asStr(goodsMap.get("i_filename"));
	    if (newFile != null && !newFile.isBlank()) {
	        vo.setI_filename(newFile);
	    } else {
	        vo.setI_filename(null); // <if test="i_filename != null"> 로만 set
	    }

	    // 1) 기본 상품정보 업데이트
	    int updated = goodsDAO.updateGoods(vo);
	    if (updated <= 0) return 0;

	    // 2) 상세이미지: detailImageList가 들어왔을 때만 전체 교체
	    @SuppressWarnings("unchecked")
	    List<ImageFileVO> detailImageList = (List<ImageFileVO>) goodsMap.get("detailImageList");
	    if (detailImageList != null && !detailImageList.isEmpty()) {
	        // 기존 레코드 삭제
	        goodsDAO.deleteGoodsImages(vo.getG_id());
	        // FK 보정 + 정렬 보정
	        for (ImageFileVO img : detailImageList) {
	            img.setG_id(vo.getG_id());
	        }
	        goodsDAO.insertGoodsImageFile((ArrayList<ImageFileVO>) detailImageList); // 배치 insert
	    }
	    return updated;
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

	@Override
	public String selectmyGoods(String m_id) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<OrderItemVO> getBizOrderItems(String mId, int page, int size) throws Exception {
        int offset = (page - 1) * size;
        Map<String, Object> params = new HashMap<>();
        params.put("mId", mId);
        params.put("limit", size);
        params.put("offset", offset);
        return goodsDAO.selectBizOrderItems(params);
	}

	@Override
	public int countBizOrderItems(String mId) throws Exception {
        return goodsDAO.countBizOrderItems(mId);
	}
	
	
}



