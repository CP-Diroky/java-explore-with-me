package ru.practicum.controllers.Private;


import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.comment.CommentDto;
import ru.practicum.dto.comment.CommentReactionDto;
import ru.practicum.dto.comment.NewCommentDto;
import ru.practicum.dto.comment.UpdateCommentDto;
import ru.practicum.mappers.CommentMapper;
import ru.practicum.models.ReactionType;
import ru.practicum.services.CommentService;

@RestController
@RequestMapping("/users/{userId}/comments")
@Validated
public class PrivateCommentController { // Комментировать можно только опубликованные события

    private final CommentService commentService;

    public PrivateCommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping("/{eventId}")
    public CommentDto addComment(@PathVariable @Positive Long userId, @PathVariable @Positive Long eventId,
                                 @RequestBody @Valid NewCommentDto commentDto) {
        return CommentMapper.toCommentDto(commentService.addComment(userId, eventId, CommentMapper.toComment(commentDto)));
    }

    @PatchMapping("/{comId}")
    public CommentDto updateComment(@PathVariable @Positive Long userId, @PathVariable @Positive Long comId,
                                    @RequestBody @Valid UpdateCommentDto commentDto) {
        return CommentMapper.toCommentDto(commentService.updateComment(userId, comId, commentDto));
    }

    @DeleteMapping("/{comId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteComment(@PathVariable @Positive Long userId, @PathVariable @Positive Long comId) {
        commentService.deleteComment(userId, comId);
    }

    @PatchMapping("/{comId}/reaction")
    public CommentReactionDto reactToComment(@PathVariable @Positive Long userId, @PathVariable @Positive Long comId,
                                             @RequestParam ReactionType reactionType) {
        return CommentMapper.toCommentReactionDto(commentService.reactToComment(userId, comId, reactionType));
    }







}
