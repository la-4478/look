<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page isELIgnored="false" %>
<%@ taglib prefix="c"   uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"  %>
<%@ taglib prefix="fn"  uri="http://java.sun.com/jsp/jstl/functions" %>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>회계관리 리스트</title>
<link href="${contextPath}/resources/css/account.css" rel="stylesheet">
</head>
<body>
<div class="container">

  <h2>회계관리</h2>
  <!-- 목록 테이블 -->
  <div class="card">
    <div class="section-title">회계관리 리스트</div>
    <table>
      <thead>
        <tr>
          <th>번호</th>
          <th>계정</th>
          <th>카테고리</th>
          <th>회계 항목(메모)</th>
          <th>금액</th>
          <th>날짜</th>
          <th>구분</th>
        </tr>
      </thead>
      <tbody>
        <c:choose>
          <c:when test="${empty txnList}">
            <tr><td class="empty" colspan="7">표시할 거래가 없습니다.</td></tr>
          </c:when>
          <c:otherwise>
            <c:forEach var="row" items="${txnList}" varStatus="st">
              <tr>
                <td><c:out value="${st.index + 1 + (page-1)*pageSize}"/></td>
                <td><c:out value="${row.accountName}"/></td>
                <td>
                  <c:choose>
                    <c:when test="${not empty row.categoryName}">
                      <c:out value="${row.categoryName}"/>
                      <span class="pill"><c:out value="${row.categoryKind}"/></span>
                    </c:when>
                    <c:otherwise>-</c:otherwise>
                  </c:choose>
                </td>
                <td><c:out value="${row.memo}"/></td>
                <td>
                  <c:choose>
                    <c:when test="${row.amount >= 0}">
                      <span class="income">+ <fmt:formatNumber value="${row.amount}" type="number"/></span>
                    </c:when>
                    <c:otherwise>
                      <span class="expense">- <fmt:formatNumber value="${-row.amount}" type="number"/></span>
                    </c:otherwise>
                  </c:choose>
                </td>
                <td>
                  <c:choose>
                    <c:when test="${row.txnDate ne null}">
                      <fmt:formatDate value="${row.txnDate}" pattern="yyyy-MM-dd"/>
                    </c:when>
                    <c:otherwise><c:out value="${row.txnDate}"/></c:otherwise>
                  </c:choose>
                </td>
                <td>
                  <c:choose>
                    <c:when test="${row.categoryKind == 'INCOME' || row.amount > 0}">수입</c:when>
                    <c:when test="${row.categoryKind == 'EXPENSE' || row.amount < 0}">지출</c:when>
                    <c:otherwise><span class="transfer">이체</span></c:otherwise>
                  </c:choose>
                </td>
              </tr>
            </c:forEach>
          </c:otherwise>
        </c:choose>
      </tbody>
    </table>

    <div class="totals">
      수입: <fmt:formatNumber value="${totals.income_total}" type="number"/> 원 &nbsp; | &nbsp;
      지출: <fmt:formatNumber value="${totals.expense_total}" type="number"/> 원 &nbsp; | &nbsp;
      순익: <fmt:formatNumber value="${totals.net_total}" type="number"/> 원
    </div>

    <!-- 페이징 -->
    <div class="pager">
      <c:url var="prevUrl" value="/admin/accountList.do">
        <c:param name="page" value="${page-1}"/>
        <c:param name="pageSize" value="${pageSize}"/>
        <c:if test="${not empty param.fromDate}"><c:param name="fromDate" value="${param.fromDate}"/></c:if>
        <c:if test="${not empty param.toDate}"><c:param name="toDate" value="${param.toDate}"/></c:if>
        <c:if test="${not empty param.accountId}"><c:param name="accountId" value="${param.accountId}"/></c:if>
        <c:if test="${not empty param.categoryId}"><c:param name="categoryId" value="${param.categoryId}"/></c:if>
      </c:url>
      <c:url var="nextUrl" value="/admin/accountList.do">
        <c:param name="page" value="${page+1}"/>
        <c:param name="pageSize" value="${pageSize}"/>
        <c:if test="${not empty param.fromDate}"><c:param name="fromDate" value="${param.fromDate}"/></c:if>
        <c:if test="${not empty param.toDate}"><c:param name="toDate" value="${param.toDate}"/></c:if>
        <c:if test="${not empty param.accountId}"><c:param name="accountId" value="${param.accountId}"/></c:if>
        <c:if test="${not empty param.categoryId}"><c:param name="categoryId" value="${param.categoryId}"/></c:if>
      </c:url>
      <form method="get" action="${pageContext.request.contextPath}/admin/accountExcel.do">
		  <input type="hidden" name="fromDate" value="${param.fromDate}">
		  <input type="hidden" name="toDate"   value="${param.toDate}">
		  <button type="submit">엑셀 다운로드</button>
		</form>
      <c:if test="${page > 1}">
        <a href="${pageContext.request.contextPath}${prevUrl}">이전</a>
      </c:if>
      <span class="current">${page} / ${totalPages}</span>
      <c:if test="${page < totalPages}">
        <a href="${pageContext.request.contextPath}${nextUrl}">다음</a>
      </c:if>
    </div>
  </div>
</div>

<script>
(function(){
  const chk = document.getElementById('isTransfer');
  const cat = document.getElementById('categoryId');
  const ctr = document.getElementById('counterAccountId');
  function toggle(){
    if(!chk) return;
    if(chk.checked){
      cat.style.display='none';
      ctr.style.display='inline-block';
      // 이체는 카테고리 사용 안함
      if(cat.value) cat.value='';
    }else{
      cat.style.display='inline-block';
      ctr.style.display='none';
      // 상대계정 초기화
      if(ctr.value) ctr.value='';
    }
  }
  if(chk){ chk.addEventListener('change', toggle); toggle(); }
})();
</script>

</body>
</html>
