package ru.practicum.dto.event;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.models.Location;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NewEventDto {
    @NotBlank
    @Size(min = 20, max = 2000)
    private String annotation;
    @NotNull
    private Long category;
    @NotBlank
    @Size(min = 20, max = 7000)
    private String description;
    @NotNull
    @JsonFormat(pattern =  "yyyy-MM-dd HH:mm:ss")
    @Future
    private LocalDateTime eventDate;
    @NotNull
    private Location location;
    private boolean paid = false;
    @PositiveOrZero
    private int participantLimit = 0;
    private boolean requestModeration = true;
    @NotBlank
    @Size(min = 3, max = 120)
    private String title;
}


