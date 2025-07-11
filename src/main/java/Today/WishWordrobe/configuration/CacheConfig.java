package Today.WishWordrobe.configuration;

import Today.WishWordrobe.domain.*;
import Today.WishWordrobe.domain.WeatherCacheKey;
import Today.WishWordrobe.domain.WeatherCacheKeySerializer;
import Today.WishWordrobe.domain.WeatherCacheValue;
import Today.WishWordrobe.domain.WeatherCacheValueSerializer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import io.lettuce.core.ClientOptions;
import io.lettuce.core.SocketOptions;

import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;

import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.util.Arrays;
import java.time.Duration;

@Slf4j
@Configuration
@EnableCaching
public class CacheConfig {

    @Bean
    public CacheManager cacheManager(){
        SimpleCacheManager cacheManager = new SimpleCacheManager();
        cacheManager.setCaches(Arrays.asList
                (new ConcurrentMapCache("clothesCache")));
        return cacheManager;
    }
   @Bean
   public RedisConnectionFactory cacheRedisConnectionFactory0(){
       RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration("127.0.0.1", 6379);
       configuration.setDatabase(0);
        //        configuration.setUsername("username");
        //        configuration.setPassword("password");

        final SocketOptions socketOptions = SocketOptions.builder().connectTimeout(Duration.ofSeconds(10)).build();
        final ClientOptions clientOptions = ClientOptions.builder().socketOptions(socketOptions).build();

       LettuceClientConfiguration lettuceClientConfiguration = LettuceClientConfiguration.builder()
               .clientOptions(clientOptions)
               .commandTimeout(Duration.ofSeconds(10))
               .shutdownTimeout(Duration.ZERO)
               .build();

        return new LettuceConnectionFactory(configuration, lettuceClientConfiguration);

   }




    /*
    1. 고객 서비스

     */



    /*
      2.  옷장서비스
        */

    @Bean
    public RedisConnectionFactory cacheRedisConnectionFactory1(){
        RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration("127.0.0.1", 6379);
        configuration.setDatabase(1);
        //        configuration.setUsername("username");
        //        configuration.setPassword("password");

        final SocketOptions socketOptions = SocketOptions.builder().connectTimeout(Duration.ofSeconds(10)).build();
        final ClientOptions clientOptions = ClientOptions.builder().socketOptions(socketOptions).build();

        LettuceClientConfiguration lettuceClientConfiguration = LettuceClientConfiguration.builder()
                .clientOptions(clientOptions)
                .commandTimeout(Duration.ofSeconds(10))
                .shutdownTimeout(Duration.ZERO)
                .build();

        return new LettuceConnectionFactory(configuration, lettuceClientConfiguration);

    }


    @Bean(name = "clothesCacheRedisTemplate")
    public RedisTemplate<ClothesCacheKey, ClothesCacheValue> clothesCacheRedisTemplate(
            RedisConnectionFactory connectionFactory){

                RedisTemplate<ClothesCacheKey, ClothesCacheValue> template = new RedisTemplate<>();

                template.setConnectionFactory(connectionFactory);

                template.setKeySerializer(new ClothesCacheKeySerializer());

                template.setValueSerializer(new ClothesCacheValueSerializer());

                return template;

    }


     /*
     3.  날씨서비스
       */
     @Bean
     public RedisConnectionFactory cacheRedisConnectionFactory2(){
         RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration("127.0.0.1", 6379);
         configuration.setDatabase(2);
         //        configuration.setUsername("username");
         //        configuration.setPassword("password");

         final SocketOptions socketOptions = SocketOptions.builder().connectTimeout(Duration.ofSeconds(10)).build();
         final ClientOptions clientOptions = ClientOptions.builder().socketOptions(socketOptions).build();

         LettuceClientConfiguration lettuceClientConfiguration = LettuceClientConfiguration.builder()
                 .clientOptions(clientOptions)
                 .commandTimeout(Duration.ofSeconds(10))
                 .shutdownTimeout(Duration.ZERO)
                 .build();

         return new LettuceConnectionFactory(configuration, lettuceClientConfiguration);

     }

    @Bean(name = "weatherCacheRedisTemplate")
    public RedisTemplate<WeatherCacheKey, WeatherCacheValue> weatherCacheRedisTemplate(
            RedisConnectionFactory connectionFactory){

        RedisTemplate<WeatherCacheKey, WeatherCacheValue> template = new RedisTemplate<>();

        template.setConnectionFactory(connectionFactory);

        template.setKeySerializer(new WeatherCacheKeySerializer());

        template.setValueSerializer(new WeatherCacheValueSerializer());

        return template;

    }


     /*
     4.  알림서비스
       */

    @Bean
    public RedisConnectionFactory cacheRedisConnectionFactory3(){
        RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration("127.0.0.1", 6379);
        configuration.setDatabase(3);
        //        configuration.setUsername("username");
        //        configuration.setPassword("password");

        final SocketOptions socketOptions = SocketOptions.builder().connectTimeout(Duration.ofSeconds(10)).build();
        final ClientOptions clientOptions = ClientOptions.builder().socketOptions(socketOptions).build();

        LettuceClientConfiguration lettuceClientConfiguration = LettuceClientConfiguration.builder()
                .clientOptions(clientOptions)
                .commandTimeout(Duration.ofSeconds(10))
                .shutdownTimeout(Duration.ZERO)
                .build();

        return new LettuceConnectionFactory(configuration, lettuceClientConfiguration);

    }

    /*
    단순 조회용
     */
    @Bean(name = "stringRedisTemplate")
    public StringRedisTemplate stringRedisTemplate(RedisConnectionFactory connectionFactory) {
        StringRedisTemplate template = new StringRedisTemplate();
        template.setConnectionFactory(connectionFactory);
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new StringRedisSerializer());
        template.afterPropertiesSet();
        log.info("StringRedisTemplate 생성 완료");
        return template;
    }


}
