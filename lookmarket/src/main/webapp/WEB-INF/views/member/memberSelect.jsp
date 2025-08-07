<%@ page language="java" pageEncoding="UTF-8" isELIgnored="false" %>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<c:set var="contextPath"  value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>회원 유형 선택</title>
    <style>
        body {padding: 50px 20px;background-color: #f8f9fa;}
        .card {cursor: pointer;transition: transform 0.2s, box-shadow 0.2s, border-color 0.2s;border: 2px solid #ddd;}
        .card:hover {transform: translateY(-5px);box-shadow: 0 5px 15px rgba(0,0,0,0.2);border-color: #4CAF50;}
        .card.selected {border-color: #4CAF50;box-shadow: 0 5px 15px rgba(0,0,0,0.3);}
        .card img {width: 80px;height: 80px;object-fit: contain;margin-bottom: 15px;}
        .btn-primary {width: 100%;padding: 12px;font-size: 16px;}
        @media (max-width: 576px) {.col-md-4 {flex: 0 0 100%;max-width: 100%;}}
    </style>
</head>
<body>
	<div class="container text-center">
	    <h1 class="mb-3">회원가입 유형 선택</h1>
	
	    <form action="${contextPath}/member/memberForm.do" method="get">
	        <input type="radio" name="m_role" value="1" id="personal" class="d-none" required>
	        <input type="radio" name="m_role" value="2" id="business" class="d-none">
	
	        <div class="d-flex justify-content-center flex-wrap gap-4 mb-4">
	            <label for="personal">
	                <div class="card p-4 text-center" id="label-personal" style="width: 280px;">
	                    <img src="${contextPath}/resources/image/signSelect1.PNG" alt="개인회원" class="mx-auto">
	                    <h5 class="mt-2 fw-bold">개인 회원 가입</h5>
	                </div>
	            </label>
	            <label for="business">
	                <div class="card p-4 text-center" id="label-business" style="width: 280px;">
	                    <img src="${contextPath}/resources/image/signSelect2.PNG" alt="사업자회원" class="mx-auto">
	                    <h5 class="mt-2 fw-bold">사업자 회원 가입</h5>
	                </div>
	            </label>
	        </div>
	
	        <div class="row justify-content-center">
	            <div class="col-md-4">
	                <button type="submit" class="btn btn-primary">다음</button>
	            </div>
	        </div>
	    </form>
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
