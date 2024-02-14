package schwarz.emil.dobrev.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import schwarz.emil.dobrev.dto.book.BookDto;
import schwarz.emil.dobrev.dto.book.CreateBookRequest;
import schwarz.emil.dobrev.dto.book.UpdateBookRequest;
import schwarz.emil.dobrev.service.interfaces.BookService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v1/books")
public class BookController {

    private final BookService bookService;

    @GetMapping
    public ResponseEntity<List<BookDto>> getAllBooks() {
        log.info("Request for getting all books");
        return ResponseEntity.ok()
                .body(bookService.getAllBooks());
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookDto> getBookById(
            @PathVariable String id
    ) {
        log.info("Request for getting book with id: {}", id);
        return ResponseEntity.ok()
                .body(bookService.getBookById(id));
    }

    @PostMapping
    public ResponseEntity<BookDto> createBook(
            @Valid @RequestBody CreateBookRequest createBookRequest
    ) {
        log.info("Request for creating book");
        return ResponseEntity.ok()
                .body(bookService.createBook(createBookRequest));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<BookDto> updateBook(
            @PathVariable String id,
            @RequestBody UpdateBookRequest updateBookRequest
    ) {
        log.info("Request for updating book with id: {}", id);
        return ResponseEntity.ok()
                .body(bookService.updateBook(id, updateBookRequest));
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteBook(
            @PathVariable String id
    ) {
        log.info("Request for deleting book with id: {}", id);
        bookService.deleteBook(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .build();
    }
}
