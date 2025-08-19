package com.lookmarket.chatbot.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface OrderBotMapper {

    // 단일 파라미터라도 XML에서 #{mId}로 쓰려면 @Param("mId") 必
    Map<String, Object> findLatestOrder(@Param("mId") String mId);

    List<Map<String, Object>> findOrderItems(@Param("oId") int oId);

    Map<String, Object> findLatestDeliveryByOrder(@Param("oId") int oId);

    Map<String, Object> findPaymentByOrder(@Param("oId") int oId);
}
