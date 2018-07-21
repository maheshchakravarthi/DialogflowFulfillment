package com.dialogflowfulfillment.config;

import com.dialogflowfulfillment.util.Constants;
import com.dialogflowfulfillment.model.Experience;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import redis.clients.jedis.JedisPoolConfig;

import java.util.List;

@Configuration
@ComponentScan("com.homedepot.dialogflowfulfillment.config")
public class RedisConfig {

    @Autowired
    Environment environment;

    static final Logger logger = LoggerFactory.getLogger(RedisConfig.class);
    @Bean
    public JedisConnectionFactory jedisConnectionFactory(){


        logger.info("In JedisConnectionFactory method");

        JedisConnectionFactory connectionFactory = new JedisConnectionFactory();
        logger.info("Current lcp set to : "+environment.getProperty("lcp"));

        String lcp = null;
        if(!StringUtils.isBlank(environment.getProperty("lcp"))){
            lcp = environment.getProperty("lcp");
        }


        if (lcp.equalsIgnoreCase("cloud")) {
            logger.info("Redis config being fetched from VCapServices");
            Config redisConfig = getRedisServiceConfig().getConfig("credentials");
            connectionFactory.setHostName(redisConfig.getString("hostname"));
            connectionFactory.setPassword(redisConfig.getString("password"));
        }
        else if (lcp.equalsIgnoreCase("local")) {
            logger.info("Redis config being fetched from local");
            connectionFactory.setHostName(Constants.localRedisHostName);
        }

        connectionFactory.setPort(Constants.redisPort);
        connectionFactory.setTimeout(Constants.redisConnectionTimeout);
        connectionFactory.setUsePool(Constants.redisIsUsePool);

        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxIdle(Constants.redisPoolMaxIdle);
        jedisPoolConfig.setMinIdle(Constants.redisPoolMinIdle);

        connectionFactory.setPoolConfig(jedisPoolConfig);

        return connectionFactory;
    }

    @Bean(name="redisTemplate")
    public RedisTemplate<String, Experience> redisTemplate(){
        RedisTemplate<String, Experience> template = new RedisTemplate<String, Experience>();
        template.setKeySerializer(new StringRedisSerializer());
        template.setConnectionFactory(jedisConnectionFactory());
        return template;
    }

    private Config getVcapService(String serviceName) {
        Config config = ConfigFactory.systemEnvironment();
        Config vcapServices = ConfigFactory.parseString(config.getString("VCAP_SERVICES"));
        List<? extends Config> services = vcapServices.getConfigList("rediscloud");

        Config service = null;

        for (Config candidate : services) {
            System.out.println("service - "+candidate.getString("name"));
            if (candidate.getString("name").equals(serviceName)) {
                service = candidate;
                break;
            }
        }
        if (service == null) {
            throw new IllegalStateException(String.format("Unable to locate service '%s' in VCAP_SERVICES", serviceName));
        }
        return service;
    }

    Config getRedisServiceConfig(){
        return getVcapService("myredis-forpoc");
    }
}
