<%@ page language="java" pageEncoding="UTF-8" isELIgnored="false"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>회원 전체 정보</title>
<style>
  table { width:100%; border-collapse:collapse; }
  th {font-size : 0.8rem;}
  th, td { border:1px solid #ddd; padding:8px; }
  thead th { background:#f8f9fa; }
  .center { text-align:center; }
  .muted { color:#777; font-size:0.9em; }
  .badge { padding:2px 6px; border-radius:4px; background:#eee; }
</style>
</head>
<body>
  <h2>회원 전체 정보</h2>

  <table>
    <thead>
      <tr>
        <th>회원번호(ID)</th>
        <th>이름</th>
        <th>성별</th>
        <th>생년월일</th>
        <th>휴대폰</th>
        <th>이메일</th>
        <th>레벨</th>
        <th>권한</th>
        <th>가입일</th>
        <th>탈퇴일</th>
        <th>상태</th>
      </tr>
    </thead>
    <tbody>
      <c:choose>
        <c:when test="${not empty memberList}">
          <c:forEach var="m" items="${memberList}">
            <tr>
              <td class="center">${m.m_id}</td>
              <td>${m.m_name}</td>

              <!-- 성별: 1=남, 2=여 (다르면 '기타') -->
              <td class="center" >
                <c:choose>
                  <c:when test="${m.m_gender == 1}">남</c:when>
                  <c:when test="${m.m_gender == 2}">여</c:when>
                  <c:otherwise>기타</c:otherwise>
                </c:choose>
              </td>

              <td class="center">${m.m_birth}</td>

              <!-- 휴대폰: 수신동의 여부 뱃지 + 값 -->
              <td>
                <span class="badge"  style="color:blue;">
                  <c:choose>
                    <c:when test="${m.m_phone_yn == 1}">수신 동의</c:when>
                    <c:otherwise>미인증</c:otherwise>
                  </c:choose>
                </span>
                <span class="muted">${m.m_phone}</span>
              </td>

              <!-- 이메일: 수신동의 여부 뱃지 + 값 -->
              <td>
                <span class="badge" style="color:blue;">
                  <c:choose>
                    <c:when test="${m.m_email_yn == 1}">수신 동의</c:when>
                    <c:otherwise>미인증</c:otherwise>
                  </c:choose>
                </span>
                <span class="muted">${m.m_email}</span>
              </td>

              <td class="center">${m.m_level}</td>

              <!-- 권한: 숫자 → 라벨 (예시: 1=USER, 2=BUSINESS, 9=ADMIN) -->
              <td class="center">
                <c:choose>
                  <c:when test="${m.m_role == 9}">ADMIN</c:when>
                  <c:when test="${m.m_role == 2}">BUSINESS</c:when>
                  <c:otherwise>USER</c:otherwise>
                </c:choose>
              </td>

              <td class="center">${m.m_joindate}</td>
              <td class="center">
                <c:out value="${empty m.m_outdate ? '-' : m.m_outdate}"/>
              </td>

              <!-- 상태: 탈퇴일 존재하면 WITHDRAWN, 아니면 ACTIVE -->
              <td class="center">
                <c:choose>
                  <c:when test="${not empty m.m_outdate}">탈퇴</c:when>
                  <c:otherwise>활동중</c:otherwise>
                </c:choose>
              </td>
            </tr>
          </c:forEach>
        </c:when>
        <c:otherwise>
          <tr>
            <td colspan="11" class="center">회원이 없습니다.</td>
          </tr>
        </c:otherwise>
      </c:choose>
    </tbody>
  </table>
</body>
</html>
