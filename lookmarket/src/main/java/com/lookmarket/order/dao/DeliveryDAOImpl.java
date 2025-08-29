package com.lookmarket.order.dao;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import com.lookmarket.order.vo.DeliveryVO;

@Repository("deliveryDAO")
public class DeliveryDAOImpl implements DeliveryDAO {
	@Autowired
	private SqlSession sqlSession;

	@Override
	public int updateStatusByOrderId(DeliveryVO deliVO) {
		int result =  sqlSession.update("mapper.deli.updatedelivery", deliVO); 
		return result;
	}

	@Override
	public Object newDelivery(DeliveryVO deliVO) throws DataAccessException {
		return sqlSession.insert("mapper.deli.insertNewdelivery", deliVO);
	}
	
	@Override
    public List<DeliveryVO> selectDeliveryListByMember(String d_m_id) {
		System.out.println("DAO에서 받은 id : " + d_m_id);
        return sqlSession.selectList("mapper.mypage.selectDeliveryListByMember", d_m_id);
    }

	@Override
	public void cencelDlivery(int o_id) throws DataAccessException {
			sqlSession.update("mapper.mypage.cencelDelivery", o_id);
		
	}

    @Override
    public DeliveryVO selectDeliveryByOrderId(int o_id) {
        return sqlSession.selectOne("mapper.admin.selectDeliveryByOrderId", o_id);
    }

}
