package com.backend.config;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
@EnableCaching
public class RedisConfig {

    /*
    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        // 配置连接工厂
        template.setConnectionFactory(factory);
        //使用Jackson2JsonRedisSerializer来序列化和反序列化redis的value值（默认使用JDK的序列化方式）
        Jackson2JsonRedisSerializer jacksonSeial = new Jackson2JsonRedisSerializer(Object.class);
        ObjectMapper om = new ObjectMapper();
        // 指定要序列化的域，field,get和set,以及修饰符范围，ANY是都有包括private和public
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        // 指定序列化输入的类型，类必须是非final修饰的，final修饰的类，比如String,Integer等会跑出异常
        om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        jacksonSeial.setObjectMapper(om);

        //使用StringRedisSerializer来序列化和反序列化redis的key和value
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(jacksonSeial);

        // 设置hash key和value序列化模式
        template.setHashKeySerializer(new StringRedisSerializer());
        template.setHashValueSerializer(jacksonSeial);

        template.afterPropertiesSet();
        return template;
    }
    */

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        // 配置连接工厂
        template.setConnectionFactory(factory);
        //使用Jackson2JsonRedisSerializer来序列化和反序列化redis的value值（默认使用JDK的序列化方式）
        GenericJackson2JsonRedisSerializer jacksonSeial = new GenericJackson2JsonRedisSerializer();

        //使用StringRedisSerializer来序列化和反序列化redis的key和value
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(jacksonSeial);

        // 设置hash key和value序列化模式
        template.setHashKeySerializer(new StringRedisSerializer());
        template.setHashValueSerializer(jacksonSeial);

        template.afterPropertiesSet();
        return template;
    }

    @Bean
    public RedisMessageListenerContainer redisMessageListenerContainer(RedisConnectionFactory factory) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(factory);
        return container;
    }

    /*
    // redis pub/sub配置，请参考<RedisPubSubConfig>配置
    @Bean
    public RedisMessageListenerContainer redisMessageListenerContainer(RedisConnectionFactory factory, MessageListenerAdapter messageListenerAdapter) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(factory);

        // 消息发布主题
        Topic topic = new PatternTopic("test_topic");
        container.addMessageListener(messageListenerAdapter, topic);

        return container;
    }

    @Bean
    public CountDownLatch countDownLatch(){
        return new CountDownLatch(1);
    }

    @Bean
    MessageListenerAdapter listenerAdapter(ReceiveMessage redisReceiver) {
        // "receiverVoucherMessage" 即为自定义ReceiveMessage中自定义的消息接受处理方法
        MessageListenerAdapter adapter = new MessageListenerAdapter(redisReceiver, "receiverVoucherMessage");
        return adapter;
    }
*/

}