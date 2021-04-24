package org.jconf.mx.springday.application;

import org.jconf.mx.springday.domain.model.Profile;
import reactor.core.publisher.Mono;

public interface ProfileApplication {

  Mono<Profile> getProfileById(String id);
}
