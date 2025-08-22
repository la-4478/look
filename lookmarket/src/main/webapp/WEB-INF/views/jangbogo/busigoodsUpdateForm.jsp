<%@ page language="java" pageEncoding="UTF-8" isELIgnored="false" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />

<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>상품 수정</title>
  <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" />
  <link href="${contextPath}/resources/css/goods.css" rel="stylesheet" type="text/css">
</head>
<body>
<div class="form-card">
  <h2>상품 수정</h2>

  <form action="${contextPath}/jangbogo/busigoodsUpdate.do" method="post" enctype="multipart/form-data">
    <input type="hidden" name="g_id" value="${goods.g_id}" />
    <!-- 기존 이미지 유지용(선택) -->
    <input type="hidden" name="old_i_filename" value="${goods.i_filename}" />

    <div class="mb-3">
      <label for="g_name" class="form-label">상품명</label>
      <input type="text" class="form-control" id="g_name" name="g_name"
             value="${goods.g_name}" required>
    </div>
    
        <div class="mb-3">
      <label for="g_brand" class="form-label">제조사</label>
      <input type="text" class="form-control" name="g_brand" id="g_brand" value="${goods.g_brand }" required>
    </div>

    <div class="mb-3">
      <label for="g_price" class="form-label">가격</label>
      <input type="number" class="form-control" id="g_price" name="g_price"
             value="${goods.g_price}" min="0" step="1" required>
    </div>
    
        <div class="mb-3">
      <label for="g_stock" class="form-label">재고</label>
      <input type="number" class="form-control" name="g_stock" id="g_stock" min="0" step="1" value="${goods.g_stock}" required>
    </div>

    <div class="mb-3">
      <label for="g_category" class="form-label">카테고리</label>
      <select class="form-select" id="g_category" name="g_category" required>
        <option value="">선택하세요</option>
        <option value="1" <c:if test="${goods.g_category == 1}">selected</c:if>>신선식품</option>
        <option value="2" <c:if test="${goods.g_category == 2}">selected</c:if>>가공식품</option>
        <option value="3" <c:if test="${goods.g_category == 3}">selected</c:if>>생활용품</option>
        <option value="4" <c:if test="${goods.g_category == 4}">selected</c:if>>패션잡화</option>
        <option value="5" <c:if test="${goods.g_category == 5}">selected</c:if>>지역특산물</option>
      </select>
    </div>

    <div class="mb-3">
      <label for="g_discription" class="form-label">상품 설명</label>
      <textarea class="form-control" id="g_discription" name="g_discription" rows="5">${goods.g_discription}</textarea>
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
    
    
    <div class="mb-3">
      <label for="g_category" class="form-label">판매 상태 변경</label>
      <select class="form-select" name="g_status" id="g_status" required>
        <option value="1" selected>판매중</option>
        <option value="2">품절</option>
        <option value="3">판매 종료</option>
      </select>
    </div>

    <div class="mb-3">
      <label for="i_filename" class="form-label"> 메인 이미지 (교체 시 선택)</label>
      <input type="file" class="form-control" id="i_filename" name="i_filename" accept="image/*">
      <c:if test="${not empty goods.i_filename}">
        <div class="form-text mt-1">현재 메인 이미지 파일: ${goods.i_filename}</div>
      </c:if>
      
    <div class="mb-3">
		<label for="sub_image"class="form-label">상세 이미지 (다중 선택 가능)</label>
		<input type="file" class="form-control" name="sub_image" id="sub_image" accept="image/*" multiple>
	<c:if test="${not empty detailImageList}">
		<div class="form-text mt-1"> 현재 상세 이미지파일:
		    <c:forEach var="img" items="${detailImageList}">
		        ${img.i_filename}<br/>
		    </c:forEach>
		</div>
	</c:if>
	</div>
    </div>

    <div class="d-flex justify-content-end">
      <button type="submit" class="btn btn-primary">수정 완료</button>
    </div>
  </form>
</div>
</body>
</html>
