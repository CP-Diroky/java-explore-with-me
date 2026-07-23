package ru.practicum.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.exceptions.ConflictException;
import ru.practicum.exceptions.NotFoundException;
import ru.practicum.models.Category;
import ru.practicum.repositories.CategoryRepository;
import ru.practicum.repositories.EventRepository;

import java.util.Collection;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final EventRepository eventRepository;

    public CategoryServiceImpl(CategoryRepository categoryRepository, EventRepository eventRepository) {
        this.categoryRepository = categoryRepository;
        this.eventRepository = eventRepository;
    }

    @Transactional
    @Override
    public Category addCategory(Category category) {
        if (!categoryRepository.findByName(category.getName()).isEmpty())
            throw new ConflictException("This category name already exists");
        return categoryRepository.save(category);
    }

    @Transactional
    @Override
    public Category updateCategory(Category category, Long id) {
        Category updatedCategory = categoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Category with id=" + id + " was not found."));
        if (!categoryRepository.findByName(category.getName()).isEmpty() &&
                !updatedCategory.getName().equals(category.getName()))
            throw new ConflictException("This category name already exists");
        updatedCategory.setName(category.getName());
        return categoryRepository.save(updatedCategory);
    }

    @Transactional
    @Override
    public void deleteCategory(Long id) {
        categoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Category with id=" + id + " was not found."));
        if (!eventRepository.findByCategoryId(id).isEmpty()) {
            throw new ConflictException("The category is not empty");
        }
        categoryRepository.deleteById(id);
    }

    @Override
    public Collection<Category> getCategories(int size, int from) {
        return categoryRepository.getCategories(size, from);
    }

    @Override
    public Category getCategoryById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Category with id=" + id + " was not found."));
    }
}
