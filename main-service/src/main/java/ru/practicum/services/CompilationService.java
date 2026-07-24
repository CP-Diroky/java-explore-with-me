package ru.practicum.services;

import ru.practicum.dto.compilation.NewCompilationDto;
import ru.practicum.dto.compilation.UpdateCompilationRequest;
import ru.practicum.models.Compilation;

import java.util.List;

public interface CompilationService {

    Compilation addCompilation(NewCompilationDto compilationDto);

    void deleteCompilation(Long compId);

    Compilation updateCompilation(Long compId, UpdateCompilationRequest request);

    List<Compilation> getCompilations(boolean pinned, int size, int from);

    Compilation getCompilation(Long compId);
}
