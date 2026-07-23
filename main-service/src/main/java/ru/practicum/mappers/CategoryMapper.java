package ru.practicum.mappers;

import lombok.experimental.UtilityClass;
import ru.practicum.dto.category.CategoryDto;
import ru.practicum.dto.category.NewCategoryDto;
import ru.practicum.models.Category;

import java.util.Collection;
import java.util.List;

@UtilityClass
public class CategoryMapper {

    public static CategoryDto toCategoryDto(Category category) {
        return new CategoryDto(
                category.getId(), category.getName()
        );
    }

    public static Category toCategory(NewCategoryDto categoryDto) {
        return new Category(categoryDto.getName());
    }

    public static Category toCategory(CategoryDto categoryDto) {
        return new Category(categoryDto.getName());
    }

    public static List<CategoryDto> toCategoryDtoList(Collection<Category> categories) {
        return categories.stream().map(CategoryMapper::toCategoryDto).toList();
    }
}
