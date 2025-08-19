<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ taglib prefix="c" uri="jakarta.tags.core"%>
    <c:set var="contextPath" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>1:1 문의</title>
</head>
<body>
<form action="${contextPath}/inquiry/insert.do" method="POST">
	<div>
		<label for="m_id" class="form-label">작성자아이디</label>
		<input type="text" name="m_id" value="${memberInfo.m_id }" readonly>
	</div>
	<div>
		<label for="title" class="form-label">제목</label>
		<input type="text" name="title" class="form-control">
	</div>
	<div>
		<label for="question" class="form-label">문의 내용</label>
		<textarea name="question" cols="3" rows="5" class="form-control"></textarea>
	</div>
	<div>
		<label for="status" class="form-label">공개여부</label>
		<input type="radio" name="status" value="0" checked>공개
		<input type="radio" name="status" value="1">비공개
	</div>
	<div>
		<input type="submit" class="submit-btn" value="문의하기">
	</div>
</form>		 
</body>
</html>