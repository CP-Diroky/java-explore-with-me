package ru.practicum.dto.comment;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.models.ReactionType;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CommentReactionDto {
    private Long id;
    private Long comment;
    private Long user;
    private ReactionType reaction;
}


