<%@ page language="java" pageEncoding="UTF-8" isELIgnored="false" %>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<c:set var="contextPath"  value="${pageContext.request.contextPath}" />

<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8" />
  <title>결제하기</title>
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.7/dist/css/bootstrap.min.css" rel="stylesheet">
  <style>
    body {
      background: #f1f1f1;
      font-family: 'Arial', sans-serif;
      padding: 20px;
    }
    .container-box {
      max-width: 1100px;
      margin: auto;
      display: grid;
      grid-template-columns: 2fr 1.2fr;
      gap: 20px;
    }
    .box {
      background: white;
      border: 1px solid #ddd;
      padding: 20px;
      border-radius: 6px;
    }
    h2 {
      font-size: 22px;
      margin-bottom: 20px;
      font-weight: bold;
    }
    label {
      margin-top: 10px;
      font-weight: bold;
    }
    input, textarea, select {
      width: 100%;
      padding: 8px;
      margin-top: 5px;
      border: 1px solid #ccc;
      border-radius: 4px;
    }
    .submit-btn {
      width: 100%;
      background: #007bff;
      color: white;
      padding: 12px;
      font-size: 16px;
      border: none;
      border-radius: 6px;
      margin-top: 20px;
    }
    .submit-btn:hover {
      background: #0056b3;
    }
    .radio-group {
      margin-top: 10px;
    }
    .radio-group label {
      margin-right: 15px;
      font-weight: normal;
    }
    .summary-table {
      width: 100%;
      font-size: 15px;
      margin-bottom: 15px;
    }
    .summary-table td {
      padding: 6px 0;
    }
    .summary-table .total {
      font-weight: bold;
      font-size: 18px;
      color: #007bff;
    }
  </style>
</head>
<body>

<div class="container-box">
  
  <!-- 주문/배송 정보 -->
  <form class="box" action="/saveOrderInfo" method="post">
    <h2>주문 정보 입력</h2>

    <label for="oi_name">주문자 이름</label>
    <input type="text" id="oi_name" name="oi_name" placeholder="주문자 이름">

    <label for="oi_receiver_name">수령자 이름</label>
    <input type="text" id="oi_receiver_name" name="oi_receiver_name" placeholder="수령자 이름">

    <label for="oi_receiver_phone">수령자 연락처</label>
    <input type="text" id="oi_receiver_phone" name="oi_receiver_phone" placeholder="01012345678">
    
    <label for="oi_email">수령자 이메일</label>
    <input type="email" id="oi_email" name="oi_email" placeholder="example@email.com">

    <label>우편번호</label>
    <div style="display: flex; gap: 10px;">
      <input type="text" id="zipcode" name="zipcode" placeholder="우편번호">
      <button type="button" onclick="alert('주소찾기 기능 구현 필요')" style="padding: 6px 12px;">주소찾기</button>
    </div>

    <label for="oi_delivery_address">배송주소</label>
    <input type="text" id="oi_delivery_address" name="oi_delivery_address" placeholder="주소 입력">

    <label for="oi_delivery_message">배송메시지</label>
    <textarea id="oi_delivery_message" name="oi_delivery_message" rows="3" placeholder="배송 요청사항을 입력하세요."></textarea>
  </form>

  <!-- 결제 요약 + 결제 수단 -->
  <div class="box">
    <h2>결제 정보</h2>

    <table class="summary-table">
      <tr>
        <td>상품금액</td>
        <td align="right">18,000원</td>
      </tr>
      <tr>
        <td>쿠폰 할인</td>
        <td align="right">-1,000원</td>
      </tr>
      <tr>
        <td>배송비</td>
        <td align="right">+2,500원</td>
      </tr>
      <tr>
        <td colspan="2"><hr></td>
      </tr>
      <tr>
        <td>총 결제금액</td>
        <td align="right" class="total">19,500원</td>
      </tr>
    </table>

    <label>결제 방법</label>
    <div class="radio-group">
      <label><input type="radio" name="payment_method" value="card" checked> 신용카드</label>
      <label><input type="radio" name="payment_method" value="kakao"> 카카오페이</label>
      <label><input type="radio" name="payment_method" value="naver"> 네이버페이</label>
      <label><input type="radio" name="payment_method" value="transfer"> 무통장입금</label>
    </div>

    <label for="installment">할부 선택</label>
    <select name="installment" id="installment">
      <option value="0">일시불</option>
      <option value="3">3개월</option>
      <option value="6">6개월</option>
    </select>

    <div style="margin-top: 10px;">
      <label><input type="checkbox" required> 전체 동의 및 결제 진행</label>
    </div>

    <button type="submit" class="submit-btn">결제하기</button>
  </div>
</div>

</body>
</html>
