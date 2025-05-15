package Today.WishWordrobe.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {

    @Bean
    public RedisConnectionFactory redisConnectionFactory(){
        return new LettuceConnectionFactory("localhost",6379);
    }

    @Bean
    public RedisTemplate<HotelCacheKey, HotelCacheValue> hotelCacheRedisTemplate(){
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(cacheRedisConnectionFactory());
        template.setKeySerializer(new HotelCacheKeySerializer());
        template.setValueSerializer(new HotelCacheValueSerializer());
        return hotelCacheRedisTemplate;

    }
}
