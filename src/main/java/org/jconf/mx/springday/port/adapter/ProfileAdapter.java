package org.jconf.mx.springday.port.adapter;

import org.jconf.mx.springday.domain.model.Profile;
import reactor.core.publisher.Mono;

public interface ProfileAdapter {

  Mono<Profile> getProfile(String id);
}
