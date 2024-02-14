package schwarz.emil.dobrev.integration;


import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import schwarz.emil.dobrev.controller.CategoryController;
import schwarz.emil.dobrev.dto.category.CreateCategoryRequest;
import schwarz.emil.dobrev.dto.category.UpdateCategoryRequest;
import schwarz.emil.dobrev.entity.Category;
import schwarz.emil.dobrev.repository.CategoryRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CategoryControllerTest extends TestConfiguration {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private CategoryRepository categoryRepository;

    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders
                .standaloneSetup(CategoryController.class)
                .build();
    }

    @BeforeEach
    void beforeEach() {
        this.categoryRepository.deleteAll();
        this.categoryRepository.saveAll(someCategories());
    }


    @Test
    @Rollback
    void shouldGetAllCategories() throws Exception {
        var categories = someCategories();


        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders
                        .get("/api/v1/categories")
                        .contentType(MediaType.APPLICATION_JSON)
        );


        resultActions.andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").exists())
                .andExpect(jsonPath("$[0].id").value(categories.get(0).getId()))
                .andExpect(jsonPath("$[0].description").value(categories.get(0).getDescription()))
                .andExpect(jsonPath("$[1].id").exists())
                .andExpect(jsonPath("$[1].id").value(categories.get(1).getId()))
                .andExpect(jsonPath("$[1].name").value(categories.get(1).getName()))
                .andExpect(jsonPath("$[1].numberOfBooks").value(categories.get(1).getNumberOfBooks()));
    }

    @Test
    @WithMockUser
    @Rollback
    void shouldCreateCategory() throws Exception {
        var request = new CreateCategoryRequest(
                "newName",
                "someDescription"
        );

        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders
                        .post("/api/v1/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(request))
                        .accept(APPLICATION_JSON_VALUE)
        );


        resultActions.andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").value(request.name()))
                .andExpect(jsonPath("$.description").value(request.description()))
                .andExpect(jsonPath("$.numberOfBooks").value(0));
    }

    @Test
    @Rollback
    @WithMockUser
    void shouldUpdateCategory() throws Exception {
        var category = someCategory();
        var request = new UpdateCategoryRequest(
                "newName",
                null
        );

        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders
                        .patch("/api/v1/categories/"+ someCategory().getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(request))
                        .accept(APPLICATION_JSON_VALUE)
        );

        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").value(request.name()))
                .andExpect(jsonPath("$.description").value(category.getDescription()))
                .andExpect(jsonPath("$.numberOfBooks").value(category.getNumberOfBooks()));
    }

    @Test
    @Rollback
    @WithMockUser
    void shouldDeleteUser() throws Exception {
        var initialSize = this.categoryRepository.findAll().size();
        var category = someCategory();

        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders
                        .delete("/api/v1/categories/" + category.getId())
                        .contentType(MediaType.APPLICATION_JSON)
        );

        resultActions.andExpect(status().isNoContent());

        var afterDeleteSize = this.categoryRepository.findAll().size();

        assertEquals(initialSize - 1, afterDeleteSize);
    }


    private Category someCategory() {
        return Category.builder()
                .id("1")
                .name("name")
                .description("desc")
                .numberOfBooks(2)
                .build();
    }

    private List<Category> someCategories() {
        return List.of(
                Category.builder()
                        .id("1")
                        .name("name")
                        .description("desc")
                        .numberOfBooks(2)
                        .build(),
                Category.builder()
                        .id("2")
                        .name("name")
                        .description("desc")
                        .numberOfBooks(3)
                        .build()
        );
    }
}
