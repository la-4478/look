<%@ page language="java" pageEncoding="UTF-8" isELIgnored="false"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>회원들의 주문내역</title>
<link rel="stylesheet" href="${contextPath}/resources/css/mypage.css"/>
</head>
<body>

<div class="order-history">
    <h2>나의 주문내역</h2>

    <c:choose>
        <c:when test="${not empty orderList}">
            <c:forEach var="order" items="${orderList}">
                <div class="order-card">
                    <div class="order-info">
                        <div>주문번호: <c:out value="${order.oId}" /></div>
                        <div>주문일자: <c:out value="${order.oiDate}" /></div>
                        <div>주문자: <c:out value="${order.oiName}" /></div>
                        <div>총 결제금액: 
                            <fmt:formatNumber value="${order.oiTotalGoodsPrice + order.oiDeliveryPrice - (order.oiSalePrice != null ? order.oiSalePrice : 0)}" pattern="#,###"/> 원
                        </div>
                    </div>
                    <div class="order-actions">
                        <a href="${contextPath}/mypage/myOrderDetail.do?oId=${order.oId}">상세보기</a>
                    </div>
                </div>
            </c:forEach>
        </c:when>
        <c:otherwise>
            <div class="no-orders">주문 내역이 없습니다.</div>
        </c:otherwise>
    </c:choose>
</div>