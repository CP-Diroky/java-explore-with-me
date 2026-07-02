package ru.practicum.stats.server.repository;

import ru.practicum.stats.dto.ViewStatsDto;
import ru.practicum.stats.server.model.EndpointHit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;


public interface EndpointHitRepository extends JpaRepository<EndpointHit, Long> {

    @Query("""
            SELECT new ru.practicum.stats.dto.ViewStatsDto(
                e.app,
                e.uri,
                COUNT(e.id)
            )
            FROM EndpointHit e
            WHERE e.timeHit >= ?1
              AND e.timeHit <= ?2
            GROUP BY e.app, e.uri
            ORDER BY COUNT(e.id) DESC
            """)
    List<ViewStatsDto> getStats(LocalDateTime start, LocalDateTime end);

    @Query("""
            SELECT new ru.practicum.stats.dto.ViewStatsDto(
                e.app,
                e.uri,
                COUNT(e.id)
            )
            FROM EndpointHit e
            WHERE e.timeHit >= ?1
              AND e.timeHit <= ?2
              AND e.uri IN ?3
            GROUP BY e.app, e.uri
            ORDER BY COUNT(e.id) DESC
            """)
    List<ViewStatsDto> getStatsUriIn(LocalDateTime start, LocalDateTime end, List<String> uris);

    @Query("""
            SELECT new ru.practicum.stats.dto.ViewStatsDto(
                e.app,
                e.uri,
                COUNT(distinct e.ip)
            )
            FROM EndpointHit e
            WHERE e.timeHit >= ?1
              AND e.timeHit <= ?2
            GROUP BY e.app, e.uri
            ORDER BY COUNT(DISTINCT e.ip) DESC
            """)
    List<ViewStatsDto> getStatsUnique(LocalDateTime start, LocalDateTime end);

    @Query("""
            SELECT new ru.practicum.stats.dto.ViewStatsDto(
                e.app,
                e.uri,
                COUNT(distinct e.ip)
            )
            FROM EndpointHit e
            WHERE e.timeHit >= ?1
              AND e.timeHit <= ?2
              AND e.uri IN ?3
            GROUP BY e.app, e.uri
            ORDER BY COUNT(DISTINCT e.ip) DESC
            """)
    List<ViewStatsDto> getStatsUniqueUriIn(LocalDateTime start, LocalDateTime end, List<String> uris);
}
