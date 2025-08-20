<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>


<h2>진행 중인 축제</h2>
<div class="festival-container">
    <c:forEach var="festival" items="${festivalList}">
        <div class="festival-card">
            <img src="${festival.image}" alt="${festival.title}" style="width: 100%; height: 200px;">
            <h3>${festival.title}</h3>
            <p>${festival.address}</p>
            <p>${festival.startDate} ~ ${festival.endDate}</p>
        </div>
    </c:forEach>
</div>

</body>
</html>