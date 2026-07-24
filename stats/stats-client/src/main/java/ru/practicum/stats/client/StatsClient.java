package ru.practicum.stats.client;

import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import ru.practicum.stats.dto.EndpointHitDto;
import ru.practicum.stats.dto.ViewStatsDto;

import java.net.URI;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

public class StatsClient {
    private static final RestTemplate restTemplate = new RestTemplate();
    private final String url;

    public StatsClient(String url) {
        this.url = url;
    }

    public void addHit(EndpointHitDto endpointHitDto) {
        restTemplate.postForObject(url + "/hit", endpointHitDto, Void.class);
    }

    public List<ViewStatsDto> viewStats(LocalDateTime start, LocalDateTime end, List<String> uris,
            boolean unique) {
        DateTimeFormatter formatter =
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        UriComponentsBuilder builder = UriComponentsBuilder
                .fromHttpUrl(url)
                .path("/stats")
                .queryParam("start", start.format(formatter))
                .queryParam("end", end.format(formatter))
                .queryParam("unique", unique);

        if (uris != null) {
            for (String uri : uris) {
                builder.queryParam("uris", uri);
            }
        }

        URI uri = builder.build().encode().toUri();

        ResponseEntity<ViewStatsDto[]> response =
                restTemplate.getForEntity(uri, ViewStatsDto[].class);

        ViewStatsDto[] body = response.getBody();
        return body == null ? List.of() : Arrays.asList(body);
    }


}