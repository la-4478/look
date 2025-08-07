<%@ page language="java" pageEncoding="UTF-8" isELIgnored="false" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>상품 수정</title>
    <style>
        .form-card {
            max-width: 600px;
            margin: 40px auto;
            padding: 30px;
            border: 1px solid #ddd;
            border-radius: 12px;
            background-color: #f9f9f9;
            box-shadow: 0 4px 10px rgba(0,0,0,0.05);
        }
        .form-card h2 {
            margin-bottom: 25px;
            border-bottom: 2px solid #333;
            padding-bottom: 10px;
        }
        .form-label {
            font-weight: bold;
        }
    </style>
</head>
<body>

<div class="form-card">
    <h2>상품 수정</h2>

    <form action="${contextPath}/goodsUpdate.do" method="post">
        <input type="hidden" name="g_id" value="${goods.g_id != null ? goods.g_id : ''}" />

        <div class="mb-3">
            <label for="g_name" class="form-label">상품명</label>
            <input type="text" class="form-control" id="g_name" name="g_name"
                   value="${not empty goods.g_name ? goods.g_name : ''}" required>
        </div>

        <div class="mb-3">
            <label for="g_price" class="form-label">가격</label>
            <input type="number" class="form-control" id="g_price" name="g_price"
                   value="${not empty goods.g_price ? goods.g_price : ''}" required>
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
            <label for="g_description" class="form-label">상품 설명</label>
            <textarea class="form-control" id="g_discription" name="g_discription" rows="5">
			${not empty goods.g_discription ? goods.g_discription : ''}
            </textarea>
        </div>
        <div class="mb-3">
            <label for="i_filename" class="form-label">이미지</label>
            <input type="file" class="form-control" name="i_filename" id="i_filename" accept="image/*">
        </div>
        <div class="d-flex justify-content-end">
            <button type="submit" class="btn btn-primary">수정 완료</button>
        </div>
    </form>
</div>

</body>
</html>
