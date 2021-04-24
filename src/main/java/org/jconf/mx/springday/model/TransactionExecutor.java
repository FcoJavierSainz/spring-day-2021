package org.jconf.mx.springday.model;

import java.util.List;
import reactor.core.publisher.Flux;

@FunctionalInterface
public interface TransactionExecutor {

  Flux<String> execute(String name, List<String> keys, List<Object> args);
}
