package ru.practicum.controllers.Public;

import jakarta.validation.constraints.Positive;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.compilation.CompilationDto;
import ru.practicum.mappers.CompilationMapper;
import ru.practicum.services.CompilationService;

import java.util.List;

@RestController
@RequestMapping("compilations")
@Validated
public class PublicCompilationController {

    private final CompilationService compilationService;

    public PublicCompilationController(CompilationService compilationService) {
        this.compilationService = compilationService;
    }

    @GetMapping
    public List<CompilationDto> getCompilations(@RequestParam(defaultValue = "false") boolean pinned,
                                                @RequestParam(defaultValue = "10") int size,
                                                @RequestParam(defaultValue = "0") int from) {
        return CompilationMapper.compilationDtoList(compilationService.getCompilations(pinned, size, from));
    }

    @GetMapping("/{compId}")
    public CompilationDto getCompilation(@PathVariable @Positive Long compId) {
            return CompilationMapper.compilationDto(compilationService.getCompilation(compId));
    }
}
