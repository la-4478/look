<%@ page language="java" pageEncoding="UTF-8" isELIgnored="false"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<%
String role = (String) session.getAttribute("loginUserRole");
String m_id = (String) session.getAttribute("loginUserId");
%>


<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>상품 목록</title>
<link href="${contextPath}/resources/css/goods.css" rel="stylesheet"
	type="text/css">
<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
</head>
<body>
	<c:if test="${not empty msg}">
		<div class="alert alert-info">${msg}</div>
	</c:if>
	<div class="container mt-4">
		<c:if
			test="${isLogOn==true and not empty memberInfo and memberInfo.m_role == 3}">
			<div class="top-right">
				<a href="${contextPath}/jangbogo/goodsAddForm.do"
					class="btn btn-primary">상품 등록</a>
			</div>
		</c:if>
		<c:if test="${businessStatus == '2' || businessStatus eq 'APPROVED'}">
			<div>
				<a href="${contextPath}/jangbogo/goodsAddForm.do"
					class="btn btn-primary">상품 등록</a>
			</div>
		</c:if>


		<c:choose>
			<c:when test="${not empty goodsMap}">
				<c:forEach var="entry" items="${goodsMap}">
					<c:set var="category" value="${entry.key}" />
					<c:set var="goodsList" value="${entry.value}" />

					<h4>
						<c:choose>
							<c:when test="${category == 'fresh'}">신선식품</c:when>
							<c:when test="${category == 'processed'}">가공식품</c:when>
							<c:when test="${category == 'living'}">생활용품</c:when>
							<c:when test="${category == 'fashion'}">패션잡화</c:when>
							<c:when test="${category == 'local'}">지역특산물</c:when>
							<c:otherwise>기타</c:otherwise>
						</c:choose>
						<a href="${contextPath}/goodsList.do?category=${category}"
							class="btn btn-sm btn-outline-secondary float-end">더보기</a>
					</h4>

					<div class="row">
						<c:forEach var="goods" items="${goodsList}">
							<c:set var="firstImage"
								value="${fn:split(goods.i_filename, ',')[0]}" />

							<div class="col-md-3 col-sm-6 mb-4">
								<div class="product-card">
									<img src="${contextPath}/resources/image/${goods.i_filename}"
										alt="${goods.g_name}" class="product-image" />

									<div class="product-name">${goods.g_name}</div>
									<div class="product-price">
										<fmt:formatNumber value="${goods.g_price}" type="currency"
											currencySymbol="₩" />
									</div>

									<div
										class="mt-2 d-flex justify-content-center align-items-center gap-2">
										<a
											href="${contextPath}/jangbogo/goodsDetail.do?g_id=${goods.g_id}"
											class="btn btn-primary btn-small">상세보기</a>

										<button class="wish-btn ${empty m_id == 'disabled'}" 
									        data-gid="${goods.g_id}" 
									        ${empty m_id}>
								       	 <span class="wish-icon">
									            <c:choose>
									                <c:when test="${myWishList != null && myWishList.contains(goods.g_id)}">
									                    <img src="${contextPath}/resources/image/like_on.png" alt="찜목록 추가됨">
									                </c:when>
									                <c:otherwise>
									                    <img src="${contextPath}/resources/image/like.png" alt="찜목록 추가하기">
									                </c:otherwise>
									            </c:choose>
									        </span>
										</button>

									</div>

									<c:if test="<%=\"admin\".equals(role)%>">
										<div class="mt-2">
											<a
												href="${contextPath}/goodsUpdateForm.do?g_id=${goods.g_id}"
												class="btn btn-warning btn-small">수정</a>
											<form action="${contextPath}/jangbogo/goodsDelete.do"
												method="post" style="display: inline;"
												onsubmit="return confirm('정말 영구 삭제하시겠습니까? 복구 불가입니다.');">
												<input type="hidden" name="g_id" value="${goods.g_id}">
												<button type="submit" class="btn btn-danger btn-small">삭제</button>
											</form>
										</div>
									</c:if>
								</div>
							</div>
						</c:forEach>
					</div>
				</c:forEach>
			</c:when>

			<c:when test="${not empty goodsList}">
				<c:set var="categoryName">
					<c:choose>
						<c:when test="${category == 'fresh'}">신선식품</c:when>
						<c:when test="${category == 'processed'}">가공식품</c:when>
						<c:when test="${category == 'living'}">생활용품</c:when>
						<c:when test="${category == 'fashion'}">패션잡화</c:when>
						<c:when test="${category == 'local'}">지역특산물</c:when>
						<c:otherwise>기타</c:otherwise>
					</c:choose>
				</c:set>

				<h1>상품 목록</h1>
				<h4>*이미지 클릭시 제품 상세 페이지로 이동합니다.</h4>
				<div class="row">
					<c:forEach var="goods" items="${goodsList}">
						<c:set var="firstImage"
							value="${fn:split(goods.i_filename, ',')[0]}" />

						<div class="col-md-3 col-sm-6 mb-4">
							<div class="product-card">
								<a
									href="${contextPath}/jangbogo/goodsDetail.do?g_id=${goods.g_id}">
									<img src="${contextPath}/resources/image/${goods.i_filename}"
									alt="${goods.g_name}" class="product-image" />
								</a>
								<div class="product-name">${goods.g_name}</div>
								<div class="product-price">
									<fmt:formatNumber value="${goods.g_price}" type="currency"
										currencySymbol="₩" />
								</div>

								<div
									class="mt-2 d-flex justify-content-center align-items-center gap-2">
									<button class="wish-btn ${empty m_id == 'disabled'}" 
								        data-gid="${goods.g_id}" 
								        ${empty m_id}>
										<span class="wish-icon"> <c:choose>
												<c:when test="${myWishList != null && myWishList.contains(goods.g_id)}">
													<img src="${contextPath}/resources/image/like_on.png" alt="찜목록 추가되있음">
												</c:when>
												<c:otherwise>
													<img src="${contextPath}/resources/image/like.png" alt="찜목록 추가하기">										
												</c:otherwise>
											</c:choose>
										</span>
									</button>
								</div>

								<c:if
									test="${isLogOn==true and not empty memberInfo and memberInfo.m_role == 3}">
									<div class="mt-2">
										<a
											href="${contextPath}/jangbogo/goodsUpdateForm.do?g_id=${goods.g_id}"
											class="btn btn-warning btn-small">수정</a>
										<form action="${contextPath}/jangbogo/goodsDelete.do"
											method="post" style="display: inline;"
											onsubmit="return confirm('정말 영구 삭제하시겠습니까? 복구 불가입니다.');">
											<input type="hidden" name="g_id" value="${goods.g_id}">
											<button type="submit" class="btn btn-danger btn-small">삭제</button>
										</form>
									</div>
								</c:if>

							</div>
						</div>
					</c:forEach>
				</div>
			</c:when>

			<c:otherwise>
				<p class="text-center mt-4">등록된 상품이 없습니다.</p>
			</c:otherwise>
		</c:choose>
	</div>
	<script>
