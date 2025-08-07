<%@ page language="java" pageEncoding="UTF-8" isELIgnored="false" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />

<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8" />
  <title>나의 주문내역</title>
  <style>
    body {
      font-family: Arial, sans-serif;
      background: #f9f9f9;
      padding: 20px;
    }
    .order-history {
      max-width: 800px;
      margin: auto;
      background: #fff;
      padding: 20px;
      border-radius: 6px;
      box-shadow: 0 2px 4px rgba(0,0,0,0.1);
    }
    h2 {
      text-align: center;
      color: #333;
      margin-bottom: 20px;
    }
    table {
      width: 100%;
      border-collapse: collapse;
    }
    th, td {
      padding: 12px;
      text-align: center;
      border-bottom: 1px solid #ddd;
    }
    th {
      background-color: #f1f1f1;
      font-weight: bold;
      color: #333;
    }
    a.detail-link {
      color: #007bff;
      text-decoration: none;
    }
    a.detail-link:hover {
      text-decoration: underline;
    }
    .no-orders {
      text-align: center;
      padding: 40px 0;
      color: #888;
    }
  </style>
</head>
<body>

  <div class="order-history">
    <h2>나의 주문내역</h2>

    <c:choose>
      <c:when test="${not empty orderList}">
        <table>
          <thead>
            <tr>
              <th>주문번호</th>
              <th>주문일자</th>
              <th>주문자</th>
              <th>총 결제금액</th>
              <th>상세보기</th>
            </tr>
          </thead>
          <tbody>
            <c:forEach var="order" items="${orderList}">
              <tr>
                <td><c:out value="${order.oi_id}" /></td>
                <td><c:out value="${order.oi_date}" /></td>
                <td><c:out value="${order.oi_name}" /></td>
                <td><c:out value="${order.oi_total_goods_price}" /> 원</td>
                <td>
                  <a class="detail-link" href="${contextPath}/orderDetail?orderId=${order.oi_id}">상세보기</a>
                </td>
              </tr>
            </c:forEach>
          </tbody>
        </table>
      </c:when>
      <c:otherwise>
        <div class="no-orders">주문 내역이 없습니다.</div>
      </c:otherwise>
    </c:choose>
  </div>

</body>
</html>
