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

<h2>배송조회</h2>

<c:choose>
    <c:when test="${not empty deliveryList}">
        <c:forEach var="delivery" items="${deliveryList}">
            <table border="1" cellspacing="0" cellpadding="5" style="margin-bottom:20px; width:100%;">
                <tr>
                    <th>주문번호</th>
                    <td>${delivery.oId}</td>
                </tr>
                <tr>
                    <th>배송상태</th>
                    <td>
                        <c:choose>
                            <c:when test="${delivery.dStatus == 1}">배송준비중</c:when>
                            <c:when test="${delivery.dStatus == 2}">배송중</c:when>
                            <c:when test="${delivery.dStatus == 3}">배송완료</c:when>
                            <c:when test="${delivery.dStatus == 4}">주문취소</c:when>
                            <c:otherwise>알수없음</c:otherwise>
                        </c:choose>
                    </td>
                </tr>
                <tr>
                    <th>배송업체 / 운송장</th>
                    <td>${delivery.dCompany} / ${delivery.dTransportNum}</td>
                </tr>
                <tr>
                    <th>배송 시작일</th>
                    <td>${delivery.dShippedDate}</td>
                </tr>
                <tr>
                    <th>배송 완료일</th>
                    <td>${delivery.dDeliveryDate}</td>
                </tr>
            </table>
        </c:forEach>
    </c:when>
    <c:otherwise>
        <p>조회할 배송 내역이 없습니다.</p>
    </c:otherwise>
</c:choose>

</body>
</html>
