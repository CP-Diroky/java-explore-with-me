package ru.practicum.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.dto.compilation.NewCompilationDto;
import ru.practicum.dto.compilation.UpdateCompilationRequest;
import ru.practicum.exceptions.NotFoundException;
import ru.practicum.models.Compilation;
import ru.practicum.models.Event;
import ru.practicum.repositories.CompilationRepository;
import ru.practicum.repositories.EventRepository;
import ru.practicum.stats.client.StatsClient;
import ru.practicum.stats.dto.ViewStatsDto;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CompilationServiceImpl implements CompilationService {

    private final CompilationRepository compilationRepository;
    private final EventRepository eventRepository;
    private final LocalDateTime start = LocalDateTime.of(1970, 1, 1, 0, 0);
    private final StatsClient statsClient = new StatsClient("http://stats-server:9090");

    public CompilationServiceImpl(CompilationRepository compilationRepository, EventRepository eventRepository) {
        this.compilationRepository = compilationRepository;
        this.eventRepository = eventRepository;
    }

    @Transactional
    @Override
    public Compilation addCompilation(NewCompilationDto compilationDto) {

        List<Event> events;

        if (compilationDto.getEvents() != null && !compilationDto.getEvents().isEmpty()) {
            events = eventRepository.findAllById(compilationDto.getEvents());
        } else {
            events = List.of();
        }

        if (!events.isEmpty()) {

            List<String> uris = events.stream().map(event -> "/events/" + event.getId()).toList();


            List<ViewStatsDto> stats = statsClient.viewStats(start, LocalDateTime.now(), uris, true);


            Map<String, Long> views = stats.stream().collect(Collectors.toMap(ViewStatsDto::getUri, ViewStatsDto::getHits));

            events = events.stream()
                    .peek(event -> {
                        if (views.get("/events/" + event.getId()) == null) event.setViews(0L);
                        else event.setViews(views.get("/events/" + event.getId()));
                    }).toList();
        }

        Compilation compilation = new Compilation(compilationDto.getTitle(), compilationDto.isPinned(),
                new HashSet<>(events));

        return compilationRepository.save(compilation);
    }

    @Transactional
    @Override
    public void deleteCompilation(Long compId) {
        compilationRepository.findById(compId)
                .orElseThrow(() -> new NotFoundException("Compilation with id=" + compId + " was not found."));
        compilationRepository.deleteById(compId);
    }

    @Transactional
    @Override
    public Compilation updateCompilation(Long compId, UpdateCompilationRequest request) {

        Compilation updatedCompilation = compilationRepository.findById(compId)
                .orElseThrow(() -> new NotFoundException("Compilation with id=" + compId + " was not found."));


        if (request.getEvents() != null) {

            List<Event> events = eventRepository.findAllById(request.getEvents());

            List<String> uris = events.stream().map(event -> "/events/" + event.getId()).toList();

            List<ViewStatsDto> stats = statsClient.viewStats(start, LocalDateTime.now(), uris, true);


            Map<String, Long> views = stats.stream().collect(Collectors.toMap(ViewStatsDto::getUri,
                    ViewStatsDto::getHits));

            events = events.stream()
                    .peek(event -> {
                        if (views.get("/events/" + event.getId()) == null) event.setViews(0L);
                        else event.setViews(views.get("/events/" + event.getId()));
                    }).toList();

            updatedCompilation.setEvents(new HashSet<>(events));
        }

        if (request.getPinned() != null) updatedCompilation.setPinned(request.getPinned());
        if (request.getTitle() != null && !request.getTitle().isBlank())
            updatedCompilation.setTitle(request.getTitle());

        return compilationRepository.save(updatedCompilation);

    }


    @Override
    public List<Compilation> getCompilations(boolean pinned, int size, int from) {

        List<Compilation> compilations = compilationRepository.getCompilations(pinned, size, from);

        if (compilations.isEmpty()) return List.of();

        for (Compilation compilation : compilations) {
            Set<Event> events = compilation.getEvents();

            if (!events.isEmpty()) {

                List<String> uris = events.stream().map(event -> "/events/" + event.getId()).toList();

                List<ViewStatsDto> stats = statsClient.viewStats(start, LocalDateTime.now(), uris, true);


                Map<String, Long> views = stats.stream().collect(Collectors.toMap(ViewStatsDto::getUri,
                        ViewStatsDto::getHits));

                events = events.stream()
                        .peek(event -> {
                            if (views.get("/events/" + event.getId()) == null) event.setViews(0L);
                            else event.setViews(views.get("/events/" + event.getId()));
                        }).collect(Collectors.toSet());

                compilation.setEvents(events);
            }
        }

        return compilations;

    }

    @Override
    public Compilation getCompilation(Long compId) {
        Compilation compilation = compilationRepository.findById(compId)
                .orElseThrow(() -> new NotFoundException("Compilation with id=" + compId + " was not found."));

        Set<Event> events = compilation.getEvents();

        if (!events.isEmpty()) {

            List<String> uris = events.stream().map(event -> "/events/" + event.getId()).toList();

            List<ViewStatsDto> stats = statsClient.viewStats(start, LocalDateTime.now(), uris, true);


            Map<String, Long> views = stats.stream().collect(Collectors.toMap(ViewStatsDto::getUri,
                    ViewStatsDto::getHits));

            events = events.stream()
                    .peek(event -> {
                        if (views.get("/events/" + event.getId()) == null) event.setViews(0L);
                        else event.setViews(views.get("/events/" + event.getId()));
                    }).collect(Collectors.toSet());

            compilation.setEvents(events);
        }

        return compilation;
    }


}



