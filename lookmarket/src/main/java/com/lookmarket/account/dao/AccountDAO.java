package com.lookmarket.account.dao;

import java.util.List;
import java.util.Map;

import org.springframework.dao.DataAccessException;

import com.lookmarket.account.vo.AccTxnVO;

public interface AccountDAO {
    public int insertTxn(AccTxnVO vo) throws DataAccessException;

    public List<Map<String,Object>> selectTxns(Map<String,Object> filter) throws DataAccessException;
    public int countTxns(Map<String,Object> filter) throws DataAccessException;
    public Map<String,Object> selectTotals(Map<String,Object> filter) throws DataAccessException;

    public List<Map<String,Object>> selectAccountOptions() throws DataAccessException;
    public List<Map<String,Object>> selectCategoryOptions() throws DataAccessException;

    public List<Map<String,Object>> selectMonthlySummary(String yyyymm) throws DataAccessException;

	public Map<String, Object> getTxnDetail(int txnId) throws DataAccessException;
}
