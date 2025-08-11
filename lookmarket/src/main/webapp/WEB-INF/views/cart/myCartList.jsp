<%@ page language="java" pageEncoding="UTF-8" isELIgnored="false" %>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<c:set var="contextPath" value="${pageContext.request.contextPath}" />

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>장바구니</title>
<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<style>
.qty-btn {
    padding: 2px 6px;
    margin: 0 2px;
    font-size: 14px;
    cursor: pointer;
}
</style>
<script>
const contextPath = "${contextPath}";

function updateCartQty(c_id, newQty) {
    if (newQty < 1) {
        alert("수량은 1 이상이어야 합니다.");
        return;
    }

    $.ajax({
        type: "POST",
        url: contextPath + "/cart/updateCartQty.do",
        data: { c_id: c_id, c_qty: newQty },
        success: function() {
            updateRowTotal(c_id, newQty);
        },
        error: function() {
            alert("수량 변경에 실패했습니다.");
        }
    });
}

function changeQty(c_id, diff, maxStock) {
    let input = $("#qty-" + c_id);
    let newQty = parseInt(input.val()) + diff;
    if (newQty < 1) newQty = 1;
    if (newQty > maxStock) {
        alert("재고를 초과할 수 없습니다.");
        return;
    }
    input.val(newQty);
    updateCartQty(c_id, newQty);
}

function updateRowTotal(c_id, qty) {
    let price = parseInt($("#price-" + c_id).data("price"));
    let delivery = parseInt($("#delivery-" + c_id).data("delivery"));
    let total = (price * qty) + delivery;
    $("#total-" + c_id).text(total.toLocaleString() + "원");

    updateFinalTotal();
}

function updateFinalTotal() {
    let totalGoodsNum = 0;
    let totalGoodsPrice = 0;
    let totalDeliveryPrice = 0;

    $(".qty-input").each(function() {
        let qty = parseInt($(this).val());
        let price = parseInt($(this).closest("tr").find(".price").data("price"));
        let delivery = parseInt($(this).closest("tr").find(".delivery").data("delivery"));

        totalGoodsNum += qty;
        totalGoodsPrice += price * qty;
        totalDeliveryPrice += delivery;
    });

    $("#p_totalGoodsNum").text(totalGoodsNum + "개");
    $("#p_totalDeliveryPrice").text(totalDeliveryPrice.toLocaleString() + "원");
    $("#p_final_totalPrice").text((totalGoodsPrice + totalDeliveryPrice).toLocaleString() + "원");
}

function delete_cart_goods(c_id) {
    if (confirm("정말로 삭제하시겠습니까?")) {
        $.ajax({
            type: "POST",
            url: contextPath + "/cart/deleteCartItem.do",
            data: { c_id: c_id },
            success: function() {
                $("#row-" + c_id).remove();
                updateFinalTotal();
            },
            error: function() {
                alert("삭제에 실패했습니다.");
            }
        });
    }
}

function fn_order_all_cart_goods() {
    if (confirm("장바구니의 모든 상품을 주문하시겠습니까?")) {
        $.ajax({
            type: "POST",
            url: contextPath + "/cart/placeOrder.do",
            success: function(response) {
                if (response === "success") {
                    alert("주문이 완료되었습니다.");
                    window.location.href = contextPath + "/order/orderResult.do";
                } else {
                    alert("주문 처리에 실패했습니다.");
                }
            },
            error: function() {
                alert("주문 처리 중 오류가 발생했습니다.");
            }
        });
    }
}
</script>
</head>
<body>
<table class="list_view">
    <tbody>
        <tr>
            <td>상품정보</td>
            <td>정가</td>
            <td>재고</td>
            <td>수량</td>
            <td>배송비</td>
            <td>합계</td>
            <td></td>
        </tr>
        <c:choose>
            <c:when test="${empty cartList}">
                <tr>
                    <td colspan=7 class="fixed"><strong>장바구니에 상품이 없습니다.</strong></td>
                </tr>
            </c:when>
            <c:otherwise>
                <c:forEach var="item" items="${cartList}">
                    <tr id="row-${item.c_id}">
                        <td class="goods_image">
                            <a href="${contextPath}/goods/goodsDetail.do?g_id=${item.g_id}">
                                ${item.g_name}
                            </a>
                        </td>
                        <td class="price" id="price-${item.c_id}" data-price="${item.g_price}">
                            ${item.g_price}원
                        </td>
                        <td>${item.g_stock}</td>
                        <td>
                            <button class="qty-btn" onclick="changeQty('${item.c_id}', -1, ${item.g_stock})">-</button>
                            <input type="number" id="qty-${item.c_id}" class="qty-input" value="${item.c_qty}" min="1" max="${item.g_stock}" readonly style="width:40px; text-align:center;">
                            <button class="qty-btn" onclick="changeQty('${item.c_id}', 1, ${item.g_stock})">+</button>
                        </td>
                        <td class="delivery" id="delivery-${item.c_id}" data-delivery="${item.g_delivery_price}">
                            ${item.g_delivery_price}원
                        </td>
                        <td id="total-${item.c_id}">
                            <fmt:formatNumber value="${(item.g_price * item.c_qty) + item.g_delivery_price}" type="number" />원
                        </td>
                        <td>
                            <button onclick="delete_cart_goods('${item.c_id}')" style="background-color:#f44336;color:#fff;border:none;padding:4px 8px;border-radius:4px;cursor:pointer;">삭제</button>
                        </td>
                    </tr>
                </c:forEach>
            </c:otherwise>
        </c:choose>
    </tbody>
</table>

<br><br>

<table class="list_view">
    <tbody>
        <tr class="fixed">
            <td>총 상품수</td>
            <td></td>
            <td>총 배송비</td>
            <td></td>
            <td>최종 결제금액</td>
        </tr>
        <tr>
            <td><p id="p_totalGoodsNum">${totalGoodsNum}개</p></td>
            <td></td>
            <td><p id="p_totalDeliveryPrice">${totalDeliveryPrice}원</p></td>
            <td></td>
            <td><p id="p_final_totalPrice">
                <fmt:formatNumber value="${totalGoodsPrice + totalDeliveryPrice - totalDiscountedPrice}" type="number" />원
            </p></td>
        </tr>
    </tbody>
</table>

<br><br>

<button type="button" onclick="fn_order_all_cart_goods()" style="width:100px;height:40px;background-color:#4CAF50;color:white;border:none;border-radius:5px;cursor:pointer;font-size:16px;">
    주문하기
</button>

<script>
$(document).ready(function(){
    updateFinalTotal();
});
</script>
</body>
</html>
