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
