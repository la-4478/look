<%@ page language="java" pageEncoding="UTF-8" isELIgnored="false" %>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<c:set var="contextPath"  value="${pageContext.request.contextPath}" />

<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>비밀번호 찾기</title>
</head>
<script src="https://code.jquery.com/jquery-3.7.1.min.js"></script>
<script>

	$(document).ready(function() {
		$("#findPw").click(function() {
			fn_findPw();
		});
	});

	function fn_findPw(){
	    var m_id=$("#m_id").val();
	    var m_name=$("#m_name").val();
	    
	    if(m_id==''){
		   	 alert("아이디를 입력하세요");
		   	 return;
	    }
	    if(m_name==''){
		   	 alert("이름을 입력하세요");
		   	 return;
	    }
	    
	    $.ajax({
	       type:"post",
	       async:false,
	       url:"${contextPath}/member/findPw.do",
	       dataType:"text",
	       data: {m_id:m_id, m_name:m_name},
	       success:function (data,textStatus){   	   
	    		if (data === null || data.trim() === "" || data === "NOT_FOUND") {
	    			$("#findPwMessage").text("입력하신 정보에 일치하는 비밀번호가 없습니다.").css("color", "red");
	    		} else {
	    			var message = "찾으시는 아이디의 비밀번호는 " + data + " 입니다.";
	    			$("#findPwMessage").text(message).css("color", "blue");
	    		}
	       },
	       error:function(data,textStatus){
	          alert("에러가 발생했습니다.");
	       }
	    }); 
	 }	
</script>
<body>
		<h2 class="mb-4 text-center">비밀번호 찾기</h2>

		<div class="col-md-6">
			<div class="mb-3">
				<label for="m_id" class="form-label">아이디</label>
				<input type="text" class="form-control" id="m_id" name="m_id">
			</div>
			<div class="mb-3">
				<label for="m_name" class="form-label">이름</label>
				<input type="text" class="form-control" id="m_name" name="m_name">
			</div>
			<div class="d-grid">
				<input type="button" class="btn btn-primary" id="findPw" value="비밀번호찾기" />
			</div>
		</div>
		<div>
			<p id="findPwMessage"></p>
		</div>
</body>
</html>
