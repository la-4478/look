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
	<link rel="stylesheet" href="${contextPath}/resources/css/mypage.css"/>
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
	<div class="my-info-card">
    <h3>내 상세 정보 수정</h3>
    <form name="frm_mod_member" action="${contextPath}/mypage/updateMyInfo.do" method="post">

        <div class="form-group icon user">
            <label>아이디</label>
            <input type="text" name="m_id" value="${myPageInfo.m_id}" readonly />
        </div>

        <div class="form-group icon pw">
            <label>비밀번호</label>
            <input type="password" name="m_pw" placeholder="새 비밀번호 입력" required="비밀번호를 입력해주세요" />
        </div>

        <div class="form-group icon user">
            <label>이름</label>
            <input type="text" name="m_name" value="${myPageInfo.m_name}" disabled />
        </div>

        <div class="form-group">
            <label>성별</label>
            <div class="checkbox-group">
                <input type="radio" name="m_gender" value="1" <c:if test="${myPageInfo.m_gender == 1}">checked</c:if> /> 남성
                <input type="radio" name="m_gender" value="2" <c:if test="${myPageInfo.m_gender == 2}">checked</c:if> /> 여성
            </div>
        </div>

        <div class="form-group icon phone">
            <label>휴대폰 번호</label>
            <input type="text" name="m_phone" value="${myPageInfo.m_phone}"/>
            <div class="checkbox-group">
                <input type="checkbox" name="m_phone_yn" value="1" <c:if test="${myPageInfo.m_phone_yn == 1}">checked</c:if> /> SMS 수신 동의
            </div>
        </div>

        <div class="form-group icon email">
            <label>이메일</label>
            <div style="display:flex; gap:5px; align-items:center;">
                <input type="text" name="m_email_id" value="${myPageInfo.m_email_id}" size="12" readonly /> @
                <input type="text" name="m_email_domain" id="m_email_domain" value="${myPageInfo.m_email_domain}" size="15" readonly />
            </div>
            <div class="checkbox-group">
                <input type="checkbox" name="m_email_yn" value="1" <c:if test="${myPageInfo.m_email_yn == 1}">checked</c:if> /> 이메일 수신 동의
            </div>
        </div>

        <div class="form-group icon address address-group">
            <label>주소</label>
            <input type="text" name="m_zipcode" id="m_zipcode" value="${myPageInfo.m_zipcode}" readonly size="10" />
            <button type="button" onclick="execDaumPostCode()">우편번호 검색</button>
            <input type="text" name="m_road_address" id="m_road_address" value="${myPageInfo.m_road_address}" size="50" placeholder="도로명 주소"/>
            <input type="text" name="m_jibun_address" id="m_jibun_address" value="${myPageInfo.m_jibun_address}" size="50" placeholder="지번 주소"/>
            <input type="text" name="m_namuji_address" value="${myPageInfo.m_namuji_address}" size="50" placeholder="상세 주소"/>
        </div>

        <div class="form-actions">
            <input type="submit" value="수정완료" />
            <input type="button" value="회원탈퇴" onclick="deletemypageInfo()" />
        </div>

    </form>
</div>

<form id="deleteForm" method="post" action="${contextPath}/mypage/deleteMyInfo.do">
    <input type="hidden" name="m_id" value="${myPageInfo.m_id}" />
</form>


</body>
</html>