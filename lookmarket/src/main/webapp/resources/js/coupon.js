(function(window, document){
  'use strict';

  function toKRW(n){ var v = Number(n || 0); return v.toLocaleString('ko-KR') + '원'; }

  function formatDate(s){
    var d = new Date(s);
    if(isNaN(d.getTime())) return s;
    var y = d.getFullYear();
    var m = String(d.getMonth()+1).padStart(2,'0');
    var day = String(d.getDate()).padStart(2,'0');
    return y + '-' + m + '-' + day;
  }

  function calcDiscount(coupon, subtotal){
    subtotal = Number(subtotal||0);
    if(subtotal <= 0) return 0;
    if(coupon.minOrder && subtotal < coupon.minOrder) return 0;
    var discount = 0;
    if(coupon.type === 'RATE'){
      discount = Math.floor(subtotal * (Number(coupon.value||0) / 100));
      if(coupon.maxDiscount) discount = Math.min(discount, coupon.maxDiscount);
    } else {
      discount = Number(coupon.value||0);
    }
    return Math.max(0, Math.min(discount, subtotal));
  }

  function defaultGetSubtotal(){
    var el = document.getElementById('totalGoodsPrice');
    if (el) return Number(el.value || 0);
    var prices = document.querySelectorAll('input[name="goodsPrice"]');
    var qtys   = document.querySelectorAll('input[name="goodsQty"]');
    var sum = 0, i;
    for(i=0;i<prices.length;i++){
      var p = Number(String(prices[i].value||'').replace(/\D/g,''));
      var q = Number((qtys[i] && qtys[i].value) || 0);
      sum += p * q;
    }
    return sum;
  }

  function renderList(items, subtotal, nodes, state){
    if(!items || !items.length){
      nodes.listBox.innerHTML = '<div class="item"><div class="meta"><div class="name">사용 가능한 쿠폰이 없습니다.</div></div></div>';
      nodes.btnApply.disabled = true;
      return;
    }
    var html = items.map(function(c){
      var discount = calcDiscount(c, subtotal);
      var disabled = discount <= 0 ? 'disabled' : '';
      var radio = '<input type="radio" name="couponPick" value="' + c.id + '" ' + disabled + '>';
      var name  = '<div class="name">' + (c.name || '') + '</div>';
      var descText = c.description ? c.description : (c.type === 'RATE' ? (c.value + '% 할인') : (toKRW(c.value) + ' 할인'));
      var desc = '<div class="desc">' + descText + '</div>';
      var cond = '<div class="cond">최소주문 ' + (c.minOrder ? toKRW(c.minOrder) : '제한 없음') + (c.maxDiscount ? (' • 최대 ' + toKRW(c.maxDiscount)) : '') + '</div>';
      var expire = c.expireAt ? ('<div class="expire">만료: ' + formatDate(c.expireAt) + '</div>') : '';
      var right = '<div class="right">' + (discount>0 ? ('예상할인 ' + toKRW(discount)) : '적용불가') + '</div>';
      return '<label class="item">' + radio + '<div class="meta">' + name + desc + cond + expire + '</div>' + right + '</label>';
    }).join('');

    nodes.listBox.innerHTML = html;
    var radios = nodes.listBox.querySelectorAll('input[name="couponPick"]');
    Array.prototype.forEach.call(radios, function(r){
      r.addEventListener('change', function(e){
        state.selectedCouponId = e.target.value;
        nodes.btnApply.disabled = !state.selectedCouponId;
      });
    });
    nodes.btnApply.disabled = true;
  }

  function openModal(modal){ modal.classList.add('show'); modal.setAttribute('aria-hidden','false'); }
  function closeModal(modal){ modal.classList.remove('show'); modal.setAttribute('aria-hidden','true'); }

  function init(userOptions){
    var options = userOptions || {};
    var apiUrl = options.apiUrl || (window.ctx ? (window.ctx + '/coupon/list.do') : '/coupon/list.do');
    var getSubtotal = options.getSubtotal || window.getOrderItemsTotal || defaultGetSubtotal;
    var onApply = options.onApply || function(discount, coupon){
      var ids = options.ids || {};
      var couponIdInput = document.getElementById(ids.couponId || 'couponId');
      var couponDiscountServer = document.getElementById(ids.couponDiscount || 'couponDiscount');
      var couponDiscountVal = document.getElementById(ids.couponDiscountVal || 'couponDiscountVal');
      var couponSummary = document.getElementById(ids.couponSummary || 'couponSummary');

      if (couponIdInput) couponIdInput.value = coupon.id;
      if (couponDiscountServer) couponDiscountServer.value = discount;
      if (couponDiscountVal) couponDiscountVal.value = discount;
      if (couponSummary) couponSummary.textContent = (coupon.name || '쿠폰') + ' 적용됨 (-' + toKRW(discount) + ')';

      if (typeof window.refreshTotals === 'function') window.refreshTotals();
    };

    var ids = (function(){
      var base = {
        btnOpen: 'btnOpenCoupon',
        modal: 'couponModal',
        btnClose: 'btnCloseCoupon',
        btnCancel: 'btnCancelCoupon',
        btnApply: 'btnApplyCoupon',
        listBox: 'couponListBox',
        notice: 'couponNotice'
      };
      var ov = options.ids || {};
      for (var k in ov) if (ov.hasOwnProperty(k)) base[k] = ov[k];
      return base;
    })();

    var nodes = {
      btnOpen: document.getElementById(ids.btnOpen),
      modal: document.getElementById(ids.modal),
      btnClose: document.getElementById(ids.btnClose),
      btnCancel: document.getElementById(ids.btnCancel),
      btnApply: document.getElementById(ids.btnApply),
      listBox: document.getElementById(ids.listBox),
      notice: document.getElementById(ids.notice)
    };

    var state = { coupons: [], selectedCouponId: null };

    if (!nodes.modal || !nodes.listBox || !nodes.btnApply) {
      console.warn('[CouponModal] 필수 요소가 없습니다. ids=', ids);
      return;
    }

    if (nodes.btnOpen) {
      nodes.btnOpen.addEventListener('click', function(){
        openModal(nodes.modal);
        loadCoupons();
      });
    }
    if (nodes.btnClose) nodes.btnClose.addEventListener('click', function(){ closeModal(nodes.modal); });
    if (nodes.btnCancel) nodes.btnCancel.addEventListener('click', function(){ closeModal(nodes.modal); });
    nodes.modal.addEventListener('click', function(e){ if (e.target === nodes.modal) closeModal(nodes.modal); });

    function loadCoupons(){
      nodes.listBox.innerHTML = '불러오는 중...';
      if (nodes.notice) nodes.notice.textContent = '적용 가능한 쿠폰만 표시됩니다.';
      fetch(apiUrl, { headers: { 'Accept': 'application/json' } })
        .then(function(res){ if(!res.ok) throw new Error('HTTP ' + res.status); return res.json(); })
        .then(function(data){
          var subtotal = Number(getSubtotal() || 0);
          state.coupons = (data || []).filter(function(c){ return !c.minOrder || subtotal >= c.minOrder; });
          renderList(state.coupons, subtotal, nodes, state);
        })
        .catch(function(err){
          nodes.listBox.innerHTML = '';
          if (nodes.notice) nodes.notice.textContent = '쿠폰 목록을 불러오는 중 오류가 발생했어요.';
          console.error('[CouponModal] load error', err);
        });
    }

    nodes.btnApply.addEventListener('click', function(){
      if (!state.selectedCouponId) return;
      var subtotal = Number(getSubtotal() || 0);
      var picked = state.coupons.filter(function(x){ return String(x.id) === String(state.selectedCouponId); })[0];
      if (!picked) { alert('선택한 쿠폰을 찾을 수 없어요.'); return; }
      var discount = calcDiscount(picked, subtotal);
      if (discount <= 0) { alert('해당 쿠폰은 현재 금액에 적용할 수 없습니다.'); return; }
      onApply(discount, picked);
      closeModal(nodes.modal);
    });

    return {
      reload: function(){ loadCoupons(); },
      open: function(){ openModal(nodes.modal); loadCoupons(); },
      close: function(){ closeModal(nodes.modal); },
      getState: function(){ return { coupons: state.coupons.slice(), selectedCouponId: state.selectedCouponId }; }
    };
  }

  window.CouponModal = { init: init };

})(window, document);
