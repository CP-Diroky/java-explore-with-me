package ru.practicum.controllers.Public;

import jakarta.validation.constraints.Positive;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.category.CategoryDto;
import ru.practicum.mappers.CategoryMapper;
import ru.practicum.services.CategoryService;

import java.util.List;

@RestController
@RequestMapping("/categories")
@Validated
public class PublicCategoryController {

    private final CategoryService categoryService;

    public PublicCategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public List<CategoryDto> getCategories(@RequestParam(defaultValue = "10") int size,
                                           @RequestParam(defaultValue = "0") int from) {
        return CategoryMapper.toCategoryDtoList(categoryService.getCategories(size, from));
    }

    @GetMapping("/{catId}")
    public CategoryDto getCategory(@PathVariable @Positive Long catId) {
        return CategoryMapper.toCategoryDto(categoryService.getCategoryById(catId));
    }
}

