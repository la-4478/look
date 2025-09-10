<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" isELIgnored="false"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<c:if test="${empty pageType}">
	<c:set var="pageType" value="${sessionScope.pageType}" />
</c:if>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>header</title>
<link href="${contextPath}/resources/css/common.css" rel="stylesheet" type="text/css">
<link href="${contextPath}/resources/css/notify.css" rel="stylesheet" type="text/css">
<!-- 해더 CSS 파일 -->
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<script>
document.addEventListener("DOMContentLoaded", function(){
  const bell  = document.getElementById('bell');
  if (!bell) return;
  const badge = document.getElementById('badge');
  const panel = document.getElementById('panel');
  const ctx   = bell.dataset.ctx || "${ctx}";

  // HTML 이스케이프
  function escapeHtml(s){ return String(s).replace(/[&<>"']/g, m => ({'&':'&amp;','<':'&lt;','>':'&gt;','"':'&quot;',"'":'&#39;'}[m])); }
  
  panel.addEventListener('click', function(e){
	    if (e.target.matches('.notify-link')) {
	      e.preventDefault();
	      const id = e.target.dataset.id;
	      const href = e.target.dataset.href;
	      if (!id || !href) return;

	      markRead(id).then(() => {
	        const item = e.target.closest('.item');
	        if (item) item.remove();
	        window.location.href = href;
	      }).catch((err) => {
	        console.error('[notify] 바로가기 실패', err);
	        alert('알림 처리 중 문제가 발생했습니다.');
	      });
	    }
	  });

  // 패널 토글
  async function togglePanel(e){
    e.stopPropagation();
    if (panel.style.display === 'block') { panel.style.display = 'none'; return; }
    await openPanel();
    panel.style.display = 'block';
  }
  document.addEventListener('click', ()=>{ panel.style.display = 'none'; });
  bell.addEventListener('click', togglePanel);

  // 뱃지 갱신
  async function refreshCount(){
    try{
      const r = await fetch(ctx + '/notify/unread-count.do', { credentials:'same-origin' });
      if(!r.ok) throw new Error('count http ' + r.status);
      const txt = await r.text();
      const count = parseInt(txt,10) || 0;
      if(count>0){ badge.style.display='inline-block'; badge.textContent = count; }
      else { badge.style.display='none'; }
    }catch(e){ console.error('[notify] count error', e); }
  }

  // 목록 열기
  async function openPanel(){
    try{
      const r = await fetch(ctx + '/notify/unread-list.do', { credentials:'same-origin', headers:{'Accept':'application/json'}});
      if(!r.ok) throw new Error('list http ' + r.status);
      const list = await r.json();
      panel.innerHTML = (list && list.length) ? list.map(n=>{
        const id    = (n.nId !== undefined && n.nId !== null) ? n.nId : n.n_id;
        const title = n.title || '';
        const msg   = n.message || '';
        const link  = (n.linkUrl || n.link_url || '#');
        return `
        <div class="item" data-id="\${id}">
        <div class="title">\${escapeHtml(title)}</div>
        <div class="msg">\${escapeHtml(msg)}</div>
        <div class="actions">
          <a href="#" 
             class="notify-link" 
             data-id="\${id}" 
             data-href="\${ctx}\${link}">
             바로가기
          </a>
          <button type="button" onclick="markRead(\${id})">읽음</button>
        </div>
      </div>
        `;
      }).join('') : '<div class="empty">새 알림 없음</div>';
    }catch(e){
      console.error('[notify] list error', e);
      panel.innerHTML = '<div class="empty">알림을 불러오지 못했습니다.</div>';
    }
  }

  // 읽음 처리
  window.markRead = async function(id, options = {}) {
  const silent = options.silent || false;
  try {
    const opts = {
      method: 'POST',
      headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
      body: 'n_id=' + encodeURIComponent(id),
      credentials: 'same-origin'
    };
    const r = await fetch(ctx + '/notify/read.do', opts);
    if (!r.ok) throw new Error('read http ' + r.status);
    await refreshCount();
    if (!silent) await openPanel();  // silent 옵션이 false일 때만 전체 새로고침
  } catch (e) {
    console.error('[notify] read error', e);
    alert('읽음 처리 실패');
  }
};


  // 초기 실행
  refreshCount();
  setInterval(refreshCount, 10000);
});
</script>
</head>
<body>
	<header>
		<c:choose>
			<c:when test="${pageType eq 'sijangbajo'}">
				<c:set var="contentsClass" value="bg-sijangbajo" />
			</c:when>
			<c:when test="${pageType eq 'jangbogo'}">
				<c:set var="contentsClass" value="bg-jangbogo" />
			</c:when>
			<c:otherwise>
				<c:set var="contentsClass" value="bg-sijangbajo" />
			</c:otherwise>
		</c:choose>
		<div class="header_top">
			<div id="logo">
				<c:choose>
					<c:when test="${pageType eq 'sijangbajo'}">
						<a href="${contextPath}/main/sijangbajoMain.do"> <img
							width="176" height="80" alt="시장봐조"
							src="${contextPath}/resources/image/sijanglogo12.png" />
						</a>
					</c:when>
					<c:when test="${pageType eq 'jangbogo'}">
						<a href="${contextPath}/main/jangbogoMain.do"  class="logo2"> <img
							width="176" height="80" alt="장보고"
							src="${contextPath}/resources/image/jangbogologo.png" />
						</a>
					</c:when>
				</c:choose>
			</div>

			<div id="head_link" class="justify-content-end">
<%-- 				<form class="d-flex" role="search" action="${contextPath}/search.do" --%>
<!-- 					method="get"> -->
<!-- 					<input class="form-control me-2" type="search" name="q" -->
<!-- 						placeholder="검색어를 입력하세요" /> -->
<!-- 					<button class="btn btn-outline-success" type="submit">🔍</button> -->
<!-- 				</form> -->
				<ul>
					<c:choose>
						<c:when test="${isLogOn eq true}">
							<li><a href="${contextPath}/member/logout.do">로그아웃</a></li>

							<c:choose>
								<c:when test="${memberInfo.m_role == 1}">
									<li><a href="${contextPath}/mypage/mypageInfo.do">마이페이지</a></li>
									<li><a href="${contextPath}/cart/myCartList.do">장바구니</a></li>
									<li><a href="${contextPath}/mypage/listMyOrderHistory.do">주문내역</a></li>
									<li><a href="${contextPath}/inquiry/inquiryList.do">1:1문의</a></li>
									<li>${memberInfo.m_name}님환영합니다.</li>
								</c:when>
								<c:when test="${memberInfo.m_role == 2}">
									<li><a href="${contextPath}/business/businessMain.do">사업자
											페이지</a></li>
									<li><a
										href="${contextPath}/business/businessGoodsList.do?category=all">내
											상품관리</a></li>
									<li><a href="${contextPath}/business/businessOrderList.do">주문관리</a></li>
									<li>${memberInfo.m_name}님환영합니다.</li>
									<div id="bell" class="bell" role="button" tabindex="0"
										aria-label="알림" data-ctx="${ctx}">
										🔔 <span id="badge" class="badge" style="display: none;">0</span>
										<div id="panel" class="panel" style="display: none;"></div>
										<div id="notifyOverlay" style="display: none;"></div>
										<div id="notifyModal" role="dialog" aria-modal="true"
											aria-labelledby="notifyTitle">
											<div class="modal-header">
												<h3 id="notifyTitle">알림</h3>
												<button type="button" id="notifyClose" aria-label="닫기">✕</button>
											</div>
											<div id="notifyBody">
												여기로 리스트가 들어감
											</div>
										</div>
									</div>
								</c:when>
								<c:when test="${memberInfo.m_role == 3}">
									<li><a
										href="${contextPath}/admin/mypage/mypageAdminInfo.do">관리자페이지</a></li>
									<li>${memberInfo.m_name}님환영합니다.</li>
								</c:when>
							</c:choose>
						</c:when>
						<c:otherwise>
							<li><a href="${contextPath}/member/loginForm.do">로그인</a></li>
							<li><a href="${contextPath}/member/memberSelect.do">회원가입</a></li>
						</c:otherwise>
					</c:choose>
				</ul>
			</div>
		</div>

		<div class="contents ${contentsClass}">
			<div class="navbar__div">
				<nav class="navbar">
					<ul class="navbar__menu">
						<c:choose>
							<c:when test="${pageType eq 'sijangbajo'}">
								<li class="menu">
									<div>
										<a href="${contextPath}/sijangbajo/sijangSearch/search.do"
											class="menu__title">전통시장 찾기</a>
									</div>
								</li>
								<li class="menu">
									<div>
										<a href="${contextPath}/sijangbajo/nearby/nearby.do"
											class="menu__title">주변상권</a>
									</div>
								</li>
								<li class="menu">
									<div>
										<a href="${contextPath}/sijangbajo/nearby/festivalList.do"
											class="menu__title">지역축제</a>
									</div>
								</li>

								<li class="menu">
									<div>
										<a href="${contextPath}/event/promotionList.do"
											class="menu__title">이벤트★</a>
									</div>
									<ul id="tipSybm1" class="navbar__submenu">
										<li><a
											href="${contextPath}/event/promotionList.do?pageType=sijangbajo">프로모션
												목록</a></li>
										<li><a
											href="${contextPath}/event/couponList.do?pageType=sijangbajo">쿠폰
												목록</a></li>
									</ul> <c:if
										test="${isLogOn==true and not empty memberInfo and memberInfo.m_role == 3}">
										<ul id="tipSybm1" class="navbar__submenu">
											<li><a href="${contextPath}/event/promotionList.do">프로모션</a></li>
											<li><a href="${contextPath}/event/couponList.do?pageType=sijangbajo">쿠폰 목록</a></li>
										</ul>
									</c:if>
								</li>

								<!-- 사용자 (m_role == 1) -->
								<c:if test="${isLogOn eq true and memberInfo.m_role == 1}">
										<div>
											<a href="${contextPath}/community/communityList.do" class="menu__title">리뷰</a>
									</div>
									<ul id="tipSybm1" class="navbar__submenu">
										<li><a
											href="${contextPath}/community/communityAddForm.do">리뷰쓰기</a></li>
									</ul>
								</c:if>

								<!-- 관리자 (m_role == 3) -->
								<c:if test="${isLogOn eq true and memberInfo.m_role == 3}">
<!-- 									<div> -->
<%-- 										<a href="${contextPath}/community/blackBoardList.do" --%>
<!-- 											class="menu__title">커뮤니티 관리</a> -->
<!-- 									</div> -->
									<ul id="tipSybm1" class="navbar__submenu">
										<li><a href="${contextPath}/community/communityList.do"
											class="menu__title">사용자 리뷰</a></li>
										<li><a href="${contextPath}/community/blackBoardList.do"
											class="menu__title">사장님 고충방</a></li>
									</ul>
								</c:if>

								<!-- 비회원 또는 기타 -->
								<c:if test="${not isLogOn or empty memberInfo}">
										<div>
											<a href="${contextPath}/community/communityList.do" class="menu__title">리뷰</a>
										</div>	
								</c:if>

								<li class="menu">
									<div>
										<a href="${contextPath}/main/jangbogoMain.do"
											class="menu__title">장보고</a>
									</div>
								</li>

							</c:when>

							<c:when test="${pageType eq 'jangbogo'}">
								<li class="menu">
									<div>
										<a href="${contextPath}/jangbogo/goodsList.do?category=all"
											class="menu__title">상품보기</a>

									</div>
									<ul id="tipSybm1" class="navbar__submenuu">
										<li><a
											href="${contextPath}/jangbogo/goodsList.do?category=all">전체보기</a></li>
										<li><a
											href="${contextPath}/jangbogo/goodsList.do?category=fresh">신선식품</a></li>
										<li><a
											href="${contextPath}/jangbogo/goodsList.do?category=processed">가공식품</a></li>
										<li><a
											href="${contextPath}/jangbogo/goodsList.do?category=living">생활용품</a></li>
										<li><a
											href="${contextPath}/jangbogo/goodsList.do?category=fashion">패션잡화</a></li>
										<li><a
											href="${contextPath}/jangbogo/goodsList.do?category=local">지역특산물</a></li>
									</ul>
								</li>
								<li class="menu">
									<div>
										<a href="${contextPath}/event/promotionList.do"
											class="menu__title">이벤트★</a>
									</div>
									<ul id="tipSybm1" class="navbar__submenuu">
										<li><a
											href="${contextPath}/event/promotionList.do?pageType=sijangbajo">프로모션
												목록</a></li>
									</ul>
								</li>
								<li class="menu">
									<div>
										<a href="${contextPath}/community/communityList.do"
											class="menu__title">리뷰</a>
									</div>
								</li>

								<li class="menu">
									<div>
										<a href="${contextPath}/main/sijangbajoMain.do"
											class="menu__title">시장봐조</a>
									</div>
								</li>
							</c:when>
						</c:choose>
					</ul>
				</nav>
			</div>
		</div>
	</header>
</body>
</html>