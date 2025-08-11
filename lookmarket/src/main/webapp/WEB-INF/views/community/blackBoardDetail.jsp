<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>고충방 상세보기</title>
</head>
<link href="${contextPath}/resources/css/community.css" rel="stylesheet" type="text/css">
<body>
<div class="detail-container">
    <div class="detail-title">${board.b_title}</div>

    <div class="meta-info">
        👤작성자: ${board.m_id} | 📅작성일: ${board.b_date} | 👁조회수: ${board.b_hit}
    </div>

    <!--건의 내용 -->
    <div class="board-content">${board.b_content}</div>

    <div class="back-btn">
        <a href="blackboardList.jsp">← 목록으로</a>
    </div>
</div>

</body>
</html>
