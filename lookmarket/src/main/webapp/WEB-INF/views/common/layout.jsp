<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" isELIgnored="false"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<c:set var="contextPath"  value="${pageContext.request.contextPath}" />

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>lookmarket</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.7/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-LN+7fdVzj6u52u30Kp6M/trliBMCMKTyK833zpbD+pXdCLuTusPj697FH4R/5mcr" crossorigin="anonymous">
    <link href="${contextPath}/resources/css/main.css" rel="stylesheet" type="text/css" media="screen">
    <script src="https://code.jquery.com/jquery-3.7.0.min.js" integrity="sha256-3gJwYpMe3Qm2LF6XnKsKuQv5JFNfEXiDAYTujMAeBAs=" crossorigin="anonymous"></script>

    <!-- 챗봇 관련 CSS -->
    <style>
        #chatbot-button {position: fixed;right: 20px;bottom: 20px;width: 60px;height: 60px;cursor: pointer;z-index: 9999;}
        #chatbot-button img {width: 100%;height: 100%;border-radius: 50%;box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);transition: transform 0.3s;}
        #chatbot-button img:hover {transform: scale(1.1);}
        #chatbot-popup {position: fixed;right: -350px;bottom: 80px;width: 300px;height: 400px;background-color: #fff;border: 1px solid #ddd;box-shadow: 0 0 10px rgba(0, 0, 0, 0.3);transition: right 0.4s ease;z-index: 9998;display: flex;flex-direction: column;}
        #chatbot-header {background-color: #4CAF50;color: #fff;padding: 10px;font-weight: bold;display: flex;justify-content: space-between;align-items: center;}
        #chatbot-body {padding: 10px;flex: 1;overflow-y: auto;}
        #chatbot-close {background: none;border: none;color: #fff;font-size: 16px;cursor: pointer;}
        #scroll-buttons{position:fixed; right:20px;bottom:100px;display:flex;flex-direction:column;gap:10px;z-index:9999;}
        .scroll-btn{width:45px;height:45px;border-radius:50%;background-color:#f5f5f5;border:none;box-shadow:0 4px 8px rgba(0,0,0,0.2);font-size:18px;cursor:pointer;}
        .scroll-btn:hover{background-color:#ddd;}
    </style>
</head>

<body>
    <c:if test="${not empty message}">
        <script>
            alert("${message}");
        </script>
    </c:if>

    <div class="container">
        <header>
            <%@ include file="header.jsp" %>
        </header>

        <div style="display: flex; padding-top: 220px;">
            <c:if test="${sideMenu == 'reveal'}">
                <aside>
                    <%@ include file="side.jsp" %>
                </aside>
            </c:if>

            <article style="flex: 1;">
                <jsp:include page="/WEB-INF/views${viewName}.jsp" />
            </article>
        </div>

        <footer>
            <%@ include file="footer.jsp" %>
        </footer>
    </div>

    <!-- 오른쪽 하단 챗봇 버튼 -->
    <div id="chatbot-button">
        <img src="${contextPath}/resources/image/chatbotyellow.png" alt="챗봇" />
    </div>

    <!-- 챗봇 팝업창 -->
    <div id="chatbot-popup">
        <div id="chatbot-header">
            <span>챗봇 상담</span>
            <button id="chatbot-close">X</button>
        </div>
        <div id="chatbot-body">
            <p>안녕하세요! 무엇을 도와드릴까요?</p>
            <!-- 추후 채팅 기능 여기에 추가 가능 -->
        </div>
    </div>
    <div id="scroll-buttons">
    	<button id="scroll-up" class="scroll-btn">▲</button>
    	<button id="scroll-down" class="scroll-btn">▼</button>
    </div>

    <!-- Bootstrap JS -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.7/dist/js/bootstrap.bundle.min.js" integrity="sha384-ndDqU0Gzau9qJ1lfW4pNLlhNTkCfHzAVBReH9diLvGRem5+R9g2FzA8ZGN954O5Q" crossorigin="anonymous"></script>

    <!-- 하나로 합쳐서 정리 -->
<script>
    $(document).ready(function() {
        // 챗봇 열기
        $('#chatbot-button').click(function() {
            $('#chatbot-popup').css('right', '20px');
        });

        // 챗봇 닫기
        $('#chatbot-close').click(function() {
            $('#chatbot-popup').css('right', '-350px');
        });

        // 위로 스크롤
        $('#scroll-up').click(function() {
            $('html, body').animate({ scrollTop: 0 }, 'slow');
        });

        // 아래로 스크롤
        $('#scroll-down').click(function() {
            $('html, body').animate({ scrollTop: $(document).height() }, 'slow');
        });
    });
</script>

    
</body>
</html>
