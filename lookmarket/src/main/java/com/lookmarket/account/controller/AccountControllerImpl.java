package com.lookmarket.account.controller;

import java.sql.Date;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.lookmarket.account.service.AccountService;
import com.lookmarket.account.vo.AccTxnVO;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Controller("accountController")
@RequestMapping("/admin")
public class AccountControllerImpl implements AccountController {

    @Autowired
    private AccountService accountService;
	@Override
    @RequestMapping("/accountList.do")
    public ModelAndView accountList(HttpServletRequest request) throws Exception {
        HttpSession session = request.getSession();

        ModelAndView mav = new ModelAndView("common/layout");
        mav.addObject("viewName", "/admin/accountList");

        session.setAttribute("sideMenu", "reveal");
        session.setAttribute("sideMenu_option", "myPage_admin");

        // 파라미터 처리
        String fromDate = request.getParameter("fromDate");
        String toDate   = request.getParameter("toDate");
        String accountId = request.getParameter("accountId");
        String categoryId = request.getParameter("categoryId");
        int page = request.getParameter("page") == null ? 1 : Integer.parseInt(request.getParameter("page"));
        int pageSize = 20;

        Map<String,Object> filter = new HashMap<>();
        LocalDate today = LocalDate.now();
        filter.put("from", Date.valueOf((fromDate!=null && !fromDate.isEmpty())? LocalDate.parse(fromDate): today.minusDays(30)));
        filter.put("to",   Date.valueOf((toDate!=null && !toDate.isEmpty())?   LocalDate.parse(toDate):   today));
        if(accountId != null && !accountId.isEmpty())  filter.put("accountId",  Long.parseLong(accountId));
        if(categoryId != null && !categoryId.isEmpty()) filter.put("categoryId", Long.parseLong(categoryId));
        filter.put("limit", pageSize);
        filter.put("offset", (page-1)*pageSize);

        List<Map<String,Object>> txnList = accountService.getTxns(filter);
        Map<String,Object> totals = accountService.getTotals(filter);
        int totalCount = accountService.countTxns(filter);
        int totalPages = (int)Math.ceil(totalCount/(double)pageSize);

        mav.addObject("txnList", txnList);
        mav.addObject("totals", totals);
        mav.addObject("page", page);
        mav.addObject("pageSize", pageSize);
        mav.addObject("totalPages", totalPages);

        // 드롭다운 옵션
        mav.addObject("accountOptions", accountService.getAccountOptions());
        mav.addObject("categoryOptions", accountService.getCategoryOptions());

        return mav;
    }
	@Override
    @PostMapping("/accountAdd.do")
    public String accountAdd(@ModelAttribute AccTxnVO form, HttpServletRequest req) throws Exception {
        boolean isTransfer = req.getParameter("isTransfer") != null;
        if(!isTransfer) {
            accountService.addTxn(form);
        } else {
            Long counterAccountId = Long.valueOf(req.getParameter("counterAccountId"));
            accountService.addTransfer(form, counterAccountId);
        }
        return "redirect:/admin/accountList.do";
    }
	
	@RequestMapping("/accountDetail.do")
	public ModelAndView accountDetail(@RequestParam("txnId") int txnId) throws Exception {
	    ModelAndView mav = new ModelAndView("common/layout");
	    mav.addObject("viewName", "/admin/accountDetail");

	    Map<String,Object> detail = accountService.getTxnDetail(txnId); // DAO에서 selectOne
	    mav.addObject("detail", detail);

	    return mav;
	}
	
	@GetMapping("/accountExcel.do")
	public void downloadAccountExcel(        HttpServletResponse response,
	        @RequestParam(value = "fromDate", required = false) String fromDate,
	        @RequestParam(value = "toDate",   required = false) String toDate,
	        @RequestParam(value = "orgId",    required = false) Long orgId) throws Exception {

	    Map<String,Object> filter = new HashMap<>();
	    // 날짜 필터 등 필요시 세팅
	    // ...

	    List<Map<String,Object>> txnList = accountService.getTxns(filter);

	    // 응답 헤더 설정
	    response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
	    response.setHeader("Content-Disposition", "attachment; filename=account_list.xlsx");

	    try (Workbook wb = new XSSFWorkbook()) {
	        Sheet sheet = wb.createSheet("회계리스트");
	        int rowNo = 0;

	        // 헤더
	        Row header = sheet.createRow(rowNo++);
	        String[] headers = {"번호","계정","카테고리","항목(메모)","금액","날짜","구분"};
	        for(int i=0; i<headers.length; i++) {
	            Cell cell = header.createCell(i);
	            cell.setCellValue(headers[i]);
	        }

	        // 데이터
	        int idx = 1;
	        for (Map<String,Object> rowData : txnList) {
	            Row row = sheet.createRow(rowNo++);
	            row.createCell(0).setCellValue(idx++);
	            row.createCell(1).setCellValue((String)rowData.get("accountName"));
	            row.createCell(2).setCellValue((String)rowData.get("categoryName"));
	            row.createCell(3).setCellValue((String)rowData.get("memo"));
	            row.createCell(4).setCellValue(((Number)rowData.get("amount")).doubleValue());
	            row.createCell(5).setCellValue(rowData.get("txnDate").toString());
	            row.createCell(6).setCellValue((String)rowData.get("categoryKind"));
	        }

	        // auto-size
	        for(int i=0; i<headers.length; i++){
	            sheet.autoSizeColumn(i);
	        }

	        // OutputStream으로 전송
	        wb.write(response.getOutputStream());
	    }
	}


}
