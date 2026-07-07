package ru.practicum.stats.server.controller;

import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.stats.dto.EndpointHitDto;
import ru.practicum.stats.dto.ViewStatsDto;
import ru.practicum.stats.server.model.EndPointHitMapper;
import ru.practicum.stats.server.service.EndpointHitService;

import java.time.LocalDateTime;
import java.util.List;

@RestController
public class EndpointHitController {

    private final EndpointHitService endpointHitService;

    public EndpointHitController(EndpointHitService endpointHitService) {
        this.endpointHitService = endpointHitService;
    }

    @PostMapping("/hit")
    @ResponseStatus(HttpStatus.CREATED)
    public void addHit(@RequestBody @Valid EndpointHitDto endpointHitDto) {
        endpointHitService.addHit(EndPointHitMapper.toEndpointHit(endpointHitDto));
    }

    @GetMapping("/stats")
    public List<ViewStatsDto> viewStats(@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime start,
                                        @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime end,
                                        @RequestParam(required = false) List<String> uris,
                                        @RequestParam(defaultValue = "false") boolean unique) {
        return endpointHitService.viewStats(start, end, uris, unique);
    }
}
