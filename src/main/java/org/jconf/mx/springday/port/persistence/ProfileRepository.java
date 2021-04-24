package org.jconf.mx.springday.port.persistence;

import org.jconf.mx.springday.domain.model.Profile;
import reactor.core.publisher.Mono;

public interface ProfileRepository {

  Mono<Boolean> saveProfile(String userId, Profile profile);

  Mono<Profile> getProfile(String userId);
}
