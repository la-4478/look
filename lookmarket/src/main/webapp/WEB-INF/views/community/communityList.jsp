<%@ page language="java" pageEncoding="UTF-8" isELIgnored="false" %>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<c:set var="contextPath"  value="${pageContext.request.contextPath}" />

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8" />
    <title>커뮤니티 리뷰 목록</title>
    <link href="${contextPath}/resources/css/community.css" rel="stylesheet" type="text/css">
</head>
<body>

<h2>커뮤니티 리뷰 목록</h2>
    <c:if test="${not empty message}">
        <script>
            alert("${message}");
        </script>
    </c:if>

<div class="news-list">
    <c:forEach var="review" items="${communityList}">
        <div class="news-item">
            <a href="${contextPath}/community/communityDetail.do?r_id=${review.r_id}" style="text-decoration:none; color: inherit;">
                <c:choose>
                    <c:when test="${not empty review.r_filename}">
                        <img src="${contextPath}/resources/image/${review.r_filename}" alt="리뷰 이미지" class="news-image" />
                    </c:when>
                    <c:otherwise>
                        <div class="news-image">이미지 없음</div>
                    </c:otherwise>
                </c:choose>
                <div class="news-content">
                    <div class="star-rating">
                        <c:forEach var="i" begin="1" end="${review.r_star}">
                            ★
                        </c:forEach>
                    </div>
                    <div class="news-title">${review.r_title}</div>
                    <div class="news-date">${review.r_date}</div>
                    <div class="btn-view">상세보기</div>
                </div>
            </a>
        </div>
    </c:forEach>
</div>

</body>
</html>
