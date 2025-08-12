<%@ page language="java" pageEncoding="UTF-8" isELIgnored="false" %>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<c:set var="contextPath"  value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>회원 유형 선택</title>
    <link rel="stylesheet" href="${contextPath}/resources/css/member.css"/>
</head>
<body>
	<div class="container text-center">
	    <h1 class="mb-3">회원가입 유형 선택</h1>
	
	        <input type="radio" name="m_role" value="1" id="personal" class="d-none" required>
	        <input type="radio" name="m_role" value="2" id="business" class="d-none">
	
	        <div class="d-flex justify-content-center flex-wrap gap-4 mb-4">
	            <label for="personal">
	                <div class="card p-4 text-center" id="label-personal" style="width: 280px;">
	                <a href="${contextPath}/member/memberForm.do">
	                    <img src="${contextPath}/resources/image/signSelect1.PNG" alt="개인회원" class="mx-auto">
	                    <h5 class="mt-2 fw-bold">개인 회원 가입</h5></a>
	                    
	                </div>

	            </label>
	            <label for="business">
	                <div class="card p-4 text-center" id="label-business" style="width: 280px;">
	               		<a href="${contextPath}/member/businessForm.do">
	                    	<img src="${contextPath}/resources/image/signSelect2.PNG" alt="사업자회원" class="mx-auto">
	                    <h5 class="mt-2 fw-bold">사업자 회원 가입</h5></a>
	                </div>
	            </label>
	        </div>	    
		</div>
    
    <script>
        const cards = document.querySelectorAll('.card');
        cards.forEach(card => {
            card.addEventListener('click', () => {
                cards.forEach(c => c.classList.remove('selected'));
                card.classList.add('selected');
            });
        });
    </script>

</body>
</html>
