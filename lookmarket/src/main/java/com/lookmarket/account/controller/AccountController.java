package com.lookmarket.account.controller;

import org.springframework.web.servlet.ModelAndView;

import com.lookmarket.account.vo.AccTxnVO;

import jakarta.servlet.http.HttpServletRequest;

public interface AccountController {
	public ModelAndView accountList(HttpServletRequest request) throws Exception;
	public String accountAdd(AccTxnVO form, HttpServletRequest req) throws Exception;
}
