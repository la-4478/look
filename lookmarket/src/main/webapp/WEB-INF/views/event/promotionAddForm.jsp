<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>event add Form</title>
    <style>
        .form-container {
            width: 600px;
            margin: 50px auto;
            padding: 30px;
            border: 1px solid #ccc;
            border-radius: 10px;
            background-color: #fafafa;
        }

        .form-container h2 {
            text-align: center;
        }

        .form-group {
            margin-bottom: 15px;
        }

        label {
            font-weight: bold;
            display: block;
            margin-bottom: 5px;
        }

        input[type="text"], textarea, select {
            width: 100%;
            padding: 10px;
            font-size: 14px;
            box-sizing: border-box;
        }

        input[type="file"] {
            margin-top: 5px;
        }

        .submit-btn {
            text-align: center;
            margin-top: 20px;
        }

        .submit-btn button {
            padding: 10px 20px;
            font-size: 16px;
            background-color: #3366cc;
            color: white;
            border: none;
            border-radius: 5px;
            cursor: pointer;
        }

        .submit-btn button:hover {
            background-color: #254a9a;
        }
    </style>
</head>
<body>
<div class="form-container">
    <h2>이벤트 등록</h2>
    <form action="insertReview.do" method="post" enctype="multipart/form-data">
        <!-- 리뷰 제목 -->
        <div class="form-group">
            <label for="r_title">이벤트 제목</label>
            <input type="text" id="r_title" name="r_title" required />
        </div>

        <!-- 내용 -->
        <div class="form-group">
            <label for="r_content">내용</label>
            <textarea id="r_content" name="r_content" rows="5" required></textarea>
        </div>

        <!-- 이미지 업로드 -->
        <div class="form-group">
            <label for="r_filename">이미지 업로드</label>
            <input type="file" id="r_filename" name="r_filename" accept="image/*" />
        </div>

        <div class="submit-btn">
            <button type="submit">등록하기</button>
        </div>
    </form>
</div>
</body>
</html>
