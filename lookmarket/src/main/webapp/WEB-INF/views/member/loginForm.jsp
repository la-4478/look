<%@ page language="java" pageEncoding="UTF-8" isELIgnored="false"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8" />
<title>로그인창</title>
<style>
html, body {
	height: 100%;
	margin: 0;
}
</style>
</head>
<body
	class="bg-light d-flex justify-content-center align-items-center min-vh-100">
	<div class="card" style="width: 400px;">
		<div class="card-body">
			<h3 class="card-title text-center mb-4">로그인</h3>
			<form action="${contextPath}/member/login.do" method="post">
				<div class="mb-3">
					<input type="text" name="m_id" class="form-control"
						placeholder="아이디" required />
				</div>
				<div class="mb-3">
					<input type="password" name="m_pw" class="form-control"
						placeholder="비밀번호" required />
				</div>
				<div class="d-grid gap-2 mb-3">
					<button type="submit" class="btn btn-primary">로그인</button>
					<button type="reset" class="btn btn-secondary">다시입력</button>
				</div>
			</form>

			<div class="mb-3 text-center">
				<a
					href="https://nid.naver.com/oauth2.0/authorize?response_type=code&client_id=0Kh9BAPo35nGcE_1Jbvx&redirect_uri=http://localhost:8090/lookmarket/member/naverCallback.do&state=lookmarket&auth_type=reauthenticate">
					<img src="${contextPath}/resources/image/naverlogin.png"
					alt="네이버 로그인" style="max-width: 100%; height: 50px;" />
				</a>
			</div>

			<div class="d-flex justify-content-center gap-3 small">
				<a href="${contextPath}/member/findIdForm.do" class="text-decoration-none text-muted">아이디 찾기</a> 
				<a href="${contextPath}/member/findPwForm.do" class="text-decoration-none text-muted">비밀번호 찾기</a> 
				<a href="${contextPath}/member/memberSelect.do" class="text-decoration-none text-muted">회원가입</a> 
				<a href="#" class="text-decoration-none text-muted">고객센터</a>
			</div>
		</div>
	</div>

</body>
</html>
