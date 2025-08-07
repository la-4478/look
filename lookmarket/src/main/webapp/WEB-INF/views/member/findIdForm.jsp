<%@ page language="java" pageEncoding="UTF-8" isELIgnored="false" %>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<c:set var="contextPath"  value="${pageContext.request.contextPath}" />

<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>아이디 찾기</title>
</head>
<script src="https://code.jquery.com/jquery-3.7.1.min.js"></script>
<script>

	$(document).ready(function() {
		$("#findId").click(function() {
			fn_findId();
		});
	});

	function fn_findId(){
	    var m_name=$("#m_name").val();
	    var m_email=$("#m_email").val();
	    
	    if(m_name==''){
		   	 alert("이름을 입력하세요");
		   	 return;
	    }
	    if(m_email==''){
		   	 alert("이메일을 입력하세요");
		   	 return;
	    }
	    
	    $.ajax({
	       type:"post",
	       async:false,  
	       url:"${contextPath}/member/findId.do",
	       dataType:"text",
	       data: {m_name:m_name, m_email:m_email},
	       success:function (data,textStatus){
	    		if (data === null || data.trim() === "" || data === "NOT_FOUND") {
	    			$("#findIdMessage").text("입력하신 정보에 일치하는 아이디가 없습니다.").css("color", "red");
	    		} else {
					var message = "찾으시는 아이디는 " + data + " 입니다.";
					$("#findIdMessage").text(message).css("color", "blue");
	    		}
	       },
	       error:function(data,textStatus){
	          alert("에러가 발생했습니다.");
	       }
	    }); 
	 }	
</script>
<body>
		<h2 class="mb-4 text-center">아이디 찾기</h2>

		<div class="col-md-6">
			<div class="mb-3">
				<label for="m_name" class="form-label">이름</label>
				<input type="text" class="form-control" id="m_name" name="m_name">
			</div>
			<div class="mb-3">
				<label for="m_email" class="form-label">이메일</label>
				<input type="email" class="form-control" id="m_email" name="m_email">
			</div>
			<div class="d-grid">
				<input type="button" class="btn btn-primary" id="findId" value="아이디찾기" />
			</div>
		</div>
		<div>
			<p id="findIdMessage"></p>
		</div>
</body>
</html>
