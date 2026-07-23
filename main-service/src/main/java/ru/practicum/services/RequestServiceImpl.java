package ru.practicum.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.dto.request.EventRequestStatusUpdateRequest;
import ru.practicum.exceptions.ConflictException;
import ru.practicum.exceptions.NotFoundException;
import ru.practicum.models.*;
import ru.practicum.repositories.EventRepository;
import ru.practicum.repositories.RequestRepository;
import ru.practicum.repositories.UserRepository;

import java.util.Collection;
import java.util.List;

@Service
public class RequestServiceImpl implements RequestService {

    private final RequestRepository requestRepository;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;

    public RequestServiceImpl(RequestRepository requestRepository, EventRepository eventRepository,
                              UserRepository userRepository) {
        this.requestRepository = requestRepository;
        this.eventRepository = eventRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Collection<Request> getOwnerRequests(Long userId, Long eventId) {
        return requestRepository.findByEventInitiatorIdAndEventId(userId, eventId);
    }

    @Override
    @Transactional
    public Collection<Request> updateOwnerRequests(Long userId, Long eventId,
                                              EventRequestStatusUpdateRequest updateRequest) {

        List<Request> requests = requestRepository.findByEventInitiatorIdAndEventIdAndIdIn(userId, eventId,
                updateRequest.getRequestIds());

        Event event = eventRepository.findByInitiatorIdAndId(userId, eventId)
                .orElseThrow(() -> new NotFoundException("Event with id=" + eventId + " was not found."));

        int limit = event.getParticipantLimit();
        int confirmedRequests = event.getConfirmedRequests();
        Status status = updateRequest.getStatus();

        if (confirmedRequests >= limit && limit != 0 && status == Status.CONFIRMED) {
            throw new ConflictException("The participant limit has been reached");
        } else if (!requestRepository.findByIdInAndStatus(updateRequest.getRequestIds(), Status.REJECTED).isEmpty() ||
                !requestRepository.findByIdInAndStatus(updateRequest.getRequestIds(), Status.CONFIRMED).isEmpty()) {
            throw new ConflictException("Request must have status PENDING");
        }

        for (Request request : requests) {
            if ((confirmedRequests >= limit && limit != 0) || status == Status.REJECTED) {
                request.setStatus(Status.REJECTED);
            } else {
                request.setStatus(Status.CONFIRMED);
                confirmedRequests++;
            }
        }


        event.setConfirmedRequests(confirmedRequests);
        eventRepository.save(event);
        return requestRepository.saveAll(requests);
    }

    public Collection<Request> getUserRequests(Long userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User with id=" + userId + " was not found."));
        return requestRepository.findByRequestorId(userId);
    }

    @Transactional
    @Override
    public Request addRequest(Long userId, Long eventId) {
        User requestor = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User with id=" + userId + " was not found."));
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Event with id=" + eventId + " was not found."));

        if (event.getInitiator().getId().equals(userId)) {
            throw new ConflictException("Event initiator cannot participate in own event");
        } else if (event.getState() != State.PUBLISHED) {
            throw new ConflictException("Cannot participate in an unpublished event");
        } else if (event.getParticipantLimit() != 0
                && event.getConfirmedRequests() >= event.getParticipantLimit()) {
            throw new ConflictException("The participant limit has been reached");
        } else if (!requestRepository.findByRequestorIdAndEventId(userId,eventId).isEmpty()) {
            throw new ConflictException("User can make only one request");
        }

        Request request = new Request();
        request.setRequestor(requestor);
        request.setEvent(event);

        if (event.getParticipantLimit() == 0) {
            request.setStatus(Status.CONFIRMED);
        } else if (!event.isRequestModeration() && event.getParticipantLimit() > 0) {
            request.setStatus(Status.CONFIRMED);
            event.setConfirmedRequests(event.getConfirmedRequests() + 1);
            eventRepository.save(event);
        } else {
            request.setStatus(Status.PENDING);
        }

        return requestRepository.save(request);

    }

    @Override
    @Transactional
    public Request cancelRequest(Long userId, Long requestId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User with id=" + userId + " was not found."));
        Request updatedRequest = requestRepository.findById(requestId)
                .orElseThrow(() -> new NotFoundException("Request with id=" + requestId + " was not found."));
        updatedRequest.setStatus(Status.CANCELED);
        return requestRepository.save(updatedRequest);
    }


}
