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
    <div class="detail-title">${blackboard.b_title}</div>

    <div class="meta-info">
        👤작성자: ${blackboard.m_id} | 📅작성일: ${blackboard.b_date} | 👁조회수: ${blackboard.b_hit}
    </div>

    <!--건의 내용 -->
    <div class="board-content">${blackboard.b_content}</div>

    <div class="back-btn">
        <a href="blackboardList.jsp">← 목록으로</a>
    </div>
</div>

</body>
</html>
