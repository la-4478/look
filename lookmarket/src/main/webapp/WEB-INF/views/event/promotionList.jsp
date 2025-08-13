<%@ page language="java" pageEncoding="UTF-8" isELIgnored="false"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>프로모션 목록</title>
    <link href="${contextPath}/resources/css/event.css" rel="stylesheet" type="text/css">
</head>
<body class="bg-light">

<div class="container py-5">
    <h2 class="mb-4 fw-bold">이벤트 프로모션</h2>

    <div class="promotion-container">
        <c:forEach var="promo" items="${promotionList}">
            <div class="promotion-card">
                <c:choose>
                    <c:when test="${not empty promo.promoBannerImg}">
                        <img src="${contextPath}/upload/${promo.promoBannerImg}" alt="프로모션 이미지" />
                    </c:when>
                    <c:otherwise>
                        <div class="promotion-no-image">이미지 없음</div>
                    </c:otherwise>
                </c:choose>
                <div class="promotion-content">
                    <h5 class="promotion-title">${promo.promoTitle}</h5>
                    <p class="promotion-dates">
                        <fmt:formatDate value="${promo.promoStartDate}" pattern="yyyy-MM-dd" /> ~
                        <fmt:formatDate value="${promo.promoEndDate}" pattern="yyyy-MM-dd" />
                    </p>
                    <a href="${contextPath}/event/promotionDetail.do?postId=${promo.postId}" class="promotion-btn">자세히 보기</a>
                </div>
            </div>
        </c:forEach>
    </div>
</div>
</body>
</html>
