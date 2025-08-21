<%@ page language="java" pageEncoding="UTF-8" isELIgnored="false" %>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<c:set var="contextPath"  value="${pageContext.request.contextPath}" />
<%
    String m_role = request.getParameter("m_role");
    if(m_role == null || (!m_role.equals("1") && !m_role.equals("2"))) {
        m_role = "1"; // 기본값 개인
    }
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>회원가입창</title>
    <script src="https://code.jquery.com/jquery-3.7.1.min.js"></script>
    <script src="https://t1.daumcdn.net/mapjsapi/bundle/postcode/prod/postcode.v2.js"></script>
    <script>
        $(document).ready(function(){
            // 가입유형 변경 시 폼 전환
            function toggleRoleForm(){
                const role = $("#m_role").val();
                if(role === "1"){ // 개인
                    $("#personalForm").show();
                    $("#businessForm").hide();
                }else if(role === "2"){ // 사업자
                    $("#personalForm").hide();
                    $("#businessForm").show();
                }
            }

            toggleRoleForm(); // 초기 실행

            $("#m_role").change(function(){
                toggleRoleForm();
            });

            let isIdChecked = false;
            let isEmailVerified = false;
            let serverAuthCode = "";

            $("#btnOverlapped").click(function(){
                const _m_id = $("#_m_id").val();

                if(_m_id ==''){
                    alert("아이디를 입력하세요.");
                    return ;
                }

                $.ajax({
                    type:"post",
                    async:false,
                    url:"${contextPath}/member/overlapped.do",
                    dataType:"text",
                    data:{m_id:_m_id},
                    success:function(data, textStatus){
                        if(data=='false'){
                            alert("사용할 수 있는 아이디입니다.");
                            $('#btnOverlapped').prop("disabled", true);
                            $('#_m_id').prop("disabled", true);
                            $('#m_id').val(_m_id);
                            isIdChecked = true;
                        }else{
                            alert("사용할 수 없는 아이디입니다.");
                        }
                    },
                    error:function(data, textStatus){
                        alert("에러가 발생했습니다.");
                    }
                });        
            });

            $("#sendAuthCodeBtn").click(function(){
                const email = $("#m_email").val();

                if(!email){
                    alert("이메일 주소를 입력해주세요.");
                    return;
                }

                $.ajax({
                    type:"POST",
                    url:"${contextPath}/member/mailCheck.do",
                    data:{"email": email},
                    success:function(data){
                        if(data !== 'true'){
                            alert("입력하신 이메일로 인증번호가 발송되었습니다.");
                            $("#authCodeInput").prop("disabled", false);
                            $("#mailCheckResult").text("인증번호가 발송되었습니다.").css("color", "green");
                            serverAuthCode = data;
                            isEmailVerified = false;
                        }else{
                            alert("이미 가입된 이메일입니다. 다른 이메일로 시도해주세요");
                            return ;
                        }
                    },
                    error: function(){
                        alert("메일 발송에 실패했습니다. 이메일 주소를 확인해주세요.");
                    }
                });            
            });

            $("#verifyAuthCodeBtn").click(function(){
                const userInputCode = $("#authCodeInput").val();

                if(!userInputCode){
                    alert("인증번호를 입력해주세요.");
                    return;
                }

                if(userInputCode === serverAuthCode){
                    $("#mailCheckResult").text("이메일 인증이 완료되었습니다.").css("color", "blue");
                    isEmailVerified = true;
                }else{
                    $("#mailCheckResult").text("인증번호가 일치하지 않습니다.").css("color", "red");
                    isEmailVerified = false;
                }        
            });

            $("#form").submit(function(e){
                if (!isIdChecked) {
                    alert("아이디 중복 체크를 완료해주세요.");
                    e.preventDefault();
                    return false;
                }
                const pw = $("#m_pw").val();
                const pw_confirm = $("#m_pw_confirm").val();
                if (pw !== pw_confirm) {
                    alert("비밀번호가 일치하지 않습니다. 다시 확인해주세요.");
                    $("#m_pw_confirm").focus();
                    e.preventDefault();
                    return false;
                }
                if (!isEmailVerified) {
                    alert("이메일 인증을 완료해주세요.");
                    e.preventDefault();
                    return false;
                }
            });
        });
        
        function exeDaumPostCode() {
            daum.postcode.load(function() {
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
                            extraRoadAddr = ' (' + extraRoadAddr + ')';
                        }

                        if(fullRoadAddr !== ''){
                            fullRoadAddr += extraRoadAddr;
                        }

                        document.getElementById('m_zipcode').value = data.zonecode;
                        document.getElementById('m_road_address').value = fullRoadAddr;
                        document.getElementById('m_jibun_address').value = data.jibunAddress;
                    }
                }).open();
            });
        }
    </script>
    <style>
        .fixed_join { width: 150px; font-weight: bold; vertical-align: top; }
        .dot_line { border-bottom: 1px dotted #aaa; }
    </style>
</head>
<body>
    <h3>회원가입</h3>
    <form action="${contextPath}/member/addMember.do" method="POST" id="form">
        <input type="hidden" name="m_role" value="1" />

        <table>
            <tbody>
                <tr class="dot_line">
                    <td class="fixed_join">아이디</td>
                    <td>
                        <input type="text" name="_m_id" id="_m_id" size="20" />
                        <input type="hidden" name="m_id" id="m_id" />
                        <input type="button" id="btnOverlapped" value="중복체크" />
                    </td>
                </tr>
                <tr class="dot_line">
                    <td class="fixed_join">비밀번호</td>
                    <td><input type="password" name="m_pw" id="m_pw" size="20" /></td>
                </tr>
                <tr class="dot_line">
                    <td class="fixed_join">비밀번호 확인</td>
                    <td><input type="password" name="m_pw_confirm" id="m_pw_confirm" size="20" /></td>
                </tr>
                <tr class="dot_line">
                    <td class="fixed_join">이름</td>
                    <td><input type="text" name="m_name" size="20" /></td>
                </tr>

                
                    <!-- 개인 회원 폼 -->
                    <tr class="dot_line">
                        <td class="fixed_join">주민등록번호</td>
                        <td><input type="text" name="m_birth" size="20" /> - <input type="text" name="m_gender" size="1" />******</td>
                    </tr>

                <tr class="dot_line">
                    <td class="fixed_join">통신사</td>
                    <td>
                        <select name="m_phone_telecom">
                            <option value="01">SKT</option>
                            <option value="02">KT</option>
                            <option value="03">LG U+</option>
                            <option value="11">알뜰폰 SKT</option>
                            <option value="12">알뜰폰 KT</option>
                            <option value="13">알뜰폰 LG U+</option>
                        </select>
                    </td>
                </tr>
                <tr class="dot_line">
                    <td class="fixed_join">전화번호</td>
                    <td><input type="text" name="m_phone" size="20" placeholder="번호만 입력" /></td>
                </tr>
                <tr class="dot_line">
                    <td class="fixed_join">이메일<br>(e-mail)</td>
                    <td>
                        <div class="form-group">
                            <div style="display: flex; align-items: center;">
                                <input type="text" id="m_email" name="m_email" class="form-control" placeholder="이메일을 입력하세요" required>
                                <button type="button" id="sendAuthCodeBtn" class="btn btn-secondary" style="margin-left: 10px; white-space: nowrap;">인증번호 발송</button>
                            </div>
                        </div>
                        <div class="form-group">
                            <div style="display: flex; align-items: center; margin-top: 5px;">
                                <input type="text" id="authCodeInput" class="form-control" placeholder="인증번호를 입력하세요" disabled>
                                <button type="button" id="verifyAuthCodeBtn" class="btn btn-primary" style="margin-left: 10px; white-space: nowrap;">인증번호 확인</button>
                            </div>
                            <p id="mailCheckResult" style="margin-top: 5px;"></p>
                        </div>
                    </td>
                </tr>
                <tr class="dot_line">
                    <td class="fixed_join">주소</td>
                    <td>
                        <input type="text" name="m_zipcode" id="m_zipcode" size="10"> <a href="javascript:exeDaumPostCode()">우편번호검색</a>
                        <br>
                        <p>
                            도로명 주소:<br><input type="text" id="m_road_address" name="m_road_address" size="50"><br><br>
                            지번 주소:<br><input type="text" id="m_jibun_address" name="m_jibun_address" size="50"><br><br>
                            상세 주소:<br><input type="text" name="m_namuji_address" size="50"><br><br>
                        </p>
                    </td>
                </tr>
                <tr class="dot_line">
                    <td class="fixed_join">광고 수신 동의</td>
                    <td>
                        <input type="checkbox" name="m_phone_yn" value="1" checked /> 쇼핑몰에서 발송하는 SMS 소식을 수신합니다.<br>
                        <input type="checkbox" name="m_email_yn" value="1" checked /> 쇼핑몰에서 발송하는 이메일 소식을 수신합니다.
                    </td>
                </tr>
            </tbody>
        </table>

        <br>
        <input type="submit" value="회원가입">
        <input type="reset" value="취소">
    </form>
</body>
</html>
