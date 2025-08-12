<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>고충방 상세보기</title>
<link href="${contextPath}/resources/css/community.css" rel="stylesheet" type="text/css">
<link href="${contextPath}/resources/css/custom.css" rel="stylesheet" type="text/css">
</head>
<body>
<div class="detail-container">
    <div class="detail-title">${blackboard.b_title}</div>

    <div class="meta-info">
        👤 작성자: ${blackboard.m_id} | 📅 작성일: ${blackboard.b_date} | 👁 조회수: ${blackboard.b_hit}
    </div>

    <div class="board-content">${blackboard.b_content}</div>

    <c:if test="${currentUserId == blackboard.m_id}">
        <div class="update-btn-wrap">
            <a href="${contextPath}/community/blackBoardUpdateForm.do?b_id=${blackboard.b_id}" class="update-btn">수정하기</a>
        </div>
    </c:if>

    <div class="back-btn">
        <a href="${contextPath}/community/blackBoardList.do">← 목록으로</a>
    </div>
</div>
</body>
</html>
