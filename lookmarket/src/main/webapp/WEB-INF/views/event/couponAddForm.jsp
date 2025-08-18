<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>쿠폰 등록</title>
    <link href="${contextPath}/resources/css/event.css" rel="stylesheet" type="text/css">
</head>
<body>
<div class="form-container">
    <h2>쿠폰 등록</h2>
    <form action="${contextPath}/event/insertCoupon.do" method="post">
        <div class="form-group">
            <label for="promoCode">쿠폰 코드</label>
            <input type="text" id="promoCode" name="promoCode" required/>
        </div>
		<input type = "hidden" name="postId" value="${postId}"/>
        <div class="form-group">
            <label for="promoDiscountType">할인 유형</label>
            <select id="promoDiscountType" name="promoDiscountType" required>
                <option value="">-- 선택 --</option>
                <option value="1">정률 할인 (%)</option>
                <option value="2">정액 할인 (₩)</option>
            </select>
        </div>

        <div class="form-group">
            <label for="promoDiscountValue">할인 수치</label>
            <input type="number" id="promoDiscountValue" name="promoDiscountValue" min="0" required />
        </div>

        <div class="form-group">
            <label for="promoMaxDiscount">최대 할인 금액 (정률 할인 시 한도, 정액 할인일 경우 비워도 됨)</label>
            <input type="number" id="promoMaxDiscount" name="promoMaxDiscount" min="0" />
        </div>

        <div class="form-group">
            <label for="promoMinPurchase">최소 구매 금액</label>
            <input type="number" id="promoMinPurchase" name="promoMinPurchase" min="0" required />
        </div>

        <div class="form-group">
            <label for="promoStartDate">사용 시작일</label>
            <input type="date" id="promoStartDate" name="promoStartDate" required />
        </div>

        <div class="form-group">
            <label for="promoEndDate">사용 종료일</label>
            <input type="date" id="promoEndDate" name="promoEndDate" required />
        </div>

        <div class="form-group">
            <label for="promoCouponActive">쿠폰 활성 상태</label>
            <select id="promoCouponActive" name="promoCouponActive" required>
                <option value="1" selected>활성</option>
                <option value="0">비활성</option>
            </select>
        </div>

        <div class="submit-btn">
            <button type="submit">등록하기</button>
            <a href="${contextPath}/event/couponList.do?postId=${postId}">취소</a>
        </div>
    </form>
    <c:out value="${postId}" default="postId 없음"/>
    
</div>
</body>
</html>
