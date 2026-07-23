package ru.practicum.services;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.dto.event.UpdateEventAdminRequest;
import ru.practicum.dto.event.UpdateEventUserRequest;
import ru.practicum.exceptions.BadRequestException;
import ru.practicum.exceptions.ConflictException;
import ru.practicum.exceptions.ForbiddenException;
import ru.practicum.exceptions.NotFoundException;
import ru.practicum.models.*;
import ru.practicum.repositories.CategoryRepository;
import ru.practicum.repositories.EventRepository;
import ru.practicum.repositories.UserRepository;
import ru.practicum.stats.client.StatsClient;
import ru.practicum.stats.dto.EndpointHitDto;
import ru.practicum.stats.dto.ViewStatsDto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final LocalDateTime start = LocalDateTime.of(1970, 1, 1, 0, 0);
    private final StatsClient statsClient = new StatsClient("http://stats-server:9090");

    public EventServiceImpl(EventRepository eventRepository, UserRepository userRepository,
                            CategoryRepository categoryRepository) {
        this.eventRepository = eventRepository;
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
    }

    @Override
    @Transactional
    public Event addEvent(Long userId, Event event, Long catId) {
        User initiator = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User with id=" + userId + " was not found."));
        Category category = categoryRepository.findById(catId)
                .orElseThrow(() -> new NotFoundException("Category with id=" + catId + " was not found"));
        if (event.getEventDate().isBefore(LocalDateTime.now().plusHours(2))) {
            throw new ForbiddenException("Field: eventDate. Error: должно содержать дату, которая еще не наступила. " +
                    "Value: " + event.getEventDate());
        }
        event.setInitiator(initiator);
        event.setCategory(category);
        event.setState(State.PENDING);

        return eventRepository.save(event);
    }

    @Override
    public List<Event> getUserEvents(Long userId, int size, int from) {
        if (userRepository.findById(userId).isEmpty())
            throw new NotFoundException("User with id=" + userId + " was not found.");

        List<Event> events = eventRepository.getUserEvents(userId, size, from);

        List<String> uris = events.stream().map(event -> "/events/" + event.getId()).toList();

        List<ViewStatsDto> stats = statsClient.viewStats(start, LocalDateTime.now(), uris, true);


        Map<String, Long> views = stats.stream().collect(Collectors.toMap(ViewStatsDto::getUri, ViewStatsDto::getHits));

        events = events.stream()
                .peek(event -> {
                    if (views.get("/events/" + event.getId()) == null) event.setViews(0L);
                    else event.setViews(views.get("/events/" + event.getId()));
                }).toList();

        return events;
    }

    @Override
    public Event getUserEvent(Long userId, Long eventId) {
        Event event = eventRepository.findByInitiatorIdAndId(userId, eventId)
                .orElseThrow(() -> new NotFoundException("Event with id=" + eventId + " was not found."));

        String uri = "/events/" + eventId;

        List<ViewStatsDto> stats = statsClient.viewStats(start, LocalDateTime.now(), List.of(uri), true);

        if (stats.isEmpty()) event.setViews(0L);
        else event.setViews(stats.getFirst().getHits());

        return event;
    }

    @Override
    @Transactional
    public Event updateEvent(Long userId, Long eventId, UpdateEventUserRequest update) {
        Event updatedEvent = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Event with id=" + eventId + " was not found."));


        if (update.getEventDate() != null && update.getEventDate().isBefore(LocalDateTime.now().plusHours(2))) {
            throw new ForbiddenException("Field: eventDate. Error: должно содержать дату, которая еще не наступила. " +
                    "Value: " + update.getEventDate());
        } else if (updatedEvent.getState() == State.PUBLISHED)
            throw new ForbiddenException("Only pending or canceled events can be changed");


        if (update.getAnnotation() != null && !update.getAnnotation().isBlank()) {
            updatedEvent.setAnnotation(update.getAnnotation());
        }
        if (update.getCategory() != null) {
            Category category = categoryRepository.findById(update.getCategory())
                    .orElseThrow(() -> new NotFoundException(
                            "Category with id=" + update.getCategory() + " was not found"));
            updatedEvent.setCategory(category);
        }
        if (update.getDescription() != null && !update.getDescription().isBlank()) {
            updatedEvent.setDescription(update.getDescription());
        }
        if (update.getEventDate() != null) {
            updatedEvent.setEventDate(update.getEventDate());
        }
        if (update.getLocation() != null) {
            updatedEvent.setLocation(update.getLocation());
        }
        if (update.getPaid() != null) {
            updatedEvent.setPaid(update.getPaid());
        }
        if (update.getParticipantLimit() != null) {
            updatedEvent.setParticipantLimit(update.getParticipantLimit());
        }
        if (update.getRequestModeration() != null) {
            updatedEvent.setRequestModeration(update.getRequestModeration());
        }
        if (update.getStateAction() != null) {
            switch (update.getStateAction()) {
                case CANCEL_REVIEW -> updatedEvent.setState(State.CANCELED);
                case SEND_TO_REVIEW -> updatedEvent.setState(State.PENDING);
            }
        }
        if (update.getTitle() != null && !update.getTitle().isBlank()) {
            updatedEvent.setTitle(update.getTitle());
        }

        String uri = "/events/" + eventId;
        List<ViewStatsDto> stats = statsClient.viewStats(start, LocalDateTime.now(), List.of(uri), true);

        if (stats.isEmpty()) updatedEvent.setViews(0L);
        else updatedEvent.setViews(stats.getFirst().getHits());

        return eventRepository.save(updatedEvent);

    }


    @Override
    public List<Event> getFilteredEvents(List<Long> users, List<String> states, List<Long> categories,
                                         LocalDateTime rangeStart,
                                         LocalDateTime rangeEnd, int size, int from) {

        List<Event> events = eventRepository.getFilteredEvents(users, users == null || users.isEmpty(),
                states, states == null || states.isEmpty(), categories,
                categories == null || categories.isEmpty(), rangeStart, rangeEnd, size, from);

        List<String> uris = events.stream().map(event -> "/events/" + event.getId()).toList();

        List<ViewStatsDto> stats = statsClient.viewStats(start, LocalDateTime.now(), uris, true);


        Map<String, Long> views = stats.stream().collect(Collectors.toMap(ViewStatsDto::getUri, ViewStatsDto::getHits));

        events = events.stream()
                .peek(event -> {
                    if (views.get("/events/" + event.getId()) == null) event.setViews(0L);
                    else event.setViews(views.get("/events/" + event.getId()));
                }).toList();


        return events;
    }

    @Override
    @Transactional
    public Event updateEvent(Long eventId, UpdateEventAdminRequest update) {
        Event updatedEvent = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Event with id=" + eventId + " was not found."));
        if (update.getEventDate() != null && update.getEventDate().isBefore(LocalDateTime.now().plusHours(1))) {
            throw new ConflictException("Event date must be at least one hour after publication");
        } else if (updatedEvent.getState() != State.PENDING &&
                update.getStateAction() == UpdateEventAdminRequest.StateAction.PUBLISH_EVENT) {
            throw new ForbiddenException("Cannot publish the event because it's not in the right state: " +
                    updatedEvent.getState());
        } else if (updatedEvent.getState() == State.PUBLISHED &&
                update.getStateAction() == UpdateEventAdminRequest.StateAction.REJECT_EVENT) {
            throw new ForbiddenException("Cannot reject the event because it is already published");
        }


        if (update.getAnnotation() != null && !update.getAnnotation().isBlank()) {
            updatedEvent.setAnnotation(update.getAnnotation());
        }
        if (update.getCategory() != null) {
            Category category = categoryRepository.findById(update.getCategory())
                    .orElseThrow(() -> new NotFoundException(
                            "Category with id=" + update.getCategory() + " was not found"));
            updatedEvent.setCategory(category);
        }
        if (update.getDescription() != null && !update.getDescription().isBlank()) {
            updatedEvent.setDescription(update.getDescription());
        }
        if (update.getEventDate() != null) {
            updatedEvent.setEventDate(update.getEventDate());
        }
        if (update.getLocation() != null) {
            updatedEvent.setLocation(update.getLocation());
        }
        if (update.getPaid() != null) {
            updatedEvent.setPaid(update.getPaid());
        }
        if (update.getParticipantLimit() != null) {
            updatedEvent.setParticipantLimit(update.getParticipantLimit());
        }
        if (update.getRequestModeration() != null) {
            updatedEvent.setRequestModeration(update.getRequestModeration());
        }
        if (update.getStateAction() != null) {
            switch (update.getStateAction()) {
                case PUBLISH_EVENT -> updatedEvent.setState(State.PUBLISHED);
                case REJECT_EVENT -> updatedEvent.setState(State.CANCELED);
            }
        }
        if (update.getTitle() != null && !update.getTitle().isBlank()) {
            updatedEvent.setTitle(update.getTitle());
        }

        String uri = "/events/" + eventId;
        List<ViewStatsDto> stats = statsClient.viewStats(start, LocalDateTime.now(), List.of(uri), true);

        if (stats.isEmpty()) updatedEvent.setViews(0L);
        else updatedEvent.setViews(stats.getFirst().getHits());

        return eventRepository.save(updatedEvent);
    }


    @Override
    @Transactional
    public List<Event> getPublicEvents(String text, List<Long> categories, Boolean paid, LocalDateTime rangeStart,
                                       LocalDateTime rangeEnd, boolean onlyAvailable, int size, int from, Sort sort,
                                       HttpServletRequest request) {


        statsClient.addHit(new EndpointHitDto(
                "ewm-main-service",
                request.getRequestURI(),
                request.getRemoteAddr(),
                LocalDateTime.now()
        ));

        if (rangeStart == null && rangeEnd == null) {
            rangeStart = LocalDateTime.now();
        } else if (rangeStart != null && rangeEnd != null && rangeStart.isAfter(rangeEnd)) {
            throw new BadRequestException("rangeStart must be before rangeEnd");
        }


        boolean categoriesAbsent = categories == null || categories.isEmpty();

        if (categoriesAbsent) {
            categories = List.of(-1L);
        }

        List<Event> events = eventRepository.findPublicEvents(text, categories, categoriesAbsent, paid, rangeStart,
                rangeEnd, onlyAvailable);

        List<String> uris = events.stream().map(event -> "/events/" + event.getId()).toList();


        List<ViewStatsDto> stats = statsClient.viewStats(start, LocalDateTime.now(), uris, true);


        Map<String, Long> views = stats.stream().collect(Collectors.toMap(ViewStatsDto::getUri, ViewStatsDto::getHits));

        for (Event event : events) {
            event.setViews(views.getOrDefault("/events/" + event.getId(), 0L));
        }

        if (sort == Sort.VIEWS) {
            events.sort((event1, event2) -> Long.compare(event2.getViews(), event1.getViews()));
        } else {
            events.sort((event1, event2) -> event2.getEventDate().compareTo(event1.getEventDate()));
        }

        List<Event> eventsResult = events.subList(from, events.size());
        if (size <= eventsResult.size()) {
            eventsResult = eventsResult.subList(0, size);
        }

        return eventsResult;

    }

    @Override
    @Transactional
    public Event getPublicEvent(Long id, HttpServletRequest request) {

        statsClient.addHit(new EndpointHitDto(
                "ewm-main-service",
                request.getRequestURI(),
                request.getRemoteAddr(),
                LocalDateTime.now()
        ));


        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Event with id=" + id + " was not found."));

        if (event.getState() != State.PUBLISHED) throw new NotFoundException("Event with id=" + id + " was not found.");

        String uri = "/events/" + id;

        List<ViewStatsDto> stats = statsClient.viewStats(start, LocalDateTime.now(), List.of(uri), true);

        if (stats.isEmpty()) event.setViews(0L);
        else event.setViews(stats.getFirst().getHits());

        return event;
    }

}
