package com.lookmarket.order.controller;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.lookmarket.cart.service.CartService;
import com.lookmarket.cart.vo.CartVO;
import com.lookmarket.member.vo.MemberVO;
import com.lookmarket.order.service.CouponService;
import com.lookmarket.order.service.DeliveryService;
import com.lookmarket.order.service.OrderService;
import com.lookmarket.order.vo.ApiResponse;
import com.lookmarket.order.vo.DeliveryVO;
import com.lookmarket.order.vo.OrderItemVO;
import com.lookmarket.order.vo.OrderVO;
import com.lookmarket.order.vo.PayVO;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Controller("orderController")
@RequestMapping(value = "/order")
public class OrderControllerImpl implements OrderController {
	@Autowired private OrderService orderService;
	@Autowired private CartService cartService;
	@Autowired private DeliveryService deliveryService;
	@Autowired private CouponService couponService;

	// 클래스 안에 필드로 Random 생성 (필요시)
	private Random random = new Random();

	// 난수 생성 메서드 (6자리 난수 예시)
	private int generateOrderNum() {
	    return 100000 + random.nextInt(900000);  // 100000 ~ 999999
	}

	@Override
	@RequestMapping(value = "/orderResult.do", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView orderResult(HttpServletRequest request, HttpServletResponse response) throws Exception {
	    System.out.println("/orderResult.do 컨트롤러 진입");
	    HttpSession session = request.getSession();

	    OrderVO orderInfo = (OrderVO) session.getAttribute("orderInfo");
	    Object itemAny     = session.getAttribute("itemVO");    // ★ 변경: 단일/리스트 모두 받기 위해 Object로
	    PayVO payVO        = (PayVO) session.getAttribute("PayVO");

	    System.out.println("orderinfo : " + orderInfo);
	    System.out.println("itemVO(any) : " + (itemAny == null ? null : itemAny.getClass().getName())); // ★ 추가: 실제 타입 로그
	    System.out.println("payVO : " + payVO);

	    // ★ 추가: 단일/리스트/배열 → 항상 List<OrderItemVO>로 정규화
	    java.util.List<OrderItemVO> itemList = toOrderItemList(itemAny);

	    if (orderInfo == null || itemList == null || payVO == null) {
	        System.out.println("if문으로 빠짐");
	        System.out.println("orderinfo : " + orderInfo);
	        System.out.println("itemList : " + itemList);
	        System.out.println("payVO : " + payVO);
	        return new ModelAndView("redirect:/order/orderForm.do");
	    }

	    ModelAndView mav = new ModelAndView("common/layout");
	    String viewName = (String) request.getAttribute("viewName");
	    if (viewName == null) viewName = "order/orderResult";
	    mav.addObject("viewName", viewName);

	    mav.addObject("orderInfo", orderInfo);
	    mav.addObject("itemVO", itemList);   // ★ 핵심: JSP는 items="${itemVO}" 그대로 사용
	    mav.addObject("payInfo", payVO);

	    session.setAttribute("sideMenu", "reveal");
	    return mav;
	}

	/** ★ 추가: 어떤 형태로 오든 List<OrderItemVO>로 변환 */
	@SuppressWarnings("unchecked")
	private java.util.List<OrderItemVO> toOrderItemList(Object any) {
	    if (any == null) return java.util.Collections.emptyList();
	    if (any instanceof java.util.List) {
	        return (java.util.List<OrderItemVO>) any;
	    }
	    if (any instanceof OrderItemVO) {
	        return java.util.Collections.singletonList((OrderItemVO) any);
	    }
	    if (any.getClass().isArray()) {
	        // OrderItemVO[] 또는 Object[] 지원
	        if (any instanceof OrderItemVO[]) {
	            return java.util.Arrays.asList((OrderItemVO[]) any);
	        } else if (any instanceof Object[]) {
	            Object[] arr = (Object[]) any;
	            java.util.List<OrderItemVO> list = new java.util.ArrayList<>(arr.length);
	            for (Object o : arr) if (o instanceof OrderItemVO) list.add((OrderItemVO) o);
	            return list;
	        }
	    }
	    return java.util.Collections.emptyList();
	}


	@Override
	@RequestMapping(value = "/orderForm.do", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView form(HttpServletRequest request, HttpServletResponse response) throws Exception {
		// 주문정보 입력
		HttpSession session = request.getSession();
		
		MemberVO memberInfo = (MemberVO) session.getAttribute("memberInfo");
	    if (memberInfo == null) {
	        return new ModelAndView("redirect:/member/loginForm.do");
	    }
		
		// 장바구니 또는 주문상품 리스트 받아오기 (예시)
	    List<OrderItemVO> myOrderList = (List<OrderItemVO>)session.getAttribute("myOrderList");

		ModelAndView mav = new ModelAndView();
		String layout = "common/layout";
		mav.setViewName(layout);
		String viewName = (String) request.getAttribute("viewName");
		
		mav.addObject("viewName", viewName);
		mav.addObject("myOrderList", myOrderList);

		session.setAttribute("sideMenu", "reveal");

		return mav;
	}
	@Override
	@PostMapping(value = "/placeOrder.do", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public ResponseEntity<Map<String, Object>> placeOrderAjax(HttpServletRequest request, HttpSession session) throws Exception {
	    MemberVO memberInfo = (MemberVO) session.getAttribute("memberInfo");
	    if (memberInfo == null) {
	        // 401 주면 jQuery error 콜백으로 감
	        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of(
	            "success", false,
	            "message", "로그인이 필요합니다."
	        ));
	    }

	    try {
	        // ====== 파라미터 수신 (네 기존 코드 재사용) ======
	        String oiName = request.getParameter("oi_name");
	        String oiReceiverName = request.getParameter("oi_receiver_name");
	        String oiReceiverPhone = request.getParameter("oi_receiver_phone");
	        String zipcode = request.getParameter("zipcode");
	        String oiDeliveryAddress = request.getParameter("oi_delivery_address");
	        String oiDeliveryMessage = request.getParameter("oi_delivery_message");
	        String paymentMethod = request.getParameter("payment_method");
	        int installment = parseIntOrZero(request.getParameter("installment"));

	        int totalGoodsPrice  = parseIntOrZero(request.getParameter("totalGoodsPrice"));
	        int deliveryFee      = parseIntOrZero(request.getParameter("deliveryFee"));
	        int finalTotalPrice  = parseIntOrZero(request.getParameter("finalTotalPrice"));

	        String[] goodsIds    = request.getParameterValues("goodsId");
	        String[] goodsNames  = request.getParameterValues("goodsName");
	        String[] goodsPrices = request.getParameterValues("goodsPrice");
	        String[] goodsQtys   = request.getParameterValues("goodsQty");

	        if (goodsIds == null || goodsIds.length == 0) {
	            // 400 보내면 jQuery error 콜백으로 감
	            return ResponseEntity.badRequest().body(Map.of(
	                "success", false,
	                "message", "주문 상품 정보가 누락되었습니다."
	            ));
	        }

	        // ====== 주문 헤더 저장 ======
	        OrderVO orderVO = new OrderVO();
	        orderVO.setMId(memberInfo.getM_id());
	        orderVO.setOiName(oiName);
	        orderVO.setOiReceiverName(oiReceiverName);
	        orderVO.setOiReceiverPhone(oiReceiverPhone);
	        orderVO.setOiDeliveryAddress("(" + zipcode + ") " + oiDeliveryAddress);
	        orderVO.setOiDeliveryMessage(oiDeliveryMessage);
	        orderVO.setOiDeliveryPrice(deliveryFee);
	        orderVO.setOiTotalGoodsPrice(finalTotalPrice);

	        orderService.addNewOrder(List.of(orderVO));
	        int oId = orderVO.getOId();

	        // ====== 주문 아이템 저장(다건) ======
	        for (int i = 0; i < goodsIds.length; i++) {
	            OrderItemVO itemVO = new OrderItemVO();
	            itemVO.setONum(generateOrderNum());
	            itemVO.setOId(oId);
	            itemVO.setOtGId(Integer.parseInt(goodsIds[i]));
	            itemVO.setOtGoodsName(goodsNames[i]);
	            itemVO.setOtGoodsPrice(Integer.parseInt(goodsPrices[i]));
	            itemVO.setOtGoodsQty(Integer.parseInt(goodsQtys[i]));
	            itemVO.setOtSalePrice(null);
	            orderService.addOrderItem(itemVO);
	        }

	        // ====== 결제 정보 저장 ======
	        PayVO payVO = new PayVO();
	        payVO.setOId(oId);
	        payVO.setPMethod(paymentMethod);
	        payVO.setPPayMonth(installment);
	        payVO.setPFinalTotalPrice(finalTotalPrice);
	        payVO.setPOrderTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
	        orderService.addNewpay(payVO);

	        // ====== 세션 ======
	        session.setAttribute("OrderVO", orderVO);
	        session.setAttribute("PayVO", payVO);

	        return ResponseEntity.ok(Map.of(
	            "success", true,
	            "message", "주문이 완료되었습니다.",
	            "oId", oId
	        ));
	    } catch (Exception e) {
	        e.printStackTrace();
	        // 500 주면 jQuery error 콜백으로 감
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
	            "success", false,
	            "message", "주문 처리 중 오류가 발생했습니다: " + e.getMessage()
	        ));
	    }
	}

