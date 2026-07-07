package ru.practicum.stats.server.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.stats.dto.ViewStatsDto;
import ru.practicum.stats.server.model.EndpointHit;
import ru.practicum.stats.server.repository.EndpointHitRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class EndpointHitServiceImpl implements EndpointHitService {

    private final EndpointHitRepository endpointHitRepository;


    public EndpointHitServiceImpl(EndpointHitRepository endpointHitRepository) {
        this.endpointHitRepository = endpointHitRepository;
    }

    @Transactional
    @Override
    public void addHit(EndpointHit endpointHit) {
        endpointHitRepository.save(endpointHit);
    }

    @Override
    public List<ViewStatsDto> viewStats(LocalDateTime start, LocalDateTime end, List<String> uris, boolean unique) {

        if (uris == null) {
            uris = List.of();
        }

        if (uris.isEmpty() && !unique) {
            return endpointHitRepository.getStats(start, end);
        } else if (uris.isEmpty() && unique) {
            return endpointHitRepository.getStatsUnique(start, end);
        } else if (!unique) {
            return endpointHitRepository.getStatsUriIn(start, end, uris);
        } else {
            return endpointHitRepository.getStatsUniqueUriIn(start, end, uris);
        }
    }

}
