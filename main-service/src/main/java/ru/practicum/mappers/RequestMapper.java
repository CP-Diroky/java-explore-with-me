package ru.practicum.mappers;

import lombok.experimental.UtilityClass;
import ru.practicum.dto.request.EventRequestStatusUpdateResult;
import ru.practicum.dto.request.ParticipationRequestDto;
import ru.practicum.models.Request;
import ru.practicum.models.Status;

import java.util.Collection;
import java.util.List;

@UtilityClass
public class RequestMapper {

    public static ParticipationRequestDto participationRequestDto(Request request) {
        return new ParticipationRequestDto(
                request.getCreated(),
                request.getEvent().getId(),
                request.getId(),
                request.getRequestor().getId(),
                request.getStatus()
        );
    }

    public static List<ParticipationRequestDto> participationRequestDtoList(Collection<Request> requests) {
        return requests.stream().map(RequestMapper::participationRequestDto).toList();
    }

    public static EventRequestStatusUpdateResult requestStatusUpdateResult(Collection<Request> requests) {
        List<ParticipationRequestDto> requestsDto = participationRequestDtoList(requests);

        List<ParticipationRequestDto> confirmedRequests = requestsDto
                .stream()
                .filter(requestDto -> requestDto.getStatus() == Status.CONFIRMED)
                .toList();

        List<ParticipationRequestDto> rejectedRequests = requestsDto
                .stream()
                .filter(requestDto -> requestDto.getStatus() == Status.REJECTED)
                .toList();

        return new EventRequestStatusUpdateResult(confirmedRequests, rejectedRequests);


    }
}

