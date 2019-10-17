package com.yujun.database.redis;

import io.lettuce.core.ReadFrom;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.*;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * @author hunter
 * @version 1.0.0
 * @date 10/16/19 9:22 PM
 * @description TODO
 **/
@Slf4j
@Configuration
public class RedisConfig {
    @Value("${redis.master.host}")
    private String redisHost;
    @Value("${redis.master.port}")
    private int port;
    @Value("${redis.password}")
    private String password;

    @Value("${redis.masterName}")
    private String masterName;

    @Value("${redis.sentinels}")
    private String sentinels[];


    /**
     * 使用Lettuce构造redis连接工厂
     * @author: yujun
     * @date: 10/16/19
     * @description: TODO
     * @param
     * @return: {@link org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory}
     * @exception:
    */
    @Bean(name = "lettuceConnectionFactory")
    public LettuceConnectionFactory lettuceConnectionFactory() {
        return new LettuceConnectionFactory(new RedisStaticMasterReplicaConfiguration(redisHost, port));
    }

    /**
     * 使用Jedis构造redis连接工厂
     * @author: yujun
     * @date: 10/16/19
     * @description: TODO
     * @param
     * @return: {@link org.springframework.data.redis.connection.jedis.JedisConnectionFactory}
     * @exception:
    */
    @Bean(name = "jedisConnectionFactory")
    public JedisConnectionFactory jedisConnectionFactory() {
        RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration(redisHost, port);
        JedisConnectionFactory jedisConnectionFactory = new JedisConnectionFactory(configuration);
        return jedisConnectionFactory;
    }

    /**
     * 使用Lettuce构造ConnectionFactory可指定读写策略，如：写入主节点，从从节点读取
     * @author: yujun
     * @date: 10/16/19
     * @description: TODO
     * @param
     * @return: {@link org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory}
     * @exception:
    */
    @Bean(name = "lettuceConnectionFactoryWithStrategy")
    public LettuceConnectionFactory lettuceConnectionFactoryWithStrategy() {
        LettuceClientConfiguration clientConfiguration = LettuceClientConfiguration.builder().readFrom(ReadFrom.SLAVE_PREFERRED).build();
        RedisStandaloneConfiguration serverConfig = new RedisStandaloneConfiguration(redisHost, port);
        return new LettuceConnectionFactory(serverConfig, clientConfiguration);
    }

    /**
     * 构造支持哨兵功能的redis connection factory,底层使用Jedis
     * @author: yujun
     * @date: 10/16/19
     * @description: TODO
     * @param
     * @return: {@link org.springframework.data.redis.connection.RedisConnectionFactory}
     * @exception:
    */
    @Bean(name = "jedisConnectionFactoryWithSentinel")
    public RedisConnectionFactory jedisConnectionFactoryWithSentinel() {
        RedisSentinelConfiguration sentinelConfiguration = new RedisSentinelConfiguration()
                .master(this.masterName);
        for(String sentinel : sentinels) {
            String[] split = sentinel.split(":");
            sentinelConfiguration.sentinel(split[0], Integer.parseInt(split[1]));
        }
        return new JedisConnectionFactory(sentinelConfiguration);
    }

    /**
     * 构造支持哨兵功能的redis connection factory,底层使用lettuce
     * @author: yujun
     * @date: 10/16/19
     * @description: TODO
     * @param
     * @return: {@link org.springframework.data.redis.connection.RedisConnectionFactory}
     * @exception:
     */
    @Bean(name = "lettuceConnectionFactoryWithSentinel")
    public RedisConnectionFactory lettuceConnectionFactoryWithSentinel() {
        RedisSentinelConfiguration sentinelConfiguration = new RedisSentinelConfiguration()
                .master(this.masterName);
        for(String sentinel : sentinels) {
            String[] split = sentinel.split(":");
            sentinelConfiguration.sentinel(split[0], Integer.parseInt(split[1]));
        }
        return new LettuceConnectionFactory(sentinelConfiguration);
    }

    @Bean(name = "redisTemplate")
    public RedisTemplate redisTemplate() {
        RedisTemplate redisTemplate = new RedisTemplate();
        redisTemplate.setConnectionFactory(this.jedisConnectionFactoryWithSentinel());
        return redisTemplate;
    }
}
