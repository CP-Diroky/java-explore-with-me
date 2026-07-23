package ru.practicum.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.models.Request;
import ru.practicum.models.Status;

import java.util.Collection;
import java.util.List;


public interface RequestRepository extends JpaRepository<Request, Long> {

    List<Request> findByEventInitiatorIdAndEventId(Long userId, Long eventId);

    List<Request> findByEventInitiatorIdAndEventIdAndIdIn(Long userId, Long eventId, Collection<Long> requestIds);


    List<Request> findByIdInAndStatus(Collection<Long> requestIds, Status status);

    List<Request> findByRequestorId(Long userId);

    List<Request> findByRequestorIdAndEventId(Long userId, Long eventId);




}
