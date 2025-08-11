<%@ page language="java" pageEncoding="UTF-8" isELIgnored="false" %>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<c:set var="contextPath"  value="${pageContext.request.contextPath}" />

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>리뷰 상세보기</title>
</head>

<body>
<div class="detail-container">
    <div class="detail-title">${review.r_title}</div>

    <div class="meta-info">
        작성자: ${review.m_id} | 작성일: ${review.r_date} | 조회수: ${review.r_hit}
    </div>

    <!-- 별점 표시 -->
    <div class="star-display">
        <c:forEach var="i" begin="1" end="5">
            <c:choose>
                <c:when test="${i <= review.r_star}">★</c:when>
                <c:otherwise>☆</c:otherwise>
            </c:choose>
        </c:forEach>
    </div>

    <!-- 이미지 출력 (있을 경우만) -->
    <c:if test="${not empty review.r_filename}">
        <img class="review-image" src="/upload/${review.r_filename}" alt="리뷰 이미지" />
    </c:if>

    <!-- 내용 -->
    <div class="review-content">
        ${review.r_content}
    </div>

	<div class="back-btn">
    	<a href="${contextPath}/community/communityList.do">← 목록으로</a>

    	<c:if test="${sessionScope.userId == review.m_id}">
        	<a href="${contextPath}/community/communityUpdateForm.do?r_id=${review.r_id}" style="margin-left: 10px;">수정하기</a>

        	<form action="${contextPath}/community/communityDelete.do" method="post" style="display:inline;">
           		<input type="hidden" name="r_id" value="${review.r_id}" />
            	<button type="submit" class="delete-btn" onclick="return confirm('정말 삭제하시겠습니까?')">삭제</button>
        	</form>
    	</c:if>
	</div>
	</div>
</body>
</html>
