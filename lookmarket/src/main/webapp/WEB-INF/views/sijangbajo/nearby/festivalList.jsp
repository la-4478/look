<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>


<head>
    <title>지역 축제 검색</title>
    <style>
        body { font-family: Arial, sans-serif; margin: 20px; }
        .festival { border: 1px solid #ccc; padding: 10px; margin-bottom: 15px; }
        .festival img { max-width: 300px; height: auto; }
    </style>
</head>
<body>

<h2>지역 축제 검색</h2>

<!-- ✅ 검색 폼 -->
<form method="get" action="${pageContext.request.contextPath}/sijangbajo/nearby/festivalList.do">

    <label for="areaCode">지역 선택:</label>
    <select name="areaCode" id="areaCode">
        <option value="">-- 지역 선택 --</option>
        <option value="1" ${areaCode == '1' ? 'selected' : ''}>서울</option>
        <option value="2" ${areaCode == '2' ? 'selected' : ''}>인천</option>
        <option value="3" ${areaCode == '3' ? 'selected' : ''}>대전</option>
        <option value="4" ${areaCode == '4' ? 'selected' : ''}>대구</option>
        <option value="5" ${areaCode == '5' ? 'selected' : ''}>광주</option>
        <option value="6" ${areaCode == '6' ? 'selected' : ''}>부산</option>
        <option value="7" ${areaCode == '7' ? 'selected' : ''}>울산</option>
        <option value="8" ${areaCode == '8' ? 'selected' : ''}>세종</option>
        <option value="31" ${areaCode == '31' ? 'selected' : ''}>경기도</option>
        <option value="32" ${areaCode == '32' ? 'selected' : ''}>강원도</option>
        <option value="33" ${areaCode == '33' ? 'selected' : ''}>충청북도</option>
        <option value="34" ${areaCode == '34' ? 'selected' : ''}>충청남도</option>
        <option value="35" ${areaCode == '35' ? 'selected' : ''}>경상북도</option>
        <option value="36" ${areaCode == '36' ? 'selected' : ''}>경상남도</option>
        <option value="37" ${areaCode == '37' ? 'selected' : ''}>전라북도</option>
        <option value="38" ${areaCode == '38' ? 'selected' : ''}>전라남도</option>
        <option value="39" ${areaCode == '39' ? 'selected' : ''}>제주도</option>
    </select>
    <button type="submit">검색</button>
</form>

<hr/>

<!-- ✅ 축제 결과 리스트 -->
<c:if test="${not empty festivalList}">
    <c:forEach var="festival" items="${festivalList}">
        <div class="festival">
            <h3>${festival.title}</h3>
            <p><strong>주소:</strong> ${festival.address}</p>

            <!-- 날짜 포맷 처리 -->
            <c:if test="${not empty festival.eventStartDate && not empty festival.eventEndDate}">
                <fmt:parseDate value="${festival.eventStartDate}" pattern="yyyyMMdd" var="startDate" />
                <fmt:parseDate value="${festival.eventEndDate}" pattern="yyyyMMdd" var="endDate" />
                <p><strong>기간:</strong>
                    <fmt:formatDate value="${startDate}" pattern="yyyy-MM-dd" /> ~
                    <fmt:formatDate value="${endDate}" pattern="yyyy-MM-dd" />
                </p>
            </c:if>

            <!-- 이미지 처리 -->
            <c:choose>
                <c:when test="${not empty festival.image}">
                    <img src="${festival.image}" alt="${festival.title}" />
                </c:when>
                <c:otherwise>
                    <img src="/images/no-image.png" alt="이미지 없음" />
                </c:otherwise>
            </c:choose>
        </div>
    </c:forEach>
</c:if>

<!-- ✅ 결과 없음 -->
<c:if test="${empty festivalList}">
    <p>검색된 축제가 없습니다.</p>
</c:if>

</body>

