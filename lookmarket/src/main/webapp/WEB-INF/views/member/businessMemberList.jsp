<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" %>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>사업자 회원 목록</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.7/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        .table th, .table td {
            vertical-align: middle;
            text-align: center;
        }
    </style>
</head>
<body>
    <div class="container mt-5">
        <h2 class="mb-4">사업자 회원 목록</h2>

        <table class="table table-bordered table-hover">
            <thead class="table-light">
                <tr>
                    <th>아이디</th>
                    <th>이름</th>
                    <th>성별</th>
                    <th>휴대폰</th>
                    <th>이메일</th>
                    <th>회원등급</th>
                    <th>회원구분</th>
                    <th>가입일</th>
                    <th>탈퇴일</th>
                    <th>관리</th>
                </tr>
            </thead>
            <tbody>
                <!-- 사업자만 출력 -->
                <c:forEach var="member" items="${memberList}">
                    <c:if test="${member.m_role == 3}">
                        <tr class="${not empty member.m_outdate ? 'table-secondary' : ''}">
                            <td>${member.m_id}</td>
                            <td>${member.m_name}</td>
                            <td>
                                <c:choose>
                                    <c:when test="${member.m_gender == 1}">남</c:when>
                                    <c:when test="${member.m_gender == 2}">여</c:when>
                                    <c:otherwise>-</c:otherwise>
                                </c:choose>
                            </td>
                            <td>${member.m_phone}</td>
                            <td>${member.m_email}</td>
                            <td>
                                <c:choose>
                                    <c:when test="${member.m_role == 1}">사용자</c:when>
                                    <c:when test="${member.m_role == 2}">관리자</c:when>
                                    <c:when test="${member.m_role == 3}">사업자</c:when>
                                    <c:otherwise>기타</c:otherwise>
                                </c:choose>
                            </td>
                            <td><fmt:formatDate value="${member.m_joindate}" pattern="yyyy-MM-dd"/></td>
                            <td>
                                <c:choose>
                                    <c:when test="${not empty member.m_outdate}">
                                        <fmt:formatDate value="${member.m_outdate}" pattern="yyyy-MM-dd"/>
                                    </c:when>
                                    <c:otherwise>-</c:otherwise>
                                </c:choose>
                            </td>
                            <td>
                                <a href="${contextPath}/admin/member/viewMember.do?m_id=${member.m_id}" class="btn btn-sm btn-primary">상세보기</a>
                            </td>
                        </tr>
                    </c:if>
                </c:forEach>
            </tbody>
        </table>

        <c:if test="${empty memberList}">
            <div class="alert alert-info text-center">등록된 사업자 회원이 없습니다.</div>
        </c:if>
    </div>
</body>
</html>
