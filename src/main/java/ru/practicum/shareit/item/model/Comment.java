package ru.practicum.shareit.item.model;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Entity
@Table(name = "comments")
public class Comment {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "comment_id")
    private Long id;

    @Column(name = "comment_text")
    private String text;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "item_id")
    private Long itemId;
}
