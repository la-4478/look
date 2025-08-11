<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<h2>사업자 승인 대기 목록</h2>
<c:if test="${empty pendingList}">
    <p>승인 대기중인 사업자가 없습니다.</p>
</c:if>
<c:if test="${not empty pendingList}">
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
    <c:forEach var="bm" items="${pendingList}">
        <tr>
            <td>${bm.m_id}</td>
            <td>${bm.m_name}</td>
            <td>${bm.bm_name}</td>
            <td>${bm.bm_reg_num}</td>
            <td>${bm.bm_type}</td>
            <td>
                <form action="${pageContext.request.contextPath}/admin/approveBusiness.do" method="post">
                    <input type="hidden" name="m_id" value="${bm.m_id}">
                    <button type="submit">승인</button>
                </form>
            </td>
            <td>
                <form action="${pageContext.request.contextPath}/admin/rejectBusiness.do" method="post">
                    <input type="hidden" name="m_id" value="${bm.m_id}">
                    <button type="submit">거부</button>
                </form>
            </td>
        </tr>
    </c:forEach>
</table>
</c:if>