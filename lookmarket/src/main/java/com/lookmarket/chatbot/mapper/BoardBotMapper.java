package com.lookmarket.chatbot.mapper;

import java.util.List;
import java.util.Map;

public interface BoardBotMapper {
    List<Map<String,Object>> searchBoard(Map<String,Object> p);
}
