package ru.practicum.mappers;

import lombok.experimental.UtilityClass;
import ru.practicum.dto.event.EventFullDto;
import ru.practicum.dto.event.EventShortDto;
import ru.practicum.dto.event.NewEventDto;
import ru.practicum.models.Event;

import java.util.Collection;
import java.util.List;

@UtilityClass
public class EventMapper {

    public static Event toEvent(NewEventDto eventDto) {
        return new Event(
                eventDto.getAnnotation(),
                eventDto.getDescription(),
                eventDto.getEventDate(),
                eventDto.getLocation(),
                eventDto.isPaid(),
                eventDto.getParticipantLimit(),
                eventDto.isRequestModeration(),
                eventDto.getTitle()
        );
    }

    public static EventShortDto toEventShortDto(Event event) {
        return new EventShortDto(
                event.getAnnotation(),
                CategoryMapper.toCategoryDto(event.getCategory()),
                event.getConfirmedRequests(),
                event.getEventDate(),
                event.getId(),
                UserMapper.toUserShortDto(event.getInitiator()),
                event.isPaid(),
                event.getTitle(),
                event.getViews()

        );
    }

    public static EventFullDto toEventFullDto(Event event) {
        return new EventFullDto(
                event.getAnnotation(),
                CategoryMapper.toCategoryDto(event.getCategory()),
                event.getConfirmedRequests(),
                event.getCreatedOn(),
                event.getDescription(),
                event.getEventDate(),
                event.getId(),
                UserMapper.toUserShortDto(event.getInitiator()),
                event.getLocation(),
                event.isPaid(),
                event.getParticipantLimit(),
                event.getPublishedOn(),
                event.isRequestModeration(),
                event.getState(),
                event.getTitle(),
                event.getViews()
        );
    }

    public static List<EventShortDto> eventShortDtoCollection(Collection<Event> events) {
        return events.stream().map(EventMapper::toEventShortDto).toList();
    }

    public static List<EventFullDto> eventFullDtoList(Collection<Event> events) {
        return events.stream().map(EventMapper::toEventFullDto).toList();
    }
}


