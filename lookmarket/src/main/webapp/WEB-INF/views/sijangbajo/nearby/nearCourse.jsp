<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />

<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8" />
  <title>추천 코스 목록</title>
  <link rel="stylesheet" href="${contextPath}/resources/css/sijang.css"/>
  <style>

  </style>

  <!-- Font Awesome for icons (CDN) -->
  <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css" />
</head>
<body class="course">

<h2>📌추천 코스 모아보기</h2>

<div class="course-container">
  <c:forEach var="course" items="${courseList}">
    <div class="course-card">
      <c:choose>
        <c:when test="${not empty course.image}">
          <img src="${contextPath}/upload/${course.image}" alt="코스 이미지" />
        </c:when>
        <c:otherwise>
          <img src="${contextPath}/resources/image/default-course.jpg" alt="기본 이미지" />
        </c:otherwise>
      </c:choose>

      <div class="content">
        <h4>${course.title}</h4>
        <p>${course.description}</p>
        <p><strong>총 소요 시간:</strong> ${course.duration}</p>

        <!-- 키워드 뱃지 -->
        <div class="badge-box">
          <c:forEach var="tag" items="${fn:split(course.keywords, ',')}">
            <c:set var="trimmedTag" value="${fn:trim(tag)}" />
            <c:choose>
              <c:when test="${trimmedTag == '먹거리'}">
                <span class="badge food"><i class="fas fa-utensils"></i>${trimmedTag}</span>
              </c:when>
              <c:when test="${trimmedTag == '가족'}">
                <span class="badge family"><i class="fas fa-people-roof"></i>${trimmedTag}</span>
              </c:when>
              <c:when test="${trimmedTag == '산책'}">
                <span class="badge walk"><i class="fas fa-person-walking"></i>${trimmedTag}</span>
              </c:when>
              <c:when test="${trimmedTag == '카페'}">
                <span class="badge cafe"><i class="fas fa-mug-hot"></i>${trimmedTag}</span>
              </c:when>
              <c:when test="${trimmedTag == '체험' || trimmedTag == '문화'}">
                <span class="badge culture"><i class="fas fa-theater-masks"></i>${trimmedTag}</span>
              </c:when>
              <c:otherwise>
                <span class="badge default"><i class="fas fa-tag"></i>${trimmedTag}</span>
              </c:otherwise>
            </c:choose>
          </c:forEach>
        </div>
      </div>
    </div>
  </c:forEach>
</div>

</body>
</html>
