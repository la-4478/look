<%@ page language="java" pageEncoding="UTF-8" isELIgnored="false"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>회원들의 주문내역</title>
</head>
<body>

<div class="order-history">
    <h2>상품 주문내역</h2>
<table border="1" cellspacing="0" cellpadding="5">
    <thead>
        <tr>
            <th>주문번호</th>
            <th>품목</th>
            <th>품목개수</th>
            <th>금액</th>
            <th>할인금액</th>
            <th>구매자</th>
            <th>수령인</th>
            <th>배송 주소</th>
        </tr>
    </thead>
    <tbody>
        <c:choose>
            <c:when test="${not empty orderList}">
            	<c:forEach var="info" items="${orderList}">
                <c:forEach var="item" items="${orderItem}">
                <c:forEach var="delivery" items="${delivery}">
                    <tr>
                    	<td>${info.oId}</td>	
                    	<td>${item.otGoodsName}</td>
                    	<td>${item.otGoodsQty}</td>
                    	<td>${item.otGoodsPrice}</td>
                    	<td>${item.otSalePrice}</td>
                    	<td>${info.oiName}</td>
                    	<td>${info.oiReceiverName}</td>
                    	<td>${info.oiDeliveryAddress} - ${info.oi_deli_namuji_address}</td>
                    </tr>
                    </c:forEach>
                   </c:forEach>
                </c:forEach>
            </c:when>
            <c:otherwise>
                <tr>
                    <td colspan="8" class="no-orders">주문 내역이 없습니다.</td>
                </tr>
            </c:otherwise>
        </c:choose>
    </tbody>
</table>

</div>