	private int parseIntOrZero(String s) {
	    try { return (s == null) ? 0 : Integer.parseInt(s); }
	    catch (NumberFormatException e) { return 0; }
	}




	// 결제 및 주문 저장 API
	@RequestMapping(value = "/payToOrderGoods.do", method = RequestMethod.POST)
	@ResponseBody
	public ApiResponse payToOrderGoods(@RequestBody Map<String, Object> payData, HttpServletRequest request)
	        throws Exception {
	    System.out.println("/payToOrderGoods.do컨트롤러 진입");
	    HttpSession session = request.getSession();
	    String member_id = (String)session.getAttribute("current_id");
	    MemberVO memberInfo = (MemberVO) session.getAttribute("memberInfo");
	    if (memberInfo == null) return new ApiResponse(false, "로그인 정보가 없습니다.");

	    System.out.println("📌 payData = " + payData);
	    System.out.println("📌 keys   = " + payData.keySet()); // ★ 추가: 들어온 키 확인

	    // 0) 필수값
	    String paymentKey = asString(payData.get("portone_paymentKey"));
	    if (paymentKey == null || paymentKey.isBlank()) {
	        return new ApiResponse(false, "결제 실패: paymentKey 없음");
	    }
	    String paymentId = asString(payData.get("paymentId")); // ★ 추가: v2 조회용
	    if (paymentId == null || paymentId.isBlank()) {
	        return new ApiResponse(false, "결제 실패: paymentId 없음");
	    }

	    Integer orderId = asInt(payData.get("or_idx"), 0);
	    Integer finalPrice = asInt(payData.get("price"), 0);
	    if (finalPrice <= 0) return new ApiResponse(false, "결제 실패: 결제 금액 오류");

	    // 1) 리스트 파싱 (배열/콤마문자 모두 대응)
	    List<Integer> goodsIds        = asIntList(payData.get("goods_num"));
	    List<String>  goodsNames      = asStringList(payData.get("goods_name"));
	    List<Integer> goodsSalesPrice = asIntList(payData.get("goods_sales_price"));
	    List<Integer> goodsQtys       = asIntList(payData.get("goods_qty"));
	    if (goodsIds.isEmpty()) return new ApiResponse(false, "주문 상품이 없습니다.");
	    if (goodsQtys.size() != goodsIds.size()) return new ApiResponse(false, "수량 정보가 누락되었습니다.");

	    // 2) 문자열 파싱 (키명을 프론트와 맞춤)
	    String receiverName  = asString(payData.get("receiver_name"));
	    String orderName     = asString(payData.get("order_name"));
	    String receiverPhone = asString(payData.get("receiverPhone"));
	    String zipcode       = asString(payData.get("zipcode"));
	    String roadAddress   = asString(payData.get("roadAddress"));
	    String namujiAddress = asString(payData.get("namujiAddress"));
	    String deliveryMsg   = asString(payData.get("delivery_message"));

	    // ★ 변경: 프론트가 보내는 값은 "참고용"으로만 사용
	    String payMethodFromClient     = asString(payData.get("pay_method"));     // (참고)
	    String cardCompanyFromClient   = asString(payData.get("card_com_name"));  // (참고)
	    Integer cardPayMonthFromClient = asInt(payData.get("card_pay_month"), 0); // (참고)
	    String ordererPhone            = asString(payData.get("pay_order_tel"));
	    Integer salesPrice = asInt(payData.get("couponDiscount"), 0);
	    String couponId = asString(payData.get("couponId"));

	    // 3) 배송비 (정책에 따라 없으면 0)
	    Integer deliveryPrice = asInt(payData.get("oiDeliveryPrice"), 0);

	    /* =========================================================
	       ★★★★★ PortOne V2 단건 조회(검증) – 환경변수 사용 ★★★★★
	       - Authorization: "PortOne {V2_API_SECRET}"
	       - GET https://api.portone.io/payments/{paymentId}
	       ========================================================= */
	    Map<String, Object> payment = fetchPortOnePayment(paymentId); // ★ 추가 (아래 헬퍼)
	    if (payment == null || payment.isEmpty()) {
	        return new ApiResponse(false, "결제 조회 실패");
	    }

	    // 상태 검증
	    String status = asString(payment.get("status"));
	    if (!"PAID".equalsIgnoreCase(status)) {
	        return new ApiResponse(false, "결제 미완료 상태: " + status);
	    }
		 // PortOne 조회 결과에서 payMethod 추출
	    String payMethod = asString(payment.get("method")); // CARD / EASY_PAY / TRANSFER ...

	    // provider 초기값
	    String provider = null;
	    
	    if ("EASY_PAY".equalsIgnoreCase(payMethod)) {
	        Map<String, Object> easyPay = asMap(payment.get("easyPay"));
	        if (easyPay != null) {
	            provider = asString(easyPay.get("provider")); // 예: KAKAOPAY, NAVERPAY, TOSS 등
	        }
	    }

	    // 카드 결제면 카드사 이름 가져오기
	    String cardCompany = null;
	    if ("CARD".equalsIgnoreCase(payMethod)) {
	        Map<String, Object> cardObj = asMap(payment.get("card"));
	        if (cardObj != null) {
	            cardCompany = asString(cardObj.get("company"));
	        }
	    }

	    // 금액 검증 (amount.total)
	    Map<String, Object> amount = asMap(payment.get("amount"));
	    long paidTotal = amount != null && amount.get("total") instanceof Number
	            ? ((Number) amount.get("total")).longValue() : -1L;
	    if (paidTotal >= 0 && paidTotal != finalPrice.longValue()) {
	        return new ApiResponse(false, "금액 불일치(요청:" + finalPrice + ", 결제:" + paidTotal + ")");
	    }

	    // 결제수단/카드사 추출 (서버 조회값 우선)
	    payMethod = firstNonNull(
	            asString(payment.get("method")),      // ex) CARD/TRANSFER/VIRTUAL_ACCOUNT/MOBILE
	            payMethodFromClient
	    );
	    Map<String, Object> cardObj = asMap(payment.get("card"));
	    cardCompany = firstNonNull(
	            cardObj != null ? asString(cardObj.get("company")) : null,
	            cardCompanyFromClient
	    );
	    Integer cardPayMonth = firstNonNullInt(
	            asInt(payment.get("installmentMonth"), -1),
	            cardObj != null ? asInt(cardObj.get("installment"), -1) : -1,
	            cardPayMonthFromClient
	    );
	    if (cardPayMonth == null || cardPayMonth < 0) cardPayMonth = 0;

	    // 4) 주문(헤더) 저장
	    OrderVO orderVO = new OrderVO();
	    orderVO.setMId(memberInfo.getM_id());
	    orderVO.setOiReceiverName(receiverName);
	    orderVO.setOiSalePrice(salesPrice);
	    orderVO.setOiName(orderName);
	    orderVO.setOiReceiverPhone(
	            (ordererPhone != null && !ordererPhone.isBlank()) ? ordererPhone : receiverPhone
	    );
	    // ★ 변경: 상세주소는 별도 칼럼이면 setOiDeliNamujiAddress(...)도 같이 세팅해
	    orderVO.setOiDeliveryAddress(joinAddress(zipcode, roadAddress));
	    orderVO.setOi_deli_namuji_address(namujiAddress);
	    orderVO.setOiDeliveryMessage(deliveryMsg);
	    orderVO.setOiDeliveryPrice(deliveryPrice);

	    // 총 상품금액 재계산(신뢰도 ↑)
	    int calcGoodsTotal = 0;
	    for (int i = 0; i < goodsIds.size(); i++) {
	        int price = (i < goodsSalesPrice.size()) ? goodsSalesPrice.get(i) : 0;
	        int qty   = goodsQtys.get(i);
	        calcGoodsTotal += price * qty;
	    }
	    orderVO.setOiTotalGoodsPrice(calcGoodsTotal);

	    orderService.addNewOrder(List.of(orderVO)); // useGeneratedKeys=true로 oId 생성
	    int generatedOrderId = orderVO.getOId();
	    System.out.println("✅ 생성된 oId = " + generatedOrderId + ", mId = " + orderVO.getMId());

	    // 5) 주문 아이템 저장
	    OrderItemVO itemVO = new OrderItemVO();
	    for (int i = 0; i < goodsIds.size(); i++) {
	        
	        itemVO.setONum(generateOrderNum()); // 정책에 맞게
	        itemVO.setOId(generatedOrderId);
	        itemVO.setOtGId(goodsIds.get(i));
	        itemVO.setOtGoodsName(i < goodsNames.size() ? goodsNames.get(i) : null);
	        itemVO.setOtGoodsPrice(i < goodsSalesPrice.size() ? goodsSalesPrice.get(i) : 0);
	        itemVO.setOtGoodsQty(goodsQtys.get(i));
	        itemVO.setOtSalePrice(null);
	        orderService.addOrderItem(itemVO);
	    }


	    // 6) 결제 저장 (서버 조회값 기준)

	    System.out.println("저장될 p_method 값: [" + payMethod + "]");
	    System.out.println("길이: " + (payMethod != null ? payMethod.length() : 0));
	    PayVO payVO = new PayVO();
	    payVO.setOId(generatedOrderId);
	    payVO.setPMethod(provider != null ? provider : payMethod); 
		 // ★ 간편결제면 provider 저장, 아니면 그냥 method 값 저장
		 payVO.setPCardName(cardCompany); // 카드사 이름
	    payVO.setPPayMonth(cardPayMonth != null ? cardPayMonth : 0);
	    payVO.setPOrdererPhone(
	            (ordererPhone != null && !ordererPhone.isBlank()) ? ordererPhone : receiverPhone
	    );
	    payVO.setPFinalTotalPrice(finalPrice);
	    payVO.setPTransactionId(paymentKey);

	    String now = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date());
	    payVO.setPOrderTime(now);
	    
