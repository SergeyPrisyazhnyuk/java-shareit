package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentDtoReturn {

    private Long id;

    private String text;

    private Long itemId;

    private String authorName;

    private LocalDateTime created;

}
