package com.lookmarket.chatbot.mapper;

import java.util.List;
import java.util.Map;

public interface PromoBotMapper {
    List<Map<String,Object>> findActivePromos(int limit);
    List<Map<String,Object>> findAvailableCouponsForUser(Map<String,Object> p);
}
