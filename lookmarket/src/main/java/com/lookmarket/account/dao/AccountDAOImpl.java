package com.lookmarket.account.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import com.lookmarket.account.vo.AccTxnVO;

@Repository("accountDAO")
public class AccountDAOImpl implements AccountDAO {

    @Autowired
    private SqlSession sqlSession;

    private static final String NS = "mapper.acc.";

    @Override
    public int insertTxn(AccTxnVO vo) throws DataAccessException {
        return sqlSession.insert(NS + "insertTxn", vo);
    }

    @Override
    public List<Map<String, Object>> selectTxns(Map<String, Object> filter) throws DataAccessException {
        return sqlSession.selectList(NS + "selectTxns", filter);
    }

    @Override
    public int countTxns(Map<String, Object> filter) throws DataAccessException {
        return sqlSession.selectOne(NS + "countTxns", filter);
    }

    @Override
    public Map<String, Object> selectTotals(Map<String, Object> filter) throws DataAccessException {
        return sqlSession.selectOne(NS + "selectTotals", filter);
    }

    @Override
    public List<Map<String, Object>> selectAccountOptions() throws DataAccessException {
        return sqlSession.selectList(NS + "selectAccountOptions");
    }

    @Override
    public List<Map<String, Object>> selectCategoryOptions() throws DataAccessException {
        return sqlSession.selectList(NS + "selectCategoryOptions");
    }

    @Override
    public List<Map<String, Object>> selectMonthlySummary(String yyyymm) throws DataAccessException {
        return sqlSession.selectList(NS + "selectMonthlySummary", yyyymm);
    }

	@Override
	public Map<String, Object> getTxnDetail(int txnId) throws DataAccessException {
		return sqlSession.selectOne(NS + "getTxnDetail", txnId);
	}
}
