package ru.practicum.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.models.Event;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface EventRepository extends JpaRepository<Event, Long> {

    @Query(value = """
            SELECT *
            FROM events
            WHERE initiator_id = ?1
            ORDER BY id
            LIMIT ?2 OFFSET ?3
            """, nativeQuery = true)
    List<Event> getUserEvents(Long userId, int size, int from);

    Optional<Event> findByInitiatorIdAndId(Long userId, Long eventId);

    List<Event> findByCategoryId(Long catId);


    @Query(value = """
            SELECT *
            FROM events
            WHERE (?2 = true OR initiator_id IN (?1))
              AND (?4 = true OR state IN (?3))
              AND (?6 = true OR category_id IN (?5))
              AND (CAST(?7 AS timestamp) IS NULL OR event_date >= ?7)
              AND (CAST(?8 AS timestamp) IS NULL OR event_date <= ?8)
            ORDER BY id
            LIMIT ?9 OFFSET ?10
            """, nativeQuery = true)
    List<Event> getFilteredEvents(List<Long> users, boolean usersEmpty, List<String> states, boolean statesEmpty,
                                  List<Long> categories, boolean categoriesEmpty, LocalDateTime rangeStart,
                                  LocalDateTime rangeEnd, int size, int from);


    @Query(value = """
            SELECT e.*
            FROM events e
            WHERE e.state = 'PUBLISHED'
              AND (
                  CAST(?1 AS VARCHAR) IS NULL
                  OR LOWER(e.annotation) LIKE LOWER(CONCAT('%', ?1, '%'))
                  OR LOWER(e.description) LIKE LOWER(CONCAT('%', ?1, '%'))
              )
              AND (
                  ?3 = TRUE
                  OR e.category_id IN (?2)
              )
              AND (
                  CAST(?4 AS BOOLEAN) IS NULL
                  OR e.paid = ?4
              )
              AND (
                  CAST(?5 AS TIMESTAMP) IS NULL
                  OR e.event_date >= ?5
              )
              AND (
                  CAST(?6 AS TIMESTAMP) IS NULL
                  OR e.event_date <= ?6
              )
              AND (
                  ?7 = FALSE
                  OR e.participant_limit = 0
                  OR (
                      SELECT COUNT(*)
                      FROM requests r
                      WHERE r.event_id = e.id
                        AND r.status = 'CONFIRMED'
                  ) < e.participant_limit
              )
            """, nativeQuery = true)
    List<Event> findPublicEvents(String text, List<Long> categories, boolean categoriesAbsent, Boolean paid,
                                 LocalDateTime rangeStart, LocalDateTime rangeEnd, boolean onlyAvailable);
}
