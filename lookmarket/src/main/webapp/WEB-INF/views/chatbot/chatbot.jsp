<%@ page language="java" pageEncoding="UTF-8" isELIgnored="false" %>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<c:set var="contextPath"  value="${pageContext.request.contextPath}" />

<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8" />
  <title>챗봇</title>
  <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
  <style>
    body {
      font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
      background: #f5f5f5;
      display: flex;
      justify-content: center;
      padding: 30px;
    }

    #chatContainer {
      width: 400px;
      background: #fff;
      border-radius: 8px;
      box-shadow: 0 4px 10px rgba(0,0,0,0.1);
      display: flex;
      flex-direction: column;
      height: 600px;
    }

    #chatHeader {
      background-color: #4CAF50;
      color: white;
      padding: 15px;
      font-size: 20px;
      font-weight: bold;
      border-radius: 8px 8px 0 0;
      text-align: center;
    }

    #chatMessages {
      flex-grow: 1;
      padding: 15px;
      overflow-y: auto;
      background-color: #e9ecef;
    }

    .message {
      margin-bottom: 15px;
      max-width: 80%;
      padding: 10px 15px;
      border-radius: 20px;
      clear: both;
      word-wrap: break-word;
      font-size: 14px;
      line-height: 1.4;
    }

    .userMessage {
      background-color: #4CAF50;
      color: white;
      float: left;
      border-bottom-left-radius: 0;
    }

    .botMessage {
      background-color: #ddd;
      color: #333;
      float: right;
      border-bottom-right-radius: 0;
    }

    #chatInputArea {
      display: flex;
      border-top: 1px solid #ddd;
      padding: 10px;
      background-color: white;
      border-radius: 0 0 8px 8px;
    }

    #userMessage {
      flex-grow: 1;
      border: 1px solid #ccc;
      border-radius: 20px;
      padding: 10px 15px;
      font-size: 14px;
      outline: none;
    }

    #sendButton {
      background-color: #4CAF50;
      border: none;
      color: white;
      padding: 0 20px;
      margin-left: 10px;
      border-radius: 20px;
      cursor: pointer;
      font-weight: bold;
      font-size: 14px;
      transition: background-color 0.3s ease;
    }

    #sendButton:hover {
      background-color: #45a049;
    }
  </style>
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
        url: '${contextPath}/chatbot/message.do',
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