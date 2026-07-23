package ru.practicum.services;

import jakarta.servlet.http.HttpServletRequest;
import ru.practicum.dto.event.UpdateEventAdminRequest;
import ru.practicum.dto.event.UpdateEventUserRequest;
import ru.practicum.models.Event;
import ru.practicum.models.Sort;

import java.time.LocalDateTime;
import java.util.List;

public interface EventService {

    Event addEvent(Long userId, Event event, Long catId);

    List<Event> getUserEvents(Long userId, int size, int from);

    Event getUserEvent(Long userId, Long eventId);

    Event updateEvent(Long userId, Long eventId, UpdateEventUserRequest update);

    Event updateEvent(Long eventId, UpdateEventAdminRequest update);

    List<Event> getFilteredEvents(List<Long> users, List<String> states, List<Long> categories, LocalDateTime rangeStart,
                                  LocalDateTime rangeEnd, int size, int from);

    List<Event> getPublicEvents(String text, List<Long> categories, Boolean paid, LocalDateTime rangeStart,
                                LocalDateTime rangeEnd, boolean onlyAvailable, int size, int from, Sort sort,
                                HttpServletRequest request);

    Event getPublicEvent(Long id, HttpServletRequest httpServletRequest);
}
