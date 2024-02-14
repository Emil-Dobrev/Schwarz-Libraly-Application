package schwarz.emil.dobrev.service.interfaces;

import schwarz.emil.dobrev.dto.book.BookDto;
import schwarz.emil.dobrev.dto.book.CreateBookRequest;
import schwarz.emil.dobrev.dto.book.UpdateBookRequest;

import java.util.List;

public interface BookService {
    BookDto createBook(CreateBookRequest createBookRequest);

    BookDto getBookById(String id);

    List<BookDto> getAllBooks();

    BookDto updateBook(String id, UpdateBookRequest updateBookRequest);

    void deleteBook(String id);
}
