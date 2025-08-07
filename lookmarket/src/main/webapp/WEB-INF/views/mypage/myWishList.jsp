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
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" />
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    <style>
        .product-card {
            border: 1px solid #ddd;
            border-radius: 8px;
            padding: 10px;
            margin-bottom: 20px;
            text-align: center;
            transition: box-shadow 0.3s;
            height: 100%;
        }

        .product-card:hover {
            box-shadow: 0 4px 8px rgba(0,0,0,0.1);
        }

        .product-image {
            height: 160px;
            object-fit: cover;
            width: 100%;
            border-radius: 5px;
        }

        .product-name {
            font-weight: bold;
            margin-top: 10px;
        }

        .product-price {
            color: #d9534f;
            margin-top: 5px;
            font-size: 16px;
        }

        .btn-small {
            padding: 4px 8px;
            font-size: 12px;
        }

        .top-right {
            text-align: right;
            margin-bottom: 15px;
        }
    </style>
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
