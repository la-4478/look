<%@ page language="java" pageEncoding="UTF-8" isELIgnored="false" %>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<c:set var="contextPath"  value="${pageContext.request.contextPath}" />
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>내정보</title>
	<script src="https://t1.daumcdn.net/mapjsapi/bundle/postcode/prod/postcode.v2.js"></script>
	<script>

		function setEmailDomain(value) {
		    const domainInput = document.getElementById("m_email_domain");
		    domainInput.value = value;
		    if (value === "") {
		        domainInput.removeAttribute("readonly");
		        domainInput.focus();
		    } else {
		        domainInput.setAttribute("readonly", true);
		    }
		}
		
		function enableAddressFields() {
		    document.getElementById("m_jibun_address").disabled = false;
		    document.getElementById("m_road_address").disabled = false;
		    document.getElementById("m_namuji_address").disabled = false;
		    document.getElementById("m_jibun_address").focus();
		}
		
		function execDaumPostCode() {
		    new daum.Postcode({
		        oncomplete: function(data) {
		            var fullRoadAddr = data.roadAddress;
		            var extraRoadAddr = '';
		
		            if(data.bname !== '' && /[동|로|가]$/g.test(data.bname)){
		                extraRoadAddr += data.bname;
		            }
		            if(data.buildingName !== '' && data.apartment === 'Y'){
		                extraRoadAddr += (extraRoadAddr !== '' ? ', ' + data.buildingName : data.buildingName);
		            }
		            if(extraRoadAddr !== ''){
		                fullRoadAddr += ' (' + extraRoadAddr + ')';
		            }
		
		            document.getElementById('m_zipcode').value = data.zonecode;
		            document.getElementById('m_road_address').value = fullRoadAddr;
		            document.getElementById('m_jibun_address').value = data.jibunAddress;
		        }
		    }).open();
		}
		
		//회원 탈퇴
		function deletemypageInfo() {
			if (confirm("정말로 회원을 탈퇴하시겠습니까?\n7일 내에 로그인 또는 회원가입 시도 시 계정이 복구됩니다.\n7일 뒤에 계정이 완전히 삭제됩니다.")) {
				document.getElementById("deleteForm").submit();
			}
		}
	</script>
<c:if test="${not empty message}">
    <script>
        alert("${message}");
    </script>
</c:if>
</head>
<body>
	<h3>내 상세 정보 수정</h3>
	<form name="frm_mod_member" action="${contextPath}/mypage/updateMyInfo.do" method="post">
		<table>
  			<tr>
    			<td>아이디</td>
    			<td><input type="text" name="m_id" value="${myPageInfo.m_id}" readonly /></td>
  			</tr>
  			<tr>
    			<td>비밀번호</td>
   				<td><input type="password" name="m_pw" placeholder="새 비밀번호 입력" /></td>
  			</tr>
  			<tr>
    			<td>이름</td>
   				<td><input type="text" name="m_name" value="${myPageInfo.m_name}" disabled /></td>
 	 		</tr>
  			<tr>
   		 		<td>성별</td>
    			<td>
					<input type="radio" name="m_gender" value="1" <c:if test="${myPageInfo.m_gender == 1}">checked</c:if> /> 남성
					<input type="radio" name="m_gender" value="2" <c:if test="${myPageInfo.m_gender == 2}">checked</c:if> /> 여성
    			</td>
 			</tr>
  			<tr>
    			<td>휴대폰 번호</td>
    			<td>
      				<input type="text" name="m_phone" value="${myPageInfo.m_phone}"/><br>
      				<input type="checkbox" name="m_phone_yn" value="1" <c:if test="${myPageInfo.m_phone_yn == 1}">checked</c:if> /> SMS 수신 동의
   	 			</td>
 			</tr>
  			<tr>
    			<td>이메일</td>
    			<td>
      				<input type="text" name="m_email_id" value="${myPageInfo.m_email_id}" size="12" /> @
      				<input type="text" name="m_email_domain" id="m_email_domain" value="${myPageInfo.m_email_domain}" size="15" />
      				<select onchange="setEmailDomain(this.value)">
	        			<option value="non">직접입력</option>
	        			<option value="hanmail.net">hanmail.net</option>
				        <option value="naver.com">naver.com</option>
				        <option value="yahoo.co.kr">yahoo.co.kr</option>
				        <option value="hotmail.com">hotmail.com</option>
				        <option value="paran.com">paran.com</option>
				        <option value="nate.com">nate.com</option>
				        <option value="google.com">google.com</option>
				        <option value="gmail.com">gmail.com</option>
				        <option value="empal.com">empal.com</option>
				        <option value="korea.com">korea.com</option>
				        <option value="freechal.com">freechal.com</option>
      				</select><br>
      				<input type="checkbox" name="m_email_yn" value="1" <c:if test="${myPageInfo.m_email_yn == 1}">checked</c:if> /> 이메일 수신 동의
    			</td>
  			</tr>
  			<tr>
    			<td>주소</td>
    			<td>
      				<input type="text" name="m_zipcode" id="m_zipcode" value="${myPageInfo.m_zipcode}" readonly size="10" />
      				<button type="button" onclick="execDaumPostCode()">우편번호 검색</button><br>
      				도로명 주소:<br>
				    <input type="text" name="m_road_address" id="m_road_address" value="${myPageInfo.m_road_address}" size="50" /><br><br>
				    지번 주소:<br>
				    <input type="text" name="m_jibun_address" id="m_jibun_address" value="${myPageInfo.m_jibun_address}" size="50" /><br><br>
				    상세 주소:<br>
      				<input type="text" name="m_namuji_address" value="${myPageInfo.m_namuji_address}" size="50" />
    			</td>
  			</tr>
		</table><br>
		<input type="submit" value="수정완료" />
		<input type="button" value="회원탈퇴" onclick="deletemypageInfo()"/>
	</form>

	<form id="deleteForm" method="post" action="${contextPath}/mypage/deleteMyInfo.do">
		<input type="hidden" name="m_id" value="${myPageInfo.m_id}" />
		
	</form>
</body>
</html>