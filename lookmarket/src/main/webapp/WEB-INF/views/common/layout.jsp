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
    <script src="https://code.jquery.com/jquery-3.7.0.min.js"></script>
    <link href="${contextPath}/resources/css/chatbot.css" rel="stylesheet" type="text/css"><!-- 챗봇 CSS 파일 -->
</head>
<script>
    /**
     * 채팅창에 말풍선을 추가한다.
     * @param {string} message - 표시할 텍스트
     * @param {boolean} isUser - true면 사용자 말풍선, false면 봇 말풍선
     */
    function appendMessage(message, isUser) {
      const messageElem = $('<div>').addClass('message').text(message);
      if (isUser) {
        messageElem.addClass('userMessage');  // 우측 정렬 등 스타일링용 클래스
      } else {
        messageElem.addClass('botMessage');   // 좌측 정렬 등 스타일링용 클래스
      }
      $('#chatMessages').append(messageElem);
      // 최근 메시지가 보이도록 스크롤을 하단으로 내린다.
      $('#chatMessages').scrollTop($('#chatMessages')[0].scrollHeight);
    }

    /**
     * 입력창의 텍스트를 서버에 보낸다.
     * - contentType: 'application/json' → 컨트롤러 @RequestBody 로 받게 함
     * - dataType: 'json' → 컨트롤러가 JSON 반환 시 자동 파싱
     */
    function sendMessage() {
      const msg = $('#userMessage').val().trim();
      if (!msg) return alert('메시지를 입력하세요.');

      // 1) 먼저 사용자 말풍선 출력
      appendMessage(msg, true);
      // 2) 입력창 비우기
      $('#userMessage').val('');

      // 3) 서버로 JSON POST
      $.ajax({
        url: '${contextPath}/chatbot/message.do',       // 컨트롤러 매핑
        method: 'POST',
        contentType: 'application/json; charset=UTF-8', // 서버가 @RequestBody로 JSON 받도록
        dataType: 'json',                               // 서버 응답을 JSON으로 파싱
        data: JSON.stringify({ message: msg }),         // {"message":"..."} 형태
        success: function(response) {
          // 컨트롤러가 ChatResponse { content: "..."}를 돌려줌
          appendMessage(response.content, false);
        },
        error: function(xhr) {
          // 에러도 JSON 포맷을 유지했으므로 .content 우선 시도
          const msg = xhr.responseJSON?.content || '서버와 통신에 실패했습니다.';
          appendMessage(msg, false);
        }
      });
    }

    // 전송 버튼 클릭
    $('#sendButton').on('click', sendMessage);

    // Enter 키 전송 (Shift+Enter 로 줄바꿈 같은 고급 기능은 추후)
    $('#userMessage').on('keypress', function(e) {
      if (e.key === 'Enter') {
        e.preventDefault();
        sendMessage();
      }
    });
  </script>

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
	    <div id="chatInputArea">
	      <input id="userMessage" type="text" placeholder="질문을 입력하세요..." autocomplete="off" />
	      <button id="sendButton">전송</button>
        </div>
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
