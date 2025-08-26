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
							<li><a href="${contextPath}/mypage/listMyOrderHistory.do">주문내역</a></li>
							<li><a href="${contextPath}/mypage/listMyDelivery.do">배송조회</a></li>
							<li><a href="${contextPath}/mypage/myWishList.do">찜목록</a></li>
							<li><a href="${contextPath}/mypage/myCommunity.do">커뮤니티</a></li>
						</ul>
					</li>
				</c:when>
				<c:when test="${sideMenu_option == 'myPage_business'}">
					<li>
						<h3>사업자 메뉴</h3>
						<ul><c:if test="${businessVO.bm_status == 2 }">
							<li><a href="${contextPath}/jangbogo/busigoodsAddForm.do">상품 등록</a></li>
							</c:if>
							<li><a href="${contextPath}/business/businessGoodsList.do?category=all">내 상품관리</a></li>
							<li><a href="${contextPath}/business/businessOrderList.do">주문관리</a></li>
							<li><a href="${contextPath}/business/BlackBoardList.do">사장님고충방</a></li>
						</ul>
					</li>
				</c:when>
				<c:when test="${sideMenu_option == 'myPage_admin'}">
					<li>
						<h3>관리자 메뉴</h3>
						<ul>
							<li><a href="${contextPath}/admin/mypage/mypageAdminInfo.do">내정보</a></li>
							<li><a href="${contextPath}/admin/allGoodsList.do">상품관리</a></li>
							<li><a href="${contextPath}/admin/allOrderList.do">주문관리</a></li>
							<li><a href="${contextPath}/admin/allMemberList.do">회원관리</a></li>
							<li><a href="${contextPath}/admin/community/allCommunityList.do">리뷰관리</a></li>
							<li><a href="${contextPath}/admin/community/allBlackBoardList.do">사장님고충방</a></li>
							<li><a href="${contextPath}/admin/ApprovalList.do">사업자 승인 요청</a></li>
							<li><a href="${contextPath}/admin/accountList.do">회계관리</a></li>
							<li><a href="${contextPath}/inquiry/inquiryList.do">1:1문의</a></li>							
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
				
				<c:when test="${sideMenu_option == 'community'}">
					<li>
						<h3>커뮤니티</h3>
						<ul>
							<li><a href="${contextPath}/community/communityList.do">커뮤니티</a></li>
							<!-- 로그인한 사용자만 리뷰쓰기 메뉴 표시 -->
							<c:if test="${not empty sessionScope.current_id}">
								<li><a href="${contextPath}/community/communityAddForm.do">리뷰쓰기</a></li>
							</c:if>
						</ul>
					</li>
				</c:when>
				<c:when test="${sideMenu_option == 'community_admin'}">
					<li>
						<h3>커뮤니티</h3>
						<ul>
							<li><a href="${contextPath}/community/blackBoardAddForm.do">사장님고충방 등록</a></li>
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