<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<table class="goods-table">
  <thead>
    <tr>
      <th>상품번호</th>
      <th>상품명</th>
      <th>브랜드</th>
      <th>카테고리</th>
      <th>가격</th>
      <th>재고</th>
      <th>상태</th>
    </tr>
  </thead>
  <tbody>
    <c:forEach var="g" items="${goodsList}">
      <tr>
        <td>${g.g_id}</td>
        <td><a href="${contextPath}/jangbogo/goodsDetail.do?g_id=${g.g_id}">${g.g_name}</a></td>
        <td>${g.g_brand}</td>
        <c:if test="${g.g_category == '1' }">
        <td>신선식품</td>
        </c:if>
        <c:if test="${g.g_category == '2' }">
        <td>가공식품</td>
        </c:if>
        <c:if test="${g.g_category == '3' }">
        <td>생활용품</td>
        </c:if>
        <c:if test="${g.g_category == '4' }">
        <td>패션잡화</td>
        </c:if>
        <c:if test="${g.g_category == '5' }">
        <td>지역특산물</td>
        </c:if>
        <td><c:out value="${g.g_price}"/></td>
        <td>${g.g_stock}</td>
        <c:if test="${g.g_status == '1' }">
        <td>판매중</td>
        </c:if>
        <c:if test="${g.g_status == '2' }">
        <td>품절</td>
        </c:if>
        <c:if test="${g.g_status == '3' }">
        <td>판매종료</td>
        </c:if>
        <td>
          <a href="${contextPath}/jangbogo/busigoodsUpdateForm.do?g_id=${g.g_id}" class="btn btn-warning btn-small">수정</a>
           <form action="${contextPath}/business/busigoodsDelete.do" method="get" style="display:inline;" onsubmit="return confirm('정말 영구 삭제하시겠습니까? 복구 불가입니다.');">
			<input type="hidden" name="g_id" value="${g.g_id}">
			  <button type="submit" class="btn btn-danger btn-small">삭제</button>
			</form>

         </td>
      </tr>
    </c:forEach>
  </tbody>
</table>
