<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>오늘의 추천시장</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body class="bg-light">

<div class="container py-5">
    <h2 class="fw-bold mb-4">오늘의 추천시장</h2>

    <!-- 👇 3개씩 카드 정렬 -->
    <div class="row">
        <c:forEach var="region" items="${regionList}">
            <div class="col-md-4 mb-4">
                <div class="card h-100 shadow-sm">
                    <div class="card-body">
                        <div class="d-flex justify-content-between align-items-center mb-3">
                            <h5 class="card-title mb-0">${region.name}</h5>
                            <div class="text-end">
                                <img src="${region.weatherIcon}" alt="날씨" width="32" height="32">
                                <div class="small text-muted">${region.weather}</div>
                            </div>
                        </div>

                        <ul class="list-group list-group-flush">
                            <c:forEach var="market" items="${region.recommendedMarkets}" varStatus="status">
                                <c:if test="${status.index < 3}"> <!-- 여기 조건이 3개까지만 보여주도록 제한 한거임 -->
                                    <li class="list-group-item d-flex justify-content-between align-items-center">
                                        ${market.name}
                                        <a href="marketDetail.jsp?id=${market.id}" class="btn btn-sm btn-outline-primary">보기</a>
                                    </li>
                                </c:if>
                            </c:forEach>
                        </ul>
                    </div>
                </div>
            </div>
        </c:forEach>
    </div>
</div>

</body>
</html>
