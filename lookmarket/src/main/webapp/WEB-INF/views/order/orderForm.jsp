<%@ page language="java" pageEncoding="UTF-8" isELIgnored="false"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />

<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8" />
<title>결제하기</title>

<style>
body {
	margin: 0;
	padding-top: 80px;
	background: #f9f9f9;
	font-family: Arial, sans-serif;
}

.container-box {
	max-width: 1100px;
	margin: auto;
	display: grid;
	grid-template-columns: 2fr 1.2fr;
	gap: 20px;
}

.box {
	margin: 10px;
	background: white;
	border: 1px solid #ddd;
	padding: 20px;
	border-radius: 6px;
}

h2 {
	font-size: 22px;
	margin-bottom: 20px;
	font-weight: bold;
}

label {
	margin-top: 10px;
	font-weight: bold;
	display: block;
}

input, textarea, select {
	width: 100%;
	padding: 8px;
	margin-top: 5px;
	border: 1px solid #ccc;
	border-radius: 4px;
	box-sizing: border-box;
}

.submit-btn {
	width: 100%;
	background: #007bff;
	color: white;
	padding: 12px;
	font-size: 16px;
	border: none;
	border-radius: 6px;
	margin-top: 20px;
	cursor: pointer;
}

.submit-btn:hover {
	background: #0056b3;
}

.radio-group {
	margin-top: 10px;
}

.radio-group label {
	margin-right: 15px;
	font-weight: normal;
	cursor: pointer;
}

.summary-table {
	width: 100%;
	font-size: 15px;
	margin-bottom: 15px;
	border-collapse: collapse;
}

.summary-table td {
	padding: 6px 0;
}

.summary-table .total {
	font-weight: bold;
	font-size: 18px;
	color: #007bff;
}

.payment-container {
	max-width: 400px;
	margin: 0 auto;
	padding: 15px;
	font-family: "Noto Sans KR", sans-serif;
	font-size: 14px;
	color: #444;
}

.payment-methods label {
	display: flex;
	align-items: center;
	margin-bottom: 10px;
	cursor: pointer;
}

.payment-methods input[type="radio"] {
	margin-right: 10px;
	flex-shrink: 0;
}

.installment {
	margin-bottom: 15px;
}

.installment label {
	display: block;
	margin-bottom: 5px;
	font-weight: 600;
}

.installment select {
	width: 100%;
	padding: 6px 8px;
	border: 1px solid #ccc;
	border-radius: 4px;
}

.agreement label {
	display: flex;
	align-items: center;
	cursor: pointer;
	margin-bottom: 20px;
}

.agreement input[type="checkbox"] {
	margin-right: 8px;
	flex-shrink: 0;
}

.pay-btn {
	width: 100%;
	padding: 12px 0;
	background-color: #007bff;
	color: white;
	font-size: 16px;
	border: none;
	border-radius: 6px;
	cursor: pointer;
	transition: background-color 0.3s;
}

.pay-btn:hover {
	background-color: #0056b3;
}
</style>

<script
	src="https://t1.daumcdn.net/mapjsapi/bundle/postcode/prod/postcode.v2.js"></script>

