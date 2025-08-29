package com.lookmarket.account.service;

import java.util.List;
import java.util.Map;

import com.lookmarket.account.vo.AccTxnVO;

public interface AccountService {
	public long addTxn(AccTxnVO vo) throws Exception;
	public void addTransfer(AccTxnVO outTxn, Long counterAccountId) throws Exception;

	public List<Map<String,Object>> getTxns(Map<String,Object> filter) throws Exception;
	public int countTxns(Map<String,Object> filter) throws Exception;
	public Map<String,Object> getTotals(Map<String,Object> filter) throws Exception;

	public List<Map<String,Object>> getAccountOptions() throws Exception;
	public List<Map<String,Object>> getCategoryOptions() throws Exception;

	public List<Map<String,Object>> getMonthlySummary(String yyyymm) throws Exception;
	public Map<String, Object> getTxnDetail(int txnId) throws Exception;
}
