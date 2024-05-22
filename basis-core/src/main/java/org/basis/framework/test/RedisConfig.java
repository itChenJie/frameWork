//package org.basis.framework.test;
//
//import com.baomidou.mybatisplus.plugins.PaginationInterceptor;
//import com.fasterxml.jackson.annotation.JsonAutoDetect;
//import com.fasterxml.jackson.annotation.PropertyAccessor;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.xiaopeng.ins.erp.common.cache.redis.RedisComponent;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.data.redis.connection.RedisConnectionFactory;
//import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
//import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.data.redis.core.StringRedisTemplate;
//import org.springframework.data.redis.serializer.*;
//import redis.clients.jedis.JedisPoolConfig;
//
//@Configuration
//public class RedisConfig {
//
//    private static final String hostName="r-bp1cvsz9n3peubt7krpd.redis.rds.aliyuncs.com";
//    private static final String password="PVygR8peP6Mc1FPj";
//    private static final int port=6379;
//
//    @Bean
//    public RedisStandaloneConfiguration redisStandaloneConfiguration(){
//        RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration(hostName, port);
//        configuration.setPassword(password);
//        configuration.setDatabase(120);
//        return configuration;
//    }
//
//    @Bean
//    public StringRedisTemplate stringRedisTemplate() {
//        RedisStandaloneConfiguration redisConfig = new RedisStandaloneConfiguration();
//        redisConfig.setHostName(hostName);
//        redisConfig.setPassword(password);
//        redisConfig.setPort(port);
//        redisConfig.setDatabase(120);
//        JedisConnectionFactory jedisConnectionFactory = new JedisConnectionFactory(redisConfig);
//        //让配置生效 否则会报错
//        //java.lang.IllegalStateException: JedisConnectionFactory was not initialized through afterPropertiesSet()
//        jedisConnectionFactory.afterPropertiesSet();
//        StringRedisTemplate redisTemplate = new StringRedisTemplate(jedisConnectionFactory);
//        return redisTemplate;
//    }
//
//    @Bean
//    public RedisTemplate<?,?> redisTemplate() {
//        RedisStandaloneConfiguration redisConfig = new RedisStandaloneConfiguration();
//        redisConfig.setHostName(hostName);
//        redisConfig.setPassword(password);
//        redisConfig.setDatabase(120);
//        redisConfig.setPort(port);
//        JedisConnectionFactory jedisConnectionFactory = new JedisConnectionFactory(redisConfig);
//        //让配置生效 否则会报错
//        //java.lang.IllegalStateException: JedisConnectionFactory was not initialized through afterPropertiesSet()
//        jedisConnectionFactory.afterPropertiesSet();
//
//        RedisTemplate<String,Object> redisTemplate = new RedisTemplate<>();
//        redisTemplate.setConnectionFactory(jedisConnectionFactory);
//        //设置序列化Value的实例化对象
//        redisTemplate.afterPropertiesSet();
//        return redisTemplate;
//    }
//}
