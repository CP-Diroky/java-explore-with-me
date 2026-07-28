package ru.practicum.services;


import ru.practicum.dto.comment.CommentDto;
import ru.practicum.dto.comment.UpdateCommentDto;
import ru.practicum.models.Comment;
import ru.practicum.models.CommentReaction;
import ru.practicum.models.ReactionType;
import ru.practicum.models.State;

import java.util.List;

public interface CommentService {

    Comment addComment(Long userId, Long eventId, Comment comment);

    Comment updateComment(Long userId, Long comId, UpdateCommentDto commentDto);

    void deleteComment(Long userId, Long comId);

    void deleteComment(Long comId);

    List<? extends CommentDto> getComments(Long eventId, Comment.Sort sort, int size, int from);

    Comment updateState(Long comId, State state);

    CommentReaction reactToComment(Long userId, Long comId, ReactionType reactionType);
}
