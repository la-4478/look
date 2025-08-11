<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>리뷰 수정하기</title>
    <link href="${contextPath}/resources/css/community.css" rel="stylesheet" type="text/css">
</head>
<body>
<div class="edit-container">
    <div class="edit-title">리뷰 수정하기</div>

    <form action="communityUpdate.do" method="post" enctype="multipart/form-data">
        <!-- 리뷰 고유 번호 숨김필드로 보내기 -->
        <input type="hidden" name="r_id" value="${review.r_id}" />

        <label for="r_title">제목</label>
        <input type="text" id="r_title" name="r_title" value="${review.r_title}" required />

        <label>별점</label>
        <div class="star-select">
            <c:forEach var="i" begin="5" end="1" step="1">
                <input type="radio" id="star${i}" name="r_star" value="${i}" 
                    <c:if test="${review.r_star == i}">checked</c:if> />
                <label for="star${i}">★</label>
            </c:forEach>
        </div>

        <label for="r_content">내용</label>
        <textarea id="r_content" name="r_content" required>${review.r_content}</textarea>

        <label for="r_file">이미지 업로드 (선택)</label>
        <input type="file" id="r_file" name="r_file" accept="image/*" />

        <c:if test="${not empty review.r_filename}">
            <div>
                현재 이미지:<br />
                <img src="/upload/${review.r_filename}" alt="리뷰 이미지" class="review-image" />
            </div>
        </c:if>

        <div class="btn-group">
            <button type="submit">수정 완료</button>
            <a href="communityDetail.jsp?r_id=${review.r_id}">취소</a>
        </div>
    </form>
</div>
</body>
</html>
