<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />

<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8" />
  <title>장보고 메인</title>
  <link rel="stylesheet" href="${contextPath}/resources/css/main2.css"/>
</head>
<body>

<div class="container mt-4">
  <!-- 1. 슬라이드 배너 -->
  <div id="mainCarousel" class="carousel slide mb-4" data-bs-ride="carousel">
    <div class="carousel-inner">
      <div class="carousel-item active">
      <a href="${contextPath }/event/promotionDetail.do?postId=1">
        <img src="${contextPath}/resources/image/jangbogo_banner2.png" class="d-block w-100" alt="..." />
      </a>
      </div>
      <div class="carousel-item">
        <img src="${contextPath}/resources/image/테스트이미지2.PNG" class="d-block w-100" alt="..." />
      </div>
      <div class="carousel-item">
        <img src="${contextPath}/resources/image/shrimp.jpg" class="d-block w-100" alt="..." />
      </div>
    </div>
    <button class="carousel-control-prev" type="button" data-bs-target="#mainCarousel" data-bs-slide="prev">
      <span class="carousel-control-prev-icon"></span>
    </button>
    <button class="carousel-control-next" type="button" data-bs-target="#mainCarousel" data-bs-slide="next">
      <span class="carousel-control-next-icon"></span>
    </button>
  </div>

  <!-- 2. 원형 퀵메뉴 -->
  <div class="row quick-menu mb-5">
    <div class="col"><a href="${contextPath}/jangbogo/goodsList.do?category=fresh"><img src="${contextPath}/resources/image/신선식품.png" /></a></div>
    <div class="col"><a href="${contextPath}/jangbogo/goodsList.do?category=processed"><img src="${contextPath}/resources/image/가공식품.png" /></a></div>
    <div class="col"><a href="${contextPath}/jangbogo/goodsList.do?category=living"><img src="${contextPath}/resources/image/생활용품.png" /></a></div>
    <div class="col"><a href="${contextPath}/jangbogo/goodsList.do?category=fashion"><img src="${contextPath}/resources/image/패션잡화.png" /></a></div>
    <div class="col"><a href="${contextPath}/jangbogo/goodsList.do?category=local"><img src="${contextPath}/resources/image/지역특산물.png" /></a></div>
  </div>
</div>
</body>
</html>
