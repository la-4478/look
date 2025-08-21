<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<h2>문의 상세</h2>
<p><b>제목:</b> ${inquiry.title}</p>
<p><b>질문:</b> ${inquiry.question}</p>
<p><b>상태:</b> 
<c:if test="${inquiry.status == 0 }"> 미답변 </c:if>
<c:if test="${inquiry.status == 1 }"> 답변완료 </c:if></p>
<hr/>

<c:choose>
  <c:when test="${not empty inquiry.answer}">
    <p><b>답변:</b> ${inquiry.answer}</p>
  </c:when>
  <c:otherwise>
    <p>아직 답변이 등록되지 않았습니다.</p>
  </c:otherwise>
</c:choose>

<!-- 관리자만 답변 입력 -->
<c:if test="${role == 3}">
<c:if test="${not empty inquiry.answer}">
  <form method="post" action="${pageContext.request.contextPath}/inquiry/answer.do?inquiryId=#{inquiry.InquiryId}">
    <textarea name="answer" rows="5" cols="50"></textarea><br>
    <button type="submit">답변 등록</button>
  </form>
  </c:if>
</c:if>
