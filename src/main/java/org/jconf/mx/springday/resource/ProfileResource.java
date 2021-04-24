package org.jconf.mx.springday.resource;

import org.jconf.mx.springday.application.ProfileApplication;
import org.jconf.mx.springday.domain.model.Profile;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("api/v1/profile")
public class ProfileResource {

  private ProfileApplication service;

  public ProfileResource(ProfileApplication service) {
    this.service = service;
  }

  @GetMapping("/{userId}")
  public Mono<Profile> getProfile(@PathVariable("userId") String userId) {
    return service.getProfileById(userId);
  }
}
