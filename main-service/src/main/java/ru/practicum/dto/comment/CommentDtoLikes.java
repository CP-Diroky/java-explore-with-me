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
public class CommentDtoLikes extends CommentDto {
    private Long likes;

    public CommentDtoLikes(Long id, Long commentator, Long event, String text, LocalDateTime created, Long likes) {
        super(id, commentator, event, text, created);
        this.likes = likes;
    }

}
