package ru.practicum.controllers.Admin;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.event.EventFullDto;
import ru.practicum.dto.event.UpdateEventAdminRequest;
import ru.practicum.mappers.EventMapper;
import ru.practicum.services.EventService;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/admin/events")
@Validated
public class AdminEventController {

    private final EventService eventService;
    private static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    public AdminEventController(EventService eventService) {
        this.eventService = eventService;
    }

    @GetMapping
    public List<EventFullDto> getFilteredEvents(@RequestParam(required = false) List<Long> users,
                                                @RequestParam(required = false) List<String> states,
                                                @RequestParam(required = false) List<Long> categories,
                                                @RequestParam(required = false)
                                                @DateTimeFormat(pattern = DATE_TIME_FORMAT)
                                                LocalDateTime rangeStart,
                                                @RequestParam(required = false)
                                                @DateTimeFormat(pattern = DATE_TIME_FORMAT)
                                                LocalDateTime rangeEnd,
                                                @RequestParam(defaultValue = "0")
                                                @PositiveOrZero int from,
                                                @RequestParam(defaultValue = "10")
                                                @Positive int size) {
        return EventMapper.eventFullDtoList(eventService.getFilteredEvents(users, states, categories, rangeStart,
                rangeEnd, size, from));

    }

    @PatchMapping("/{eventId}")
    public EventFullDto updateEvent(@PathVariable @Positive Long eventId,
                                    @RequestBody @Valid UpdateEventAdminRequest request) {
        return EventMapper.toEventFullDto(eventService.updateEvent(eventId, request));
    }
}