<script>
	function execDaumPostcode() {
		new daum.Postcode(
				{
					oncomplete : function(data) {
						// 선택한 주소 정보를 각 input에 넣어줌
						document.getElementById('zipcode').value = data.zonecode; // 우편번호
						document.getElementById('oi_delivery_address').value = data.address; // 주소
						// 필요하면 상세주소 input 초기화 또는 focus 처리
						document.getElementById('oi_delivery_detail').value = '';
						document.getElementById('oi_delivery_detail').focus();
					}
				}).open();
	}
	
	async function requestCardPayment() {
		console.log("함수 진입");
	  const f = document.forms['form_order'];
	  const orderName = "장보고 주문결제";
	  const price = f['total_price'] ? f['total_price'].value : 1000;
	  const or_idx = Number(f?.order_id?.value) > 0 ? f.order_id.value : new Date().getTime();
	  console.log("or_idx:", or_idx);
	  const ctx = "${pageContext.request.contextPath}";

	  // 휴대폰 번호 3개 입력값 합치기
	  const tel1 = f['tel1']?.value.trim();
	  const tel2 = f['tel2']?.value.trim();
	  const tel3 = f['tel3']?.value.trim();
	  const phoneRaw = [tel1, tel2, tel3].join('');
	  // 휴대폰번호 필수 체크 (11자리)
	  if (!phoneRaw || phoneRaw.length !== 11 || !/^\d{11}$/.test(phoneRaw)) {
	    alert("휴대폰 번호를 정확히 입력해 주세요! (예: 010-1234-5678)");
	    return;
	  }
	  console.log("phoneRaw = [" + phoneRaw + "]");
	  // 주소 필수값(도로명 or 지번)
	  const roadAddress = f['roadAddress']?.value.trim();
	  const jibunAddress = f['jibunAddress']?.value.trim();
	  if (!roadAddress && !jibunAddress) {
	    alert("주소를 입력해 주세요!");
	    return;
	  }

	  const data = {
	    or_idx: or_idx,
	    pd_name: orderName,
	    price: price,
	    receiver_name: f['receiver_name']?.value,
	    tel1: tel1,
	    tel2: tel2,
	    tel3: tel3,
	    goods_num: f['goods_num']?.value,
	    goods_name: f['goods_name']?.value,
	    goods_sales_price: f['goods_sales_price']?.value,
	    order_name: f['order_name']?.value,
	    order_num : f['order_num']?.value,
	    zipcode: f['zipcode']?.value,
	    roadAddress: roadAddress,
	    jibunAddress: jibunAddress,
	    namujiAddress: f['namujiAddress']?.value,
	    delivery_message: f['delivery_message']?.value,
	    delivery_method: f['delivery_method']?.value,
	    pay_method: f['pay_method']?.value,
	    card_com_name: f['card_com_name']?.value,
	    card_pay_month: f['card_pay_month']?.value,
	    pay_order_tel: f['pay_order_tel']?.value
	  };
	  const paymentId = `PAYMENT_${Date.now()}_${Math.floor(Math.random() * 1000000)}`;

	  console.log({
		  name: data.receiver_name,
		  phone: phoneRaw,
		  email: "${sessionScope.memberInfo.email1}@${sessionScope.memberInfo.email2}",
		  
		  address: {
		    addressLine1: roadAddress || jibunAddress,
		    addressLine2: data.namujiAddress,
		    postalCode: data.zipcode
		  }
		});
	  // 결제창 호출 (storeId, channelKey는 네 실제값으로 교체!!)
	  const response = await PortOne.requestPayment({
	    storeId:"store-292f1f91-b8c2-4608-9394-615315d5f811",   // ★교체필수
	    channelKey: "channel-key-16983525-2a28-41f4-b177-b4f8e27769dc", // ★교체필수
	    paymentId: paymentId,
	    orderName: data.pd_name,
	    totalAmount: data.price,
	    currency: "CURRENCY_KRW",
	    payMethod: "CARD",
	    customer: {	
	      fullName: data.receiver_name,
	      phoneNumber: String(phoneRaw), // 11자리 숫자!
	      email: "${sessionScope.memberInfo.email1}@${sessionScope.memberInfo.email2}",
	      address: {
	        addressLine1: roadAddress || jibunAddress,  // 필수(도로명/지번 둘 중 하나라도)
	        addressLine2: data.namujiAddress,           // 상세주소(없으면 빈값)
	        postalCode: data.zipcode                    // 우편번호(없으면 빈값)
	      }
	    }
	    // 추가 필드는 PortOne 공식문서 참고
	  });
	  console.log("💳 [PortOne 결제 응답 전체]", response);
	  alert("[PortOne 결제 응답 전체]\n" + JSON.stringify(response, null, 2));
	  // 결제 실패
	  if (response.code != null) {
	    alert(response.message);
	    return;
	  }
	  // 결제 식별자 추출 (paymentKey, imp_uid, txId 중 실제로 오는 값!)
	  const paymentKey = response.paymentKey || response.imp_uid || response.id || response.txId;
	  const txId = response.txId;
	  if (!paymentKey && !txId) {
		  alert("결제는 되었지만 paymentKey를 받지 못했습니다. 관리자에게 문의하세요.");
		  console.error("📛 결제 응답 이상:", response);
		  return;
		}
	  
	//어떤 식별자인지(프론트에서 서버로 함께 전달)
	  let paymentKeyType = "unknown";
	  if (response.paymentKey) paymentKeyType = "paymentKey";
	  else if (response.imp_uid) paymentKeyType = "imp_uid";
	  else if (response.id) paymentKeyType = "id";
	  else if (response.txId) paymentKeyType = "txId";
	  
	  // 결제 성공시 서버로 주문/결제 내역 전달
	  try {
	  const res = await fetch("/petmillie/order/payToOrderGoods.do", {
	    method: "POST",
	    headers: { "Content-Type": "application/json" },
	    body: JSON.stringify({
	      ...data,
	      paymentId: paymentId,
	      portone_paymentKey: paymentKey,
	      paymentStatus: response.status
	    })
	  });
	   const text = await res.text();
	  try {
	    const result = JSON.parse(text);
	    alert(result.message || "주문이 완료되었습니다!");
	    if (result.success) {
	      window.location.href = `${ctx}/order/payComplete.do`;
	    }
	  } catch (e) {
	    console.error("❌ JSON 파싱 실패! 응답 텍스트:", text);
	    alert("서버에서 이상한 응답이 왔어요. 관리자에게 문의해주세요.");
	  }

	} catch (e) {
	  console.error("❌ fetch 요청 실패:", e);
	  alert("서버와의 통신 중 오류 발생! 결제는 되었을 수 있으니 꼭 확인 부탁드립니다!");
	}
	}
</script>

