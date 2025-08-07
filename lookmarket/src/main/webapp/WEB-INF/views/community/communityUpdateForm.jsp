<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>리뷰 수정하기</title>
    <style>
        .edit-container {
            width: 800px;
            margin: 50px auto;
            padding: 30px;
            border: 1px solid #ccc;
            border-radius: 10px;
            background-color: #fefefe;
        }
        .edit-title {
            font-size: 24px;
            font-weight: bold;
            margin-bottom: 20px;
        }
        label {
            display: block;
            margin-top: 15px;
            font-weight: bold;
        }
        input[type="text"], textarea {
            width: 100%;
            padding: 8px;
            margin-top: 5px;
            border-radius: 5px;
            border: 1px solid #ccc;
            font-size: 16px;
        }
        textarea {
            height: 150px;
            resize: vertical;
        }
        .star-select {
            margin-top: 5px;
            font-size: 20px;
        }
        .star-select input[type="radio"] {
            display: none;
        }
        .star-select label {
            color: #ccc;
            cursor: pointer;
        }
        .star-select input[type="radio"]:checked ~ label {
            color: gold;
        }
        .review-image {
            max-width: 100%;
            margin-top: 10px;
            border-radius: 5px;
        }
        .btn-group {
            margin-top: 30px;
            text-align: center;
        }
        button, .back-btn a {
            padding: 10px 20px;
            margin: 0 10px;
            background-color: #3366cc;
            color: white;
            border: none;
            border-radius: 5px;
            cursor: pointer;
            text-decoration: none;
            font-size: 16px;
        }
        button:hover, .back-btn a:hover {
            background-color: #254a9a;
        }
    </style>
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
