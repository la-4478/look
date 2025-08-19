<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>프로모션 수정</title>
    <link href="${contextPath}/resources/css/event.css" rel="stylesheet" type="text/css">
</head>
<body>
<div class="form-container">
    <h2>프로모션 수정</h2>
    <form action="${contextPath}/event/updatePromotionPost.do" method="post" enctype="multipart/form-data">
        <input type="hidden" name="postId" value="${promo.postId}" />
        
        <div class="form-group">
            <label for="promoTitle">제목</label>
            <input type="text" id="promoTitle" name="promoTitle" value="${promo.promoTitle}" required />
        </div>

        <div class="form-group">
            <label for="promoContent">내용</label>
            <textarea id="promoContent" name="promoContent" rows="5" required>${promo.promoContent}</textarea>
        </div>

        <div class="form-group">
            <label for="promoStartDate">시작일</label>
            <input type="date" id="promoStartDate" name="promoStartDate" value="${promo.promoStartDate}" required />
        </div>

        <div class="form-group">
            <label for="promoEndDate">종료일</label>
            <input type="date" id="promoEndDate" name="promoEndDate" value="${promo.promoEndDate}" required />
        </div>

        <div class="form-group">
            <label for="imageFile">이미지 변경 (선택)</label>
            <input type="file" id="imageFile" name="imageFile" accept="image/*" />
            <c:if test="${not empty promo.promoBannerImg}">
                <p>현재 이미지:</p>
                <img src="${contextPath}/resources/image/${promo.promoBannerImg}" alt="프로모션 이미지" style="max-width: 300px;"/>
            </c:if>
        </div>

        <div class="submit-btn">
            <button type="submit">수정하기</button>
            <a href="${contextPath}/event/promotionList.do">취소</a>
        </div>
    </form>
</div>
</body>
</html>
