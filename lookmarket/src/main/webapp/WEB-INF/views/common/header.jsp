<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" isELIgnored="false"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<c:set var="contextPath"  value="${pageContext.request.contextPath}" />
<c:if test="${empty pageType}">
    <c:set var="pageType" value="${sessionScope.pageType}"/>
</c:if>

<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>header</title>
	<style>
		* {margin: 0;padding: 0;box-sizing: border-box;}
		body {background-color: white;}
		ul {list-style: none;padding: 0;text-align: center;}
		a{text-decoration: none;color: black;}
		header {position: fixed; top:0; background-color: #fff; margin:0 auto; width: 1296px; height: 220px; overflow : hidden;z-index: 1000;}
		.navbar__menu {display: flex;justify-content: space-evenly;	gap: 40px;}
		.navbar__submenu{margin-top: 10px;}
		header:hover{height: 400px;}
		.contents {width: 100%;height: 500px;margin-top: 50px;}
		.navbar__div nav.navbar {position: relative;z-index: 1001;}
		.menu {position: relative;padding: 0px 10px;}
		.menu__title {display: block;padding: 4px 15px;font-weight: bold;}
		.menu__title:hover {border-radius: 5px;}
		#head_link {display: flex;justify-content: space-between;align-items: center;padding: 0px 20px;width: 100%;}
		#head_link form {display: flex;align-items: center;height: 40px;}
		#head_link input[type="search"] {height: 32px;font-size: 14px;padding: 0 10px;}
		#head_link button {height: 32px;padding: 0 10px;font-size: 14px;margin-left: 5px;}
		#head_link ul {display: flex;align-items: center;list-style: none;margin: 0;padding: 0;}
		#head_link ul li {font-size: 14px;}
		.bg-sijangbajo {background-color: #f0eaff;}
		.bg-jangbogo {background-color: #eaffea;}
	</style>
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
	                    <img width="176" height="80" alt="시장봐조" src="${contextPath}/resources/image/sijangbajologo.png" />
	                </a>
	            </c:when>
	            <c:when test="${pageType eq 'jangbogo'}">
	                <a href="${contextPath}/main/jangbogoMain.do">
	                    <img width="176" height="80" alt="장보고" src="${contextPath}/resources/image/jangbogologo.png" />
	                </a>
	            </c:when>
	        </c:choose>
	    </div>			
	
        <div id="head_link">
            <form class="d-flex" role="search" action="${contextPath}/search.do" method="get">
                <input class="form-control me-2" type="search" name="q" placeholder="검색어를 입력하세요" />
                <button class="btn btn-outline-success" type="submit">🔍</button>
           	</form>
            <ul>
                <c:choose>
                    <c:when test="${isLogOn eq true}">
                        <li><a href="${contextPath}/member/logout.do">로그아웃</a></li>
                        <li><a href="${contextPath}/mypage/mypageInfo.do">마이페이지</a></li>
                        
                        <c:choose>
                            <c:when test="${memberInfo.m_role == 1}">
                                <li><a href="${contextPath}/cart/myCartList.do">장바구니</a></li>
                                <li><a href="#">주문내역</a></li>
                                <li><a href="${contextPath}/chatbot/chatbot.do">고객센터</a></li>
                            </c:when>
                            <c:when test="${memberInfo.m_role == 2}">
                                <li><a href="#">상품관리</a></li>
                                <li><a href="#">주문관리</a></li>
                                <li><a href="${contextPath}/chatbot/chatbot.do">고객센터</a></li>
                            </c:when>
                            <c:when test="${memberInfo.m_role == 3}">
                                <li><a href="#">상품관리</a></li>
                                <li><a href="#">회원관리</a></li>
                                <li><a href="${contextPath}/chatbot/chatbot.do">회계관리</a></li>
                            </c:when>
                        </c:choose>
                    </c:when>
                    <c:otherwise>
                        <li><a href="${contextPath}/member/loginForm.do">로그인</a></li>
                        <li><a href="${contextPath}/member/memberSelect.do">회원가입</a></li>
                        <li><a href="${contextPath}/chatbot/chatbot.do">고객센터</a></li>
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
									<a href="${contextPath}/sijangbajo/sijangSearch/search.do" class="menu__title">전통시장 찾기</a>
								</div>
							</li>
							<li class="menu">
								<div>
									<a href="${contextPath}/sijangbajo/nearby/nearby.do" class="menu__title">주변상권</a>
								</div>
								<ul id="tipSybm1" class="navbar__submenu">
									<li><a href="${contextPath}/sijangbajo/nearby/nearby.do">주변상권</a></li>
		            				<li><a href="${contextPath}/sijangbajo/nearby/recommendCourse.do">추천코스</a></li>
		            				<li><a href="#">지역축제</a></li>
								</ul>
							</li>
							<li class="menu">
								<div>
									<a href="${contextPath}/event/promotionList.do" class="menu__title">이벤트★</a>
								</div>
								<ul id="tipSybm1" class="navbar__submenu">
		                                <li><a href="${contextPath}/event/promotionList.do?pageType=sijangbajo">프로모션 목록</a></li>
								</ul>
								<ul id="tipSybm1" class="navbar__submenu">
		                                <li><a href="${contextPath}/event/promotionAddForm.do?pageType=sijangbajo">프로모션 등록</a></li>
								</ul>
							</li>
							<li class="menu">
								<div>

									<a href="${contextPath}/sijangbajo/clean/clean.do" class="menu__title">클린업체</a>
								</div>
							</li>
							
							<li class="menu">
								<div>
									<a href="${contextPath}/community/communityList.do" class="menu__title">커뮤니티</a>
								</div>
								<ul id="tipSybm1" class="navbar__submenu">
			            			<li><a href="${contextPath}/community/communityList.do">리뷰보기</a></li>
			            			<li><a href="${contextPath}/community/communityAddForm.do">리뷰쓰기</a></li>
								</ul>
							</li>
							<li class="menu">
								<div>
									<a href="${contextPath}/event/promotionList.do" class="menu__title">이벤트★</a>
									
								</div>
								<ul id="tipSybm1" class="navbar__submenu">
		                                <li><a href="${contextPath}/event/promotionList.do?pageType=sijangbajo">프로모션 목록</a></li>
								</ul>
							</li>
							
							<li class="menu">
								<div>
									<a href="${contextPath}/main/jangbogoMain.do" class="menu__title">장보고</a>
								</div>
							</li>
					
						</c:when>

                    	<c:when test="${pageType eq 'jangbogo'}">
                    		<li class="menu">
								<div>
									<a href="${contextPath}/jangbogo/goodsList.do?category=fresh" class="menu__title">상품보기</a>
									
								</div>
								<ul id="tipSybm1" class="navbar__submenu">
									<li><a href="${contextPath}/jangbogo/goodsList.do?category=all">전체보기</a></li>
	        						<li><a href="${contextPath}/jangbogo/goodsList.do?category=fresh">신선식품</a></li>
	        						<li><a href="${contextPath}/jangbogo/goodsList.do?category=processed">가공식품</a></li>
	        						<li><a href="${contextPath}/jangbogo/goodsList.do?category=living">생활용품</a></li>
	        						<li><a href="${contextPath}/jangbogo/goodsList.do?category=fashion">패션잡화</a></li>
	        						<li><a href="${contextPath}/jangbogo/goodsList.do?category=local">지역특산물</a></li>
								</ul>
							</li>
							<li class="menu">
								<div>
									<a href="${contextPath}/event/promotionList.do" class="menu__title">이벤트★</a>
								</div>
								<ul id="tipSybm1" class="navbar__submenu">
		                                <li><a href="${contextPath}/event/promotionList.do?pageType=sijangbajo">프로모션 목록</a></li>
								</ul>
							</li>							
							<li class="menu">
								<div>
									<a href="${contextPath}/community/communityList.do" class="menu__title">커뮤니티</a>
								</div>
								<ul id="tipSybm1" class="navbar__submenu">
			            			<li><a href="${contextPath}/community/communityList.do">커뮤니티 리뷰목록</a></li>
			            			<li><a href="${contextPath}/community/communityAddForm.do">리뷰 작성</a></li>
			            			<li><a href="${contextPath}/community/communityUpdateForm.do">리뷰 수정</a></li>
			            			<li><a href="${contextPath}/jangbogo/goodsUpdateForm.do" class="menu__title">상품수정</a></li>
								</ul>
							</li>
							
							<li class="menu">
								<div>
									<a href="${contextPath}/community/communityList.do" class="menu__title">주문</a>
								</div>
								<ul id="tipSybm1" class="navbar__submenu">
									<a href="${contextPath}/order/orderForm.do" class="menu__title">주문정보</a>
									<a href="${contextPath}/order/orderResult.do" class="menu__title">주문결과</a>
									<a href="${contextPath}/member/memberList.do" class="menu__title">회원관리</a>
									<a href="${contextPath}/member/businessMemberList.do" class="menu__title">사업자목록</a>
								</ul>
							</li>
							<li class="menu">
								<div>
									<a href="${contextPath}/main/sijangbajoMain.do" class="menu__title">시장봐조</a>
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