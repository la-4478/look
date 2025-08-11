<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
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
            <th>작성자</th>
            <th>작성일</th>
            <th>조회수</th>
        </tr>
    </thead>
    <tbody>
        <c:forEach var="board" items="${blackboardList}">
            <tr>
                <td>${board.b_id}</td>
                <td class="title-cell">
                    <a href="blackboardDetail.jsp?b_id=${board.b_id}">
                        <c:if test="${board.b_secret == 2}">
                            <span class="lock-icon">🔒</span>
                        </c:if>
                        ${board.b_title}
                    </a>
                </td>
                <td>${board.m_id}</td>
                <td>${board.b_date}</td>
                <td>${board.b_hit}</td>
            </tr>
        </c:forEach>
    </tbody>
</table>

</body>
</html>
