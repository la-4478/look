<%@ page language="java" pageEncoding="UTF-8" isELIgnored="false" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>회계관리 상세</title>
    <link rel="stylesheet" href="${contextPath}/resources/css/account.css">
</head>
<body>
<div class="container">
    <h2>회계관리 상세</h2>

    <div class="detail-row">
        <span class="label">거래ID:</span>
        <span class="value">${detail.txnId}</span>
    </div>

    <div class="detail-row">
        <span class="label">계정:</span>
        <span class="value">${detail.accountName}</span>
    </div>

    <div class="detail-row">
        <span class="label">항목명(메모):</span>
        <span class="value">${detail.memo}</span>
    </div>

    <div class="detail-row">
        <span class="label">금액:</span>
        <span class="value">
            <c:choose>
                <c:when test="${detail.amount >= 0}">
                    <span class="income">+ <fmt:formatNumber value="${detail.amount}" type="number"/> 원</span>
                </c:when>
                <c:otherwise>
                    <span class="expense">- <fmt:formatNumber value="${-detail.amount}" type="number"/> 원</span>
                </c:otherwise>
            </c:choose>
        </span>
    </div>

    <div class="detail-row">
        <span class="label">거래일:</span>
        <span class="value"><fmt:formatDate value="${detail.txnDate}" pattern="yyyy-MM-dd"/></span>
    </div>

    <div class="detail-row">
        <span class="label">구분:</span>
        <span class="value">
            <c:choose>
                <c:when test="${detail.categoryKind == 'INCOME' || detail.amount > 0}">수입</c:when>
                <c:when test="${detail.categoryKind == 'EXPENSE' || detail.amount < 0}">지출</c:when>
                <c:otherwise>이체</c:otherwise>
            </c:choose>
        </span>
    </div>

    <div class="detail-row">
        <span class="label">카테고리:</span>
        <span class="value">
            <c:out value="${detail.categoryName}"/>
        </span>
    </div>

    <div class="detail-row">
        <span class="label">거래처:</span>
        <span class="value">${detail.partnerName}</span>
    </div>

    <div class="detail-row">
        <span class="label">등록일:</span>
        <span class="value"><fmt:formatDate value="${detail.createdAt}" pattern="yyyy-MM-dd HH:mm:ss"/></span>
    </div>

    <div class="btn-area">
        <a href="${contextPath}/admin/accountList.do">목록으로</a>
        <a href="${contextPath}/admin/accountEdit.do?txnId=${detail.txnId}">수정</a>
    </div>
</div>
</body>
</html>
