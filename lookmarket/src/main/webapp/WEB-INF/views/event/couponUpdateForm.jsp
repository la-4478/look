<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="contextPath" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>쿠폰 수정</title>
    <link href="${contextPath}/resources/css/event.css" rel="stylesheet" type="text/css">
</head>
<body>
<div class="form-container">
    <h2>쿠폰 수정</h2>
    <form action="${contextPath}/event/updateCoupon.do" method="post">
        <input type="hidden" name="promoId" value="${coupon.promoId}"/>
        <input type="hidden" name="postId" value="${postId}"/>

        <div class="form-group">
            <label for="promoCode">쿠폰 코드</label>
            <input type="text" id="promoCode" name="promoCode" value="${coupon.promoCode}" required/>
        </div>

        <div class="form-group">
            <label for="promoDiscountType">할인 유형</label>
            <select id="promoDiscountType" name="promoDiscountType" required>
                <option value="">-- 선택 --</option>
                <option value="1" <c:if test="${coupon.promoDiscountType == 1}">selected</c:if>>정률 할인 (%)</option>
                <option value="2" <c:if test="${coupon.promoDiscountType == 2}">selected</c:if>>정액 할인 (₩)</option>
            </select>
        </div>

        <div class="form-group">
            <label for="promoDiscountValue">할인 수치</label>
            <input type="number" id="promoDiscountValue" name="promoDiscountValue" min="0" value="${coupon.promoDiscountValue}" required/>
        </div>

        <div class="form-group">
            <label for="promoMaxDiscount">최대 할인 금액</label>
            <input type="number" id="promoMaxDiscount" name="promoMaxDiscount" min="0" value="${coupon.promoMaxDiscount}" />
        </div>

        <div class="form-group">
            <label for="promoMinPurchase">최소 구매 금액</label>
            <input type="number" id="promoMinPurchase" name="promoMinPurchase" min="0" value="${coupon.promoMinPurchase}" required />
        </div>

        <div class="form-group">
            <label for="promoStartDate">사용 시작일</label>
            <input type="date" id="promoStartDate" name="promoStartDate" value="${coupon.promoStartDate}" required />
        </div>

        <div class="form-group">
            <label for="promoEndDate">사용 종료일</label>
            <input type="date" id="promoEndDate" name="promoEndDate" value="${coupon.promoEndDate}" required />
        </div>

        <div class="form-group">
            <label for="promoCouponActive">쿠폰 활성 상태</label>
            <select id="promoCouponActive" name="promoCouponActive" required>
                <option value="1" <c:if test="${coupon.promoCouponActive == 1}">selected</c:if>>활성</option>
                <option value="0" <c:if test="${coupon.promoCouponActive == 0}">selected</c:if>>비활성</option>
            </select>
        </div>

        <div class="submit-btn">
            <button type="submit">수정하기</button>
            <a href="${contextPath}/event/couponList.do?postId=${postId}">취소</a>
        </div>
    </form>
</div>
</body>
</html>
