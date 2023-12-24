package com.example.Shareit.Item;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface CommentMapper {
    @Mapping(source = "comment.user.name", target = "authorName")
    CommentDto toCommentDTO(Comment comment);
}
