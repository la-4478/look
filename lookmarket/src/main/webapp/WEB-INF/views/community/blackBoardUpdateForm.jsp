<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>고충방 수정</title>
    <link href="${contextPath}/resources/css/community.css" rel="stylesheet" type="text/css">
</head>
<body>
<div class="edit-container">
    <div class="edit-title">사장님 고충방 수정하기</div>

    <form action="blackboardUpdate.do" method="post">
        <input type="hidden" name="b_id" value="${board.b_id}" />

        <label for="b_title">제목</label>
        <input type="text" id="b_title" name="b_title" value="${board.b_title}" required />

        <label for="b_content">내용</label>
        <textarea id="b_content" name="b_content" required>${board.b_content}</textarea>

        <label for="b_secret">공개 여부</label>
        <select id="b_secret" name="b_secret" required>
            <option value="1" <c:if test="${board.b_secret == 1}">selected</c:if>>공개</option>
            <option value="2" <c:if test="${board.b_secret == 2}">selected</c:if>>비공개</option>
        </select>

        <div class="btn-group">
            <button type="submit">수정 완료</button>
            <a href="blackboardDetail.jsp?b_id=${board.b_id}">취소</a>
        </div>
    </form>
</div>
</body>
</html>
