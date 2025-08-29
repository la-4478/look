<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<link rel="stylesheet" href="${contextPath}/resources/css/admin.css">

<div class="approval-wrap">
  <h2 class="approval-title">사업자 승인 대기 목록</h2>

  <c:if test="${not empty msg}">
    <div class="approval-alert info">${msg}</div>
  </c:if>

  <c:choose>
    <c:when test="${empty approvalList}">
      <div class="approval-table-card approval-empty">승인 대기중인 사업자가 없습니다.</div>
    </c:when>
    <c:otherwise>
      <div class="approval-table-card">
        <table class="approval-table">
          <thead>
            <tr>
              <th>아이디</th>
              <th>연락처</th>
              <th>상호명</th>
              <th>사업자번호</th>
              <th>업종</th>
              <th>상태</th>
              <th>작업</th>
            </tr>
          </thead>
          <tbody>
            <c:set var="hasPending" value="false" />
            <c:forEach var="data" items="${approvalList}">
              <c:forEach var="biz" items="${data.pendingList}">
                <c:if test="${biz.bm_status == 1}">
                  <c:set var="hasPending" value="true" />
                  <tr>
                    <td>${data.member.m_id}</td>
                    <td class="approval-sub">${data.member.m_phone}</td>
                    <td>${biz.bm_name}</td>
                    <td>${biz.bm_reg_num}</td>
                    <td>${biz.bm_type}</td>
                    <td><span class="approval-badge wait">승인 대기</span></td>
                    <td>
                      <form action="${contextPath}/admin/approveBusiness.do" method="post" style="display:inline">
                        <input type="hidden" name="m_id" value="${biz.m_id}">
                        <button class="approval-btn primary" type="submit">승인</button>
                      </form>
                      <form action="${contextPath}/admin/rejectBusiness.do" method="post" style="display:inline">
                        <input type="hidden" name="m_id" value="${biz.m_id}">
                        <button class="approval-btn danger" type="submit">거부</button>
                      </form>
                    </td>
                  </tr>
                </c:if>
              </c:forEach>
            </c:forEach>

            <c:if test="${not hasPending}">
              <tr>
                <td colspan="7" class="approval-empty">승인 대기중인 사업자가 없습니다.</td>
              </tr>
            </c:if>
          </tbody>
        </table>
      </div>
    </c:otherwise>
  </c:choose>
</div>
