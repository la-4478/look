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
<c:if test="${not empty message}">
  <div class="alert alert-info">${message}</div>
</c:if>
<div class="detail-container">
    <div class="detail-title">${blackBoard.b_title}</div>

    <div class="meta-info">
        👤 작성자: ${blackBoard.m_id} | 📅 작성일: ${blackBoard.b_date} | 👁 조회수: ${blackBoard.b_hit}
    </div>

    <div class="board-content">${blackBoard.b_content}</div><hr>
    <div class="board-comment">
    <h1>댓글</h1>
    <c:forEach var="c" items="${commentList}">
  	  		<div>
   			 ${c.c_m_id} : ${c.c_content}<br>작성 시간 : ${c.c_credate}
    		</div>
    	<hr>
    </c:forEach>
	</div>
	<form action="${contextPath}/inquiry/insertcomment.do" method="post">
	<div class="board-comment">
		<input type="hidden" name="b_id" value="${blackBoard.b_id}">
		<textarea name="c_content" maxlength="2000"  required></textarea>
		<input type="submit" value="작성하기">
	</div>
	</form>

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
        <a href="${contextPath}/admin/community/allBlackBoardList.do">← 목록으로</a>
    </div>
</div>
</body>
</html>
