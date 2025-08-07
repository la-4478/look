<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>프로모션 상세</title>
    <!-- ✅ Bootstrap 5 -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</head>
<body class="bg-light">

<div class="container py-5">
    <!-- 🔹 프로모션 제목 -->
    <h2 class="fw-bold mb-4">${promo.title}</h2>

    <div class="card mb-5 shadow-sm">
        <!-- 🔹 이미지 or 대체 표시 -->
        <c:choose>
            <c:when test="${not empty promo.imageUrl}">
                <img src="/upload/${promo.imageUrl}" class="card-img-top" alt="프로모션 이미지">
            </c:when>
            <c:otherwise>
                <div class="card-img-top d-flex justify-content-center align-items-center text-white bg-secondary" style="height: 300px;">
                    이미지 없음
                </div>
            </c:otherwise>
        </c:choose>

        <!-- 🔹 상세 내용 -->
        <div class="card-body">
            <p class="text-muted mb-2">기간: ${promo.startDate} ~ ${promo.endDate}</p>
            <hr>
            <p style="white-space: pre-line;">${promo.description}</p>
        </div>
    </div>

    <!-- 🔙 목록으로 돌아가기 버튼 -->
    <a href="promotionList.jsp" class="btn btn-secondary">← 목록으로 돌아가기</a>
</div>

</body>
</html>
