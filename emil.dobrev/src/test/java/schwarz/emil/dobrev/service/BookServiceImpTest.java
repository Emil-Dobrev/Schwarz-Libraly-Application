package schwarz.emil.dobrev.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import schwarz.emil.dobrev.dto.book.CreateBookRequest;
import schwarz.emil.dobrev.dto.book.UpdateBookRequest;
import schwarz.emil.dobrev.entity.Book;
import schwarz.emil.dobrev.entity.Category;
import schwarz.emil.dobrev.exception.BookAlreadyExistException;
import schwarz.emil.dobrev.repository.BookRepository;
import schwarz.emil.dobrev.service.interfaces.CategoryService;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookServiceImpTest {
    @Mock
    BookRepository bookRepository;
    @Mock
    CategoryService categoryService;
    @InjectMocks
    BookServiceImp bookService;

    @Test
    void shouldCreateBook() {
        CreateBookRequest createBookRequest = new CreateBookRequest(
                "title",
                "author",
                "publisher",
                2022,
                "1"
        );

        when(bookRepository.findAll()).thenReturn(List.of());
        when(categoryService.incrementTotalBooks(createBookRequest.categoryId()))
                .thenReturn(someCategory());

        var createdBook = bookService.createBook(createBookRequest);

        verify(categoryService, times(1)).incrementTotalBooks(createBookRequest.categoryId());
        verify(bookRepository, times(1)).save(any(Book.class));

        assertEquals(createdBook.category(), someCategory());
        assertEquals(createdBook.title(), createBookRequest.title());
        assertEquals(createdBook.author(), createBookRequest.author());
        assertEquals(createdBook.publishingYear(), createBookRequest.publishingYear());
    }

    @Test
    void shouldThrowBookAlreadyExistException() {
        CreateBookRequest createBookRequest = new CreateBookRequest(
                "title",
                "author",
                "publisher",
                2022,
                "1"
        );

        var book = someBook();
        when(bookRepository.findAll()).thenReturn(List.of(book));

        assertThrows(BookAlreadyExistException.class, () -> bookService.createBook(createBookRequest));
    }

    @Test
    void shouldGetBookById() {
        var book = someBook();
        var category = someCategory();

        when(bookRepository.findById(book.getId())).thenReturn(Optional.of(book));
        when(categoryService.getCategoryById(category.getId())).thenReturn(category);

        var bookDto = bookService.getBookById(book.getId());

        verify(bookRepository, times(1)).findById(book.getId());
        verify(categoryService, times(1)).getCategoryById(category.getId());

        assertEquals(bookDto.category(), category);
        assertEquals(bookDto.id(), book.getId());
        assertEquals(bookDto.author(), book.getAuthor());
    }


    @Test
    void shouldUpdateBook() {
        var category = someCategory();
        var updateBookRequest = new UpdateBookRequest(
                "newTitle",
                "newAuthor",
                null,
                null,
                "newId"
        );
        var book = someBook();

        when(bookRepository.findById(book.getId())).thenReturn(Optional.of(book));
        when(bookRepository.findAll()).thenReturn(List.of());
        when(categoryService.getCategoryById(any(String.class))).thenReturn(category);

        var updatedBook = bookService.updateBook(book.getId(), updateBookRequest);

        verify(bookRepository, times(1)).findById(any(String.class));
        verify(bookRepository, times(1)).findAll();
        verify(categoryService, times(1)).incrementTotalBooks(updateBookRequest.categoryId());
        verify(categoryService, times(1)).decrementTotalBooks(category.getId());


        assertEquals(updatedBook.title(), updateBookRequest.title());
        assertEquals(updatedBook.author(), updateBookRequest.author());

        assertNotEquals(updatedBook.publishingYear(), updateBookRequest.publishingYear());
        assertNotEquals(updatedBook.publisher(), updateBookRequest.publisher());
    }

    @Test
    void shouldDeleteBook() {
        var book = someBook();

        when(bookRepository.findById(book.getId())).thenReturn(Optional.of(book));

        bookService.deleteBook(book.getId());

        verify(bookRepository, times(1)).findById(book.getId());
        verify(bookRepository, times(1)).delete(book);
        verify(categoryService, times(1)).decrementTotalBooks(book.getCategoryId());
    }

    private Category someCategory() {
        return Category.builder()
                .id("1")
                .name("category")
                .description("desc")
                .numberOfBooks(1)
                .build();
    }

    private Book someBook() {
        return Book.builder()
                .id("testId")
                .title("title")
                .author("author")
                .publisher("publisher")
                .publishingYear(2022)
                .categoryId("1")
                .build();
    }
}