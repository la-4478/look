<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ taglib prefix="c" uri="jakarta.tags.core" %>
   	<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<h2>사업자 승인 대기 목록</h2>
<c:if test="${not empty msg}">
    <div class="alert alert-info">${msg}</div>
</c:if>
<c:if test="${empty approvalList}">
    <p>승인 대기중인 사업자가 없습니다.</p>
</c:if>

<c:if test="${not empty approvalList}">
<table border="1">
    <tr>
        <th>아이디</th>
        <th>이름</th>
        <th>상호명</th>
        <th>사업자번호</th>
        <th>업종</th>
        <th>승인</th>
        <th>거부</th>
    </tr>
    <tr>

    <c:forEach var="data" items="${approvalList}">
    	<td>${data.member.m_id}</td>
    	<td>${data.member.m_phone}</td>
    		<c:forEach var="biz" items="${data.pendingList}">
    		<c:choose>
    			<c:when test="${biz.bm_status == 1}">
	    		<td>${biz.bm_name}</td>
		    		<td>${biz.bm_reg_num}</td>
			    		<td>${biz.bm_type}</td>
				    		<td>승인 대기</td>
				</c:when>
			    		<c:otherwise>
		        		승인 대기중인 사업자가 없습니다.
	    		</c:otherwise>
			</c:choose>
            <td>
                <form action="${contextPath}/admin/approveBusiness.do" method="post">
                    <input type="hidden" name="m_id" value="${biz.m_id}">
                    <button type="submit">승인</button>
                </form>
            </td>
            <td>
                <form action="${contextPath}/admin/rejectBusiness.do" method="post">
                    <input type="hidden" name="m_id" value="${biz.m_id}">
                    <button type="submit">거부</button>
                </form>
                <form action="${contextPath}/admin/reversBusiness.do" method="post">
                	<input type="hidden" name="m_id" value="${biz.m_id}">
                	<button type="submit">승인 되돌리기</button>
                </form>
            </td>
    </c:forEach>
    </c:forEach>
    </tr>
</table>
</c:if>