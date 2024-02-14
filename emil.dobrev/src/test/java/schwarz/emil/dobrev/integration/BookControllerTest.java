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
import schwarz.emil.dobrev.controller.BookController;
import schwarz.emil.dobrev.dto.book.CreateBookRequest;
import schwarz.emil.dobrev.entity.Book;
import schwarz.emil.dobrev.entity.Category;
import schwarz.emil.dobrev.repository.BookRepository;
import schwarz.emil.dobrev.repository.CategoryRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
class BookControllerTest extends TestConfiguration {


    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private CategoryRepository categoryRepository;

    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders
                .standaloneSetup(BookController.class)
                .build();
    }

    @BeforeEach
    void beforeEach() {
        bookRepository.deleteAll();
        bookRepository.saveAll(generateBooks());
        categoryRepository.save(createCategory());
    }


    @Test
    void shouldGetAllBooks() throws Exception {
        // Given
        List<Book> books = generateBooks();


        // When
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders
                        .get("/api/v1/books")
                        .contentType(MediaType.APPLICATION_JSON)
        );

        // Then
        resultActions.andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").exists())
                .andExpect(jsonPath("$[0].id").value(books.get(0).getId()))
                .andExpect(jsonPath("$[0].title").value(books.get(0).getTitle()))
                .andExpect(jsonPath("$[1].id").exists())
                .andExpect(jsonPath("$[1].id").value(books.get(1).getId()))
                .andExpect(jsonPath("$[1].title").value(books.get(1).getTitle()));
    }

    @Test
    @WithMockUser
    @Rollback
    void shouldCreateBook() throws Exception {
        var request = new CreateBookRequest(
                "title",
                "auth",
                "pub",
                2019,
                "1"
        );

        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders
                        .post("/api/v1/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(request))
                        .accept(APPLICATION_JSON_VALUE)
        );


        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.title").exists())
                .andExpect(jsonPath("$.title").value(request.title()))
                .andExpect(jsonPath("$.author").value(request.author()))
                .andExpect(jsonPath("$.publisher").value(request.publisher()))
                .andExpect(jsonPath("$.publishingYear").value(request.publishingYear()));
    }

    @Rollback
    @WithMockUser
    @Test
    void shouldDeleteBook() throws Exception {
        int initialSize = bookRepository.findAll().size();

        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders
                        .delete("/api/v1/books/testId")
                        .contentType(MediaType.APPLICATION_JSON)

        );

        resultActions.andExpect(status().isNoContent());
        int finalSize = bookRepository.findAll().size();

        // Assert that the size of the repository has decreased by 1
        assertEquals(initialSize - 1, finalSize);
    }


    private List<Book> generateBooks() {
        return List.of(
                Book.builder()
                        .id("testId")
                        .title("title")
                        .author("author")
                        .publisher("publisher")
                        .publishingYear(2022)
                        .categoryId("1")
                        .build(),
                Book.builder()
                        .id("testId2")
                        .title("title")
                        .author("author")
                        .publisher("publisher")
                        .publishingYear(2022)
                        .categoryId("1")
                        .build()
        );
    }

    private Category createCategory() {
        return Category.builder()
                .id("1")
                .name("name")
                .description("desc")
                .numberOfBooks(2)
                .build();
    }
}
