<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>고충방 상세보기</title>
</head>
    <style>
        .detail-container {
            width: 800px;
            margin: 50px auto;
            padding: 30px;
            border: 1px solid #ccc;
            border-radius: 10px;
            background-color: #fefefe;
        }

        .detail-title {
            font-size: 24px;
            font-weight: bold;
            margin-bottom: 10px;
        }

        .meta-info {
            font-size: 14px;
            color: #666;
            margin-bottom: 15px;
        }

        .board-content {
            font-size: 16px;
            line-height: 1.6;
            white-space: pre-wrap; /* 줄바꿈 유지 */
        }

        .back-btn {
            margin-top: 30px;
            text-align: center;
        }

        .back-btn a {
            padding: 8px 16px;
            background-color: #3366cc;
            color: white;
            text-decoration: none;
            border-radius: 5px;
        }

        .back-btn a:hover {
            background-color: #254a9a;
        }
    </style>


<body>
<div class="detail-container">
    <div class="detail-title">${board.b_title}</div>

    <div class="meta-info">
        👤작성자: ${board.m_id} | 📅작성일: ${board.b_date} | 👁조회수: ${board.b_hit}
    </div>

    <!-- 공개 여부 -->
    <div class="meta-info">
        <c:choose>
            <c:when test="${board.b_secret eq 1}">🔓공개</c:when>
            <c:when test="${board.b_secret eq 2}">🔓비공개</c:when>
            <c:otherwise>❓알 수 없음</c:otherwise>
        </c:choose>
    </div>

    <!--건의 내용 -->
    <div class="board-content">${board.b_content}</div>

    <div class="back-btn">
        <a href="blackboardList.jsp">← 목록으로</a>
    </div>
</div>

</body>
</html>
