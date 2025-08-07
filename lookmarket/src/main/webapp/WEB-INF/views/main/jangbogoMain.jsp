<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />

<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8" />
  <title>장보고 메인</title>
  <style>
    .quick-menu img {
      width: 200px;
      height: 200px;
      border-radius: 50%;
      object-fit: cover;
      margin-bottom: 8px;
    }

    .quick-menu .col {
      text-align: center;
      font-size: 14px;
    }

    .product-card img {
      height: 150px;
      object-fit: cover;
    }

    .product-card {
      border: 1px solid #eee;
      padding: 10px;
      border-radius: 8px;
    }

    .tab-button {
      margin-right: 10px;
    }
    .carousel-item img {
  	  height: 400px; /* 원하는 높이로 조절 */
      object-fit: cover; /* 이미지가 잘리지 않고 꽉 차도록 */
      width: 1200px; /* 가로는 꽉 채움 */
    }
    
  </style>
</head>
<body>

<div class="container mt-4">
  <!-- 1. 슬라이드 배너 -->
  <div id="mainCarousel" class="carousel slide mb-4" data-bs-ride="carousel">
    <div class="carousel-inner">
      <div class="carousel-item active">
        <img src="${contextPath}/resources/image/테스트이미지.PNG" class="d-block w-100" alt="..." />
      </div>
      <div class="carousel-item">
        <img src="${contextPath}/resources/image/테스트이미지2.PNG" class="d-block w-100" alt="..." />
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
    <div class="col"><img src="${contextPath}/resources/image/신선식품.png" /></div>
    <div class="col"><img src="${contextPath}/resources/image/가공식품.png" /></div>
    <div class="col"><img src="${contextPath}/resources/image/생활용품.png" /></div>
    <div class="col"><img src="${contextPath}/resources/image/패션잡화.png" /></div>
    <div class="col"><img src="${contextPath}/resources/image/지역특산물.png" /></div>
  </div>
  <!-- 3. 카테고리별 추천상품 -->
<h5 class="mt-5">카테고리별 추천상품</h5>

<c:forEach var="entry" items="${goodsMap}">
    <c:set var="category" value="${entry.key}" />
    <c:set var="goodsList" value="${entry.value}" />

    <h6 class="mt-4">
        <c:choose>
            <c:when test="${category == 1}">신선식품</c:when>
            <c:when test="${category == 2}">가공식품</c:when>
            <c:when test="${category == 3}">생활용품</c:when>
            <c:when test="${category == 4}">패션잡화</c:when>
            <c:when test="${category == 5}">지역특산물</c:when>
        </c:choose>
        <a href="${contextPath}/goodsList.do?category=${category}" class="btn btn-sm btn-outline-secondary float-end">더보기</a>
    </h6>

    <div class="row row-cols-2 row-cols-md-4 g-3 mb-4">
        <c:forEach var="goods" items="${goodsList}" begin="0" end="1"> <!-- 각 카테고리별 2개만 -->
            <div class="col">
                <div class="product-card">
                    <img src="${contextPath}/resources/images/${goods.g_image}" class="w-100" alt="${goods.g_name}" />
                    <div class="mt-2">${goods.g_name}</div>
                    <div class="text-danger fw-bold">
                        <fmt:formatNumber value="${goods.g_price}" type="currency" currencySymbol="₩"/>
                    </div>
                    <a href="${contextPath}/goodsDetail.do?g_id=${goods.g_id}" class="btn btn-primary btn-sm mt-2">상세보기</a>
                </div>
            </div>
        </c:forEach>
    </div>
</c:forEach>
	
  <!-- 4. 실시간 인기상품 -->
  <h5>실시간 인기상품</h5>
  <div class="row row-cols-2 row-cols-md-4 g-3 mb-5">
    <c:forEach var="item" items="${popularList}">
      <div class="col">
        <div class="product-card">
          <img src="${contextPath}/resources/image/${item.img}" class="w-100" />
          <div class="mt-2">${item.name}</div>
          <div class="text-danger fw-bold">${item.price}원</div>
        </div>
      </div>
    </c:forEach>
  </div>

  <!-- 5. 타임세일 -->
  <h5>타임세일 <span class="text-muted">| 매일 오전 10시</span></h5>
  <div class="mb-3">
    <button class="btn btn-outline-primary btn-sm tab-button">오늘</button>
    <button class="btn btn-outline-secondary btn-sm tab-button">수요일</button>
    <button class="btn btn-outline-secondary btn-sm tab-button">금요일</button>
    <!-- 필요시 요일 추가 -->
  </div>
  <div class="row row-cols-2 row-cols-md-4 g-3">
    <c:forEach var="item" items="${timeSaleList}">
      <div class="col">
        <div class="product-card">
          <img src="${contextPath}/resources/image/${item.img}" class="w-100" />
          <div class="mt-2">${item.name}</div>
          <div class="text-danger fw-bold">${item.salePrice}원</div>
        </div>
      </div>
    </c:forEach>
  </div>
</div>
</body>
</html>
