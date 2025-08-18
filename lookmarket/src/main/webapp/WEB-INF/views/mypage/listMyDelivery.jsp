<%@ page language="java" pageEncoding="UTF-8" isELIgnored="false"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />

<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8"/>
<title>배송조회</title>
<link rel="stylesheet" href="${contextPath}/resources/css/mypage.css"/>
</head>
<body>

<div class="delivery-list">
    <h2>나의 배송조회</h2>

    <c:choose>
        <c:when test="${not empty listMyDelivery}">
            <c:forEach var="delivery" items="${listMyDelivery}">
                <div class="delivery-item-card">
                    <div>주문번호: ${delivery.o_id}</div>
                    <div>배송상태: 
                        <c:choose>
                            <c:when test="${delivery.d_status == 1}">배송준비중</c:when>
                            <c:when test="${delivery.d_status == 2}">배송중</c:when>
                            <c:when test="${delivery.d_status == 3}">배송완료</c:when>
                            <c:when test="${delivery.d_status == 4}">주문취소</c:when>
                            <c:otherwise>알 수 없음</c:otherwise>
                        </c:choose>
                    </div>
                    <div>배송업체 / 운송장: ${delivery.d_company} / ${delivery.d_transport_num}</div>
                    <div>배송시작일: ${delivery.d_shipped_date}, 배송완료일: ${delivery.d_delivery_date}</div>
                </div>
            </c:forEach>
        </c:when>
        <c:otherwise>
            <div class="no-delivery">조회할 배송 내역이 없습니다.</div>
        </c:otherwise>
    </c:choose>
</div>

</body>
</html>
