<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<c:set var="contextPath" value="${pageContext.request.contextPath}" />
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

/* 반응형 */
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
}

</style>
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
