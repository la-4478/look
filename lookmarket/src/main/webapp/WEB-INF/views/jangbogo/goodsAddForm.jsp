<%@ page language="java" pageEncoding="UTF-8" isELIgnored="false" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>상품 등록</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" />
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
    <h2>상품 등록</h2>
    <form action="${contextPath}/goodsAdd.do" method="post" enctype="multipart/form-data">
        <div class="mb-3">
            <label for="g_name" class="form-label">상품명</label>
            <input type="text" class="form-control" name="g_name" id="g_name" required>
        </div>

        <div class="mb-3">
            <label for="g_price" class="form-label">가격</label>
            <input type="number" class="form-control" name="g_price" id="g_price" required>
        </div>

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

        <div class="mb-3">
            <label for="g_description" class="form-label">설명</label>
            <textarea class="form-control" name="g_description" id="g_description" rows="4" required></textarea>
        </div>

        <div class="mb-3">
            <label for="i_filename" class="form-label">이미지</label>
            <input type="file" class="form-control" name="i_filename" id="i_filename" accept="image/*">
        </div>

        <div class="d-flex justify-content-between">
            <button type="submit" class="btn btn-primary">등록하기</button>
            <button type="reset" class="btn btn-secondary">초기화</button>
        </div>
    </form>
</div>

</body>
</html>
