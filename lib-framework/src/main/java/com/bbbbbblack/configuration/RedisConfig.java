package com.bbbbbblack.configuration;

import com.bbbbbblack.dao.BookDao;
import com.bbbbbblack.domain.entity.Book;
import com.bbbbbblack.utils.FastJsonRedisSerializer;
import com.bbbbbblack.utils.PushUtil;
import com.getui.push.v2.sdk.api.PushApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.List;

@Configuration
public class RedisConfig {

    @Autowired
    BookDao bookDao;
    @Autowired
    PushApi pushApi;

    @Bean
    @SuppressWarnings({"unchecked", "rawtypes"})
    public RedisTemplate<Object, Object> redisTemplate(RedisConnectionFactory factory) {
        RedisTemplate<Object, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(factory);
        FastJsonRedisSerializer fastJsonRedisSerializer = new FastJsonRedisSerializer<>(Object.class);

        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(fastJsonRedisSerializer);

        template.setHashKeySerializer(new StringRedisSerializer());
        template.setHashValueSerializer(fastJsonRedisSerializer);

        template.afterPropertiesSet();
        return template;
    }

    @Bean
    public CacheManager cacheManager(RedisConnectionFactory factory) {
        RedisCacheConfiguration redisCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig();
        redisCacheConfiguration
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new FastJsonRedisSerializer<>(Object.class)))
                .disableCachingNullValues()
                //缓冲空间名称前缀
//              .prefixCacheNameWith("xxx")
                //全局缓存过期时间
                .entryTtl(Duration.ofMinutes(30L));
        return RedisCacheManager
                .builder(factory)
                .cacheDefaults(redisCacheConfiguration)
                .build();
    }

    //监听容器
    @Bean
    RedisMessageListenerContainer container(RedisConnectionFactory factory,
                                            MessageListenerAdapter returnBookAdapter) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(factory);
        //订阅了一个叫returnBook的通道
        container.addMessageListener(returnBookAdapter, new PatternTopic("returnBook"));
        //这个container 可以添加多个 messageListener
        return container;
    }


    @Bean
    MessageListener returnBookListener(RedisTemplate<Object, Object> redisTemplate) {
        return (message, pattern) -> {
            String messages = new String(message.getBody(), StandardCharsets.UTF_8);
            String substring = messages.substring(1, messages.lastIndexOf('"'));
            List<Object> list = redisTemplate.opsForList().range("order:" + substring,0L,-1L);
            assert list != null;
            for (Object clientId : list) {
                Book book = bookDao.selectById(substring);
                PushUtil.pushMessage(pushApi,
                        "预订提醒",
                        "您订阅的书名为" + book.getTitle() + "的书有人归还", clientId.toString());
                System.out.println("您订阅的书名为" + book.getTitle() + "的书有人归还");
            }
        };
    }

    @Bean
    MessageListenerAdapter returnBookAdapter(MessageListener returnBookListener) {
        return new MessageListenerAdapter(returnBookListener);
    }


}