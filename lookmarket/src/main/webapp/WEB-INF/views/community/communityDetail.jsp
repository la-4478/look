<%@ page language="java" pageEncoding="UTF-8" isELIgnored="false" %>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<c:set var="contextPath"  value="${pageContext.request.contextPath}" />

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>리뷰 상세보기</title>
    <style>
        .detail-container {
            width: 800px;
            margin: 50px auto;
            padding: 30px;
            border: 1px solid #ccc;
            border-radius: 10px;
            background-color: #fefefe;
            
        }

        .detail-title {
            font-size: 24px;
            font-weight: bold;
            margin-bottom: 10px;
        }

        .meta-info {
            font-size: 14px;
            color: #666;
            margin-bottom: 15px;
        }

        .star-display {
            color: gold;
            font-size: 20px;
            margin-bottom: 15px;
        }

        .review-image {
            max-width: 100%;
            height: auto;
            margin-bottom: 20px;
        }

        .review-content {
            font-size: 16px;
            line-height: 1.6;
        }

        .back-btn {
            margin-top: 30px;
            text-align: center;
        }

        .back-btn a {
            padding: 8px 16px;
            background-color: #3366cc;
            color: white;
            text-decoration: none;
            border-radius: 5px;
        }

        .back-btn a:hover {
            background-color: #254a9a;
        }
    </style>
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
    </div>
</div>
</body>
</html>
