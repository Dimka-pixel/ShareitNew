package com.example.Shareit.Item;

import com.example.Shareit.User.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class CommentMapper {

    private final UserRepository userRepository;

    public CommentDTO mapToCommentDTO(Comment comment) {
        CommentDTO commentDTO = CommentDTO.builder()
                .id(comment.getId())
                .authorName(comment.getUser().getName())
                .text(comment.getText())
                .created(comment.getCreated())
                .build();
        return commentDTO;
    }


}
