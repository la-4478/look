package config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.lookmarket.chatbot.mapper.GoodsBotMapper;
import com.lookmarket.chatbot.util.GoodsNameExtractor;

@Configuration
public class ExtractorConfig {
    @Bean
    public GoodsNameExtractor goodsNameExtractor(GoodsBotMapper goodsMapper) {
        return new GoodsNameExtractor(goodsMapper::listAllGoodsNames);
    }

}
