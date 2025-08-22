<%@ page language="java" pageEncoding="UTF-8" isELIgnored="false"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="contextPath" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>쿠폰 리스트</title>
<link href="${contextPath}/resources/css/event.css" rel="stylesheet" type="text/css">
</head>
<body>
<div class="container">
    <h2>쿠폰 리스트</h2>

    <c:if test="${not empty couponList}">
    <div class="table-wrapper">
        <table class="coupon-table">
            <thead>
                <tr>
                    <th>쿠폰 코드</th>
                    <th>할인 유형</th>
                    <th>할인 수치</th>
                    <th>최대 할인 금액</th>
                    <th>최소 구매 금액</th>
                    <th>사용 기간</th>
                    <th>활성 상태</th>
                    <th>상세보기</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="coupon" items="${couponList}">
                    <tr>
                        <td>${coupon.promoCode}</td>
                        <td>
                            <c:choose>
                                <c:when test="${coupon.promoDiscountType == 1}">정률 할인 (%)</c:when>
                                <c:when test="${coupon.promoDiscountType == 2}">정액 할인 (₩)</c:when>
                                <c:otherwise>기타</c:otherwise>
                            </c:choose>
                        </td>
                        <td>
                            <c:choose>
                                <c:when test="${coupon.promoDiscountType == 1}">
                                    ${coupon.promoDiscountValue}%
                                </c:when>
                                <c:when test="${coupon.promoDiscountType == 2}">
                                    <fmt:formatNumber value="${coupon.promoDiscountValue}" type="currency" currencySymbol="₩"/>
                                </c:when>
                                <c:otherwise>-</c:otherwise>
                            </c:choose>
                        </td>
                        <td>
                            <c:choose>
                                <c:when test="${coupon.promoMaxDiscount != null && coupon.promoMaxDiscount > 0}">
                                    <fmt:formatNumber value="${coupon.promoMaxDiscount}" type="currency" currencySymbol="₩"/>
                                </c:when>
                                <c:otherwise>-</c:otherwise>
                            </c:choose>
                        </td>
                        <td>
                            <fmt:formatNumber value="${coupon.promoMinPurchase}" type="currency" currencySymbol="₩"/>
                        </td>
                        <td>
                            <fmt:formatDate value="${coupon.promoStartDate}" pattern="yyyy-MM-dd" /> ~ 
                            <fmt:formatDate value="${coupon.promoEndDate}" pattern="yyyy-MM-dd" />
                        </td>
                        <td>
                            <c:choose>
                                <c:when test="${coupon.promoCouponActive}">
                                    <span class="active">활성</span>
                                </c:when>
                                <c:otherwise>
                                    <span class="expired">비활성</span>
                                </c:otherwise>
                            </c:choose>
                        </td>
                        <td class="btn-group">
                            <a href="${contextPath}/event/couponDetail.do?promoId=${coupon.promoId}">상세보기</a>
                            <c:if test="${isLogOn eq true and memberInfo.m_role == 3}">
						        <a href="${contextPath}/event/couponUpdateForm.do?promoId=${coupon.promoId}">수정</a>
						        <a href="${contextPath}/event/deleteCoupon.do?promoId=${coupon.promoId}${not empty postId ? '&postId=' += postId : ''}"
						           onclick="return confirm('삭제하시겠습니까?')">삭제</a>
						    </c:if>
                        </td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
      </div>
    </c:if>
    <c:if test="${empty couponList}">
        <p>등록된 쿠폰이 없습니다.</p>
    </c:if>
    <c:if test="${isLogOn eq true and memberInfo.m_role == 3}">
        <div class="action-buttons">
            <a href="${contextPath}/event/promotionList.do" class="btn-return">프로모션 목록으로 돌아가기</a>
            <a href="${contextPath}/event/couponAddForm.do?postId=${postId}" class="btn-add">쿠폰 등록</a>
        </div>
    </c:if>
</div>
</body>
</html>
