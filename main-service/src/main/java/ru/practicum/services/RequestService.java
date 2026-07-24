package ru.practicum.services;

import ru.practicum.dto.request.EventRequestStatusUpdateRequest;
import ru.practicum.models.Request;

import java.util.Collection;

public interface RequestService {

    Collection<Request> getOwnerRequests(Long userId, Long eventId);

    Collection<Request> updateOwnerRequests(Long userId, Long eventId, EventRequestStatusUpdateRequest updateRequest);

    Collection<Request> getUserRequests(Long userId);

    Request addRequest(Long userId, Long eventId);

    Request cancelRequest(Long userId, Long requestId);
}
