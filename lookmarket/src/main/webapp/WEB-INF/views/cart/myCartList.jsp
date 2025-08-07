<%@ page language="java" pageEncoding="UTF-8" isELIgnored="false" %>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<c:set var="contextPath"  value="${pageContext.request.contextPath}" />

<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>장바구니</title>
</head>
<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<script>
	const contextPath = "${contextPath}";
</script>
<script>
	function updateCartQty(c_id, newQty) {
	    if (newQty < 1) {
	        alert("수량은 1 이상이어야 합니다.");
	        return;
	    }
	
	    if (confirm("수량을 변경하시겠습니까?")) {
	        $.ajax({
	            type: "POST",
	            url: contextPath + "/cart/updateCartQty.do",
	            data: {
	                c_id: c_id,
	                c_qty: newQty
	            },
	            success: function(response) {
	                alert("수량이 변경되었습니다.");
	                location.reload();
	            },
	            error: function(xhr, status, error) {
	                alert("수량 변경에 실패했습니다.");
	                console.error("에러 내용:", error);
	            }
	        });
	    }
	}
	
	function delete_cart_goods(c_id) {
	    if (confirm("정말로 삭제하시겠습니까?")) {
	        $.ajax({
	            type: "POST",
	            url: contextPath + "/cart/deleteCartItem.do",
	            data: { c_id: c_id },
	            success: function(response) {
	                alert("상품이 삭제되었습니다.");
	                location.reload();  // 삭제 후 페이지 새로고침
	            },
	            error: function(xhr, status, error) {
	                alert("삭제에 실패했습니다.");
	                console.error("삭제 에러:", error);
	            }
	        });
	    }
	}
</script>
<body>
	<table class="list_view">
		<tbody>
			<tr>
				<td>상품정보</td>
				<td>정가</td>
				<td>판매가</td>
				<td>재고</td>
				<td>수량</td>
				<td>배송비</td>
				<td>합계</td>
				<td></td>
			</tr>
			<c:choose>
				<c:when test="${empty cartList}">
					<tr>
						<td colspan=8 class="fixed">
							<strong>장바구니에 상품이 없습니다.</strong>
						</td>
					</tr>
				</c:when>
			<c:otherwise>
				<c:forEach var="item" items="${cartList}" varStatus="status">
					<tr>
						<td class="goods_image">
							<a href="${contextPath}/goods/goodsDetail.do?g_id=${item.g_id}">
<%-- 									<img width="75" alt="상품이미지" src="${contextPath}/thumbnails.do?g_id=${item.g_id}&fileName=${item.i_filename}" /> --%>
								<br/>
								${item.g_name}
							</a>
						</td>
						<td class="price">${item.g_price}원</td>
						<td class="price">${item.g_sale_price}원</td>
						<td>${item.g_stock}</td>
						<td>
						    <input type="number"
						           name="qty_${item.c_id}"
						           value="${item.c_qty}"
						           min="1"
						           max="${item.g_stock}"
						           onchange="updateCartQty('${item.c_id}', this.value)"
						           style="width: 50px; text-align: center;" />
						</td>
						<td>${item.g_delivery_price}원</td>
						<td>
							<fmt:formatNumber value="${(item.g_sale_price * item.c_qty) + item.g_delivery_price}" type="number" var="itemTotalPrice" />
							${itemTotalPrice}원
						</td>
						<td>
							<button type="button" 
							        onclick="delete_cart_goods('${item.c_id}')" 
							        style="padding:4px 8px; background-color:#f44336; color:#fff; border:none; border-radius:4px; cursor:pointer;">
							    삭제
							</button>
						</td>
					</tr>
				</c:forEach>
			</c:otherwise>
			</c:choose><br><br>
			<table class="list_view">
			    <tbody>
			        <tr class="fixed">
			            <td class="fixed">총 상품수</td>
			            <td></td>
			            <td>총 배송비</td>
			            <td></td>
			            <td>최종 결제금액</td>
			        </tr>
			        <tr>
			            <td>
			                <p id="p_totalGoodsNum">${totalGoodsNum}개</p>
			                <input id="h_totalGoodsNum" type="hidden" value="${totalGoodsNum}" />
			            </td>
			            <td></td>
			            <td>
			                <p id="p_totalDeliveryPrice">${totalDeliveryPrice}원</p>
			                <input id="h_totalDeliveryPrice" type="hidden" value="${totalDeliveryPrice}" />
			            </td>
			            <td></td>
			            <td>
			                <p id="p_final_totalPrice">
			                    <fmt:formatNumber value="${totalGoodsPrice + totalDeliveryPrice - totalDiscountedPrice}" type="number" var="total_price" />
			                    ${total_price}원
			                </p>
			                <input id="h_final_totalPrice" type="hidden" value="${totalGoodsPrice + totalDeliveryPrice - totalDiscountedPrice}" />
			            </td>
			        </tr>
			    </tbody>
			</table><br><br>
			
			<button type="button" onclick="fn_order_all_cart_goods()" style="width: 100px; height: 40px; background-color: #4CAF50; color: white; border: none; border-radius: 5px; cursor: pointer; font-size: 16px;">
			  주문하기
			</button>
	</body>
</html>