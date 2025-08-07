<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>프로모션 목록</title>
</head>
<body class="bg-light">

<div class="container py-5">
    <h2 class="mb-4 fw-bold">이벤트 프로모션</h2>

    <div class="row row-cols-1 row-cols-sm-2 row-cols-md-3 g-4">
        <c:forEach var="promo" items="${promotionList}">
            <div class="col">
                <div class="card h-100 border">
                    <c:choose>
                        <c:when test="${not empty promo.imageUrl}">
                            <img src="/upload/${promo.imageUrl}" class="card-img-top" alt="프로모션 이미지">
                        </c:when>
                        <c:otherwise>
                            <div class="card-img-top d-flex justify-content-center align-items-center text-white bg-secondary" style="height: 180px;">
                                이미지 없음
                            </div>
                        </c:otherwise>
                    </c:choose>
                    <div class="card-body d-flex flex-column">
                        <h5 class="card-title">${promo.title}</h5>
                        <p class="card-text text-muted">${promo.startDate} ~ ${promo.endDate}</p>
                        <a href="promotionDetail.jsp?id=${promo.id}" class="btn btn-primary mt-auto">자세히 보기</a>
                    </div>
                </div>
            </div>
        </c:forEach>
    </div>
</div>
</body>
</html>
