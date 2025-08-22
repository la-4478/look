<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>리뷰 등록</title>
    <link href="${contextPath}/resources/css/community.css" rel="stylesheet" type="text/css">
</head>
<body>
<div class="form-container">
    <h2>리뷰 등록</h2>
    <form action="${contextPath}/community/insertReview.do" method="post" enctype="multipart/form-data">
    	<p><strong>작성자:</strong> ${sessionScope.memberInfo.m_id}</p>
        <!-- 리뷰 제목 -->
        <div class="form-group">
            <label for="r_title">리뷰 제목</label>
            <input type="text" id="r_title" name="r_title" required />
        </div>

        <!-- 상품 ID -->
        <div class="form-group">
            <label for="g_name">상품 명</label>
            <input type="text" id="g_name" name="g_name" readonly />
        </div>

        <!-- 별점 ★ 클릭 방식 -->
        <div class="form-group">
            <label for="r_star">별점</label>
            <div class="star-rating">
                <input type="radio" id="star5" name="r_star" value="5"><label for="star5">★</label>
                <input type="radio" id="star4" name="r_star" value="4"><label for="star4">★</label>
                <input type="radio" id="star3" name="r_star" value="3"><label for="star3">★</label>
                <input type="radio" id="star2" name="r_star" value="2"><label for="star2">★</label>
                <input type="radio" id="star1" name="r_star" value="1"><label for="star1">★</label>
            </div>
        </div>

        <!-- 내용 -->
        <div class="form-group">
            <label for="r_content">내용</label>
            <textarea id="r_content" name="r_content" rows="5" required></textarea>
        </div>

        <!-- 이미지 업로드 -->
        <div class="form-group">
            <label for="r_filename">이미지 업로드</label>
            <input type="file" id="r_filename" name="r_filename" />
        </div>

        <!-- 공개 여부 -->
        <div class="form-group">
            <label for="r_secret">공개 여부</label>
            <select id="r_secret" name="r_secret">
                <option value="public">공개</option>
                <option value="private">비공개</option>
            </select>
        </div>

        <div class="submit-btn">
            <button type="submit">등록하기</button>
        </div>
    </form>
</div>
</body>
</html>
