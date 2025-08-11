<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>이벤트 등록</title>
    <link href="${contextPath}/resources/css/event.css" rel="stylesheet" type="text/css">
</head>
<body>
<div class="form-container">
    <h2>이벤트 등록</h2>
    <form action="insertReview.do" method="post" enctype="multipart/form-data">
        <!-- 리뷰 제목 -->
        <div class="form-group">
            <label for="r_title">이벤트 제목</label>
            <input type="text" id="r_title" name="r_title" required />
        </div>

        <!-- 내용 -->
        <div class="form-group">
            <label for="r_content">내용</label>
            <textarea id="r_content" name="r_content" rows="5" required></textarea>
        </div>

        <!-- 이미지 업로드 -->
        <div class="form-group">
            <label for="r_filename">이미지 업로드</label>
            <input type="file" id="r_filename" name="r_filename" accept="image/*" />
        </div>

        <div class="submit-btn">
            <button type="submit">등록하기</button>
        </div>
    </form>
</div>
</body>
</html>
