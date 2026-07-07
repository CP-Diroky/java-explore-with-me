package ru.practicum.stats.server.model;

import lombok.experimental.UtilityClass;
import ru.practicum.stats.dto.EndpointHitDto;

@UtilityClass
public class EndPointHitMapper {

    public static EndpointHit toEndpointHit(EndpointHitDto endpointHitDto) {
        return new EndpointHit(
            endpointHitDto.getApp(), endpointHitDto.getUri(), endpointHitDto.getIp(), endpointHitDto.getTimestamp()
        );
    }
}
