<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>클린업체 검색</title>
    <style>
        body {
            font-family: 'Noto Sans KR', sans-serif;
            padding: 20px;
        }
        .search-form {
            width: 80%;
            margin: 0 auto 30px auto;
            display: flex;
            justify-content: space-between;
            align-items: center;
            flex-wrap: wrap;
        }
        .search-form label {
            margin-bottom: 10px;
        }
        .search-form select,
        .search-form input[type="text"] {
            padding: 8px;
            font-size: 14px;
        }
        .search-form input[type="submit"] {
            padding: 8px 16px;
            background-color: #5c7cfa;
            color: white;
            border: none;
            border-radius: 4px;
            cursor: pointer;
            margin-left: 10px;
        }

        table {
            width: 80%;
            margin: 0 auto;
            border-collapse: collapse;
        }

        th, td {
            padding: 12px;
            border: 1px solid #ddd;
            text-align: left;
        }

        th {
            background-color: #e0e7ff;
        }

        caption {
            font-size: 1.4em;
            margin-bottom: 10px;
            font-weight: bold;
        }
    </style>
</head>
<body>

    <h2 style="text-align:center;">청결 우수 매장 검색</h2>

    <!-- 🔍 검색 폼 -->
    <form class="search-form" action="cleanlocation.jsp" method="get">
        <label>
            지역:
            <select name="region">
                <option value="">전체</option>
                <option value="서울특별시">서울특별시</option>
                <option value="부산광역시">부산광역시</option>
                <option value="대구광역시">대구광역시</option>
                <option value="인천광역시">인천광역시</option>
                <option value="광주광역시">광주광역시</option>
                <option value="대전광역시">대전광역시</option>
                <option value="울산광역시">울산광역시</option>
                <option value="세종특별자치시">세종특별자치시</option>
                <option value="경기도">경기도</option>
                <option value="강원도">강원도</option>
                <option value="충청북도">충청북도</option>
                <option value="충청남도">충청남도</option>
                <option value="전라북도">전라북도</option>
                <option value="전라남도">전라남도</option>
                <option value="경상북도">경상북도</option>
                <option value="경상남도">경상남도</option>
                <option value="제주특별자치도">제주특별자치도</option>
            </select>
        </label>

        <label>
            매장명:
            <input type="text" name="keyword" placeholder="매장명을 입력하세요" />
        </label>

        <input type="submit" value="검색" />
    </form>

    <!-- 📋 검색 결과 테이블 -->
    <table>
        <caption>검색 결과</caption>
        <thead>
            <tr>
                <th>매장 이름</th>
                <th>주소</th>
            </tr>
        </thead>
        <tbody>
            <c:choose>
                <c:when test="${not empty cleanList}">
                    <c:forEach var="store" items="${cleanList}">
                        <tr>
                            <td><c:out value="${store.name}" /></td>
                            <td><c:out value="${store.address}" /></td>
                        </tr>
                    </c:forEach>
                </c:when>
                <c:otherwise>
                    <tr>
                        <td colspan="2" style="text-align:center;">검색 결과가 없습니다.</td>
                    </tr>
                </c:otherwise>
            </c:choose>
        </tbody>
    </table>

</body>
</html>
