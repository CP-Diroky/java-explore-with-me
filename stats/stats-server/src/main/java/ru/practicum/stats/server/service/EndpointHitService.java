package ru.practicum.stats.server.service;

import ru.practicum.stats.dto.ViewStatsDto;
import ru.practicum.stats.server.model.EndpointHit;

import java.time.LocalDateTime;
import java.util.List;

public interface EndpointHitService {

    void addHit(EndpointHit endpointHit);

    List<ViewStatsDto> viewStats(LocalDateTime start, LocalDateTime end, List<String> uris, boolean unique);

}
