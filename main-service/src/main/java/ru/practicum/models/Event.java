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
@Table(name = "events")
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 2000)
    private String annotation;
    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;
    @Column(name = "confirmed_requests")
    private int confirmedRequests;
    @Column(name = "created_on")
    private LocalDateTime createdOn = LocalDateTime.now();
    @Column(length = 7000)
    private String description;
    @Column(name = "event_date")
    private LocalDateTime eventDate;
    @ManyToOne
    @JoinColumn(name = "initiator_id")
    private User initiator;
    @Embedded
    private Location location;
    private boolean paid;
    @Column(name = "participant_limit")
    private int participantLimit;
    @Column(name = "published_on")
    private LocalDateTime publishedOn;
    @Column(name = "request_moderation")
    private boolean requestModeration;
    @Enumerated(value = EnumType.STRING)
    private State state;
    @Column(length = 120)
    private String title;
    private Long views;


    public Event(String annotation, String description, LocalDateTime eventDate,
                 Location location, boolean paid, int participantLimit, boolean requestModeration, String title) {
        this.annotation = annotation;
        this.description = description;
        this.eventDate = eventDate;
        this.location = location;
        this.paid = paid;
        this.participantLimit = participantLimit;
        this.requestModeration = requestModeration;
        this.title = title;
    }
}



