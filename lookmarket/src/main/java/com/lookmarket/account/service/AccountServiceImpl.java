package com.lookmarket.account.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lookmarket.account.dao.AccountDAO;
import com.lookmarket.account.vo.AccTxnVO;

@Service("accountService")
public class AccountServiceImpl implements AccountService {
	@Autowired
    private AccountDAO accountDAO;

    @Transactional
    @Override
    public long addTxn(AccTxnVO vo) throws Exception {
        if (vo.getAmount() == null || vo.getAmount().compareTo(BigDecimal.ZERO) == 0) {
            throw new IllegalArgumentException("금액은 0이 될 수 없습니다.");
        }
        accountDAO.insertTxn(vo);
        return vo.getTxnId() == null ? -1L : vo.getTxnId();
    }

    @Transactional
    @Override
    public void addTransfer(AccTxnVO outTxn, Long counterAccountId) throws Exception {
        if (counterAccountId == null || outTxn.getAccountId().equals(counterAccountId)) {
            throw new IllegalArgumentException("이체 상대계정이 올바르지 않습니다.");
        }
        if (outTxn.getAmount() == null || outTxn.getAmount().compareTo(BigDecimal.ZERO) == 0) {
            throw new IllegalArgumentException("금액은 0이 될 수 없습니다.");
        }

        // 출금 금액은 음수로 보정
        if (outTxn.getAmount().signum() > 0) {
            outTxn.setAmount(outTxn.getAmount().negate());
        }

        String tk = UUID.randomUUID().toString();
        outTxn.setCategoryId(null);
        outTxn.setTransferKey(tk);
        accountDAO.insertTxn(outTxn);

        // 입금 건
        AccTxnVO inTxn = new AccTxnVO();
        inTxn.setTxnDate(outTxn.getTxnDate());
        inTxn.setAccountId(counterAccountId);
        inTxn.setCategoryId(null);
        inTxn.setAmount(outTxn.getAmount().negate());
        inTxn.setMemo(outTxn.getMemo() == null ? "이체 입금" : outTxn.getMemo());
        inTxn.setPartnerName(outTxn.getPartnerName());
        inTxn.setTransferKey(tk);
        accountDAO.insertTxn(inTxn);
    }

    @Override
    public List<Map<String, Object>> getTxns(Map<String, Object> filter) throws Exception {
        return accountDAO.selectTxns(filter);
    }

    @Override
    public int countTxns(Map<String, Object> filter) throws Exception {
        return accountDAO.countTxns(filter);
    }

    @Override
    public Map<String, Object> getTotals(Map<String, Object> filter) throws Exception {
        return accountDAO.selectTotals(filter);
    }

    @Override
    public List<Map<String, Object>> getAccountOptions() throws Exception {
        return accountDAO.selectAccountOptions();
    }

    @Override
    public List<Map<String, Object>> getCategoryOptions() throws Exception {
        return accountDAO.selectCategoryOptions();
    }

    @Override
    public List<Map<String, Object>> getMonthlySummary(String yyyymm) throws Exception {
        return accountDAO.selectMonthlySummary(yyyymm);
    }

	@Override
	public Map<String, Object> getTxnDetail(int txnId) throws Exception {
		return accountDAO.getTxnDetail(txnId);
	}
}
