package ru.practicum.dto.compilation;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateCompilationRequest {
    List<Long> events;
    Boolean pinned;
    @Size(max = 50)
    String title;
}
