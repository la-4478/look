<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="contextPath" value="${pageContext.request.contextPath}"/>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>사장님 고충방 글쓰기</title>
<title>고충방 글쓰기</title>
<link href="${contextPath}/resources/css/community.css" rel="stylesheet" type="text/css">

</head>
<body class="blackwrite">

	<div class="write-container">
		<h2>사장님 고충방 글쓰기</h2>

		<form action="${pageContext.request.contextPath}/community/insertBlackBoard.do" method="post">
			<p><strong>작성자:</strong> ${sessionScope.current_id}</p>
    		<input type="hidden" name="m_id" value="${sessionScope.current_id}" />
			<div class="form-group">
				<label for="b_title">제목</label> <input type="text" id="b_title"
					name="b_title" required>
			</div>
			<div class="form-group">
				<label for="b_content">내용</label>
				<textarea id="b_content" name="b_content" required></textarea>
			</div>

        <div class="form-buttons">
            <input type="submit" value="등록">
            <a href="${pageContext.request.contextPath}/community/blackBoardList.do">목록으로</a>
        </div>
    </form>
</div>


</body>
</html>