$(document).ready(function() {
    $('.wish-btn').click(function() {
    	console.log('버튼 클릭 감지');
        if ($(this).prop('disabled')) {
            console.log('찜 버튼 클릭 불가: 로그인 필요');
            return;
        }

        const btn = $(this);
        const g_id = btn.data('gid');
        console.log('찜 버튼 클릭됨, g_id:', g_id);

        $.ajax({
            url: '${contextPath}/wishlist/toggle.do',
            method: 'POST',
            data: { gId: g_id },
            beforeSend: function() {
                console.log('AJAX 요청 전송 준비 중...');
            },
            success: function(result) {
                console.log('서버 응답:', result);

                if (result === 'login_required') {
                    alert('로그인 후 이용 가능합니다.');
                    return;
                }

                const img = btn.find('.wish-icon img');
                if (result === 'added') {
                	img.attr('src', '${contextPath}/resources/image/like_on.png');
                    console.log('찜 추가 완료');
                    alert('찜목록에 추가완료')
                } else if (result === 'removed') {
                	img.attr('src', '${contextPath}/resources/image/like.png');
                    console.log('찜 제거 완료');
                    alert('찜목록 삭제완료')
                } else {
                    console.warn('알 수 없는 서버 응답:', result);
                }
            },
            error: function(xhr, status, error) {
                console.error('AJAX 요청 실패', status, error);
                alert('찜 처리 중 오류 발생');
            }
        });
    });
});
</script>


</body>
</html>
