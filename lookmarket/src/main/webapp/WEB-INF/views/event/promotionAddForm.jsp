<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>이벤트 등록</title>
    <link href="${contextPath}/resources/css/event.css" rel="stylesheet" type="text/css">
</head>
<body>
<div class="form-container">
    <h2>이벤트 등록</h2>
    <form action="${contextPath}/event/insertPromotionPost.do" method="post" enctype="multipart/form-data">
        
        <div class="form-group">
            <label for="title">이벤트 제목</label>
            <input type="text" id="promoTitle" name="promoTitle" required />
        </div>

        <div class="form-group">
            <label for="content">내용</label>
            <textarea id="promoContent" name="promoContent" rows="5" required></textarea>
        </div>

        <div class="form-group">
            <label for="start_date">이벤트 시작일</label>
            <input type="date" id="promoStartDate" name="promoStartDate" required />
        </div>

        <div class="form-group">
            <label for="end_date">이벤트 종료일</label>
            <input type="date" id="promoEndDate" name="promoEndDate" required />
        </div>

        <!-- 쿠폰 연동 -->
        <div class="form-group">
            <label for="promo_id">연동할 쿠폰 (선택)</label>
            <select id="promo_id" name="promo_id">
                <option value="">-- 선택 안 함 --</option>
                <c:forEach var="promo" items="${couponList}">
                    <option value="${promo.promo_id}">
                        [${promo.promo_code}]
                        ${promo.promo_discount_type == 1 ? '정액할인' : promo.promo_discount_type == 2 ? '퍼센트할인' : '무료배송'}
                    </option>
                </c:forEach>
            </select>
        </div>
        <div class="form-group">
            <label for="imageFile">이미지 업로드</label>
            <input type="file" id="imageFile" name="imageFile" accept="image/*" />
        </div>

        <div class="submit-btn">
            <button type="submit">등록하기</button>
        </div>
    </form>
</div>
</body>
</html>
