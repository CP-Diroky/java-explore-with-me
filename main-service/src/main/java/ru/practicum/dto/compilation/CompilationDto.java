package ru.practicum.dto.compilation;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.dto.event.EventShortDto;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CompilationDto {
    List<EventShortDto> events;
    Long id;
    boolean pinned;
    String title;
}
