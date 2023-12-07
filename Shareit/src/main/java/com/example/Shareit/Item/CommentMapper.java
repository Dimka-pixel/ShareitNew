package com.example.Shareit.Item;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface CommentMapper {
    @Mapping(target = "authorName", expression = "java(comment.getUser().getName())")
    CommentDTO toCommentDTO(Comment comment);
}
