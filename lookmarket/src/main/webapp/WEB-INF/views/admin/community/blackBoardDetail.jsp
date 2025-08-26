<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>고충방 상세보기</title>
<link href="${contextPath}/resources/css/community.css" rel="stylesheet" type="text/css">
</head>
<body>
<div class="detail-container">
    <div class="detail-title">${blackBoard.b_title}</div>

    <div class="meta-info">
        👤 작성자: ${blackBoard.m_id} | 📅 작성일: ${blackBoard.b_date} | 👁 조회수: ${blackBoard.b_hit}
    </div>

    <div class="board-content">${blackBoard.b_content}</div>
	
	<div class="board-comment">
		<textarea></textarea>
	</div>
    <c:if test="${currentUserId == blackBoard.m_id}">
        <div class="update-btn-wrap">
            <a href="${contextPath}/community/blackBoardUpdateForm.do?b_id=${blackBoard.b_id}" class="update-btn">수정하기</a>

            <!-- ✅ 삭제 버튼 추가 -->
            <form action="${contextPath}/community/blackBoardDelete.do" method="post" style="display:inline;" onsubmit="return confirm('정말 삭제하시겠습니까?');">
                <input type="hidden" name="b_id" value="${blackBoard.b_id}" />
                <button type="submit" class="delete-btn">삭제하기</button>
            </form>
        </div>
    </c:if>

    <div class="back-btn">
        <a href="${contextPath}/community/blackBoardList.do">← 목록으로</a>
    </div>
</div>
</body>
</html>
