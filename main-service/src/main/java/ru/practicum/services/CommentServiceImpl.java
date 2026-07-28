package ru.practicum.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.dto.comment.CommentDto;
import ru.practicum.dto.comment.DislikeCountDto;
import ru.practicum.dto.comment.LikeCountDto;
import ru.practicum.dto.comment.UpdateCommentDto;
import ru.practicum.exceptions.ConflictException;
import ru.practicum.exceptions.NotFoundException;
import ru.practicum.mappers.CommentMapper;
import ru.practicum.models.*;
import ru.practicum.repositories.CommentReactionRepository;
import ru.practicum.repositories.CommentRepository;
import ru.practicum.repositories.EventRepository;
import ru.practicum.repositories.UserRepository;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final CommentReactionRepository commentReactionRepository;

    public CommentServiceImpl(CommentRepository commentRepository, UserRepository userRepository,
                              EventRepository eventRepository, CommentReactionRepository commentReactionRepository) {
        this.commentRepository = commentRepository;
        this.userRepository = userRepository;
        this.eventRepository = eventRepository;
        this.commentReactionRepository = commentReactionRepository;
    }

    @Override
    @Transactional
    public Comment addComment(Long userId, Long eventId, Comment comment) {
        User commentator = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User with id=" + userId + " was not found."));
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Event with id=" + eventId + " was not found."));
        if (event.getState() != State.PUBLISHED)
            throw new ConflictException("Only published events can be commented");
        comment.setCommentator(commentator);
        comment.setEvent(event);
        comment.setState(State.PENDING);


        return commentRepository.save(comment);
    }


    @Override
    @Transactional
    public Comment updateComment(Long userId, Long comId, UpdateCommentDto commentDto) {
        userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User with id=" + userId + " was not found."));
        Comment updatedComment = commentRepository.findById(comId)
                .orElseThrow(() -> new NotFoundException("Comment with id=" + comId + " was not found."));

        if (!updatedComment.getCommentator().getId().equals(userId))
            throw new ConflictException("Only commentator can update comment");

        if (commentDto.getText() != null && !commentDto.getText().isBlank()) {
            updatedComment.setText(commentDto.getText());
        }

        updatedComment.setState(State.PENDING);

        return commentRepository.save(updatedComment);

    }

    @Override
    @Transactional
    public void deleteComment(Long userId, Long comId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User with id=" + userId + " was not found."));
        commentRepository.findById(comId)
                .orElseThrow(() -> new NotFoundException("Comment with id=" + comId + " was not found."));

        commentRepository.deleteById(comId);
    }


    @Override
    @Transactional
    public void deleteComment(Long comId) {
        commentRepository.findById(comId)
                .orElseThrow(() -> new NotFoundException("Comment with id=" + comId + " was not found."));

        commentRepository.deleteById(comId);
    }


    @Override
    @Transactional
    public Comment updateState(Long comId, State state) {
        Comment updatedComment = commentRepository.findById(comId)
                .orElseThrow(() -> new NotFoundException("Comment with id=" + comId + " was not found."));

        if (state != null) updatedComment.setState(state);

        return commentRepository.save(updatedComment);

    }

    @Override
    public List<? extends CommentDto> getComments(Long eventId, Comment.Sort sort, int size, int from) {
        eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Event with id=" + eventId + " was not found."));
        List<Comment> comments;

        if (sort != null) {
            switch (sort) {
                case LATEST -> comments = commentRepository.getLatestComments(eventId, size, from);
                case EARLIEST -> comments = commentRepository.getEarliestComments(eventId, size, from);
                case MOST_LIKED -> {
                    Map<Long, Long> likesCount = commentReactionRepository.getLikes(eventId)
                            .stream()
                            .collect(Collectors.toMap(
                                    LikeCountDto::getCommentId,
                                    LikeCountDto::getLikes));
                    comments = commentRepository.findByEventIdAndState(eventId, State.PUBLISHED)
                            .stream()
                            .peek(comment -> comment.setLikes(likesCount.getOrDefault(comment.getId(), 0L)))
                            .sorted((comment1, comment2) ->
                                    Long.compare(comment2.getLikes(), comment1.getLikes()))
                            .skip(from)
                            .limit(size)
                            .toList();
                    return CommentMapper.commentDtoLikesList(comments);

                }
                case MOST_DISLIKED -> {
                    Map<Long, Long> dislikesCount = commentReactionRepository.getDislikes(eventId)
                            .stream()
                            .collect(Collectors.toMap(
                                    DislikeCountDto::getCommentId,
                                    DislikeCountDto::getDislikes));
                    comments = commentRepository.findByEventIdAndState(eventId, State.PUBLISHED)
                            .stream()
                            .peek(comment -> comment.setDislikes(dislikesCount.getOrDefault(comment.getId(), 0L)))
                            .sorted((comment1, comment2) ->
                                    Long.compare(comment2.getDislikes(), comment1.getDislikes()))
                            .skip(from)
                            .limit(size)
                            .toList();
                    return CommentMapper.commentDtoDislikesList(comments);
                }
                default -> comments = commentRepository.getLatestComments(eventId, size, from);
            }
        } else {
            comments = commentRepository.getLatestComments(eventId, size, from);
        }

        return CommentMapper.commentDtoList(comments);

    }

    @Override
    @Transactional
    public CommentReaction reactToComment(Long userId, Long comId, ReactionType reactionType) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User with id=" + userId + " was not found."));
        Comment comment = commentRepository.findById(comId)
                .orElseThrow(() -> new NotFoundException("Comment with id=" + comId + " was not found."));
        if (comment.getState() != State.PUBLISHED)
            throw new ConflictException("Only published comments can be reacted to");
        CommentReaction commentReaction = new CommentReaction();
        commentReaction.setComment(comment);
        commentReaction.setUser(user);
        if (reactionType == null) {
            commentReaction.setReaction(ReactionType.NONE);
        } else {
            commentReaction.setReaction(reactionType);
        }

        return commentReactionRepository.save(commentReaction);
    }


}


