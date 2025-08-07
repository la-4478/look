<%@ page language="java" pageEncoding="UTF-8" isELIgnored="false"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<c:set var="contextPath"  value="${pageContext.request.contextPath}" />

<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>side</title>
</head>
<body>
	<nav class="side">
		<ul>
			<c:choose>
				<c:when test="${sideMenu_option == 'myPage'}">
					<li>
						<h3>마이페이지</h3>
						<ul>
							<li><a href="${contextPath}/mypage/mypageInfo.do">내정보</a></li>
							<li><a href="${contextPath}/cart/myCartList.do">장바구니</a></li>
							<li><a href="${contextPath}/mypage/listMyOrderHistory.do">주문내역</a></li>
							<li><a href="#">배송조회</a></li>
							<li><a href="${contextPath}/mypage/myWishList.do">찜목록</a></li>
							<li><a href="${contextPath}/mypage/myCommunity.do">커뮤니티</a></li>
						</ul>
					</li>
				</c:when>
				<c:when test="${sideMenu_option == 'myPage_business'}">
					<li>
						<h3>마이페이지</h3>
						<ul>
							<li><a href="${contextPath}/business/mypage/mypageBusinessInfo.do">내정보</a></li>
							<li><a href="${contextPath}/business/jangbogo/businessGoodsList.do">상품관리</a></li>
							<li><a href="${contextPath}/business/order/businessOrderList.do">주문관리</a></li>
							<li><a href="${contextPath}/business/member/myMemberList.do">회원관리</a></li>
							<li><a href="${contextPath}/business/mypage/myCommunityList.do">커뮤니티</a></li>
							<li><a href="${contextPath}/business/mypage/myBlackBoardList.do">사장님고충방</a></li>
						</ul>
					</li>
				</c:when>
				<c:when test="${sideMenu_option == 'myPage_admin'}">
					<li>
						<h3>마이페이지</h3>
						<ul>
							<li><a href="${contextPath}/admin/mypage/mypageAdminInfo.do">내정보</a></li>
							<li><a href="${contextPath}/admin/jangbogo/allGoodsList.do">상품관리</a></li>
							<li><a href="${contextPath}/admin/order/allOrderList.do">주문관리</a></li>
							<li><a href="${contextPath}/admin/member/allMemberList.do">회원관리</a></li>
							<li><a href="${contextPath}/admin/mypage/allCommunityList.do">커뮤니티</a></li>
							<li><a href="${contextPath}/admin/mypage/allBlackBoardList.do">사장님고충방</a></li>
							<li><a href="${contextPath}/admin/accountList.do">회계관리</a></li>
							
						</ul>
					</li>
				</c:when>
				<c:when test="${sideMenu_option == 'search'}">
					<li>
						<h3>전통시장 찾기</h3>
						<ul>
							<li><a href="${contextPath}/sijangbajo/sijangSearch/search.do">시장찾기</a></li>
						</ul>
				</c:when>
				<c:when test="${sideMenu_option == 'nearby'}">
					<li>
						<h3>주변상권</h3>
						<ul>
							<li><a href="${contextPath}/sijangbajo/nearby/nearby.do">주변상권</a></li>
							<li><a href="${contextPath}/sijangbajo/nearby/nearCourse.do">추천코스</a></li>
						</ul>
				</c:when>
				<c:when test="${sideMenu_option == 'event'}">
					<li>
						<h3>이벤트</h3>
						<ul>
							<li><a href="${contextPath}/event/promotionList.do">프로모션</a></li>
						</ul>
				</c:when>
				<c:when test="${sideMenu_option == 'clean'}">
					<li>
						<h3>클린업체</h3>
						<ul>
							<li><a href="${contextPath}/sijangbajo/clean/clean.do">클린업체</a></li>
						</ul>
				</c:when>
				<c:when test="${sideMenu_option == 'community'}">
					<li>
						<h3>커뮤니티</h3>
						<ul>
							<li><a href="${contextPath}/community/promotionList.do">커뮤니티</a></li>
						</ul>
				</c:when>
				<c:when test="${sideMenu_option == 'community_admin'}">
					<li>
						<h3>커뮤니티</h3>
						<ul>
							<li><a href="${contextPath}/community/communityList.do">커뮤니티</a></li>
							<li><a href="${contextPath}/community/blackBoardList.do">사장님고충방</a></li>
						</ul>
				</c:when>
				<c:when test="${sideMenu_option == 'category'}">
					<li>
						<h3>카테고리</h3>
						<ul>
							<li><a href="${contextPath}/jangbogo/goodsList.do?">신선식품</a></li>
							<li><a href="${contextPath}/jangbogo/goodsList.do?">가공식품</a></li>
							<li><a href="${contextPath}/jangbogo/goodsList.do?">생활용품</a></li>
							<li><a href="${contextPath}/jangbogo/goodsList.do?">패션잡화</a></li>
							<li><a href="${contextPath}/jangbogo/goodsList.do?">지역특산물 </a></li>
						</ul>
				</c:when>
			</c:choose>
		</ul>
	</nav>
</body>
</html>