package com.example.Shareit.Item;
//Pull requests
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommentDTO {
    int id;
    @NotBlank
    String text;
    String authorName;
    LocalDateTime created;

}
