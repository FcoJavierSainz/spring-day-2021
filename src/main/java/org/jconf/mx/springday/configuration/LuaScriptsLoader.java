package org.jconf.mx.springday.configuration;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.data.redis.connection.ReactiveScriptingCommands;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.scripting.ScriptSource;
import org.springframework.scripting.support.ResourceScriptSource;
import reactor.core.publisher.Flux;

public class LuaScriptsLoader {

  private ReactiveScriptingCommands commands;
  private final Logger logger = LoggerFactory.getLogger(this.getClass());
  private final Resource[] resources;

  public LuaScriptsLoader(
      ReactiveScriptingCommands commands, Resource[] resources) {
    this.commands = commands;
    this.resources = resources;
  }

  public Map<String, TransactionScript> load() {
    Map<String, TransactionScript> scripts = Arrays.stream(resources).
        map(this::generateScript)
        .collect(Collectors.toMap(TransactionScript::getTransactionName, Function.identity()));

    Flux.fromIterable(scripts.values())
        .flatMap(script -> commands
            .scriptExists(script.getSha())
            .doOnNext(exist -> logger
                .info("Script {}  with sha1 {}, Does exist? {}", script.getTransactionName(),
                    script.getSha(), exist))
            .filter(exist -> !exist)
            .flatMap(exist -> commands
                .scriptLoad(ByteBuffer.wrap(script.getRedisScript().getScriptAsString().getBytes()))
                .doOnNext(sha -> logger
                    .info("Script {} loaded with sha1 {}", script.getTransactionName(), sha))
                .retry(3)
            )

        )
        .onErrorContinue((throwable, script) -> logger
            .error("Error loading Script " + script, throwable))
        .subscribe();

    return scripts;
  }

  private TransactionScript generateScript(Resource resource) {

    ScriptSource scriptSource = new ResourceScriptSource(resource);
    try {
      RedisScript<String> script = RedisScript.of(scriptSource.getScriptAsString());
      return new TransactionScript(script.getSha1(),
          resource.getFilename().replaceFirst("[.][^.]+$", ""), script);
    } catch (IOException e) {
      throw new IllegalArgumentException(e);
    }
  }

}
