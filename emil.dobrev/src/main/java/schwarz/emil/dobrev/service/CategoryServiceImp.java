package schwarz.emil.dobrev.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import schwarz.emil.dobrev.dto.category.CreateCategoryRequest;
import schwarz.emil.dobrev.dto.category.UpdateCategoryRequest;
import schwarz.emil.dobrev.entity.Category;
import schwarz.emil.dobrev.exception.CategoryNotFoundException;
import schwarz.emil.dobrev.repository.CategoryRepository;
import schwarz.emil.dobrev.service.interfaces.CategoryService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImp implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Override
    public Category createCategory(CreateCategoryRequest createCategoryRequest) {
        var category = Category.builder()
                .name(createCategoryRequest.name())
                .description(createCategoryRequest.description())
                .numberOfBooks(0)
                .build();
        return this.categoryRepository.save(category);
    }

    @Override
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    @Override
    public Category getCategoryById(String id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException(id));
    }

    @Override
    public Category updateCategory(String id, UpdateCategoryRequest updateCategoryRequest) {
        var category = getCategoryById(id);

        if (updateCategoryRequest.name() != null) {
            category.setName(updateCategoryRequest.name());
        }

        if (updateCategoryRequest.description() != null) {
            category.setDescription(updateCategoryRequest.description());
        }

        return categoryRepository.save(category);
    }

    @Override
    public void deleteCategory(String id) {
        var category = getCategoryById(id);
        categoryRepository.delete(category);
    }

    @Override
    public Category incrementTotalBooks(String id) {
        var category = getCategoryById(id);
        category.setNumberOfBooks(category.getNumberOfBooks() + 1);
        return categoryRepository.save(category);
    }

    @Override
    public void decrementTotalBooks(String categoryId) {
        var category = getCategoryById(categoryId);
        category.setNumberOfBooks(category.getNumberOfBooks() - 1);
        categoryRepository.save(category);
    }
}
