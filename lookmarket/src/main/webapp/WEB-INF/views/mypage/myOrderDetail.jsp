<%@ page language="java" pageEncoding="UTF-8" isELIgnored="false"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />

<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8"/>
<title>주문 상세정보</title>
<link rel="stylesheet" href="${contextPath}/resources/css/mypage.css"/>
</head>
<body>

<div class="order-result">
    <h2>주문 상세정보</h2>

    <!-- 주문 정보 카드 -->
    <div class="order-info-card">
        <div>주문번호: <c:out value="${order.oId}" /></div>
        <div>주문자 이름: <c:out value="${order.oiName}" /></div>
        <div>수령자 이름: <c:out value="${order.oiReceiverName}" /></div>
        <div>수령자 연락처: <c:out value="${order.oiReceiverPhone}" /></div>
        <div>배송 주소: <c:out value="${order.oiDeliveryAddress}" /></div>
        <div>상세 주소: <c:out value="${order.oi_deli_namuji_address}" /></div>
        <div>배송 메시지: <c:out value="${order.oiDeliveryMessage}" /></div>
        <div>배송비: <fmt:formatNumber value="${order.oiDeliveryPrice}" pattern="#,###"/> 원</div>
        <div>상품 총액: <fmt:formatNumber value="${order.oiTotalGoodsPrice}" pattern="#,###"/> 원</div>
        <div>할인 금액: <fmt:formatNumber value="${order.oiSalePrice != null ? order.oiSalePrice : 0}" pattern="#,###"/> 원</div>
        <div>주문일자: <c:out value="${order.oiDate}" /></div>
    </div>

    <!-- 주문 상품 목록 카드 -->
    <h3>주문 상품</h3>
    <c:forEach var="item" items="${orderItems}">
        <div class="order-item-card">
            <div><span>상품명:</span> <span><c:out value="${item.otGoodsName}" /></span></div>
            <div><span>수량:</span> <span><c:out value="${item.otGoodsQty}" />개</span></div>
            <div><span>가격:</span> <span><fmt:formatNumber value="${item.otGoodsPrice}" pattern="#,###"/> 원</span></div>
            <div><span>합계:</span> <span><fmt:formatNumber value="${item.otGoodsPrice * item.otGoodsQty}" pattern="#,###"/> 원</span></div>
        </div>
    </c:forEach>

    <div style="text-align: center; margin-top: 20px;">
        <a href="${contextPath}/mypage/listMyOrderHistory.do" class="back-btn">주문내역으로 돌아가기</a>
    </div>
</div>


</body>
</html>
