package ru.practicum.controllers.Admin;

import jakarta.validation.constraints.Positive;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.comment.CommentDto;
import ru.practicum.mappers.CommentMapper;
import ru.practicum.models.State;
import ru.practicum.services.CommentService;

@RestController
@RequestMapping("/admin/comments")
@Validated
public class AdminCommentController {

    private final CommentService commentService;

    public AdminCommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PatchMapping("/{comId}")
    public CommentDto updateState(@PathVariable @Positive Long comId, @RequestParam(required = false) State state) {
        return CommentMapper.toCommentDto(commentService.updateState(comId, state));
    }

    @DeleteMapping("/{comId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteComment(@PathVariable @Positive Long comId) {
        commentService.deleteComment(comId);
    }

}
