package ru.practicum.stats.server.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "endpoint_hits")
public class EndpointHit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String app;
    private String uri;
    private String ip;
    @Column(name = "time_hit")
    private LocalDateTime timeHit;

    public EndpointHit(String app, String uri, String ip, LocalDateTime timeHit) {
        this.app = app;
        this.uri = uri;
        this.ip = ip;
        this.timeHit = timeHit;
    }
}
