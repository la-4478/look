<%@ page language="java" pageEncoding="UTF-8" isELIgnored="false"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />

<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8" />
  <title>결제하기</title>

  <!-- PortOne Browser SDK -->
  <script src="https://cdn.portone.io/v2/browser-sdk.js"></script>
  <!-- 다음 우편번호 -->
  <script src="https://t1.daumcdn.net/mapjsapi/bundle/postcode/prod/postcode.v2.js"></script>

  <link rel="stylesheet" href="${contextPath}/resources/css/cartorder.css"/>

    <script>
  /** 숫자 안전 변환 */
  function toInt(v) {
    if (v == null) return 0;
    const n = Number(String(v).replace(/[^\d.-]/g, ''));
    return Number.isFinite(n) ? n : 0;
  }

  /** 주문상품 목록에서 Σ(가격×수량) 계산 (숨김 input 기반) */
  function getOrderItemsTotal() {
    const prices = Array.from(document.querySelectorAll('input[name="goodsPrice"]')).map(el => toInt(el.value));
    const qtys   = Array.from(document.querySelectorAll('input[name="goodsQty"]')).map(el => toInt(el.value));
    let sum = 0;
    for (let i = 0; i < prices.length; i++) {
      sum += prices[i] * (qtys[i] || 0);
    }
    return sum;
  }

  /** 히든값 세팅 헬퍼 */
  function setHidden(selector, value) {
    const el = document.querySelector(selector);
    if (el) el.value = String(value);
  }

  /** 요약표 텍스트 업데이트 */
  function setSummaryText({ goodsTotal, coupon = 0, delivery = 0 }) {
    const elGoods    = document.getElementById('sumGoods');
    const elCoupon   = document.getElementById('sumCoupon');
    const elDelivery = document.getElementById('sumDelivery');
    const elFinal    = document.getElementById('sumFinal');

    const final = goodsTotal - coupon + delivery;

    if (elGoods)    elGoods.textContent    = goodsTotal.toLocaleString('ko-KR') + '원';
    if (elCoupon)   elCoupon.textContent   = '-' + coupon.toLocaleString('ko-KR') + '원';
    if (elDelivery) elDelivery.textContent = '+' + delivery.toLocaleString('ko-KR') + '원';
    if (elFinal)    elFinal.textContent    = final.toLocaleString('ko-KR') + '원';

    setHidden('#totalGoodsPrice', goodsTotal);
    setHidden('#couponDiscountVal', coupon);
    setHidden('#deliveryFeeVal', delivery);
    setHidden('#finalTotalPrice', final);

    return final;
  }

  function refreshTotals() {
    const goodsTotal = getOrderItemsTotal();
    const coupon   = toInt(document.querySelector('#couponDiscountVal')?.value) || 0;
    const delivery = toInt(document.querySelector('#deliveryFeeVal')?.value) || 0;

    const final = setSummaryText({ goodsTotal, coupon, delivery });
    console.log('[TOTALS]', { goodsTotal, coupon, delivery, final });
    return final;
  }

  document.addEventListener('DOMContentLoaded', () => {
    refreshTotals();
    document.querySelectorAll('input[name="goodsQty"]').forEach(el => {
      el.addEventListener('input', refreshTotals);
      el.addEventListener('change', refreshTotals);
    });
  });

  const ctx = "${contextPath}";

  function parseMoney(text){ return Number((text||"").toString().replace(/\D/g,"")); }
  function makeOrderName(names){ return (!names||!names.length) ? "" : (names.length===1?names[0]:`${names[0]} 외 ${names.length-1}건`); }

  function execDaumPostcode() {
    new daum.Postcode({
      oncomplete : function(data) {
        document.getElementById('zipcode').value = data.zonecode;
        document.getElementById('oi_delivery_address').value = data.address;
        const detail = document.getElementById('oi_deli_namuji_address');
        if (detail) { detail.value = ''; detail.focus(); }
      }
    }).open();
  }

  document.addEventListener("DOMContentLoaded", () => {
    const btn = document.querySelector('input[type="button"][value="결제하기"]');
    if (btn) btn.addEventListener("click", (e) => { e.preventDefault(); requestCardPayment(); });
  });

  async function requestCardPayment() {
    const f = document;

    const oiName           = f.querySelector('#oi_name')?.value?.trim() || "";
    const receiverName     = f.querySelector('#oi_receiver_name')?.value?.trim() || "";
    const receiverPhoneRaw = f.querySelector('#oi_receiver_phone')?.value || "";
    const zipcode          = f.querySelector('#zipcode')?.value?.trim() || "";
    const roadAddress      = f.querySelector('#oi_delivery_address')?.value?.trim() || "";
    const namujiAddress    = f.querySelector('#oi_deli_namuji_address')?.value?.trim() || "";
    const deliveryMessage  = f.querySelector('#oi_delivery_message')?.value?.trim() || "";
    const email            = f.querySelector('#oi_member_email')?.value?.trim() || "";
    console.log(namujiAddress);

    const goodsIds    = Array.from(f.querySelectorAll('input[name="goodsId"]')).map(el => Number(el.value));
    const goodsNames  = Array.from(f.querySelectorAll('input[name="goodsName"]')).map(el => el.value);
    const goodsPrices = Array.from(f.querySelectorAll('input[name="goodsPrice"]')).map(el => Number(el.value));
    const goodsQtys   = Array.from(f.querySelectorAll('input[name="goodsQty"]')).map(el => Number(el.value));

    const totalCell   = f.querySelector('.summary-table .total')?.textContent || "";
    const totalPrice  = parseMoney(totalCell);
    const orderName   = makeOrderName(goodsNames);

    const orIdx    = f.querySelector('#or_idx')?.value || null;
    const orderNum = f.querySelector('#o_id')?.value   || null;

    if (!oiName) return alert("주문자 이름을 입력하세요.");
    if (!receiverName) return alert("수령자 이름을 입력하세요.");
    if (receiverPhoneRaw.replace(/\D/g, "").length < 10) return alert("수령자 연락처 형식이 올바르지 않습니다.");
    if (!zipcode) return alert("우편번호를 입력하세요.");
    if (!roadAddress) return alert("주소를 입력하세요.");
    if (!goodsIds.length) return alert("주문 상품이 없습니다.");
    const emailOk = /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email);
    if (!emailOk) return alert("구매자 이메일을 올바르게 입력하세요.");

    const payloadBase = {
      or_idx: orIdx,
      pd_name: orderName,
      price: totalPrice,
      receiver_name: receiverName,
      receiverPhone: receiverPhoneRaw,
      goods_num: goodsIds,
      goods_name: goodsNames,
      goods_sales_price: goodsPrices,
      goods_qty: goodsQtys,
      order_name: oiName,
      order_num: orderNum,
      zipcode: zipcode,
      roadAddress: roadAddress,
      namujiAddress: namujiAddress,
      delivery_message: deliveryMessage,
      delivery_method: "parcel",
      email : email
    };

    const paymentId = `PAYMENT_${Date.now()}_${Math.floor(Math.random()*1e6)}`;

   const response = await PortOne.requestPayment({
 	  storeId:    "store-292f1f91-b8c2-4608-9394-615315d5f811",
 	  channelKey: "channel-key-16983525-2a28-41f4-b177-b4f8e27769dc",
 	  paymentId:  paymentId,
 	  orderName:  payloadBase.pd_name,
 	  totalAmount: payloadBase.price,
 	  currency:   "KRW",
 	  payMethod:  "CARD",  // ★ 여기! pay_method → payMethod 로 수정
 	  customer: {
 	    fullName:    receiverName,
 	    phoneNumber: String(receiverPhoneRaw),
 	    email: email,
 	    address: {
 	      addressLine1: roadAddress,
 	      addressLine2: namujiAddress,
 	      postalCode:   zipcode
 	    }
 	  }
 	});

    if (response.code != null) {
      alert(response.message || "결제 실패");
      return;
    }

    const paymentKey = response.paymentKey || response.imp_uid || response.id || response.txId;
    if (!paymentKey) {
      alert("결제는 되었지만 paymentKey를 받지 못했습니다. 관리자에게 문의하세요.");
      console.error("📛 결제 응답 이상:", response);
      return;
    }

    // ★ 변경: v2에서는 method를 우선 사용
    const payMethodFromPortOne   = response.method || response.payMethod || null;
    const cardCompanyFromPortOne = response.card?.company || null;
    const cardPayMonthFromPO     = (typeof response.card?.installment === 'number') ? response.card.installment : 0;

    const sendBody = {
      ...payloadBase,
      payMethod: payMethodFromPortOne,  // ★ 추가: 응답에서 받은 결제수단을 서버에 전달
      card_com_name: cardCompanyFromPortOne,
      card_pay_month: cardPayMonthFromPO,
      paymentId: paymentId,
      portone_paymentKey: paymentKey,
      paymentStatus: response.status
    };

    try {
      const res = await fetch(ctx + "/order/payToOrderGoods.do", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(sendBody)
      });

      const text = await res.text();
      let result;
      try { result = JSON.parse(text); }
      catch (e) {
        console.error("❌ JSON 파싱 실패! 응답 텍스트:", text);
        return alert("서버 응답 형식 오류. 관리자에게 문의해주세요.");
      }

      alert(result.message || "주문이 완료되었습니다!");
      if (result.success) {
        window.location.href = ctx + "/order/orderResult.do";
      }
    } catch (e) {
      console.error("❌ 서버 통신 실패:", e);
      alert("서버 통신 오류. 결제 상태를 꼭 확인해주세요!");
    }
  }
  </script>
