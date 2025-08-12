<%@ page language="java" pageEncoding="UTF-8" isELIgnored="false" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<%
    String m_id = (String) session.getAttribute("loginUserId");
%>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8" />
    <title>${goods.g_name} - 상품 상세</title>
<link href="${contextPath}/resources/css/goods.css" rel="stylesheet" type="text/css">
</head>
<body>
<div class="detail-container">

    <!-- 상단: 상품 이미지 + 정보 -->
    <div class="top-section">
        <img src="http://localhost:8090/lookmarket/file/${goods.g_id}/${goods.i_filename}" alt="${goods.g_name}" />

        <div class="product-info">
            <h2>${goods.g_name}</h2>

            <!-- 찜 버튼 -->
            <button
                id="wishBtn"
                data-gid="${goods.g_id}"
                <c:if test="${empty m_id}">disabled class="disabled" title="로그인 후 이용 가능"</c:if>>
					<c:choose>
						<c:when test="${fn:contains(myWishList, goods.g_id)}">❤️</c:when>
						<c:otherwise>🤍</c:otherwise>
					</c:choose>
				</button>

            <p><strong>브랜드:</strong> ${goods.g_brand}</p>

            <div class="price">
                <del><fmt:formatNumber value="${goods.g_price}" type="currency" currencySymbol="₩" /></del>
            </div>

            <form action="${contextPath}/cart/addCartItem.do" method="post">
                <input type="hidden" name="g_id" value="${goods.g_id}" />
                <label>수량:
                    <input type="number" name="qty" class="form-control" value="1" min="1" max="${goods.g_stock}" />
                </label>
                <button type="submit" class="btn btn-success">장바구니 담기</button>
            </form>

            <div class="mt-3">
                <p><strong>재고:</strong> ${goods.g_stock} 개</p>
                <p><strong>입고일:</strong> ${goods.g_credate}</p>
                <p><strong>제조일자:</strong> ${goods.g_manufactured_date}</p>
                <p><strong>유통기한:</strong> ${goods.g_expiration_date}</p>	
                <p><strong>배송비:</strong>
                    <c:choose>
                        <c:when test="${goods.g_delivery_price == 0}">무료배송</c:when>
                        <c:otherwise><fmt:formatNumber value="${goods.g_delivery_price}" type="currency" currencySymbol="₩" /></c:otherwise>
                    </c:choose>
                </p>
                <p><strong>상태:</strong>
                    <c:choose>
                        <c:when test="${goods.g_status == 1}">판매중</c:when>
                        <c:when test="${goods.g_status == 2}">품절</c:when>
                        <c:when test="${goods.g_status == 3}">판매종료</c:when>
                        <c:otherwise>미정</c:otherwise>
                    </c:choose>
                </p>
            </div>
        </div>
    </div>

    <!-- 하단: 상세 이미지 반복 출력 -->
    <div class="bottom-section">
        <h4 class="mt-5 mb-3">상세 설명</h4>
        <p>${goods.g_discription}</p>

        <c:forEach var="img" items="${detailImageList}">
            <img src="${contextPath}/resources/image/${img}" alt="상세 이미지" />
        </c:forEach>
    </div>

</div>

<script>
    $(function() {
        $('#wishBtn').click(function() {
            if ($(this).prop('disabled')) return;

            const btn = $(this);
            const g_id = btn.data('gid');

            $.ajax({
                url: '${contextPath}/wishlist/toggle.do',
                method: 'POST',
                data: { g_id: g_id },
                success: function(result) {
                    if (result === 'added') {
                        btn.html('❤️');
                    } else if (result === 'removed') {
                        btn.html('🤍');
                    }
                },
                error: function() {
                    alert('찜 처리 중 오류 발생');
                }
            });
        });
    });
</script>

</body>
</html>
