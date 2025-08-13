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
<script>
  // 페이지 어디든 contextPath 쓰고 있으면 맞춰 사용
  var ctx = "${contextPath}"; // 없으면 ""로 둬도 됨

  // (선택) Spring Security CSRF 쓰면 메타태그에서 읽어 헤더 세팅
  var csrfHeader = $('meta[name="_csrf_header"]').attr('content');
  var csrfToken  = $('meta[name="_csrf"]').attr('content');

  // 폼 submit 가로채서 AJAX로 전송
  $(document).on('submit', 'form[action$="/cart/addCartItem.do"]', function (e) {
    e.preventDefault();

    const $form = $(this);
    const gId   = $form.find('input[name="g_id"]').val();
    const qty   = $form.find('input[name="qty"]').val() || 1; // 기본 1

    // 유효성 체크 살짝
    if (!gId) { alert("상품ID가 없습니다."); return; }
    if (Number(qty) <= 0) { alert("수량은 1 이상이어야 합니다."); return; }

    $.ajax({
      url: ctx + "/cart/addCartItem.do",
      method: "POST",
      data: {
        g_id: gId,
        c_qty: qty // ★ qty → c_qty로 매핑해서 보냄 (CartVO 바인딩용)
      },
      beforeSend: function (xhr) {
        if (csrfHeader && csrfToken) xhr.setRequestHeader(csrfHeader, csrfToken);
      },
      success: function (res) {
        // 컨트롤러가 "success" 문자열을 리턴
        if (typeof res === 'string' && res.trim() === 'success') {
          alert("장바구니에 담겼습니다 🛒");
          // 필요하면 장바구니 배지 갱신, 모달 열기 등 여기서 처리
          // ex) $('#cartCount').text(parseInt($('#cartCount').text()) + Number(qty));
        } else {
          alert("담기 처리에 실패했습니다. 다시 시도해 주세요.");
          console.warn("addCartItem response:", res);
        }
      },
      error: function (xhr) {
        if (xhr.status === 401) {
          alert("로그인이 필요합니다.");
          // location.href = ctx + "/member/loginForm.do";
          return;
        }
        alert("서버 오류로 실패했습니다.");
        console.error("addCartItem error:", xhr);
      }
    });
  });
</script>
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
