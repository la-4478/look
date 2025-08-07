<%@ page language="java" pageEncoding="UTF-8" isELIgnored="false" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>회계관리 상세</title>
    <style>
        .container {
            width: 60%;
            margin: 50px auto;
            padding: 20px;
            border: 1px solid #ddd;
            border-radius: 10px;
            background-color: #f9f9f9;
        }

        h2 {
            text-align: center;
            margin-bottom: 30px;
        }

        .detail-row {
            margin-bottom: 15px;
            font-size: 16px;
        }

        .label {
            display: inline-block;
            width: 120px;
            font-weight: bold;
        }

        .value {
            display: inline-block;
        }

        .btn-area {
            text-align: center;
            margin-top: 30px;
        }

        .btn-area a {
            padding: 8px 16px;
            background-color: #3366cc;
            color: white;
            text-decoration: none;
            border-radius: 5px;
            margin: 0 5px;
        }

        .btn-area a:hover {
            background-color: #254e99;
        }
    </style>
</head>
<body>
<div class="container">
    <h2>회계관리 상세</h2>

    <div class="detail-row">
        <span class="label">항목명:</span>
        <span class="value">${account.a_title}</span>
    </div>

    <div class="detail-row">
        <span class="label">금액:</span>
        <span class="value">${account.a_amount} 원</span>
    </div>

    <div class="detail-row">
        <span class="label">날짜:</span>
        <span class="value">${account.a_date}</span>
    </div>

    <div class="detail-row">
        <span class="label">구분:</span>
        <span class="value">${account.a_type}</span>
    </div>

    <div class="detail-row">
        <span class="label">비고:</span>
        <span class="value">${account.a_note}</span>
    </div>

    <div class="btn-area">
        <a href="${contextPath}/accountList">목록으로</a>
        <a href="${contextPath}/accountEdit?a_id=${account.a_id}">수정</a>
    </div>
</div>
</body>
</html>
