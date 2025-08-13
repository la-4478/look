<%@ page language="java" pageEncoding="UTF-8" isELIgnored="false" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />

<%
    String m_id = (String) session.getAttribute("loginUserId");
    if (m_id == null) {
        response.sendRedirect(request.getContextPath() + "/member/loginForm.do");
        return;
    }
%>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>내 찜 목록</title>
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    <link rel="stylesheet" href="${contextPath}/resources/css/mypage.css"/>
</head>
<body>
<div class="container mt-4">
    <h2>내 찜 목록</h2>

    <div class="row">
        <c:choose>
            <c:when test="${not empty wishlist}">
                <c:forEach var="wish" items="${wishlist}">
                    <div class="col-md-3 col-sm-6 mb-4">
                        <div class="product-card">
                            <img src="${contextPath}/resources/image/${wish.g_image}" alt="${wish.g_name}" class="product-image" />

                            <div class="product-name">${wish.g_name}</div>
                            <div class="product-price">
                                <fmt:formatNumber value="${wish.price}" type="currency" currencySymbol="₩" />
                            </div>

                            <div class="mt-2 d-flex justify-content-center gap-2">
                                <a href="${contextPath}/goodsDetail.do?g_id=${wish.g_id}" class="btn btn-primary btn-small">상세보기</a>

                                <form action="${contextPath}/wishlist/removeWish.do" method="post" style="display:inline;">
                                    <input type="hidden" name="g_id" value="${wish.g_id}" />
                                    <button type="submit" class="btn btn-danger btn-small">삭제</button>
                                </form>
                            </div>
                        </div>
                    </div>
                </c:forEach>
            </c:when>
            <c:otherwise>
                <p class="text-center mt-4">찜한 상품이 없습니다.</p>
            </c:otherwise>
        </c:choose>
    </div>
</div>
</body>
</html>
