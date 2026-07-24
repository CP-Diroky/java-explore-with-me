package ru.practicum.services;

import ru.practicum.models.Category;

import java.util.Collection;


public interface CategoryService {

    Category addCategory(Category category);

    Category updateCategory(Category category, Long id);

    void deleteCategory(Long id);

    Collection<Category> getCategories(int from, int size);

    Category getCategoryById(Long id);

}
