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
         <div id="chatMessages"></div>
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
}

/**
 * 입력창의 텍스트를 서버에 보낸다.
 * - contentType: 'application/json' → 컨트롤러 @RequestBody 로 받게 함
 * - dataType: 'json' → 컨트롤러가 JSON 반환 시 자동 파싱
 */
// JSP의 sendMessage()에 추가
 let inFlight = false;

function sendMessage() {
if (inFlight) return;

const text = $('#userMessage').val().trim();
if (!text) { alert('메시지를 입력하세요.'); return; }

inFlight = true;
$('#sendButton').prop('disabled', true);

// 1) 사용자 말풍선 출력
appendMessage(text, true);
// 2) 입력창 비우기
$('#userMessage').val('');

// 3) 서버로 JSON POST
$.ajax({
url: '${contextPath}/chatbot/message.do',
method: 'POST',
contentType: 'application/json; charset=UTF-8',
dataType: 'json',
data: JSON.stringify({ message: text })
})
.done(function(response) {
const content = response?.content ?? '응답 형식이 올바르지 않습니다.';
appendMessage(content, false);
})
.fail(function(xhr) {
const errMsg = xhr.responseJSON?.content || '서버와 통신에 실패했습니다.';
appendMessage(errMsg, false);
})
.always(function() {
inFlight = false;                    // ✅ 여기서 풀어줘야 두 번째 요청 가능
$('#sendButton').prop('disabled', false);
$('#userMessage').focus();
});
}

//이벤트 바인딩(중복 바인딩 방지)
$('#sendButton').off('click.chat').on('click.chat', sendMessage);
$('#userMessage').off('keydown.chat').on('keydown.chat', function(e) {
if (e.key === 'Enter') {
e.preventDefault();
sendMessage();
}
});

  </script>
<script>
$(document).ready(function() {
	  // 열기
	  $('#chatbot-button').on('click', function() {
	    $('#chatbot-popup').addClass('open');
	  });

	  // 닫기
	  $('#chatbot-close').on('click', function() {
	    $('#chatbot-popup').removeClass('open');
	  });

	  // (선택) 바깥 클릭 시 닫기
	  $(document).on('click', function(e) {
	    const $popup = $('#chatbot-popup');
	    const $button = $('#chatbot-button');
	    if (!$popup.is(e.target) && $popup.has(e.target).length === 0 &&
	        !$button.is(e.target) && $button.has(e.target).length === 0) {
	      $popup.removeClass('open');
	    }
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
