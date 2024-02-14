package schwarz.emil.dobrev.service.interfaces;

import schwarz.emil.dobrev.dto.category.CreateCategoryRequest;
import schwarz.emil.dobrev.dto.category.UpdateCategoryRequest;
import schwarz.emil.dobrev.entity.Category;

import java.util.List;

public interface CategoryService {
    Category createCategory(CreateCategoryRequest createCategoryRequest);

    List<Category> getAllCategories();

    Category getCategoryById(String id);

    Category updateCategory(String id, UpdateCategoryRequest updateCategoryRequest);

    void deleteCategory(String id);

    Category incrementTotalBooks(String id);

    void decrementTotalBooks(String categoryId);
}
