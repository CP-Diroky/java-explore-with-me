package ru.practicum.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.dto.comment.DislikeCountDto;
import ru.practicum.dto.comment.LikeCountDto;
import ru.practicum.models.CommentReaction;

import java.util.List;

public interface CommentReactionRepository extends JpaRepository<CommentReaction, Long> {

    @Query("""
            SELECT new ru.practicum.dto.comment.LikeCountDto(
                        cr.comment.id,
                        COUNT(cr)
            )
            FROM CommentReaction cr
            WHERE cr.reaction = 'LIKE' AND cr.comment.event.id = ?1
            GROUP BY cr.comment.id
            """)
    List<LikeCountDto> getLikes(Long eventId);

    @Query("""
            SELECT new ru.practicum.dto.comment.DislikeCountDto(
                        cr.comment.id,
                        COUNT(cr)
            )
            FROM CommentReaction cr
            WHERE cr.reaction = 'DISLIKE' AND cr.comment.event.id = ?1
            GROUP BY cr.comment.id
            """)
    List<DislikeCountDto> getDislikes(Long eventId);
}

