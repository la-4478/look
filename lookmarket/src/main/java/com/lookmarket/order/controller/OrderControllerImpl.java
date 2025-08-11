package com.lookmarket.order.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.lookmarket.member.vo.MemberVO;
import com.lookmarket.order.service.OrderService;
import com.lookmarket.order.vo.ApiResponse;
import com.lookmarket.order.vo.OrderItemVO;
import com.lookmarket.order.vo.OrderVO;
import com.lookmarket.order.vo.PayVO;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Controller("orderController")
@RequestMapping(value="/order")
public class OrderControllerImpl implements OrderController{
	@Autowired
	private OrderService orderService;
	
	@Override
	@RequestMapping(value="/orderResult.do", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView orderResult(HttpServletRequest request, HttpServletResponse response) throws Exception{
		//주문완료
		HttpSession session = request.getSession();
		ModelAndView mav = new ModelAndView();
		String layout = "common/layout";
		mav.setViewName(layout);
		
		String viewName = (String)request.getAttribute("viewName");
		mav.addObject("viewName", viewName);
		
        // 세션에서 주문정보와 주문상품 리스트 가져오기
        OrderVO orderInfo = (OrderVO) session.getAttribute("orderInfo");
        List<OrderItemVO> orderProductList = (List<OrderItemVO>) session.getAttribute("orderProductList");
        MemberVO memberInfo = (MemberVO) session.getAttribute("memberInfo");
        PayVO payVO = (PayVO) session.getAttribute("PayVO");
		
		session.setAttribute("sideMenu", "reveal");
		
        // Model에 데이터 전달
        mav.addObject("orderInfo", orderInfo);
        mav.addObject("orderProductList", orderProductList);
        mav.addObject("memberInfo", memberInfo);
        mav.addObject("payInfo", payVO);
		
		return mav;
	}
	
	@Override
	@RequestMapping(value="/orderForm.do", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView form(HttpServletRequest request, HttpServletResponse response) throws Exception{
		//배송정보
		HttpSession session = request.getSession();
	    // 로그인 체크
	    if (session.getAttribute("isLogOn") == null && session.getAttribute("memberInfo") == null) {
	        // 로그인 안 되어 있으면 로그인 페이지로 리다이렉트
	        return new ModelAndView("redirect:/member/loginForm.do");
	    }
		
		ModelAndView mav = new ModelAndView();
		String layout = "common/layout";
		mav.setViewName(layout);
		String viewName = (String)request.getAttribute("viewName");
		mav.addObject("viewName", viewName);
		
		session.setAttribute("sideMenu", "reveal");
	
		return mav;
	}
	
	@Override
	@RequestMapping(value="/placeOrder.do", method=RequestMethod.POST)
	public ModelAndView placeOrder(HttpServletRequest request) {
		// 주문 처리 (장바구니 -> 주문)
	    ModelAndView mav = new ModelAndView();
	    HttpSession session = request.getSession();
	    String currentId = (String) session.getAttribute("current_id");
	    if (currentId == null) {
	        mav.setViewName("redirect:/member/login.do"); // 로그인 페이지로 강제 이동
	        return mav;
	    }

	    try {
	        orderService.processOrder(currentId);
	        // 주문 성공 시 주문 완료 페이지로 이동
	        mav.setViewName("redirect:/order/orderResult.do");
	    } catch (Exception e) {
	        e.printStackTrace();
	        // 에러 시 오류 페이지 또는 주문 페이지로 이동
	        mav.setViewName("order/orderForm"); 
	        mav.addObject("errorMessage", "주문 처리 중 오류가 발생했습니다.");
	    }
	    return mav;
	}

	// 결제 및 주문 저장 API
	@RequestMapping(value="/payToOrderGoods.do", method=RequestMethod.POST)
	@ResponseBody
	public ApiResponse payToOrderGoods(@RequestBody Map<String, Object> payData, HttpServletRequest request) throws Exception {
	    HttpSession session = request.getSession();
	    MemberVO memberInfo = (MemberVO) session.getAttribute("memberInfo");
	    if (memberInfo == null) {
	        return new ApiResponse(false, "로그인 정보가 없습니다.");
	    }

	    System.out.println("📌 payData = " + payData);

	    // 0) 필수값
	    String paymentKey = (String) payData.get("portone_paymentKey");
	    if (paymentKey == null || paymentKey.isBlank()) {
	        return new ApiResponse(false, "결제 실패: paymentKey 없음");
	    }

	    Object orIdxObj = payData.get("or_idx"); // 클라에서 생성한 임시 주문번호(있으면 사용, 없어도 됨)
	    int orderId = (orIdxObj instanceof Number) ? ((Number) orIdxObj).intValue() : 0;

	    // 1) 숫자 파싱 (상품 ID/수량/가격류)
	    int goodsId      = parseIntStrict(payData.get("goods_num"), "goods_num");
	    int quantity     = parseIntStrict(payData.get("quantity"), "quantity");
	    int goodsPrice   = parseIntStrict(payData.get("goods_price"), "goods_price");
	    int deliveryPrice= parseIntStrict(payData.get("oiDeliveryPrice"), "oiDeliveryPrice");
	    int finalPrice   = parseIntStrict(payData.get("price"), "price");

	    // 2) 문자열 파싱
	    String receiverName   = (String) payData.get("receiver_name");
	    String orderName      = (String) payData.get("order_name");
	    String receiverPhone  = (String) payData.get("oiReceiverPhone");
	    String zipcode        = (String) payData.get("zipcode");
	    String address1       = (String) payData.get("address1");
	    String address2       = (String) payData.get("address2");
	    String deliveryMsg    = (String) payData.get("delivery_message");
	    String goodsName      = (String) payData.get("goods_name");

	    String payMethod      = (String) payData.get("pay_method");
	    String cardCompany    = (String) payData.get("card_com_name");
	    Integer cardPayMonth  = safeInt(payData.get("card_pay_month"), 0);
	    String ordererPhone   = (String) payData.get("pay_order_tel");

	    // 3) 주문(헤더) 저장
	    OrderVO orderVO = new OrderVO();

	    orderVO.setMId(memberInfo.getM_id());
	    orderVO.setOiReceiverName(receiverName);
	    orderVO.setOiName(orderName);
	    orderVO.setOiReceiverPhone(receiverPhone);
	    orderVO.setOiDeliveryAddress(joinAddress(zipcode, address1, address2));
	    orderVO.setOiDeliveryMessage(deliveryMsg);
	    orderVO.setOiDeliveryPrice(deliveryPrice);
	    orderVO.setOiTotalGoodsPrice(finalPrice);

	    // DB에 저장하면서 oId 생성되도록(useGeneratedKeys=true, keyProperty="oId")
	    // 기존 시그니처가 List라면 그대로 맞춤
	    orderService.addNewOrder(List.of(orderVO));

	    int generatedOrderId = orderVO.getOId();
	    System.out.println("✅ 생성된 oId = " + generatedOrderId + ", mId = " + orderVO.getMId());

	    // 4) 주문 아이템 저장
	    OrderItemVO itemVO = new OrderItemVO();
	    itemVO.setOId(generatedOrderId);
	    itemVO.setOtGId(goodsId);
	    itemVO.setOtGoodsName(goodsName);
	    itemVO.setOtGoodsPrice(goodsPrice);
	    itemVO.setOtGoodsQty(quantity);
	    itemVO.setOtSalePrice(null); // 할인 없으면 null

	    // 예: orderService.addOrderItem(itemVO);
	    // (메서드가 없다면 만들어서 호출하세요)
	    orderService.addOrderItem(itemVO);

	    // 5) 결제 저장 (PayVO 이름/세터 정확히 맞춤)
	    PayVO payVO = new PayVO();
	    payVO.setOId(generatedOrderId);
	    payVO.setPMethod(payMethod);
	    payVO.setPCardName(cardCompany);
	    payVO.setPPayMonth(cardPayMonth != null ? cardPayMonth : 0);
	    payVO.setPOrdererPhone(ordererPhone);
	    payVO.setPFinalTotalPrice(finalPrice);
	    payVO.setPTransactionId(paymentKey); // PortOne paymentKey 저장

	    String now = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date());
	    payVO.setPOrderTime(now);

	    orderService.addNewpay(payVO);

	    // 6) 카트 비우기 (member_id + goods_id 기준)
	    orderService.removeCartItem(memberInfo.getM_id(), goodsId);

	    // 7) 세션 저장
	    session.setAttribute("PayVO", payVO);
	    session.setAttribute("OrderVO", orderVO);
	    session.setAttribute("OrderItemVO", itemVO);

	    return new ApiResponse(true, "주문 및 결제 완료되었습니다!");
	}

	private int parseIntStrict(Object o, String field) {
	    if (o == null) throw new IllegalArgumentException(field + "가 null입니다!");
	    try {
	        return Integer.parseInt(String.valueOf(o));
	    } catch (NumberFormatException e) {
	        throw new IllegalArgumentException(field + " 숫자 파싱 실패: " + o);
	    }
	}

	private Integer safeInt(Object o, int def) {
	    if (o == null) return def;
	    try {
	        return Integer.parseInt(String.valueOf(o));
	    } catch (NumberFormatException e) {
	        return def;
	    }
	}

	private String joinAddress(String zipcode, String addr1, String addr2) {
	    StringBuilder sb = new StringBuilder();
	    if (zipcode != null && !zipcode.isBlank()) sb.append("(").append(zipcode).append(") ");
	    if (addr1 != null) sb.append(addr1);
	    if (addr2 != null && !addr2.isBlank()) sb.append(" ").append(addr2);
	    return sb.toString().trim();
	}

	@ExceptionHandler(RuntimeException.class)
	@ResponseBody
	public Map<String, Object> handleRuntimeException(RuntimeException ex) {
	    Map<String, Object> map = new HashMap<>();
	    map.put("success", false);
	    try {
	        com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
	        com.fasterxml.jackson.databind.JsonNode json = mapper.readTree(ex.getMessage());
	        map.put("message", json.has("message") ? json.get("message").asText() : ex.getMessage());
	    } catch (Exception e) {
	        map.put("message", ex.getMessage());
	    }
	    return map;
	}


		@RequestMapping(value="/payComplete.do", method = {RequestMethod.POST,RequestMethod.GET})
		@Override
		public ModelAndView payComplete(HttpServletRequest request, HttpServletResponse response) throws Exception {
			String viewName = (String) request.getAttribute("viewName");
			HttpSession session = request.getSession();
			List<OrderVO> myOrderList = (List<OrderVO>)session.getAttribute("myOrderList");
			String memberInfo = (String)session.getAttribute("memberInfo");
			String PayVO = (String)session.getAttribute("PayVO");
			ModelAndView mav = new ModelAndView("/common/layout");
			mav.addObject("body", "/WEB-INF/views/"+viewName+".jsp");
			
			return mav;
		}

}
