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
<link href="${contextPath}/resources/css/common.css" rel="stylesheet" type="text/css"><!-- 해더 CSS 파일 -->
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
						<a href="${contextPath}/main/sijangbajoMain.do"> 
						<img width="176" height="80" alt="시장봐조"
							 src="${contextPath}/resources/image/sijangbajologo.png" />
						</a>
					</c:when>
					<c:when test="${pageType eq 'jangbogo'}">
						<a href="${contextPath}/main/jangbogoMain.do"> 
						<img width="176" height="80" alt="장보고"
							 src="${contextPath}/resources/image/jangbogologo.png" />
						</a>
					</c:when>
				</c:choose>
			</div>

			<div id="head_link">
				<form class="d-flex" role="search" action="${contextPath}/search.do"
					method="get">
					<input class="form-control me-2" type="search" name="q"
						placeholder="검색어를 입력하세요" />
					<button class="btn btn-outline-success" type="submit">🔍</button>
				</form>
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
									<li>${memberInfo.m_name}님 환영합니다.</li>
								</c:when>
								<c:when test="${memberInfo.m_role == 2}">
									<li><a href="${contextPath}/business/businessMain.do">사업자 페이지</a></li>
									<li><a href="${contextPath}/business/businessGoodsList.do?category=all">내 상품관리</a></li>
									<li><a href="${contextPath}/business/businessOrderList.do">주문관리</a></li>
									<li>${memberInfo.m_name}님 환영합니다.</li>
								</c:when>
								<c:when test="${memberInfo.m_role == 3}">
									<li><a href="${contextPath}/admin/mypage/mypageAdminInfo.do">관리자페이지</a></li>
									<li>${memberInfo.m_name}님 환영합니다.</li>
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
									<ul id="tipSybm1" class="navbar__submenu">
										<li><a href="${contextPath}/sijangbajo/nearby/nearby.do">주변상권</a></li>
										<li><a
											href="${contextPath}/sijangbajo/nearby/nearCourse.do">추천코스</a></li>
										<li><a href="${contextPath}/sijangbajo/nearby/festivalList.do">지역축제</a></li>
									</ul>
								</li>
								<li class="menu">

							<!-- 사용자 (m_role == 1) -->
								<c:if test="${isLogOn eq true and memberInfo.m_role == 1}">
								<div>
									<a href="${contextPath}/community/communityList.do" class="menu__title">리뷰</a>
								</div>
									<ul id="tipSybm1" class="navbar__submenu">
										<li><a href="${contextPath}/community/communityAddForm.do">리뷰쓰기</a></li>
									</ul>
								</c:if>

							<!-- 사업자 (m_role == 2) -->
								<c:if test="${isLogOn eq true and memberInfo.m_role == 2}">
								<div>
									<a href="${contextPath}/community/blackBoardList.do" class="menu__title">사장님 커뮤니티</a>
								</div>
									<ul id="tipSybm1" class="navbar__submenu">
										<li><a href="${contextPath}/community/blackBoardAddForm.do">사장님 고충방 등록</a></li>
									</ul>
								</c:if>
							<!-- 관리자 (m_role == 3) -->
								<c:if test="${isLogOn eq true and memberInfo.m_role == 3}">
								<div>
									<a href="${contextPath}/community/blackBoardList.do" class="menu__title">커뮤니티 관리</a>
								</div>
									<ul id="tipSybm1" class="navbar__submenu">
										<li><a href="${contextPath}/community/communityList.do" class="menu__title">사용자 리뷰</a></li>
										<li><a href="${contextPath}/community/blackBoardList.do" class="menu__title">사장님 고충방</a></li>
									</ul>
								</c:if>
								
							<!-- 비회원 또는 기타 -->
							<c:if test="${not isLogOn or empty memberInfo}">
								<div>
									<a href="${contextPath}/community/communityList.do" class="menu__title">커뮤니티</a>
								</div>
							</c:if>
							</li>
								
							<li class="menu">
									<div>
										<a href="${contextPath}/event/promotionList.do" class="menu__title">이벤트★</a>											
									</div>
									<ul id="tipSybm1" class="navbar__submenu">
										<li><a href="${contextPath}/event/promotionList.do?pageType=sijangbajo">프로모션 목록</a></li>
										<li><a href="${contextPath}/event/couponList.do?pageType=sijangbajo">쿠폰 목록</a></li>
									</ul> 
									<c:if test="${isLogOn==true and not empty memberInfo and memberInfo.m_role == 3}">
										<ul id="tipSybm1" class="navbar__submenu">
											<li><a href="${contextPath}/event/promotionAddForm.do?pageType=sijangbajo">프로모션 등록</a></li>
											<li><a href="${contextPath}/event/couponAddForm.do">쿠폰 등록</a></li>
											<li><a href="${contextPath}/event/couponList.do?pageType=sijangbajo">쿠폰 목록</a></li>
										</ul>
									</c:if>
							</li>

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