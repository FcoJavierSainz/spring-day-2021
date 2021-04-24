package org.jconf.mx.springday.configuration;

import java.util.Arrays;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InjectionPoint;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.connection.ReactiveScriptingCommands;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializationContext.RedisSerializationContextBuilder;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(AppProperties.class)
public class RedisConfiguration {

  private Logger logger = LoggerFactory.getLogger(RedisConfiguration.class);

  public static <T> RedisSerializationContextBuilder<String, T> serializer(Class<T> clazz) {
    RedisSerializationContextBuilder<String, T> builder =
        RedisSerializationContext.newSerializationContext();
    builder.key(new StringRedisSerializer());
    builder.hashKey(new StringRedisSerializer());
    builder.value(new Jackson2JsonRedisSerializer<>(clazz));
    builder.hashValue(new Jackson2JsonRedisSerializer<>(clazz));
    return builder;
  }

  @Bean
  @Scope("prototype")
  Logger getLogger(InjectionPoint injectPoint) {
    return LoggerFactory.getLogger(injectPoint.getMember().getDeclaringClass());
  }

  @Bean
  ReactiveScriptingCommands reactiveScriptingCommands(ReactiveRedisConnectionFactory factory) {
    return factory.getReactiveConnection().scriptingCommands();
  }

  @Bean
  Map<String, TransactionScript> loadScripts(
      ReactiveScriptingCommands commands, AppProperties properties, ResourceLoader resourceLoader) {
    Resource[] resources = {
      new ClassPathResource("/tableReservation.lua"),
      new ClassPathResource("/cancelReservation.lua")
    };
    if (logger.isInfoEnabled()) {
      logger.info("Transaction files loaded: {}", Arrays.toString(resources));
    }
    return new LuaScriptsLoader(commands, resources).load();
  }
}
