<%@ page language="java" pageEncoding="UTF-8" isELIgnored="false" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />

<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>내 정보 수정</title>
  <script src="https://t1.daumcdn.net/mapjsapi/bundle/postcode/prod/postcode.v2.js"></script>
  <style>
    body { font-family: sans-serif; padding: 2rem; }
    h2 { margin-bottom: 1rem; }
    table { width: 100%; border-collapse: collapse; }
    td { padding: 8px; vertical-align: top; }
    th { text-align: left; font-size: 0.9rem; padding: 8px; }
    input[type=text],
    input[type=password],
    textarea {
      width: 100%;
      padding: 6px;
      border: 1px solid #ccc;
      border-radius: 4px;
    }
    input[type=submit], input[type=button], button {
      padding: 8px 12px;
      margin-right: 8px;
      border: none;
      border-radius: 4px;
      background-color: #007BFF;
      color: white;
      cursor: pointer;
    }
    input[type=button]:last-child {
      background-color: #dc3545;
    }
    .badge { padding: 2px 6px; border-radius: 4px; background: #eee; color: #333; }
    .section { margin-bottom: 2rem; }
    .label-row td:first-child { font-weight: bold; width: 120px; }
  </style>

  <script>
    function setEmailDomain(value) {
      const domainInput = document.getElementById("m_email_domain");
      domainInput.value = value;
      domainInput.readOnly = value !== "";
      if (value === "") domainInput.focus();
    }

    function execDaumPostCode() {
      new daum.Postcode({
        oncomplete: function(data) {
          let fullRoadAddr = data.roadAddress;
          let extra = '';
          if (data.bname && /[동|로|가]$/.test(data.bname)) extra += data.bname;
          if (data.buildingName && data.apartment === 'Y') {
            extra += (extra ? ', ' : '') + data.buildingName;
          }
          if (extra) fullRoadAddr += ' (' + extra + ')';
          document.getElementById('m_zipcode').value = data.zonecode;
          document.getElementById('m_road_address').value = fullRoadAddr;
          document.getElementById('m_jibun_address').value = data.jibunAddress;
        }
      }).open();
    }

    function deletemypageInfo() {
      if (confirm("정말로 탈퇴하시겠습니까?\n7일 내 로그인 시 복구됩니다.\n7일 뒤 완전히 삭제됩니다.")) {
        document.getElementById("deleteForm").submit();
      }
    }
  </script>

  <c:if test="${not empty message}">
    <script>alert("${message}");</script>
  </c:if>
</head>
<body>

  <h2>내 정보 수정</h2>

  <form name="frm_mod_member" action="${contextPath}/mypage/updateMyInfo.do" method="post">
    <table>
      <tr class="label-row">
        <td>아이디</td>
        <td><input type="text" name="m_id" value="${member.m_id}" readonly /></td>
      </tr>
      <tr class="label-row">
        <td>비밀번호</td>
        <td><input type="password" name="m_pw" placeholder="새 비밀번호 입력" /></td>
      </tr>
      <tr class="label-row">
        <td>이름</td>
        <td><input type="text" name="m_name" value="${member.m_name}" readonly /></td>
      </tr>
      <tr class="label-row">
        <td>성별</td>
        <td>
          <label><input type="radio" name="m_gender" value="1" <c:if test="${member.m_gender == 1}">checked</c:if>> 남성</label>
          <label><input type="radio" name="m_gender" value="2" <c:if test="${member.m_gender == 2}">checked</c:if>> 여성</label>
        </td>
      </tr>
      <tr class="label-row">
        <td>휴대폰</td>
        <td>
          <input type="text" name="m_phone" value="${member.m_phone}" />
          <label><input type="checkbox" name="m_phone_yn" value="1" <c:if test="${member.m_phone_yn == 1}">checked</c:if>> SMS 수신 동의</label>
        </td>
      </tr>
      <tr class="label-row">
        <td>이메일</td>
        <td>
          <input type="text" name="m_email_id" value="${member.m_email_id}" size="12" readonly /> @
          <input type="text" name="m_email_domain" id="m_email_domain" value="${member.m_email_domain}" size="15" readonly />
          <label><input type="checkbox" name="m_email_yn" value="1" <c:if test="${member.m_email_yn == 1}">checked</c:if>> 이메일 수신 동의</label>
        </td>
      </tr>
      <tr class="label-row">
        <td>주소</td>
        <td>
          <div style="margin-bottom: 8px;">
            <input type="text" name="m_zipcode" id="m_zipcode" value="${member.m_zipcode}" size="10" readonly />
            <button type="button" onclick="execDaumPostCode()">주소 찾기</button>
          </div>
          <div>
            도로명 주소<br>
            <input type="text" name="m_road_address" id="m_road_address" value="${member.m_road_address}" />
            <br><br>지번 주소<br>
            <input type="text" name="m_jibun_address" id="m_jibun_address" value="${member.m_jibun_address}" />
            <br><br>상세 주소<br>
            <input type="text" name="m_namuji_address" value="${member.m_namuji_address}" />
          </div>
        </td>
      </tr>
    </table><br>

    <input type="submit" value="수정 완료" />
    <input type="button" value="회원 탈퇴" onclick="deletemypageInfo()" />
  </form>

  <form id="deleteForm" method="post" action="${contextPath}/mypage/deleteMyInfo.do">
    <input type="hidden" name="m_id" value="${member.m_id}" />
  </form>

</body>
</html>
