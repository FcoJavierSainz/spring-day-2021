package org.jconf.mx.springday.configuration;

import org.springframework.data.redis.core.script.RedisScript;

public class TransactionScript {

  String sha;
  String transactionName;
  RedisScript<String> redisScript;

  public TransactionScript(String sha, String transactionName, RedisScript<String> redisScript) {
    this.sha = sha;
    this.transactionName = transactionName;
    this.redisScript = redisScript;
  }

  public String getSha() {
    return sha;
  }

  public String getTransactionName() {
    return transactionName;
  }

  public RedisScript<String> getRedisScript() {
    return redisScript;
  }
}
