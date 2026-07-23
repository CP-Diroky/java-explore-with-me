package ru.practicum.mappers;

import lombok.experimental.UtilityClass;
import ru.practicum.dto.compilation.CompilationDto;
import ru.practicum.models.Compilation;

import java.util.Collection;
import java.util.List;

@UtilityClass
public class CompilationMapper {

    public static CompilationDto compilationDto(Compilation compilation) {
        return new CompilationDto(EventMapper.eventShortDtoCollection(compilation.getEvents()), compilation.getId(),
                compilation.getPinned(), compilation.getTitle());
    }

    public static List<CompilationDto> compilationDtoList(Collection<Compilation> compilations) {
        return compilations.stream().map(CompilationMapper::compilationDto).toList();
    }
}
