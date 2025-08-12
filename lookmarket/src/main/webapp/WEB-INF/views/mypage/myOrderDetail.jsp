<%@ page language="java" pageEncoding="UTF-8" isELIgnored="false" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />

<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8" />
  <title>주문 결과</title>
<link rel="stylesheet" href="${contextPath}/resources/css/mypage.css"/>
</head>
<body>

  <div class="order-result">
    <h2>주문 상세정보</h2>

    <table>
      <tr>
        <th>주문자 이름</th>
        <td><c:out value="${orderInfo.oi_name}" /></td>
      </tr>
      <tr>
        <th>수령자 이름</th>
        <td><c:out value="${orderInfo.oi_receiver_name}" /></td>
      </tr>
      <tr>
        <th>수령자 연락처</th>
        <td><c:out value="${orderInfo.oi_receiver_phone}" /></td>
      </tr>
      <tr>
        <th>수령자 이메일</th>
        <td><c:out value="${orderInfo.oi_email}" /></td>
      </tr>
      <tr>
        <th>배송 주소</th>
        <td><c:out value="${orderInfo.oi_delivery_address}" /></td>
      </tr>
      <tr>
        <th>배송 메시지</th>
        <td><c:out value="${orderInfo.oi_delivery_message}" /></td>
      </tr>
      <tr>
        <th>배송비</th>
        <td><c:out value="${orderInfo.oi_delivery_price}" /> 원</td>
      </tr>
      <tr>
        <th>상품 총액</th>
        <td><c:out value="${orderInfo.oi_total_goods_price}" /> 원</td>
      </tr>
      <tr>
        <th>할인 금액</th>
        <td><c:out value="${orderInfo.oi_sale_price}" /> 원</td>
      </tr>
      <tr>
        <th>주문일자</th>
        <td><c:out value="${orderInfo.oi_date}" /></td>
      </tr>
    </table>
  </div>

</body>
</html>
