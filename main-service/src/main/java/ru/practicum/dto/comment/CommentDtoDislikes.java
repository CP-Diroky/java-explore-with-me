package ru.practicum.dto.comment;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class CommentDtoDislikes extends CommentDto {
    private Long dislikes;

    public CommentDtoDislikes(Long id, Long commentator, Long event, String text, LocalDateTime created, Long dislikes) {
        super(id, commentator, event, text, created);
        this.dislikes = dislikes;
    }
}
