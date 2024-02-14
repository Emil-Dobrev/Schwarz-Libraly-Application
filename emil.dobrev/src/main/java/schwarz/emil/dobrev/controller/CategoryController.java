package schwarz.emil.dobrev.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import schwarz.emil.dobrev.dto.category.CreateCategoryRequest;
import schwarz.emil.dobrev.dto.category.UpdateCategoryRequest;
import schwarz.emil.dobrev.entity.Category;
import schwarz.emil.dobrev.service.interfaces.CategoryService;

import java.util.List;


@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v1/categories")
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping
    public ResponseEntity<List<Category>> getAllCategories() {
        log.info("Request for getting all categories");
        return ResponseEntity.ok()
                .body(categoryService.getAllCategories());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Category> getCategoryById(
            @PathVariable String id
    ) {
        log.info("Request for getting category with id: {}", id);
        return ResponseEntity.ok()
                .body(categoryService.getCategoryById(id));
    }

    @PostMapping
    public ResponseEntity<Category> createCategory(
            @Valid @RequestBody CreateCategoryRequest createCategoryRequest
    ) {
        log.info("Request for creating category");
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(categoryService.createCategory(createCategoryRequest));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Category> updateCategory(
            @PathVariable String id,
            @RequestBody UpdateCategoryRequest updateCategoryRequest
    ) {
        log.info("Request for updating category with id: {}", id);
        return ResponseEntity.ok()
                .body(categoryService.updateCategory(id, updateCategoryRequest));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteCategory(
            @PathVariable String id
    ) {
        log.info("Request for deleting category with id: {}", id);
        categoryService.deleteCategory(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
