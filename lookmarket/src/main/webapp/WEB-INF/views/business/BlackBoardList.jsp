<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8" />
    <title>사장님 고충방</title>
    <link href="${contextPath}/resources/css/community.css" rel="stylesheet" type="text/css">
    <style>
        body {
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            background-color: #f4f6f8;
            margin: 40px;
        }

        h2 {
            text-align: center;
            color: #333;
            margin-bottom: 30px;
            font-weight: 700;
            font-size: 2em;
        }

        table {
            width: 90%;
            margin: 0 auto;
            border-collapse: collapse;
            box-shadow: 0 4px 8px rgba(0,0,0,0.1);
            background-color: white;
            border-radius: 10px;
            overflow: hidden;
        }

        thead {
            background-color: #3366cc;
            color: white;
        }

        thead th {
            padding: 15px 10px;
            font-weight: 600;
            text-align: center;
        }

        tbody tr {
            border-bottom: 1px solid #ddd;
            transition: background-color 0.3s ease;
        }

        tbody tr:hover {
            background-color: #e9f0ff;
        }

        tbody td {
            padding: 12px 10px;
            text-align: center;
            color: #555;
        }

        .title-cell {
            text-align: left;
            padding-left: 20px;
        }

        .title-cell a {
            text-decoration: none;
            color: #3366cc;
            font-weight: 600;
        }

        .title-cell a:hover {
            text-decoration: underline;
            color: #254a9a;
        }

        @media screen and (max-width: 768px) {
            table, thead, tbody, th, td, tr {
                display: block;
            }

            thead tr {
                display: none;
            }

            tbody tr {
                margin-bottom: 20px;
                border: 1px solid #ccc;
                border-radius: 8px;
                padding: 15px;
                background: white;
                box-shadow: 0 1px 4px rgba(0,0,0,0.1);
            }

            tbody td {
                text-align: right;
                padding-left: 50%;
                position: relative;
            }

            tbody td::before {
                content: attr(data-label);
                position: absolute;
                left: 15px;
                width: 45%;
                padding-left: 10px;
                font-weight: 700;
                text-align: left;
                color: #444;
            }

            .title-cell {
                padding-left: 15px;
                text-align: left;
            }
            .content-cell {
			    text-align: left;
			    max-width: 300px;
			    white-space: nowrap;
			    overflow: hidden;
			    text-overflow: ellipsis;
			}
    </style>
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
        <c:forEach var="board" items="${myBoard}">
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
<div class="d-flex justify-content-end mb-3">
<a href="${contextPath}/community/blackBoardAddForm.do" class="btn btn-primary">고충방 등록</a>
</div>
</body>
</html>
