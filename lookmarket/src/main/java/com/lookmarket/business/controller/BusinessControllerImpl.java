package com.lookmarket.business.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.lookmarket.goods.service.GoodsService;
import com.lookmarket.goods.vo.GoodsVO;
import com.lookmarket.member.service.MemberService;
import com.lookmarket.member.vo.BusinessVO;
import com.lookmarket.member.vo.MemberVO;
import com.lookmarket.order.service.DeliveryService;
import com.lookmarket.order.vo.OrderItemVO;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Controller("business")
@RequestMapping(value="/business")
public class BusinessControllerImpl implements BusinessController{
	@Autowired
	private MemberVO memberVO;
	@Autowired
	private MemberService memberService;
	@Autowired
	private GoodsService goodsService;
	@Autowired
	private DeliveryService deliveryService;
	

	//viewName 수정 필요
	@Override
	@RequestMapping(value="/mypageBusinessInfo.do", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView mypageBusinessInfo(HttpServletRequest request, HttpServletResponse response) throws Exception{
		//사업자 정보
		HttpSession session = request.getSession();
		ModelAndView mav = new ModelAndView();
		String layout = "common/layout";
		mav.setViewName(layout);
		String viewName = (String)request.getAttribute("viewName");
		mav.addObject("viewName", viewName);
		

	    session.setAttribute("sideMenu", "reveal");
		session.setAttribute("sideMenu_option", "myPage_business");
		
		return mav;
	}
	
	@Override
	@RequestMapping(value="/businessGoodsList.do", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView businessGoodsList(@RequestParam("category") String category, HttpServletRequest request, HttpServletResponse response) throws Exception {
		//사업자 상품 리스트
	    HttpSession session = request.getSession();
		String m_id = (String) session.getAttribute("loginUserId");
		System.out.println("세션에서 가져온 로그인 아이디 " + m_id);
		if ("all".equalsIgnoreCase(category)) category = null;
		List<GoodsVO> goodsList = goodsService.getMyGoodsByCategory(category, m_id);

		ModelAndView mav = new ModelAndView();
		String layout = "common/layout";
		mav.setViewName(layout);
		String viewName = (String)request.getAttribute("viewName");
		System.out.println(viewName);
		mav.addObject("viewName", viewName);
		
	    session.setAttribute("sideMenu", "reveal");
		session.setAttribute("sideMenu_option", "myPage_business");
	    mav.addObject("goodsList", goodsList);
	    mav.addObject("category", category);
		
		
		return mav;
	}
	
	@Override
	@RequestMapping(value="/businessOrderList.do", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView businessOrderList(HttpServletRequest request, HttpServletResponse response) throws Exception {
	    HttpSession session = request.getSession();
	    ModelAndView mav = new ModelAndView();
	    System.out.println("businessOrderList 컨트롤러 진입");
	    String layout = "common/layout";
	    mav.setViewName(layout);

	    String viewName = (String) request.getAttribute("viewName");
	    if (viewName == null || viewName.isBlank()) viewName = "business/orderList";
	    mav.addObject("viewName", viewName);

	    String m_id = (String) session.getAttribute("loginUserId");
	    if (m_id == null) {
	        mav.setViewName("redirect:/member/loginForm.do");
	        return mav;
	    }

	    // 페이징 파라미터
	    int page = parseIntOrDefault(request.getParameter("page"), 1);
	    int size = parseIntOrDefault(request.getParameter("size"), 20);

	    // (옵션) 내 상품 리스트가 필요하면 유지
	    List<GoodsVO> myGoods = goodsService.myGoodsList(m_id);
	    if(myGoods == null) {
	    	System.out.println("상품리스트 없음");
	        mav.setViewName("redirect:/business/businessMain.do");
	        return mav;
	    }
	    mav.addObject("myGoods", myGoods);
	    System.out.println("myGoods 가져오고 저장함" + myGoods.toString());

	    // 주문 아이템 조회 (JOIN X, EXISTS O)
	    List<OrderItemVO> orders = goodsService.getBizOrderItems(m_id, page, size);
	    int total = goodsService.countBizOrderItems(m_id);
	    if(total == 0) {
	    	System.out.println("주문정보 없음");
	        mav.setViewName("redirect:/business/businessMain.do");
	        return mav;
	    }
	    int totalPages = (int) Math.ceil(total / (double) size);
	    System.out.println("orders 가져오고 저장함" + orders.toString());
	    
	    mav.addObject("orders", orders);
	    mav.addObject("page", page);
	    mav.addObject("size", size);
	    mav.addObject("totalPages", totalPages);
	    mav.addObject("totalElements", total);

	    session.setAttribute("sideMenu", "reveal");
	    session.setAttribute("sideMenu_option", "myPage_business");
	    return mav;
	}

	private int parseIntOrDefault(String s, int def) {
	    try { return (s == null || s.isBlank()) ? def : Integer.parseInt(s); }
	    catch (Exception e) { return def; }
	}
	
	@Override
	@RequestMapping(value="/member/myMemberList.do", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView myMemberList(HttpServletRequest request, HttpServletResponse response) throws Exception{
		//회원 리스트
		HttpSession session;
		ModelAndView mav = new ModelAndView();
		String layout = "common/layout";
		mav.setViewName(layout);
		String viewName = (String)request.getAttribute("viewName");
		mav.addObject("viewName", viewName);
		
		session = request.getSession();
	    session.setAttribute("sideMenu", "reveal");
		session.setAttribute("sideMenu_option", "myPage_business");
			
		return mav;
	}
	
	@Override
	@RequestMapping(value="/mypage/myCommunityList.do", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView myCommunityList(HttpServletRequest request, HttpServletResponse response) throws Exception{
		//커뮤니티
		HttpSession session;
		ModelAndView mav = new ModelAndView();
		String layout = "common/layout";
		mav.setViewName(layout);
		String viewName = (String)request.getAttribute("viewName");
		mav.addObject("viewName", viewName);
		
		session = request.getSession();
	    session.setAttribute("sideMenu", "reveal");
		session.setAttribute("sideMenu_option", "myPage_business");
		
		return mav;
	}
	
	@Override
	@RequestMapping(value="/mypage/myBlackBoardList.do", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView myBlackBoardList(HttpServletRequest request, HttpServletResponse response) throws Exception{
		//사장님 고충방
		HttpSession session;
		ModelAndView mav = new ModelAndView();
		String layout = "common/layout";
		mav.setViewName(layout);
		String viewName = (String)request.getAttribute("viewName");
		mav.addObject("viewName", viewName);
		
		session = request.getSession();
	    session.setAttribute("sideMenu", "reveal");
		session.setAttribute("sideMenu_option", "myPage_business");
		
		return mav;
	}

	@Override
	@RequestMapping(value="/businessMain.do", method={RequestMethod.GET, RequestMethod.POST})
	public ModelAndView BusinessMain(HttpServletRequest request, HttpServletResponse response) throws Exception {
	    HttpSession session = request.getSession(false);
	    if (session == null || !Boolean.TRUE.equals(session.getAttribute("isLogOn"))) {
	        return new ModelAndView("redirect:/member/loginForm.do");
	    }

	    String mId = (String) session.getAttribute("loginUserId");
	    if (mId == null) return new ModelAndView("redirect:/member/loginForm.do");

	    // 상태 캐싱 (없으면 조회)
	    String businessStatus = (String) session.getAttribute("businessStatus");
	    if (businessStatus == null) {
	        businessStatus = memberService.status(mId);
	        session.setAttribute("businessStatus", businessStatus);
	    }


	    BusinessVO businessVO = memberService.findBusinessByMemberId(mId);
	    System.out.println("m_id : " + businessVO.getM_id());
	    System.out.println("m_id : " + businessVO.getBm_name());
	    System.out.println("m_id : " + businessVO.getBm_reg_num());
	    System.out.println("m_id : " + businessVO.getBm_status());
	    System.out.println("m_id : " + businessVO.getBm_type());
	    session.setAttribute("businessVO", businessVO);


	    MemberVO memberVO = (MemberVO) session.getAttribute("memberInfo");
	    if (memberVO == null) {
	        memberVO = memberService.findMemberById(mId);
	        session.setAttribute("memberInfo", memberVO);
	    }

	    ModelAndView mav = new ModelAndView();
		String layout = "common/layout";
	    mav.setViewName(layout);
	    String viewName = (String) request.getAttribute("viewName");
	    mav.addObject("viewName", viewName);

	    mav.addObject("memberInfo", memberVO);
	    mav.addObject("businessVO", businessVO);
	    mav.addObject("businessStatus", businessStatus);

	    session.setAttribute("sideMenu", "reveal");
	    session.setAttribute("sideMenu_option", "myPage_business");

	    return mav;
	}

	@Override
	@RequestMapping(value="/updateDelivery.do", method= {RequestMethod.GET, RequestMethod.POST})
	@ResponseBody
	public Map<String, Object> updateDelivery(@RequestParam("o_id") int orderId, @RequestParam("d_status") int dStatus /* 2 = 배송중*/) throws Exception {
	    Map<String, Object> res = new HashMap<>();
	    System.out.println("orderId : " + orderId + "dStatus" + dStatus);
	    try {
	        int updated = deliveryService.updateStatusByOrderId(orderId, dStatus);
	        res.put("success", updated > 0);
	        res.put("d_status", dStatus);
	        res.put("message", updated > 0 ? "업데이트 성공" : "대상 주문이 없거나 이미 동일 상태입니다.");
	    } catch (Exception e) {
	        res.put("success", false);
	        res.put("message", "서버 오류: " + e.getMessage());
	    }
	    return res;
	}
}
