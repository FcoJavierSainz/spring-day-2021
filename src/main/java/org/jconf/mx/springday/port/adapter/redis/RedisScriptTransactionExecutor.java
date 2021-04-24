package org.jconf.mx.springday.port.adapter.redis;

import java.util.List;
import java.util.Map;
import org.jconf.mx.springday.configuration.TransactionScript;
import org.jconf.mx.springday.model.TransactionExecutor;
import org.springframework.data.redis.core.ReactiveRedisOperations;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import reactor.core.publisher.Flux;

@Component
public class RedisScriptTransactionExecutor implements TransactionExecutor {

  private Map<String, TransactionScript> scripts;
  private ReactiveRedisOperations<String, String> redisOperations;

  public RedisScriptTransactionExecutor(
      Map<String, TransactionScript> scripts,
      ReactiveRedisOperations<String, String> redisOperations) {
    Assert.notEmpty(scripts, "Scripts list should not be empty");
    Assert.notNull(redisOperations, "Redis Template should not be null");
    this.scripts = scripts;
    this.redisOperations = redisOperations;
  }

  @Override
  public Flux<String> execute(String transactionName, List<String> keys, List<Object> args) {
    RedisScript<String> script = scripts.get(transactionName).getRedisScript();
    return redisOperations.execute(script, keys, args);
  }
}
