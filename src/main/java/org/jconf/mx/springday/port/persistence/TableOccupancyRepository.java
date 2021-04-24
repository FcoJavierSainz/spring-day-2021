package org.jconf.mx.springday.port.persistence;

import java.util.List;
import java.util.Map;
import reactor.core.publisher.Mono;

public interface TableOccupancyRepository {

  Mono<Boolean> saveOccupancy(Integer tableId, Integer occupancy);

  Mono<Map<Integer, Integer>> getMultipleOccupancies(List<Integer> keys);

  Mono<Integer> getTableOccupancy(Integer tableId);
}
