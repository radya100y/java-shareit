package ru.practicum.shareit.item.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.SuperBuilder;
import ru.practicum.shareit.user.User;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@SuperBuilder
@RequiredArgsConstructor @AllArgsConstructor @Getter
@Entity @Table(name = "comments", schema = "public")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "item_id")
    @NotNull
    private Item item;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @NotNull
    private User author;

    @Column(name = "comment", nullable = false)
    private String text;

    @Column(name = "created", nullable = false)
    private LocalDateTime created;

}