	    orderService.addNewpay(payVO);
	    
    	// 주문 시간 예시 (DB나 현재 시간)
    	LocalDateTime orderDateTime = LocalDateTime.now(); // 지금 시간 예시
    	LocalDate shippedDate;

    	// 기준 시간 = 정오 12시
    	LocalTime cutoffTime = LocalTime.NOON;

    	// 주문 시간이 정오 이전이면 당일출발, 이후면 다음날출발
    	if (orderDateTime.toLocalTime().isBefore(cutoffTime)) {
    	    shippedDate = orderDateTime.toLocalDate(); // 당일
    	} else {
    	    shippedDate = orderDateTime.toLocalDate().plusDays(1); // 다음날
    	}

    	DeliveryVO deliVO = new DeliveryVO();
    	deliVO.setO_id(generatedOrderId);
    	//운송장 번호 난수 생성
    	deliVO.setD_transport_num(String.valueOf(generateOrderNum()));
    	//배송 시작일
    	deliVO.setD_shipped_date(shippedDate.toString());

    	// 배송 완료일 = 시작일 + 1일
    	LocalDate deliveryDate = shippedDate.plusDays(1);
    	deliVO.setD_delivery_date(deliveryDate.toString());
    	deliVO.setD_m_id(memberInfo.getM_id());

