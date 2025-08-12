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
    <script src="https://code.jquery.com/jquery-3.7.0.min.js"></script>1
    <link href="${contextPath}/resources/css/chatbot.css" rel="stylesheet" type="text/css"><!-- 챗봇 CSS 파일 -->
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
                <jsp:include page="/WEB-INF/views/${viewName}.jsp" />
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
