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
  <script>
  const contextPath = '${contextPath}';
  function fn_delivery_on(oId){
	  if (!confirm('배송대기(준비중) 상태를 배송중으로 변경하시겠습니까?')) return;

	    $.ajax({
	        type: "POST",
	        url: contextPath + "/business/updateDelivery.do",
	        data: { o_id: oId, d_status: 2 },
	        success: function(res) {
	       	if (res && res.success) {
	        	alert("배송 상태가 '배송중'으로 변경되었습니다.");
	            location.reload(); // ← 페이지 새로고침
	        }
	        if (!res || res.success !== true) {
	            alert(res && res.message ? res.message : '상태 변경에 실패했습니다.');
	            return;
	        }
	     // 해당 행만 갱신
	        const $row = $('tr[data-oid="' + oId + '"]');
	        // 상태 텍스트 갱신
	        $row.find('.status-cell').text('배송중');

	        // 버튼 교체: 배송등록 버튼 제거, 배송조회 버튼 추가(없으면)
	        const $actions = $row.find('.actions');
	        $actions.find('button.btn:contains("배송등록")').remove();

	        // 배송조회 버튼이 없다면 추가(중복 방지)
	        if ($actions.find('a.btn:contains("배송조회")').length === 0) {
	          const traceHref = contextPath + '/order/shipTrace.do?o_id=' + oId;
	          $actions.append(' <a class="btn" href="' + traceHref + '">배송조회</a>');
	        }

	        // (선택) 토스트 느낌
	        // alert('배송상태가 "배송중"으로 변경되었습니다.');
	      },
	      
	      error: function(xhr, status, err) {
	          // 서버에서 에러 본문을 보냈다면 조금 더 친절하게
	          const msg = xhr && xhr.responseJSON && xhr.responseJSON.message
	            ? xhr.responseJSON.message
	            : (xhr.responseText || '요청 처리 중 오류가 발생했습니다.');
	          alert('상태 변경 실패: ' + msg);
	      }
	    });
  }
  </script>
</head>
<body>


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
     <h1>내 상품 주문 목록</h1>
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
  <c:forEach var="o" items="${orders}">
    <%-- 상태 필드 유연 처리: o_status 우선, 없으면 status (없으면 null이라 자동으로 '-') --%>

    <%-- 금액 계산: (개별가격 * 수량) - (할인, null이면 0) --%>
    <c:set var="sale" value="${empty o.otSalePrice ? 0 : o.otSalePrice}" />
    <c:set var="lineTotal" value="${o.otGoodsPrice * o.otGoodsQty - sale}" />

    <tr>
      <%-- 주문번호: OrderItemVO에는 OId(주문ID), ONum(주문아이템PK) 둘 다 있음. 상세가 o_id로 받으니 OId 사용 --%>
      <td><a href="${contextPath}/order/detail.do?o_id=${o.OId}">${o.OId}</a></td>

      <%-- 주문일시: VO에 없음 → 대체 표시 --%>
      <td><fmt:formatDate value="${o.order_time}" pattern="yyyy/MM/dd HH:mm:ss" timeZone="Asia/Seoul"/></td>

      <%-- 상품명: otGoodsName 사용. item_count는 없으므로 안전 가드 --%>
      <td>
        <c:out value="${o.otGoodsName}" />
      </td>

      <%-- 수량: otGoodsQty --%>
      <td>${o.otGoodsQty}</td>

      <%-- 금액: 계산값 포맷팅 --%>
      <td><fmt:formatNumber value="${lineTotal}" pattern="#,###" />원</td>

      <%-- 구매자: VO에 없음 → 대체 표시 --%>
      <td>${o.buyer_name} <span class="muted"></span></td>

      <%-- 상태: 기존 로직 유지 (st가 null이면 아래 otherwise로 '-') --%>
<td>
  <c:set var="st" value="${empty o.o_status ? o.status : o.o_status}" />
  <c:choose>
    <%-- 1) 배송완료: 배송완료일자 존재 --%>
	<c:when test="${o.d_status == 3}">배송완료</c:when>
    <c:when test="${o.d_status == 2}">배송중</c:when>
    <c:when test="${o.d_status == 1}">배송준비중</c:when>
    <c:when test="${o.d_status == 4}">주문취소</c:when>

    <c:otherwise>-</c:otherwise>
  </c:choose>
</td>


      <%-- 관리: 상세는 OId로 유지, 나머지 버튼은 st가 null이면 노출 안 됨(기존 조건 그대로) --%>
      <td class="actions">
        <a class="btn" href="${contextPath}/order/detail.do?o_id=${o.OId}">상세</a>
        <c:if test="${o.d_status == 1}">
          <button class="btn" type="button" onclick="fn_delivery_on(${o.OId})"> 배송등록</button>
        </c:if>
        <c:if test="${o.d_status == 3}">
          <a class="btn" href="${contextPath}/order/shipTrace.do?o_id=${o.OId}">배송조회</a>
        </c:if>
      </td>
    </tr>
  </c:forEach>

  <c:if test="${empty orders}">
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
