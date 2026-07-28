package ru.practicum.mappers;

import lombok.experimental.UtilityClass;
import ru.practicum.dto.comment.*;
import ru.practicum.models.Comment;
import ru.practicum.models.CommentReaction;

import java.util.Collection;
import java.util.List;

@UtilityClass
public class CommentMapper {

    public static Comment toComment(NewCommentDto commentDto) {
        return new Comment(commentDto.getText());
    }

    public static CommentDto toCommentDto(Comment comment) {
        return new CommentDto(
                comment.getId(),
                comment.getCommentator().getId(),
                comment.getEvent().getId(),
                comment.getText(),
                comment.getCreated()
        );
    }

    public static CommentDtoLikes commentDtoLikes(Comment comment) {
        return new CommentDtoLikes(
                comment.getId(),
                comment.getCommentator().getId(),
                comment.getEvent().getId(),
                comment.getText(),
                comment.getCreated(),
                comment.getLikes()
        );
    }

    public static CommentDtoDislikes commentDtoDislikes(Comment comment) {
        return new CommentDtoDislikes(
                comment.getId(),
                comment.getCommentator().getId(),
                comment.getEvent().getId(),
                comment.getText(),
                comment.getCreated(),
                comment.getDislikes()
        );
    }

    public static CommentReactionDto toCommentReactionDto(CommentReaction commentReaction) {
        return new CommentReactionDto(
                commentReaction.getId(),
                commentReaction.getComment().getId(),
                commentReaction.getUser().getId(),
                commentReaction.getReaction()
        );
    }

    public static List<CommentDto> commentDtoList(List<Comment> comments) {
        return comments.stream().map(CommentMapper::toCommentDto).toList();
    }

    public static List<CommentDtoLikes> commentDtoLikesList(Collection<Comment> comments) {
        return comments.stream().map(CommentMapper::commentDtoLikes).toList();
    }

    public static List<CommentDtoDislikes> commentDtoDislikesList(Collection<Comment> comments) {
        return comments.stream().map(CommentMapper::commentDtoDislikes).toList();
    }
}