    	deliveryService.NewDelivery(deliVO);
    	
	    // 7) 카트 비우기
	    for (Integer gid : goodsIds) {
	        orderService.removeCartItem(memberInfo.getM_id(), gid);
	    }
	    
	    couponService.useCoupon(couponId, member_id);
	    
	    orderService.recordTransactionAfterPayment(orderVO, payVO);
	    
	    // 8) 세션 저장
	    session.setAttribute("itemVO", itemVO);
	    session.setAttribute("PayVO", payVO);
	    session.setAttribute("orderInfo", orderVO);

	    return new ApiResponse(true, "주문 및 결제 완료되었습니다!");
	}


	/** ===================== 헬퍼들 ===================== */

	// ★ 추가: PortOne V2 결제 단건 조회 (HttpURLConnection 대체안)
	@SuppressWarnings("unchecked")
	private Map<String, Object> fetchPortOnePayment(String paymentId) {
	    String secret = System.getenv("PORTONE_V2_SECRET");
	    if (secret == null || secret.isBlank()) {
	        throw new IllegalStateException("PORTONE_V2_SECRET 환경변수 미설정");
	    }

	    java.net.HttpURLConnection conn = null;
	    try {
	        java.net.URL url = new java.net.URL("https://api.portone.io/payments/" + java.net.URLEncoder.encode(paymentId, java.nio.charset.StandardCharsets.UTF_8));
	        conn = (java.net.HttpURLConnection) url.openConnection();
	        conn.setRequestMethod("GET");
	        conn.setRequestProperty("Authorization", "PortOne " + secret); // ★ 추가
	        conn.setRequestProperty("Accept", "application/json; charset=UTF-8");
	        conn.setConnectTimeout(8000);
	        conn.setReadTimeout(8000);

	        int code = conn.getResponseCode();
	        java.io.InputStream is = (code >= 200 && code < 300) ? conn.getInputStream() : conn.getErrorStream();
	        String json = new String(is.readAllBytes(), java.nio.charset.StandardCharsets.UTF_8);
	        // Jackson ObjectMapper로 Map 파싱
	        com.fasterxml.jackson.databind.ObjectMapper om = new com.fasterxml.jackson.databind.ObjectMapper();
	        return om.readValue(json, Map.class);
	    } catch (Exception e) {
	        e.printStackTrace();
	        return java.util.Collections.emptyMap();
	    } finally {
	        if (conn != null) conn.disconnect();
	    }
	}

	// 안전 캐스팅 유틸
	@SuppressWarnings("unchecked")
	private Map<String,Object> asMap(Object o) {
	    return (o instanceof Map) ? (Map<String,Object>) o : null;
	}
	private String firstNonNull(String... vals) {
	    for (String v : vals) if (v != null && !v.isBlank()) return v;
	    return null;
	}
	private Integer firstNonNullInt(Integer... vals) {
	    for (Integer v : vals) if (v != null && v >= 0) return v;
	    return null;
	}

	
	private static String asString(Object o){
	    return o == null ? null : String.valueOf(o);
	}
	private static Integer asInt(Object o, int def){
	    try{
	        if (o == null) return def;
	        if (o instanceof Number n) return n.intValue();
	        String s = String.valueOf(o).replaceAll("[^0-9-]", "");
	        return s.isBlank() ? def : Integer.parseInt(s);
	    }catch(Exception e){ return def; }
	}
	private static List<String> asStringList(Object o){
	    if (o == null) return java.util.List.of();
	    if (o instanceof java.util.List<?> l) return l.stream().map(String::valueOf).toList();
	    if (o instanceof String[] arr) return java.util.Arrays.stream(arr).map(String::valueOf).toList();
	    String s = String.valueOf(o);
	    if (s.contains(",")) return java.util.Arrays.stream(s.split(",")).map(String::trim).toList();
	    return java.util.List.of(s);
	}
	private static List<Integer> asIntList(Object o){
	    return asStringList(o).stream().map(v -> {
	        try { return Integer.parseInt(v.replaceAll("[^0-9-]", "")); }
	        catch(Exception e){ return 0; }
	    }).toList();
	}
	private String joinAddress(String zipcode, String addr1) {
	    StringBuilder sb = new StringBuilder();
	    if (zipcode != null && !zipcode.isBlank()) sb.append("(").append(zipcode).append(") ");
	    if (addr1 != null && !addr1.isBlank()) sb.append(addr1.trim());
	    return sb.toString().trim();
	}


	@RequestMapping(value = "/payComplete.do", method = { RequestMethod.POST, RequestMethod.GET })
	@Override
	public ModelAndView payComplete(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String viewName = (String) request.getAttribute("viewName");
		HttpSession session = request.getSession();
		List<OrderVO> myOrderList = (List<OrderVO>) session.getAttribute("myOrderList");
		MemberVO memberInfo = (MemberVO) session.getAttribute("memberInfo");
		PayVO PayVO = (PayVO) session.getAttribute("PayVO");
		ModelAndView mav = new ModelAndView("/common/layout");
		mav.addObject("body", "/WEB-INF/views/" + viewName + ".jsp");

		return mav;
	}
	
	@RequestMapping(value="/orderAllCartGoods.do", method = RequestMethod.POST)
	public ModelAndView orderAllCartGoods(
	        @RequestParam("goodsQty") String[] goodsQty,
	        @RequestParam("goodsId")  String[] goodsId,
	        HttpServletRequest request) throws Exception {

	    HttpSession session = request.getSession(false);
	    if (session == null) return new ModelAndView("redirect:/member/login.do");

	    MemberVO memberVO = (MemberVO) session.getAttribute("memberInfo");
	    if (memberVO == null) return new ModelAndView("redirect:/member/login.do");

	    if (goodsId == null || goodsQty == null || goodsId.length == 0 || goodsId.length != goodsQty.length) {
	        ModelAndView err = new ModelAndView("/common/layout");
	        err.addObject("viewName", "/cart/myCartList");
	        err.addObject("message", "주문 데이터가 올바르지 않습니다.");
	        return err;
	    }

	    @SuppressWarnings("unchecked")
	    List<CartVO> cartList = (List<CartVO>) session.getAttribute("cartList");
	    if (cartList == null) {
	        // 세션 만료/새탭 대비: DB에서 다시 로드
	        String currentId = (String) session.getAttribute("current_id");
	        cartList = cartService.placeOrder(currentId);
	        session.setAttribute("cartList", cartList);
	    }

	    // g_id → CartVO 맵
	    Map<Integer, CartVO> cartByGid = new HashMap<>();
	    for (CartVO c : cartList) cartByGid.put(c.getG_id(), c);

	    List<OrderItemVO> myOrderList = new ArrayList<>();
	    for (int i = 0; i < goodsId.length; i++) {
	        int gId = Integer.parseInt(goodsId[i]);
	        int qty = Integer.parseInt(goodsQty[i]);

	        CartVO c = cartByGid.get(gId);
	        if (c == null) continue; // 싱크 깨진 항목 스킵(원하면 에러 처리)

	        OrderItemVO item = new OrderItemVO();
	        item.setONum(generateOrderNum());
	        item.setOtGId(c.getG_id());
	        item.setOtGoodsName(c.getG_name());
	        item.setOtGoodsPrice(c.getG_price());
	        item.setOtGoodsQty(qty);	
	        // 필요하면 배송비/할인도 붙임
	        myOrderList.add(item);
	    }


	    if (myOrderList.isEmpty()) {
	        ModelAndView err = new ModelAndView("/common/layout");
	        err.addObject("body", "/WEB-INF/views/cart/myCartList.jsp");
	        err.addObject("errorMessage", "주문할 상품이 없습니다.");
	        return err;
	    }

	    session.setAttribute("myOrderList", myOrderList);
	    session.setAttribute("memberInfo", memberVO);

	    ModelAndView mav = new ModelAndView("/common/layout");
	    mav.addObject("viewName", "/order/orderForm");
	    return mav;
	}
	

}
