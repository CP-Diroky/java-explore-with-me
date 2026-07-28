package ru.practicum.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.models.Comment;
import ru.practicum.models.State;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    @Query(value = """
            SELECT *
            FROM comments
            WHERE event_id = ?1 AND state = 'PUBLISHED'
            ORDER BY created desc
            LIMIT ?2 OFFSET ?3
            """, nativeQuery = true)
    List<Comment> getLatestComments(Long eventId, int size, int from);

    @Query(value = """
            SELECT *
            FROM comments
            WHERE event_id = ?1 AND state = 'PUBLISHED'
            ORDER BY created asc
            LIMIT ?2 OFFSET ?3
            """, nativeQuery = true)
    List<Comment> getEarliestComments(Long eventId, int size, int from);



    List<Comment> findByEventIdAndState(Long eventId, State state);

}
