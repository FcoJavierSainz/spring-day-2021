package org.jconf.mx.springday.application;

import org.jconf.mx.springday.domain.model.Profile;
import org.jconf.mx.springday.port.adapter.ProfileAdapter;
import org.jconf.mx.springday.port.persistence.ProfileRepository;
import org.jconf.mx.springday.port.persistence.ReservationRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class ProfileApplicationService implements ProfileApplication {

  private final ProfileAdapter profileAdapter;
  private final ProfileRepository profileRepository;
  private final ReservationRepository reservationRepository;

  public ProfileApplicationService(
      ProfileAdapter profileAdapter,
      ReservationRepository reservationRepository,
      ProfileRepository profileRepository) {
    this.profileAdapter = profileAdapter;
    this.reservationRepository = reservationRepository;
    this.profileRepository = profileRepository;
  }

  @Override
  public Mono<Profile> getProfileById(String id) {
    return profileRepository
        .getProfile(id)
        .switchIfEmpty(
            profileAdapter
                .getProfile(id)
                .flatMap(
                    profile ->
                        profileRepository
                            .saveProfile(id, profile)
                            .log("Saving profile: " + id)
                            .map(saved -> profile)))
        .flatMap(
            profile ->
                reservationRepository
                    .getReservationByUserId(profile.getEmail())
                    .map(profile::addReservation)
                    .defaultIfEmpty(profile));
  }
}
