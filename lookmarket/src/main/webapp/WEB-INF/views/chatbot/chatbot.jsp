<%@ page language="java" pageEncoding="UTF-8" isELIgnored="false" %>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<c:set var="contextPath"  value="${pageContext.request.contextPath}" />

<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8" />
  <title>챗봇</title>
  <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<link href="${contextPath}/resources/css/chatbot.css" rel="stylesheet" type="text/css"> <!-- 챗봇 CSS 파일 -->
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
      if (isUser) {
        messageElem.addClass('userMessage');
      } else {
        messageElem.addClass('botMessage');
      }
      $('#chatMessages').append(messageElem);
      $('#chatMessages').scrollTop($('#chatMessages')[0].scrollHeight);
    }

    function sendMessage() {
      const msg = $('#userMessage').val().trim();
      if (!msg) return alert('메시지를 입력하세요.');

      appendMessage(msg, true);
      $('#userMessage').val('');

      $.ajax({
        url: '${contextPath}/chatbot/ask.do',
        method: 'POST',
        contentType: 'application/json',
        data: JSON.stringify({ message: msg }),
        success: function(response) {
          appendMessage(response, false);
        },
        error: function() {
          appendMessage('서버와 통신에 실패했습니다.', false);
        }
      });
    }

    $('#sendButton').on('click', sendMessage);

    $('#userMessage').on('keypress', function(e) {
      if (e.key === 'Enter') {
        e.preventDefault();
        sendMessage();
      }
    });
  </script>
</body>
</html>