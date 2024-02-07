package ru.practicum.shareit.item.dto;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;

@UtilityClass
public class CommentMapper {

    public static Comment toCommentFromRequest(CommentRequest comment, Item item, User user) {

        return Comment.builder()
                .item(item)
                .author(user)
                .text(comment.getText())
                .created(LocalDateTime.now())
                .build();
    }

    public static CommentResponse toCommentResponseFromComment(Comment comment) {

        return CommentResponse.builder()
                .id(comment.getId())
                .text(comment.getText())
                .authorName(comment.getAuthor().getName())
                .created(comment.getCreated())
                .build();
    }
}
