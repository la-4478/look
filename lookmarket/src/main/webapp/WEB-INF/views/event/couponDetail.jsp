<%@ page language="java" pageEncoding="UTF-8" isELIgnored="false"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<c:set var="today" value="<%= new java.util.Date() %>" />
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>쿠폰 상세</title>
    <link href="${contextPath}/resources/css/event.css" rel="stylesheet" type="text/css">
    <style>
        .readonly-field {
            background-color: #f9f9f9;
            padding: 8px;
            border: 1px solid #ddd;
            border-radius: 4px;
        }
        .expired {
            color: red;
            font-weight: bold;
        }
        .active {
            color: green;
            font-weight: bold;
        }
        .example-box {
            background-color: #eef2ff;
            border: 1px solid #bbc;
            padding: 10px;
            margin-top: 15px;
            border-radius: 4px;
        }
    </style>
</head>
<body>
<div class="form-container">
    <h2>쿠폰 상세</h2>

    <div class="form-group">
        <label>쿠폰 코드</label>
        <div class="readonly-field">${coupon.promoCode}</div>
    </div>

    <div class="form-group">
        <label>할인 유형</label>
        <div class="readonly-field">
            <c:choose>
                <c:when test="${coupon.promoDiscountType == 1}">정률 할인 (%)</c:when>
                <c:when test="${coupon.promoDiscountType == 2}">정액 할인 (₩)</c:when>
                <c:otherwise>기타</c:otherwise>
            </c:choose>
        </div>
    </div>

    <div class="form-group">
        <label>할인 수치</label>
        <div class="readonly-field">${coupon.promoDiscountValue}</div>
    </div>

    <div class="form-group">
        <label>최대 할인 금액</label>
        <div class="readonly-field">
            <c:out value="${coupon.promoMaxDiscount != null ? coupon.promoMaxDiscount : '-'}"/>
        </div>
    </div>

    <div class="form-group">
        <label>최소 구매 금액</label>
        <div class="readonly-field">${coupon.promoMinPurchase}</div>
    </div>

    <div class="form-group">
        <label>사용 시작일</label>
        <div class="readonly-field">
            <fmt:formatDate value="${coupon.promoStartDate}" pattern="yyyy-MM-dd" />
        </div>
    </div>

    <div class="form-group">
        <label>사용 종료일</label>
        <div class="readonly-field">
            <fmt:formatDate value="${coupon.promoEndDate}" pattern="yyyy-MM-dd" />
            <c:choose>
                <c:when test="${coupon.promoEndDate lt today}">
                    <span class="expired">[만료됨]</span>
                </c:when>
                <c:otherwise>
                    <span class="active">[사용 가능]</span>
                </c:otherwise>
            </c:choose>
        </div>
    </div>

    <div class="form-group">
        <label>활성 상태</label>
        <div class="readonly-field">
            <c:out value="${coupon.promoCouponActive == 1 ? '활성' : '비활성'}" />
        </div>
    </div>

    <!-- 할인 예시 -->
    <div class="form-group">
        <label>할인 적용 예시 (₩10,000 구매 시)</label>
        <div class="example-box">
            <c:choose>
                <c:when test="${coupon.promoDiscountType == 1}">
                    <c:set var="percentDiscount" value="${coupon.promoDiscountValue * 10000 / 100}" />
                    <c:choose>
                        <c:when test="${coupon.promoMaxDiscount > 0 && percentDiscount > coupon.promoMaxDiscount}">
                            <c:set var="discountAmount" value="${coupon.promoMaxDiscount}" />
                        </c:when>
                        <c:otherwise>
                            <c:set var="discountAmount" value="${percentDiscount}" />
                        </c:otherwise>
                    </c:choose>
                </c:when>
                <c:when test="${coupon.promoDiscountType == 2}">
                    <c:set var="discountAmount" value="${coupon.promoDiscountValue}" />
                </c:when>
                <c:otherwise>
                    <c:set var="discountAmount" value="0" />
                </c:otherwise>
            </c:choose>
            할인 금액: <fmt:formatNumber value="${discountAmount}" type="currency" currencySymbol="₩"/>
        </div>
    </div>

    <div class="submit-btn">
        <a href="${contextPath}/event/couponUpdateForm.do?promoId=${coupon.promoId}">수정하기</a>
        <a href="${contextPath}/event/couponList.do?postId=${coupon.postId}">목록으로</a>
    </div>
</div>
</body>
</html>
