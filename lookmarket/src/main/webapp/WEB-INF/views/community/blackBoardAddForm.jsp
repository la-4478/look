<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>고충방 글쓰기</title>
    <link href="${contextPath}/resources/css/community.css" rel="stylesheet" type="text/css">
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
