package com.lookmarket.chatbot.mapper;

import java.util.List;
import java.util.Map;

public interface OrderBotMapper {
    Map<String,Object> findLatestOrder(String mId);
    List<Map<String,Object>> findOrderItems(int oId);
    Map<String,Object> findLatestDeliveryByOrder(int oId);
    Map<String,Object> findPaymentByOrder(int oId);
}
