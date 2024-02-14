package schwarz.emil.dobrev.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import schwarz.emil.dobrev.dto.category.CreateCategoryRequest;
import schwarz.emil.dobrev.dto.category.UpdateCategoryRequest;
import schwarz.emil.dobrev.entity.Category;
import schwarz.emil.dobrev.repository.CategoryRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoryServiceImpTest {
    @Mock
    CategoryRepository categoryRepository;

    @InjectMocks
    CategoryServiceImp categoryService;

    @Test
    void shouldCreateCategory() {
        var request = new CreateCategoryRequest(
                "name",
                "description"
        );


        categoryService.createCategory(request);

        verify(categoryRepository, times(1)).save(any(Category.class));
    }


    @Test
    void shouldUpdateCategory() {
        var request = new UpdateCategoryRequest(
                "newName",
                null
        );
        var category = someCategory();

        when(categoryRepository.findById(category.getId()))
                .thenReturn(Optional.of(category));

        categoryService.updateCategory(category.getId(), request);

        verify(categoryRepository, times(1)).save(category);
        verify(categoryRepository, times(1)).findById(category.getId());
    }

    @Test
    void shouldDeleteCategory() {
        var category = someCategory();

        when(categoryRepository.findById(category.getId()))
                .thenReturn(Optional.of(category));

        categoryService.deleteCategory(category.getId());

        verify(categoryRepository, times(1)).findById(category.getId());
        verify(categoryRepository, times(1)).delete(category);
    }

    @Test
    void shouldIncrementTotalBooks() {
        var category = someCategory();

        when(categoryRepository.findById(category.getId()))
                .thenReturn(Optional.of(category));

        categoryService.incrementTotalBooks(category.getId());

        verify(categoryRepository, times(1)).save(category);
        assertEquals(2, category.getNumberOfBooks());
    }

    @Test
    void shouldDecrementTotalBooks() {
        var category = someCategory();

        when(categoryRepository.findById(category.getId()))
                .thenReturn(Optional.of(category));

        categoryService.decrementTotalBooks(category.getId());

        verify(categoryRepository, times(1)).save(category);
        assertEquals(0, category.getNumberOfBooks());
    }

    private Category someCategory() {
        return Category.builder()
                .id("1")
                .name("category")
                .description("desc")
                .numberOfBooks(1)
                .build();
    }
}