</head>
<body>

	<div class="container-box">
		<form action="${contextPath}/order/orderResult.do" method="post">
			<!-- 주문/배송 정보 -->
			<div class="box">
				<h2>주문 정보 입력</h2>

				<label for="oi_name">주문자 이름</label> <input type="text" id="oi_name"
					name="oi_name" placeholder="주문자 이름" required> <label
					for="oi_receiver_name">수령자 이름</label> <input type="text"
					id="oi_receiver_name" name="oi_receiver_name" placeholder="수령자 이름"
					required> <label for="oi_receiver_phone">수령자 연락처</label> <input
					type="tel" id="oi_receiver_phone" name="oi_receiver_phone"
					placeholder="01012345678" pattern="[0-9]{10,11}" required>

				<label for="zipcode">우편번호</label>
				<div style="display: flex; gap: 10px;">
					<input type="text" id="zipcode" name="zipcode" placeholder="우편번호"
						required>
					<button type="button" onclick="execDaumPostcode()"
						style="width: 145px; cursor: pointer;">주소찾기</button>
				</div>

				<label for="oi_delivery_address">배송주소</label> <input type="text"
					id="oi_delivery_address" name="oi_delivery_address"
					placeholder="주소 입력" required> <label
					for="oi_delivery_message">배송메시지</label>
				<textarea id="oi_delivery_message" name="oi_delivery_message"
					rows="3" placeholder="배송 요청사항을 입력하세요."></textarea>
			</div>

			<!-- 주문상품 리스트 출력 -->
			<div class="box">
				<h2>주문 상품 목록</h2>
				<table style="width: 100%; border-collapse: collapse;">
					<thead>
						<tr>
							<th
								style="border-bottom: 1px solid #ccc; padding: 8px; text-align: left;">상품명</th>
							<th
								style="border-bottom: 1px solid #ccc; padding: 8px; text-align: right;">가격</th>
							<th
								style="border-bottom: 1px solid #ccc; padding: 8px; text-align: right;">수량</th>
							<th
								style="border-bottom: 1px solid #ccc; padding: 8px; text-align: right;">합계</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach var="item" items="${orderProductList}">
							<tr>
								<td style="padding: 8px;">${item.otGoodsName}</td>
								<td style="padding: 8px; text-align: right;"><fmt:formatNumber
										value="${item.otGoodsPrice}" pattern="#,###" /> 원</td>
								<td style="padding: 8px; text-align: right;">${item.otGoodsQty}</td>
								<td style="padding: 8px; text-align: right;"><fmt:formatNumber
										value="${item.otGoodsPrice * item.otGoodsQty}" pattern="#,###" />
									원</td>
							</tr>

							<!-- 상품별 주문 정보를 숨김 필드로 같이 전달 -->
							<input type="hidden" name="goodsId" value="${item.otGId}" />
							<input type="hidden" name="goodsName" value="${item.otGoodsName}" />
							<input type="hidden" name="goodsPrice"
								value="${item.otGoodsPrice}" />
							<input type="hidden" name="goodsQty" value="${item.otGoodsQty}" />
						</c:forEach>
					</tbody>
				</table>
			</div>

			<!-- 결제 요약 + 결제 수단 -->
			<div class="box">
				<h2>결제 정보</h2>

				<table class="summary-table" role="presentation">
					<tr>
						<td>상품금액</td>
						<td align="right"><fmt:formatNumber
								value="${totalGoodsPrice}" pattern="#,###" />원</td>
					</tr>
					<tr>
						<td>쿠폰 할인</td>
						<td align="right">-<fmt:formatNumber
								value="${couponDiscount}" pattern="#,###" />원
						</td>
					</tr>
					<tr>
						<td>배송비</td>
						<td align="right">+<fmt:formatNumber value="${deliveryFee}"
								pattern="#,###" />원
						</td>
					</tr>
					<tr>
						<td colspan="2"><hr></td>
					</tr>
					<tr>
						<td>총 결제금액</td>
						<td align="right" class="total"><fmt:formatNumber
								value="${finalTotalPrice}" pattern="#,###" />원</td>
					</tr>
				</table>

				<label>결제 방법</label>
				<div class="radio-group">
					<label><input type="radio" name="payment_method"
						value="card" checked required> 신용카드</label> <label><input
						type="radio" name="payment_method" value="kakao" required>
						카카오페이</label> <label><input type="radio" name="payment_method"
						value="naver" required> 네이버페이</label> <label><input
						type="radio" name="payment_method" value="transfer" required>
						무통장입금</label>
				</div>

				<label for="installment">할부 선택</label> <select name="installment"
					id="installment">
					<option value="0">일시불</option>
					<option value="3">3개월</option>
					<option value="6">6개월</option>
				</select>

				<div style="margin-top: 10px;">
					<label><input type="checkbox" required> 전체 동의 및 결제
						진행</label>
				</div>

				<button type="submit" class="submit-btn">결제하기</button>
			</div>
		</form>
	</div>

</body>
</html>
