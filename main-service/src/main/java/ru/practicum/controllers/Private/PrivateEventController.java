package ru.practicum.controllers.Private;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.event.EventFullDto;
import ru.practicum.dto.event.EventShortDto;
import ru.practicum.dto.event.NewEventDto;
import ru.practicum.dto.event.UpdateEventUserRequest;
import ru.practicum.dto.request.EventRequestStatusUpdateRequest;
import ru.practicum.dto.request.EventRequestStatusUpdateResult;
import ru.practicum.dto.request.ParticipationRequestDto;
import ru.practicum.mappers.EventMapper;
import ru.practicum.mappers.RequestMapper;
import ru.practicum.services.EventService;
import ru.practicum.services.RequestService;

import java.util.List;

@RestController
@Validated
@RequestMapping("/users/{userId}/events")
public class PrivateEventController {

    private final EventService eventService;
    private final RequestService requestService;

    public PrivateEventController(EventService eventService, RequestService requestService) {
        this.eventService = eventService;
        this.requestService = requestService;
    }

    @GetMapping
    public List<EventShortDto> getUserEvents(@PathVariable @Positive Long userId,
                                             @RequestParam(defaultValue = "10") int size,
                                             @RequestParam(defaultValue = "0") int from,
                                             HttpServletRequest request) {
        return EventMapper.eventShortDtoCollection(eventService.getUserEvents(userId, size, from));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EventFullDto addEvent(@PathVariable @Positive Long userId, @RequestBody @Valid NewEventDto eventDto) {
        return EventMapper.toEventFullDto(eventService.addEvent(userId, EventMapper.toEvent(eventDto),
                eventDto.getCategory()));
    }

    @GetMapping("/{eventId}")
    public EventFullDto getUserFullEvent(@PathVariable @Positive Long userId, @PathVariable @Positive Long eventId) {
        return EventMapper.toEventFullDto(eventService.getUserEvent(userId, eventId));
    }

    @PatchMapping("/{eventId}")
    public EventFullDto updateEvent(@PathVariable @Positive Long userId, @PathVariable @Positive Long eventId,
                                    @RequestBody @Valid UpdateEventUserRequest update) {
        System.out.println("PATCH");
        return EventMapper.toEventFullDto(eventService.updateEvent(userId, eventId, update));
    }


    @GetMapping("/{eventId}/requests")
    public List<ParticipationRequestDto> getUserRequests(@PathVariable @Positive Long userId,
                                                         @PathVariable @Positive Long eventId) {
        return RequestMapper.participationRequestDtoList(requestService.getOwnerRequests(userId, eventId));
    }

    @PatchMapping("/{eventId}/requests")
    public EventRequestStatusUpdateResult updateRequests(@PathVariable @Positive Long userId,
                                                         @PathVariable @Positive Long eventId,
                                                         @RequestBody @Valid EventRequestStatusUpdateRequest updateRequest) {
        return RequestMapper.requestStatusUpdateResult(requestService.updateOwnerRequests(userId, eventId, updateRequest));
    }

}
