package ru.practicum.models;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "comments",
        uniqueConstraints = { @UniqueConstraint(columnNames = { "commentator_id", "event_id" }) })
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "commentator_id")
    private User commentator;
    @ManyToOne
    @JoinColumn(name = "event_id")
    private Event event;
    private String text;
    private LocalDateTime created = LocalDateTime.now();
    @Enumerated(EnumType.STRING)
    private State state;
    private Long likes;
    private Long dislikes;

    public Comment(String text) {
        this.text = text;
    }

    public enum Sort {
        LATEST, EARLIEST, MOST_LIKED, MOST_DISLIKED
    }

}
