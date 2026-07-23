package ru.practicum.controllers.Public;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.Positive;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.event.EventFullDto;
import ru.practicum.mappers.EventMapper;
import ru.practicum.models.Sort;
import ru.practicum.services.EventService;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/events")
public class PublicEventController {

    private final EventService eventService;
    private static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    public PublicEventController(EventService eventService) {
        this.eventService = eventService;
    }

    @GetMapping
    public List<EventFullDto> getPublicEvents(@RequestParam(required = false) String text,
                                              @RequestParam(required = false) List<Long> categories,
                                              @RequestParam(required = false) Boolean paid,
                                              @RequestParam(required = false)
                                              @DateTimeFormat(pattern = DATE_TIME_FORMAT)
                                              LocalDateTime rangeStart,
                                              @RequestParam(required = false)
                                              @DateTimeFormat(pattern = DATE_TIME_FORMAT)
                                              LocalDateTime rangeEnd,
                                              @RequestParam(defaultValue = "false") boolean onlyAvailable,
                                              @RequestParam(defaultValue = "10") int size,
                                              @RequestParam(defaultValue = "0") int from,
                                              @RequestParam(required = false) Sort sort,
                                              HttpServletRequest request) {
        return EventMapper.eventFullDtoList(eventService.getPublicEvents(text, categories, paid, rangeStart,
                rangeEnd, onlyAvailable, size, from, sort, request));
    }

    @GetMapping("/{id}")
    public EventFullDto getEvent(@PathVariable @Positive Long id, HttpServletRequest request) {
        return EventMapper.toEventFullDto(eventService.getPublicEvent(id, request));
    }
}


