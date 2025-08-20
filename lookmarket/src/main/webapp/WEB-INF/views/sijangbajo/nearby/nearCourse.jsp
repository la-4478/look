<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />

<h2>📌추천 코스 모아보기</h2>

<div class="course-container">
  <c:forEach var="course" items="${courseList}">
    <div class="course-card">
      <c:choose>
        <c:when test="${not empty course.image}">
          <img src="${course.image}" alt="코스 이미지" />
        </c:when>
        <c:otherwise>
          <img src="${contextPath}/resources/image/default-course.jpg" alt="기본 이미지" />
        </c:otherwise>
      </c:choose>

      <div class="content">
        <p>가게명 : ${course.title}</p>
        <p>주소 : ${course.address}</p>

        <div class="badge-box">
          <c:forEach var="tag" items="${fn:split(course.keywords, ',')}">
            <c:set var="trimmedTag" value="${fn:trim(tag)}" />
            <span class="badge">${trimmedTag}</span>
          </c:forEach>
        </div>
      </div>
    </div>
  </c:forEach>
</div>
