package ru.practicum.controllers.Public;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.comment.CommentDto;
import ru.practicum.models.Comment;
import ru.practicum.services.CommentService;

import java.util.List;

@RestController
@RequestMapping("/comments")
@Validated
public class PublicCommentController {

    private final CommentService commentService;

    public PublicCommentController(CommentService commentService) {
        this.commentService = commentService;
    }


    @GetMapping("/{eventId}")
    public List<? extends CommentDto> getComments(@PathVariable @Positive Long eventId,
                                        @RequestParam(required = false) Comment.Sort sort,
                                        @RequestParam(defaultValue = "10") @PositiveOrZero int size,
                                        @RequestParam(defaultValue = "0") @PositiveOrZero int from) {
        return commentService.getComments(eventId, sort, size, from);
    }


}
