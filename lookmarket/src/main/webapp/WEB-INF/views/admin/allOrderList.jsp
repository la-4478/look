<%@ page language="java" pageEncoding="UTF-8" isELIgnored="false"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>회원들의 주문내역</title>
<style>
  table { width: 100%; border-collapse: collapse; }
  th, td { border: 1px solid #ccc; padding: 8px; text-align: center; }
  th { background-color: #f5f5f5; }
  .pagination { margin-top: 20px; text-align: center; }
  .pagination a, .pagination strong { margin: 0 5px; text-decoration: none; color: #333; }
  .pagination a:hover { text-decoration: underline; }
</style>
</head>
<body>
<div class="order-history">
  <h2>상품 주문내역</h2>

  <table>
    <thead>
      <tr>
        <th>주문번호</th>
        <th>품목</th>
        <th>품목개수</th>
        <th>금액</th>
        <th>할인금액</th>
        <th>구매자</th>
        <th>수령인</th>
        <th>배송 주소</th>
      </tr>
    </thead>
    <tbody>
      <c:choose>
        <c:when test="${not empty pagedOrderList}">
          <c:forEach var="order" items="${pagedOrderList}">
            <tr>
              <td>${order.order.oId}</td>
              <td>
			    <c:forEach var="item" items="${order.orderItems}">
			      ${item.otGoodsName}<br/>
			    </c:forEach>
			  </td>
			  <td>
			    <c:forEach var="item" items="${order.orderItems}">
			      ${item.otGoodsQty}<br/>
			    </c:forEach>
			  </td>
			  <td>
			    <c:forEach var="item" items="${order.orderItems}">
			      ${item.otGoodsPrice}<br/>
			    </c:forEach>
			  </td>
              <td>${order.order.oiSalePrice}</td>
              <td>${order.order.oiName}</td>
              <td>${order.order.oiReceiverName}</td>
              <td>${order.order.oiDeliveryAddress} - ${order.order.oi_deli_namuji_address}</td>
            </tr>
          </c:forEach>
        </c:when>
        <c:otherwise>
          <tr>
            <td colspan="8">주문 내역이 없습니다.</td>
          </tr>
        </c:otherwise>
      </c:choose>
    </tbody>
  </table>

  <div class="pagination">
    <c:forEach var="i" begin="1" end="${totalPages}">
      <c:choose>
        <c:when test="${i == currentPage}">
          <strong>[${i}]</strong>
        </c:when>
        <c:otherwise>
          <a href="${contextPath}/admin/allOrderList.do?page=${i}">[${i}]</a>
        </c:otherwise>
      </c:choose>
    </c:forEach>
  </div>
</div>
</body>
</html>
