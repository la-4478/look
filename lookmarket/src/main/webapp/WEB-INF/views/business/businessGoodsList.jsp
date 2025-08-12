<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<c:set var="cat" value="${empty param.category ? 'all' : param.category}" />

<link rel="stylesheet" href="${contextPath}/resources/css/busigoods.css"/>

<div class="wrap">
  <h1>카테고리별 상품</h1>

  <nav class="tabs" role="tablist" aria-label="상품 카테고리">
    <a class="tab ${cat == 'all' ? 'active' : ''}" href="${contextPath}/business/businessGoodsList.do?category=all">전체</a>
    <a class="tab ${cat == '1' ? 'active' : ''}" href="${contextPath}/business/businessGoodsList.do?category=1">신선식품</a>
    <a class="tab ${cat == '2' ? 'active' : ''}" href="${contextPath}/business/businessGoodsList.do?category=2">가공식품</a>
    <a class="tab ${cat == '3' ? 'active' : ''}" href="${contextPath}/business/businessGoodsList.do?category=3">생활용품</a>
    <a class="tab ${cat == '4' ? 'active' : ''}" href="${contextPath}/business/businessGoodsList.do?category=4">패션잡화</a>
    <a class="tab ${cat == '5' ? 'active' : ''}" href="${contextPath}/business/businessGoodsList.do?category=5">지역특산물</a>
  </nav>

  <!-- 데이터는 현재 모델에 담아와야 합니다: goodsList -->
  <jsp:include page="/WEB-INF/views/business/_goodsListPartial.jsp">
    <jsp:param name="category" value="${cat}" />
    <jsp:param name="mine" value="true" />
  </jsp:include>
</div>
