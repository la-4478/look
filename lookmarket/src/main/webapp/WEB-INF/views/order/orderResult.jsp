<%@ page language="java" pageEncoding="UTF-8" isELIgnored="false"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />

<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8" />
<title>주문 결과</title>
<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<link rel="stylesheet" href="${contextPath}/resources/css/cartorder.css"/>
</head>
<body class="c">

	<div class="order-result">
		<h2>주문 결과</h2>
		<table>
			<tr>
				<th>주문자 이름</th>
				<td><c:out value="${orderInfo.oiName}" /></td>
			</tr>
			<tr>
				<th>수령자 이름</th>
				<td><c:out value="${orderInfo.oiReceiverName}" /></td>
			</tr>
			<tr>
				<th>수령자 연락처</th>
				<td><c:out value="${orderInfo.oiReceiverPhone}" /></td>
			</tr>
			<tr>
				<th>배송 주소</th>
				<td><c:out value="${orderInfo.oiDeliveryAddress}" /></td>
			</tr>
			<tr>
				<th>상세주소</th>
				<td><c:out value="${orderInfo.oi_deli_namuji_address}"/></td>
			</tr>
			<tr>
				<th>배송 메시지</th>
				<td><c:out value="${orderInfo.oiDeliveryMessage}" /></td>
			</tr>
			<tr>
				<th>배송비</th>
				<td><c:out value="${orderInfo.oiDeliveryPrice}" /> 원</td>
			</tr>
			<tr>
				<th>상품 총액</th>
				<td><c:out value="${orderInfo.oiTotalGoodsPrice}" /> 원</td>
			</tr>
			<tr>
				<th>할인 금액</th>
				<td><c:out value="${orderInfo.oiSalePrice}" /> 원</td>
			</tr>
		</table>
		<h3>주문 상품</h3>
		<table>
			<tr>
				<th>상품명</th>
				<th>수량</th>
				<th>가격</th>
			</tr>
			<c:forEach var="item" items="${itemVO}">
				<tr>
					<td><c:out value="${item.otGoodsName}" /></td>
					<td><c:out value="${item.otGoodsQty}" /></td>
					<td><fmt:formatNumber value="${item.otGoodsPrice}"
							pattern="#,###" /> 원</td>
				</tr>
			</c:forEach>

		</table>
	</div>
	<div style="text-align: center; margin-top: 20px;">
		<a href="${contextPath}/main/jangbogoMain.do"
			style="padding: 10px 20px; background: #007bff; color: #fff; border-radius: 4px; text-decoration: none;">쇼핑
			계속하기</a>
	</div>


</body>
</html>
