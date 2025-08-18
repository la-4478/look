package com.lookmarket.chatbot.mapper;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.lookmarket.chatbot.vo.AppUserVO;
import com.lookmarket.chatbot.vo.ChatCallLogVO;
import com.lookmarket.chatbot.vo.ChatFeedbackVO;
import com.lookmarket.chatbot.vo.ChatMessageVO;
import com.lookmarket.chatbot.vo.ChatSessionVO;

@Mapper
public interface ChatMapper {
  // user
  int insertUser(AppUserVO u);
  AppUserVO findUserById(@Param("userId") long userId);
  AppUserVO findUserByUsername(@Param("username") String username);

  // session
  int insertSession(ChatSessionVO s);
  int updateSessionTitle(@Param("sessionId") long sessionId, @Param("title") String title);
  ChatSessionVO findSession(@Param("sessionId") long sessionId);
  List<ChatSessionVO> listSessionsByUser(@Param("userId") long userId,
                                         @Param("limit") int limit,
                                         @Param("offset") int offset);
  int deleteSession(@Param("sessionId") long sessionId);

  // message
  int insertMessage(ChatMessageVO m);
  List<ChatMessageVO> listMessages(@Param("sessionId") long sessionId,
                                   @Param("limit") int limit);
  List<ChatMessageVO> listMessagesAfter(@Param("sessionId") long sessionId,
                                        @Param("afterId") long afterMessageId);
  

  // call log
  int insertCallLog(ChatCallLogVO log);

  // feedback
  int insertFeedback(ChatFeedbackVO fb);
  List<ChatFeedbackVO> listFeedbackByMessage(@Param("messageId") long messageId);
}