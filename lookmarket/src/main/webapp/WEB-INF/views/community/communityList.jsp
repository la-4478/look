<%@ page language="java" pageEncoding="UTF-8" isELIgnored="false" %>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<c:set var="contextPath"  value="${pageContext.request.contextPath}" />

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8" />
    <title>커뮤니티 리뷰 목록</title>
    <style>
        body {
            font-family: '맑은 고딕', sans-serif;
            margin: 20px;
            background-color: #f9f9f9;
        }
        .news-list {
            display: flex;
            flex-wrap: wrap;
            gap: 20px;
        }
        .news-item {
            background-color: #fff;
            width: 260px;
            border: 1px solid #ddd;
            border-radius: 5px;
            box-shadow: 0 0 5px #ccc;
            overflow: hidden;
            display: flex;
            flex-direction: column;
        }
        .news-image {
            width: 100%;
            height: 150px;
            object-fit: cover;
            background-color: #3366cc;
            color: white;
            display: flex;
            align-items: center;
            justify-content: center;
            font-size: 16px;
        }
        .news-content {
            padding: 15px;
            flex-grow: 1;
            display: flex;
            flex-direction: column;
            justify-content: space-between;
        }
        .news-title {
            font-size: 16px;
            font-weight: bold;
            margin-bottom: 10px;
            height: 48px; /* 약 3줄 */
            overflow: hidden;
            line-height: 1.4em;
        }
        .news-date {
            font-size: 12px;
            color: #666;
            margin-bottom: 10px;
        }
        .btn-view {
            background-color: #0077cc;
            color: white;
            text-align: center;
            padding: 8px 0;
            border: none;
            border-radius: 3px;
            cursor: pointer;
            text-decoration: none;
            display: block;
        }
        .btn-view:hover {
            background-color: #005fa3;
        }
        /* 별점 금색 */
        .star-rating {
            color: gold;
            font-size: 18px;
            margin-bottom: 8px;
        }
    </style>
</head>
<body>

<h2>커뮤니티 리뷰 목록</h2>

<div class="news-list">
    <c:forEach var="review" items="${communityList}">
        <div class="news-item">
            <a href="${contextPath}/community/communityDetail.do?r_id=${review.r_id}" style="text-decoration:none; color: inherit;">
                <c:choose>
                    <c:when test="${not empty review.r_filename}">
                        <img src="/upload/${review.r_filename}" alt="리뷰 이미지" class="news-image" />
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
