package ru.practicum.shareit.item.model;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Entity(name = "comments")
public class Comment {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "comment_id")
    private Long id;

    @Column(name = "comment_text")
    private String text;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private Long userId;

    @ManyToOne
    @JoinColumn(name = "item_id")
    private Long itemId;
}
