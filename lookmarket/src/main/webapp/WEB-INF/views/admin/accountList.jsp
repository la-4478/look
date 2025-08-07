<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>회계관리 리스트</title>
    <style>
        .container {
            width: 90%;
            margin: 0 auto;
            padding: 30px 0;
        }

        table {
            width: 100%;
            border-collapse: collapse;
        }

        th, td {
            border: 1px solid #ddd;
            padding: 12px;
            text-align: center;
        }

        th {
            background-color: #f2f2f2;
        }

        tr:hover {
            background-color: #f9f9f9;
        }

        .total {
            text-align: right;
            font-weight: bold;
            margin-top: 20px;
        }
    </style>
</head>
<body>
<div class="container">
    <h2>회계관리 리스트</h2>

    <table>
        <thead>
        <tr>
            <th>번호</th>
            <th>회계 항목</th>
            <th>금액</th>
            <th>날짜</th>
            <th>구분</th> <!-- 수입/지출 등 -->
        </tr>
        </thead>
        <tbody>
        <c:forEach var="account" items="${accountList}" varStatus="status">
            <tr>
                <td>${status.index + 1}</td>
                <td>${account.a_title}</td>
                <td>${account.a_amount} 원</td>
                <td>${account.a_date}</td>
                <td>${account.a_type}</td>
            </tr>
        </c:forEach>
        </tbody>
    </table>

    <div class="total">
        총계: 
        <span>
            <c:out value="${totalAmount}" /> 원
        </span>
    </div>
</div>
</body>
</html>
