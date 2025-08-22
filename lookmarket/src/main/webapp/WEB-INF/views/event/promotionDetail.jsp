<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="contextPath"  value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>프로모션 상세</title>
    <link href="${contextPath}/resources/css/event.css" rel="stylesheet" type="text/css">

</head>
<body class="bg-light">

<div class="container py-5">
    <h2 class="fw-bold mb-4">프로모션 상세</h2>

    <form>
        <!-- 🔹 제목 -->
        <div class="form-group">
            <label for="promoTitle">제목</label>
            <input type="text" id="promoTitle" class="form-control" value="${promo.promoTitle}" readonly />
        </div>

        <!-- 🔹 이미지 -->
        <div class="form-group">
            <label>이미지</label><br />
            <c:choose>
                <c:when test="${not empty promo.promoBannerImg}">
                    <img src="${contextPath}/resources/image/${promo.promoBannerImg}" alt="프로모션 이미지" style="max-width: 100%; height: auto;" />
                </c:when>
                <c:otherwise>
                    <div class="text-secondary">이미지 없음</div>
                </c:otherwise>
            </c:choose>
        </div>

        <!-- 🔹 기간 -->
        <div class="form-group">
            <label>이벤트 기간</label>
            <div class="d-flex gap-2">
				<fmt:formatDate value="${promo.promoStartDate}" pattern="yyyy-MM-dd" var="startDate" />
				<input type="date" class="form-control" value="${startDate}" readonly />
				
				<fmt:formatDate value="${promo.promoEndDate}" pattern="yyyy-MM-dd" var="endDate" />
				<input type="date" class="form-control" value="${endDate}" readonly />

            </div>
        </div>

        <!-- 🔹 상세 내용 -->
        <div class="form-group">
            <label for="promoContent">상세 내용</label>
            <textarea id="promoContent" class="form-control" rows="10" readonly>${promo.promoContent}</textarea>
        </div>

        <!-- 🔙 목록으로 돌아가기 -->
        <div class="form-group mt-4">
            <a href="${contextPath}/event/promotionList.do" class="btn btn-secondary">← 목록으로</a>
             <c:set var="m_role" value="${sessionScope.m_role != null ? sessionScope.m_role : sessionScope.memberInfo.m_role}" />
		    <c:if test="${m_role == '3'}">
		        <a href="${contextPath}/event/promotionUpdateForm.do?postId=${promo.postId}" class="btn btn-primary">수정하기</a>
		    </c:if>
        </div>
    </form>
</div>

</body>
</html>
