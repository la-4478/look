<%@ page language="java" pageEncoding="UTF-8" isELIgnored="false" %>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<c:set var="contextPath"  value="${pageContext.request.contextPath}" />

<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8" />
  <title>챗봇</title>
  <!-- jQuery (Ajax 용) -->
  <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
  <!-- UI 스타일 -->
  <link href="${contextPath}/resources/css/chatbot.css" rel="stylesheet" type="text/css">
</head>
<body>
  <div id="chatContainer">
    <div id="chatHeader">고객센터 챗봇</div>
    <div id="chatMessages"></div>
    <div id="chatInputArea">
      <input id="userMessage" type="text" placeholder="질문을 입력하세요..." autocomplete="off" />
      <button id="sendButton">전송</button>
    </div>
  </div>

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

// 이벤트 바인딩(중복 바인딩 방지)
$('#sendButton').off('click.chat').on('click.chat', sendMessage);
$('#userMessage').off('keydown.chat').on('keydown.chat', function(e) {
  if (e.key === 'Enter') {
    e.preventDefault();
    sendMessage();
  }
});

  </script>
</body>
</html>
