<%@ page language="java" pageEncoding="UTF-8" isELIgnored="false" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />

<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>상품 등록</title>
  	<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" />
  	<link href="${contextPath}/resources/css/goods.css" rel="stylesheet" type="text/css">
</head>
<body>

<div class="form-card">
  <h2>상품 등록</h2>
  <form action="${contextPath}/jangbogo/goodsAdd.do" method="post" enctype="multipart/form-data">
    <!-- g_name -->
    <div class="mb-3">
      <label for="g_name" class="form-label">상품명</label>
      <input type="text" class="form-control" name="g_name" id="g_name" required>
    </div>

    <!-- g_brand -->
    <div class="mb-3">
      <label for="g_brand" class="form-label">제조사</label>
      <input type="text" class="form-control" name="g_brand" id="g_brand" required>
    </div>

    <!-- g_price -->
    <div class="mb-3">
      <label for="g_price" class="form-label">가격</label>
      <input type="number" class="form-control" name="g_price" id="g_price" min="0" step="1" required>
    </div>

    <!-- g_stock -->
    <div class="mb-3">
      <label for="g_stock" class="form-label">재고</label>
      <input type="number" class="form-control" name="g_stock" id="g_stock" min="0" step="1" required>
    </div>

    <!-- g_category -->
    <div class="mb-3">
      <label for="g_category" class="form-label">카테고리</label>
      <select class="form-select" name="g_category" id="g_category" required>
        <option value="">선택하세요</option>
        <option value="1">신선식품</option>
        <option value="2">가공식품</option>
        <option value="3">생활용품</option>
        <option value="4">패션잡화</option>
        <option value="5">지역특산물</option>
      </select>
    </div>

    <!-- g_discription -->
    <div class="mb-3">
      <label for="g_discription" class="form-label">설명</label>
      <textarea class="form-control" name="g_discription" id="g_discription" rows="4" required></textarea>
    </div>

    <!-- g_delivery_price -->
    <div class="mb-3">
      <label for="g_delivery_price" class="form-label">배송비</label>
      <input type="number" class="form-control" name="g_delivery_price" id="g_delivery_price" min="0" step="1" required>
    </div>

    <!-- g_manufactured_date -->
    <div class="mb-3">
      <label for="g_manufactured_date" class="form-label">상품 제조일자</label>
      <input type="text" class="form-control" name="g_manufactured_date" id="g_manufactured_date"
             value="제조일자는 배송받은 제품 포장지 상단에 적혀있습니다" readonly>
    </div>

    <!-- g_expiration_date -->
    <div class="mb-3">
      <label for="g_expiration_date" class="form-label">상품 소비기한</label>
      <input type="text" class="form-control" name="g_expiration_date" id="g_expiration_date"
             value="소비기한은 배송받은 제품 포장지 상단에 적혀있습니다" readonly>
    </div>

   
    <!-- i_filename (대표 이미지) -->
    <div class="mb-3">
      <label for="i_filename" class="form-label">이미지</label>
      <input type="file" class="form-control" name="i_filename" id="i_filename" accept="image/*">
      <input type="hidden" name="cre_m_id" value="${loginUserId}"/>
    </div>

    <!-- (선택) g_status를 기본값으로 숨겨 보낼 수도 있음: 컨트롤러 intParams 포함됨 -->
    <!-- <input type="hidden" name="g_status" value="1"> -->

    <div class="d-flex justify-content-between">
      <button type="submit" class="btn btn-primary">등록하기</button>
      <button type="reset" class="btn btn-secondary">초기화</button>
    </div>
  </form>
</div>

</body>
</html>
