<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
<div class="my-coupons-container">
    <h2>내 쿠폰 목록</h2>
    
    <c:choose>
        <c:when test="${not empty myCoupons}">
            <table class="coupon-table">
                <thead>
                    <tr>
                        <th>쿠폰 코드</th>
                        <th>할인 금액</th>
                        <th>최대 할인</th>
                        <th>최소 구매</th>
                        <th>유효 기간</th>
                        <th>사용 여부</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="coupon" items="${myCoupons}">
                        <tr>
                            <td>${coupon.promoCode}</td>
                            <td> <fmt:formatNumber value="${Math.floor(coupon.promoDiscountValue)}" pattern="0" />%</td>
                            <td>${coupon.promoMaxDiscount}원</td>
                            <td>${coupon.promoMinPurchase}원</td>
                            <td>
							    <fmt:formatDate value="${coupon.promoStartDate}" pattern="yyyy-MM-dd" />
							    ~
							    <fmt:formatDate value="${coupon.promoEndDate}" pattern="yyyy-MM-dd" />
							</td>
                            <td>
                                <c:choose>
                                    <c:when test="${not empty coupon.promoUsedDate}">
                                        사용됨
                                    </c:when>
                                    <c:otherwise>
                                        미사용
                                    </c:otherwise>
                                </c:choose>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </c:when>
        <c:otherwise>
            <p>발급받은 쿠폰이 없습니다.</p>
        </c:otherwise>
    </c:choose>
</div>
</body>
</html>