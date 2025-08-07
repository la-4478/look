<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>고충방 글쓰기</title>
    <style>
        body {
            font-family: '맑은 고딕', sans-serif;
            background-color: #f9f9f9;
            padding: 30px;
        }

        .write-container {
            width: 800px;
            margin: 0 auto;
            background-color: #fff;
            border: 1px solid #ddd;
            padding: 30px;
            border-radius: 10px;
        }

        h2 {
            margin-bottom: 20px;
            text-align: center;
        }

        .form-group {
            margin-bottom: 20px;
        }

        label {
            display: block;
            font-weight: bold;
            margin-bottom: 8px;
        }

        input[type="text"], textarea, select {
            width: 100%;
            padding: 10px;
            font-size: 14px;
            border: 1px solid #ccc;
            border-radius: 5px;
        }

        textarea {
            height: 200px;
            resize: vertical;
        }

        .form-buttons {
            text-align: center;
            margin-top: 30px;
        }

        .form-buttons input[type="submit"],
        .form-buttons a {
            padding: 10px 20px;
            background-color: #3366cc;
            color: white;
            text-decoration: none;
            border: none;
            border-radius: 5px;
            margin: 0 5px;
            cursor: pointer;
        }

        .form-buttons input[type="submit"]:hover,
        .form-buttons a:hover {
            background-color: #254a9a;
        }
    </style>
</head>
<body>

<div class="write-container">
    <h2>사장님 고충방 글쓰기</h2>

    <form action="blackBoardAddForm.jsp" method="post">
        <div class="form-group">
            <label for="b_title">제목</label>
            <input type="text" id="b_title" name="b_title" required>
        </div>

        <div class="form-group">
            <label for="b_content">내용</label>
            <textarea id="b_content" name="b_content" required></textarea>
        </div>

        <div class="form-group">
            <label for="b_secret">공개 여부</label>
            <select id="b_secret" name="b_secret">
                <option value="1">공개</option>
                <option value="2">비공개</option>
            </select>
        </div>

        <div class="form-buttons">
            <input type="submit" value="등록">
            <a href="blackBoardList.jsp">목록으로</a>
        </div>
    </form>
</div>

</body>
</html>
