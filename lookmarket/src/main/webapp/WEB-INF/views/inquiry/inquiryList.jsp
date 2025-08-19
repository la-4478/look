<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<c:set var="contextPath" value="${pageContext.request.contextPath}" />

<h2>내 1:1 문의 목록</h2>

<c:choose>
  <c:when test="${memberInfo.m_role == 1}">
    <table border="1" width="100%">
      <thead>
        <tr>
          <th>번호</th>
          <th>제목</th>
          <th>상태</th>
          <th>등록일</th>
        </tr>
      </thead>
      <tbody>
        <c:forEach var="inq" items="${inquiryList}">
          <tr>
            <td>${inq.inquiryId}</td>
            <td>
              <a href="${contextPath}/inquiry/detail.do?inquiryId=${inq.inquiryId}">
                ${inq.title}
              </a>
            </td>
            <td>
              <c:choose>
                <c:when test="${inq.status == 0}">미답변</c:when>
                <c:when test="${inq.status == 1}">답변완료</c:when>
                <c:otherwise>-</c:otherwise>
              </c:choose>
            </td>
            <td><fmt:formatDate value="${inq.createdAt}" pattern="yyyy-MM-dd HH:mm" timeZone="UTC"/></td>
          </tr>
        </c:forEach>
        <c:if test="${empty inquiryList}">
          <tr><td colspan="4" style="text-align:center">문의가 없습니다.</td></tr>
        </c:if>
      </tbody>
    </table>

    <p style="margin-top:10px">
      <a href="${contextPath}/inquiry/inquiryAddForm.do">문의하기</a>
    </p>
  </c:when>

  <c:when test="${memberInfo.m_role == 3}">
    <table border="1" width="100%">
      <thead>
        <tr>
          <th>번호</th>
          <th>제목</th>
          <th>등록일</th>
          <th>상태</th>
        </tr>
      </thead>
      <tbody>
        <c:forEach var="inq" items="${inquiryList}">
          <tr>
            <td>${inq.inquiryId}</td>
            <td><a href="${contextPath}/inquiry/detail.do?inquiryId=${inq.inquiryId}">${inq.title}</a></td>
            <td><fmt:formatDate value="${inq.createdAt}" pattern="yyyy-MM-dd HH:mm" timeZone="UTC"/></td>
            <td>
              <c:choose>
                <c:when test="${inq.status == 0}">
                  <span style="color:red;">미답변</span>
                </c:when>
                <c:when test="${inq.status == 1}">
                  <span style="color:blue;">답변완료</span>
                </c:when>
                <c:otherwise>-</c:otherwise>
              </c:choose>
              </td>
          </tr>
        </c:forEach>
        <c:if test="${empty inquiryList}">
          <tr><td colspan="5" style="text-align:center">처리할 문의가 없습니다.</td></tr>
        </c:if>
      </tbody>
    </table>
  </c:when>

  <c:otherwise>
    권한이 없습니다.
  </c:otherwise>
</c:choose>
