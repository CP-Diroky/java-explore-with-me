package ru.practicum.stats.client;

import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import ru.practicum.stats.dto.EndpointHitDto;
import ru.practicum.stats.dto.ViewStatsDto;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

public class StatsClient {
    private static final RestTemplate restTemplate = new RestTemplate();
    private static final String URL = "http://localhost:9090";


    public static void addHit(EndpointHitDto endpointHitDto) {
        restTemplate.postForObject(URL + "/hit", endpointHitDto, Void.class);
    }

    public static List<ViewStatsDto> viewStats(LocalDateTime start, LocalDateTime end,
                                               List<String> uris, boolean unique) {
        String ampersand = "&";

        StringBuilder query = new StringBuilder("/stats?");

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        String queryStart = URLEncoder.encode(start.format(formatter), StandardCharsets.UTF_8);
        query.append("start=").append(queryStart);

        String queryEnd = URLEncoder.encode(end.format(formatter), StandardCharsets.UTF_8);
        query.append(ampersand).append("end=").append(queryEnd);

        if (uris != null) {
            for (String uri: uris) {
                query.append(ampersand).append("uris=").append(uri);
            }
        }

        query.append(ampersand).append("unique=").append(unique);

        ResponseEntity<ViewStatsDto[]> response = restTemplate.getForEntity(URL + query, ViewStatsDto[].class);

        ViewStatsDto[] body = response.getBody();
        return body == null ? List.of() : Arrays.asList(body);

    }


}