</head>

<body class="a">
  <div class="container-box">
    <!-- 주문/배송 정보 -->
    <div class="box">
      <h2>주문 정보 입력</h2>

      <label for="oi_name">주문자 이름</label>
      <input type="text" id="oi_name" name="oi_name" value="${memberInfo.m_name}" readonly>

      <label for="oi_receiver_name">수령자 이름</label>
      <input type="text" id="oi_receiver_name" name="oi_receiver_name" placeholder="수령자 이름" required>

      <label for="oi_receiver_phone">수령자 연락처</label>
      <input type="text" id="oi_receiver_phone" name="oi_receiver_phone" placeholder="01012345678" pattern="[0-9]{10,11}" required>
	  <label for="oi_member_email">구매자 이메일</label>
	  <input type="text" id="oi_member_email" name="oi_member_email" placeholder="구매자 이메일" required>
      <label for="zipcode">우편번호</label>
      <div style="display: flex; gap: 10px;">
        <input type="text" id="zipcode" name="zipcode" placeholder="우편번호" required>
        <button type="button" onclick="execDaumPostcode()" style="width: 145px; cursor: pointer;">주소찾기</button>
      </div>

      <label for="oi_delivery_address">배송주소</label>
      <input type="text" id="oi_delivery_address" name="oi_delivery_address" placeholder="주소 입력" required>

      <label for="oi_deli_namuji_address">상세주소</label>
      <input type="text" id="oi_deli_namuji_address" name="oi_deli_namuji_address" placeholder="나머지 주소 입력">

      <label for="oi_delivery_message">배송메시지</label>
      <textarea id="oi_delivery_message" name="oi_delivery_message" rows="3" placeholder="배송 요청사항을 입력하세요."></textarea>
    </div>

    <!-- 주문상품 리스트 출력 -->
    <div class="box">
      <h2>주문 상품 목록</h2>
      <table style="width: 100%; border-collapse: collapse;">
        <thead>
          <tr>
            <th style="border-bottom: 1px solid #ccc; padding: 8px; text-align: left;">상품명</th>
            <th style="border-bottom: 1px solid #ccc; padding: 8px; text-align: right;">가격</th>
            <th style="border-bottom: 1px solid #ccc; padding: 8px; text-align: right;">수량</th>
            <th style="border-bottom: 1px solid #ccc; padding: 8px; text-align: right;">합계</th>
          </tr>
        </thead>
        <tbody>
          <c:forEach var="item" items="${myOrderList}">
            <tr>
              <td style="padding: 8px;">${item.otGoodsName}</td>
              <td style="padding: 8px; text-align: right;"><fmt:formatNumber value="${item.otGoodsPrice}" pattern="#,###" /> 원</td>
              <td style="padding: 8px; text-align: right;">${item.otGoodsQty}개</td>
              <td style="padding: 8px; text-align: right;"><fmt:formatNumber value="${item.otGoodsPrice * item.otGoodsQty}" pattern="#,###" />원</td>
            </tr>

            <!-- 서버 전송용 히든 -->
            <input type="hidden" name="goodsId"    value="${item.otGId}" />
            <input type="hidden" name="goodsName"  value="${item.otGoodsName}" />
            <input type="hidden" name="goodsPrice" value="${item.otGoodsPrice}" />
            <input type="hidden" name="goodsQty"   value="${item.otGoodsQty}" />
          </c:forEach>
        </tbody>
      </table>
    </div>

    <!-- 결제 요약 (라디오/할부 선택 제거) -->
    <div class="box">
      <h2>결제 정보</h2>

      <table class="summary-table" role="presentation">
		  <tr>
		    <td>상품금액</td>
		    <td align="right"><span id="sumGoods">
		      <fmt:formatNumber value="${totalGoodsPrice}" pattern="#,###" />원
		    </span></td>
		  </tr>
		  <tr>
		    <td>쿠폰 할인</td>
		    <td align="right"><span id="sumCoupon">
		      -<fmt:formatNumber value="${couponDiscount}" pattern="#,###" />원
		    </span></td>
		  </tr>
		  <tr>
		    <td>배송비</td>
		    <td align="right"><span id="sumDelivery">
		      +<fmt:formatNumber value="${deliveryFee}" pattern="#,###" />원
		    </span></td>
		  </tr>
		  <tr><td colspan="2"><hr></td></tr>
		  <tr>
		    <td>총 결제금액</td>
		    <td align="right" class="total"><span id="sumFinal">
		      <fmt:formatNumber value="${finalTotalPrice}" pattern="#,###" />원
		    </span></td>
		  </tr>
		</table>
		
		<!-- 숫자 히든(포맷 없이 원시 숫자) : 모델이 null이면 0 -->
		<input type="hidden" id="totalGoodsPrice"   value="${totalGoodsPrice != null ? totalGoodsPrice : 0}">
		<input type="hidden" id="couponDiscountVal" value="${couponDiscount  != null ? couponDiscount  : 0}">
		<input type="hidden" id="deliveryFeeVal"    value="${deliveryFee     != null ? deliveryFee     : 0}">
		<input type="hidden" id="finalTotalPrice"   value="${finalTotalPrice != null ? finalTotalPrice : 0}">
      <div style="margin-top: 10px;">
        <label> 전체 동의 및 결제 진행</label>
        <input type="checkbox" required>
              <input type="button" class="submit-btn" value="결제하기" />
      </div>


    </div>
  </div>
</body>
</html>
