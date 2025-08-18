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
  function appendMessage(message, isUser) {
    const messageElem = $('<div>').addClass('message').text(message);
    messageElem.addClass(isUser ? 'userMessage' : 'botMessage');
    $('#chatMessages').append(messageElem);
    $('#chatMessages').scrollTop($('#chatMessages')[0].scrollHeight);
  }

  let inFlight = false;

  function sendMessage() {
    if (inFlight) return;

    const text = $('#userMessage').val().trim();
    if (!text) { alert('메시지를 입력하세요.'); return; }

    inFlight = true;
    $('#sendButton').prop('disabled', true);

    appendMessage(text, true);
    $('#userMessage').val('');

    $.ajax({
      url: '${contextPath}/chatbot/message.do',
      method: 'POST',
      contentType: 'application/json; charset=UTF-8',
      dataType: 'json',
      data: JSON.stringify({ message: text })
    })
    .done(function(res) {
      // 항상 ChatResponse { content, sessionId, error }
      const msg = res && typeof res.content === 'string'
        ? res.content
        : '응답 형식이 올바르지 않습니다.';
      appendMessage(msg, false);
    })
    .fail(function(xhr) {
      const err = (xhr.responseJSON && xhr.responseJSON.content)
        ? xhr.responseJSON.content
        : '서버와 통신에 실패했습니다.';
      appendMessage(err, false);
    })
    .always(function() {
      inFlight = false;
      $('#sendButton').prop('disabled', false);
      $('#userMessage').focus();
    });
  }

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
