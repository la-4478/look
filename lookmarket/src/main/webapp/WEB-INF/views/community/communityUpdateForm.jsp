<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>리뷰 수정하기</title>
    <link href="${contextPath}/resources/css/community.css" rel="stylesheet" type="text/css">
</head>
<body>
<div class="form-container">
    <h2>리뷰 수정하기</h2>
    <form action="${contextPath}/community/communityUpdate.do" method="post" enctype="multipart/form-data">
        <!-- 리뷰 고유 번호 숨김필드 -->
        <input type="hidden" name="r_id" value="${review.r_id}" />
        
        <!-- 작성자 표시 및 숨김필드 -->
        <div class="form-group">
            <label>작성자</label>
            <div class="readonly-field">${sessionScope.memberInfo.m_id}</div>
            <input type="hidden" name="m_id" value="${sessionScope.memberInfo.m_id}" />
        </div>

        <!-- 리뷰 제목 -->
        <div class="form-group">
            <label for="r_title">리뷰 제목</label>
            <input type="text" id="r_title" name="r_title" value="${review.r_title}" required />
        </div>

        <!-- 상품 ID -->
        <div class="form-group">
            <label for="g_name">상품 ID</label>
            <input type="text" id="g_name" name="g_name" value="${review.g_name}" required />
        </div>

        <!-- 별점 ★ 클릭 방식 -->
        <div class="form-group">
            <label for="r_star">별점</label>
            <div class="star-rating">
                <input type="radio" id="star5" name="r_star" value="5" <c:if test="${review.r_star == '5'}">checked</c:if>><label for="star5">★</label>
                <input type="radio" id="star4" name="r_star" value="4" <c:if test="${review.r_star == '4'}">checked</c:if>><label for="star4">★</label>
                <input type="radio" id="star3" name="r_star" value="3" <c:if test="${review.r_star == '3'}">checked</c:if>><label for="star3">★</label>
                <input type="radio" id="star2" name="r_star" value="2" <c:if test="${review.r_star == '2'}">checked</c:if>><label for="star2">★</label>
                <input type="radio" id="star1" name="r_star" value="1" <c:if test="${review.r_star == '1'}">checked</c:if>><label for="star1">★</label>
            </div>
        </div>

        <!-- 내용 -->
        <div class="form-group">
            <label for="r_content">내용</label>
            <textarea id="r_content" name="r_content" rows="5" required>${review.r_content}</textarea>
        </div>

        <!-- 이미지 업로드 -->
        <div class="form-group">
            <label for="r_filename">이미지 업로드 (선택)</label>
            <input type="file" id="r_filename" name="r_filename" />
            <c:if test="${not empty review.r_filename}">
                <div style="margin-top:10px;">
                    현재 이미지:<br/>
                    <img src="${contextPath}/upload/${review.r_filename}" alt="리뷰 이미지" style="max-width:100%; border-radius: 5px;" />
                </div>
            </c:if>
        </div>

        <!-- 공개 여부 -->
        <div class="form-group">
            <label for="r_secret">공개 여부</label>
            <select id="r_secret" name="r_secret">
                <option value="public" <c:if test="${review.r_secret == 'public'}">selected</c:if>>공개</option>
                <option value="private" <c:if test="${review.r_secret == 'private'}">selected</c:if>>비공개</option>
            </select>
        </div>

        <div class="submit-btn">
            <button type="submit">수정 완료</button>
            <a href="${contextPath}/community/communityDetail.do?r_id=${review.r_id}" style="margin-left:10px; text-decoration:none; color:#3366cc;">취소</a>
        </div>
    </form>
</div>
</body>
</html>
