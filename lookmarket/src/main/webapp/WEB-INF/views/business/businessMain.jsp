<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />

<%-- 상태값 호환: businessStatus 우선, 없으면 이전에 쓰던 status 사용 --%>
<c:set var="bizStatus" value="${empty businessStatus ? sessionScope.businessStatus : businessStatus}" />
<c:if test="${empty bizStatus}">
  <c:set var="bizStatus" value="${empty status ? sessionScope.status : status}" />
</c:if>
<c:set var="isApproved" value="${bizStatus == '2' || bizStatus eq 'APPROVED'}" />

<link rel="stylesheet" href="${contextPath}/resources/css/business.css" />

<div class="wrap">
  <h2>사업자 정보</h2>

  <c:choose>
    <c:when test="${bizStatus == '1' || bizStatus eq 'PENDING'}">
      <div class="banner pending">승인 대기 중입니다.</div>
    </c:when>
    <c:when test="${isApproved}">
      <div class="banner approved">승인 완료되었습니다.</div>
    </c:when>
    <c:when test="${bizStatus == '3' || bizStatus eq 'REJECTED'}">
      <div class="banner rejected">승인 거부되었습니다.</div>
    </c:when>
    <c:otherwise>
      <div class="banner info">상태 정보가 없습니다.</div>
    </c:otherwise>
  </c:choose>

  <section class="section">
    <h3>기본 정보</h3>
    <table class="kv">
      <tbody>
        <tr>
          <th>아이디</th>
          <td>
            <c:choose>
              <c:when test="${not empty businessVO}">${businessVO.m_id}</c:when>
              <c:otherwise>${memberInfo.m_id}</c:otherwise>
            </c:choose>
          </td>
        </tr>
        <tr><th>상호명</th><td>${businessVO.bm_name}</td></tr>
        <tr><th>사업자등록번호</th><td>${businessVO.bm_reg_num}</td></tr>
        <tr><th>사업자 유형</th><td>${businessVO.bm_type}</td></tr>
        <tr>
          <th>승인 상태</th>
          <td>
            <c:choose>
              <c:when test="${bizStatus == '1' || bizStatus eq 'PENDING'}">대기</c:when>
              <c:when test="${isApproved}">승인</c:when>
              <c:when test="${bizStatus == '3' || bizStatus eq 'REJECTED'}">거부</c:when>
              <c:otherwise>미정</c:otherwise>
            </c:choose>
          </td>
        </tr>
      </tbody>
    </table>

    <div class="actions">
      <a class="btn" href="${contextPath}/business/profileEditForm.do">프로필 수정</a>
    </div>
  </section>

  <section class="section">
    <h3>운영</h3>
    <div class="actions toolbar">
      <a class="btn primary"
         href="${contextPath}/jangbogo/goodsAddForm.do"<c:if test="${!isApproved}">disabled</c:if>>상품 등록</a>
      <a class="btn" href="${contextPath}/jangbogo/goodsList.do?mine=true">내 상품 관리</a>
      <a class="btn" href="${contextPath}/order/businessOrders.do">주문/매출</a>
    </div>
    <p class="note">
      ※ 승인 완료 후에만 상품 등록이 가능합니다.
    </p>
  </section>
</div>
