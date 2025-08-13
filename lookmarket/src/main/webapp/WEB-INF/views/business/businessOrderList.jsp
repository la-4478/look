<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<c:set var="statusParam" value="${empty param.status ? 'all' : param.status}" />
<c:set var="q" value="${param.q}" />
<c:set var="dateFrom" value="${param.dateFrom}" />
<c:set var="dateTo" value="${param.dateTo}" />
<c:set var="sort" value="${empty param.sort ? 'recent' : param.sort}" />
<c:set var="page" value="${empty param.page ? 1 : param.page}" />

<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8" />
  <meta http-equiv="X-UA-Compatible" content="IE=edge"/>
  <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
  <title>내 상품 주문 목록</title>
  <link rel="stylesheet" href="${contextPath}/resources/css/business.css"/>
</head>
<body>
  <main class="wrap" id="content" role="main">
    <header>
      <h1>내 상품 주문 목록</h1>
    </header>

    <!-- 필터/검색 -->
    <section class="section filters">
      <form method="get" action="${contextPath}/order/businessOrders.do" class="filter-form">
        <input type="hidden" name="page" value="1"/>
        <label>
          상태
          <select name="status">
            <option value="all" ${statusParam=='all'?'selected':''}>전체</option>
            <option value="PENDING" ${statusParam=='PENDING'?'selected':''}>결제대기</option>
            <option value="PAID" ${statusParam=='PAID'?'selected':''}>결제완료</option>
            <option value="SHIPPED" ${statusParam=='SHIPPED'?'selected':''}>배송중</option>
            <option value="COMPLETED" ${statusParam=='COMPLETED'?'selected':''}>구매확정</option>
            <option value="CANCELED" ${statusParam=='CANCELED'?'selected':''}>취소/환불</option>
          </select>
        </label>

        <label>
          기간
          <input type="date" name="dateFrom" value="${dateFrom}"/>
          ~
          <input type="date" name="dateTo" value="${dateTo}"/>
        </label>

        <label>
          정렬
          <select name="sort">
            <option value="recent" ${sort=='recent'?'selected':''}>최신순</option>
            <option value="amount" ${sort=='amount'?'selected':''}>금액높은순</option>
          </select>
        </label>

        <label class="grow">
          검색(주문번호/상품명/구매자)
          <input type="search" name="q" value="${q}" placeholder="예: 20250812 / 사과 / 홍길동"/>
        </label>

        <button type="submit" class="btn primary">검색</button>
        <a class="btn" href="${contextPath}/order/businessOrders.do">초기화</a>
      </form>
    </section>

    <!-- 주문 리스트 -->
    <section class="section">
      <table class="orders-table">
        <thead>
          <tr>
            <th>주문번호</th>
            <th>주문일시</th>
            <th>상품</th>
            <th>수량</th>
            <th>금액</th>
            <th>구매자</th>
            <th>상태</th>
            <th>관리</th>
          </tr>
        </thead>
        <tbody>
          <c:forEach var="o" items="${orderList}">
            <%-- 상태 필드 유연 처리: o_status 우선, 없으면 status --%>
            <c:set var="st" value="${empty o.o_status ? o.status : o.o_status}" />
            <tr>
              <td><a href="${contextPath}/order/detail.do?o_id=${o.o_id}">${o.o_id}</a></td>
              <td><fmt:formatDate value="${o.order_time}" pattern="yyyy-MM-dd HH:mm"/></td>
              <td>
                <c:out value="${o.g_name}"/>
                <c:if test="${o.item_count>1}"> 외 ${o.item_count-1}개</c:if>
              </td>
              <td>${o.quantity}</td>
              <td><fmt:formatNumber value="${empty o.total_price ? o.pay_amount : o.total_price}" pattern="#,###"/>원</td>
              <td>
                <c:out value="${empty o.buyer_name ? o.m_name : o.buyer_name}"/>
                <span class="muted">(${empty o.buyer_id ? o.m_id : o.buyer_id})</span>
              </td>
              <td>
                <c:choose>
                  <c:when test="${st=='PENDING' || st==1}">결제대기</c:when>
                  <c:when test="${st=='PAID' || st==2}">결제완료</c:when>
                  <c:when test="${st=='SHIPPED' || st==3}">배송중</c:when>
                  <c:when test="${st=='COMPLETED' || st==4}">구매확정</c:when>
                  <c:when test="${st=='CANCELED' || st==5}">취소/환불</c:when>
                  <c:otherwise>-</c:otherwise>
                </c:choose>
              </td>
              <td class="actions">
                <a class="btn" href="${contextPath}/order/detail.do?o_id=${o.o_id}">상세</a>
                <c:if test="${st=='PAID' || st==2}">
                  <a class="btn" href="${contextPath}/order/shipForm.do?o_id=${o.o_id}">배송등록</a>
                </c:if>
                <c:if test="${st=='SHIPPED' || st==3}">
                  <a class="btn" href="${contextPath}/order/shipTrace.do?o_id=${o.o_id}">배송조회</a>
                </c:if>
              </td>
            </tr>
          </c:forEach>

          <c:if test="${empty orderList}">
            <tr><td colspan="8">조건에 맞는 주문이 없습니다.</td></tr>
          </c:if>
        </tbody>
      </table>
    </section>

    <!-- 페이지네이션 -->
    <c:if test="${not empty pageInfo}">
      <c:set var="curr" value="${pageInfo.page}" />
      <c:set var="totalPages" value="${pageInfo.totalPages}" />
      <nav class="pagination" aria-label="페이지 이동">
        <c:if test="${curr>1}">
          <a class="btn" href="${contextPath}/order/businessOrders.do?status=${statusParam}&dateFrom=${dateFrom}&dateTo=${dateTo}&q=${fn:escapeXml(q)}&sort=${sort}&page=${curr-1}">이전</a>
        </c:if>
        <span class="muted">페이지 ${curr} / ${totalPages}</span>
        <c:if test="${curr<totalPages}">
          <a class="btn" href="${contextPath}/order/businessOrders.do?status=${statusParam}&dateFrom=${dateFrom}&dateTo=${dateTo}&q=${fn:escapeXml(q)}&sort=${sort}&page=${curr+1}">다음</a>
        </c:if>
      </nav>
    </c:if>
  </main>
</body>
</html>
