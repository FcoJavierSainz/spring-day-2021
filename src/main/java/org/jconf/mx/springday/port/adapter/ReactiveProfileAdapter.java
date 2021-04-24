package org.jconf.mx.springday.port.adapter;

import com.github.javafaker.Faker;
import org.jconf.mx.springday.domain.model.Profile;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class ReactiveProfileAdapter implements ProfileAdapter {

  @Override
  public Mono<Profile> getProfile(String id) {
    Faker faker = new Faker();
    return Mono.just(new Profile(id, faker.dragonBall().character()));
  }
}
