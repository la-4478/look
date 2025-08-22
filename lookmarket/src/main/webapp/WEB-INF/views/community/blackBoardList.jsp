<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8" />
    <title>사장님 고충방</title>
    <link href="${contextPath}/resources/css/community.css" rel="stylesheet" type="text/css">
</head>
<body>

<h2>사장님 고충방</h2>

<table>
    <thead>
        <tr>
            <th>번호</th>
            <th>제목</th>
            <th>내용</th>
            <th>작성자</th>
            <th>작성일</th>
            <th>조회수</th>
        </tr>
    </thead>
    <tbody>
        <c:forEach var="board" items="${blackBoardList}">
            <tr>
                <td data-label="번호">${board.b_id}</td>
                <td data-label="제목" class="title-cell">
                    <a href="blackBoardDetail.do?b_id=${board.b_id}">
                        ${board.b_title}
                    </a>
                </td>
                <td data-label="내용" class="content-cell">
				  <c:choose>
				    <c:when test="${fn:length(board.b_content) > 10}">
				      ${fn:substring(board.b_content, 0, 10)}...
				    </c:when>
				    <c:otherwise>
				      ${board.b_content}
				    </c:otherwise>
				  </c:choose>
				</td>
                <td data-label="작성자">${board.m_id}</td>
                <td data-label="작성일">${board.b_date}</td>
                <td data-label="조회수">${board.b_hit}</td>
            </tr>
        </c:forEach>
    </tbody>
</table>

</body>
</html>
