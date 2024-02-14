package schwarz.emil.dobrev.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import schwarz.emil.dobrev.dto.book.BookDto;
import schwarz.emil.dobrev.dto.book.CreateBookRequest;
import schwarz.emil.dobrev.dto.book.UpdateBookRequest;
import schwarz.emil.dobrev.entity.Book;
import schwarz.emil.dobrev.exception.BookAlreadyExistException;
import schwarz.emil.dobrev.exception.BookNotFoundException;
import schwarz.emil.dobrev.repository.BookRepository;
import schwarz.emil.dobrev.service.interfaces.BookService;
import schwarz.emil.dobrev.service.interfaces.CategoryService;

import java.util.List;

@RequiredArgsConstructor
@Service
public class BookServiceImp implements BookService {

    private final BookRepository bookRepository;
    private final CategoryService categoryService;


    @Transactional
    @Override
    public BookDto createBook(CreateBookRequest request) {
        var book = Book.builder()
                .title(request.title())
                .author(request.author())
                .publisher(request.publisher())
                .publishingYear(request.publishingYear())
                .categoryId(request.categoryId())
                .build();

        checkIfBookAlreadyExist(book);

        var category = categoryService.incrementTotalBooks(request.categoryId());
        bookRepository.save(book);

        return new BookDto(book, category);
    }

    @Override
    public BookDto getBookById(String id) {
        var book = bookRepository.findById(id)
                .orElseThrow(() -> new BookNotFoundException(id));
        var category = categoryService.getCategoryById(book.getCategoryId());
        return new BookDto(book, category);
    }

    @Override
    public List<BookDto> getAllBooks() {
        return bookRepository
                .findAll()
                .stream()
                .map(book -> {
                    var category = categoryService.getCategoryById(book.getCategoryId());
                    return new BookDto(book, category);
                })
                .toList();
    }

    @Transactional
    @Override
    public BookDto updateBook(String id, UpdateBookRequest request) {
        var existingBook = bookRepository.findById(id)
                .orElseThrow(() -> new BookNotFoundException(id));

        if (request.title() != null) {
            existingBook.setTitle(request.title());
        }
        if (request.author() != null) {
            existingBook.setAuthor(request.author());
        }
        if (request.categoryId() != null) {
            // check if category exist
            categoryService.getCategoryById(request.categoryId());

            //hande increment totalBooks of a new category and decrement for old category
            if (!request.categoryId().equals(existingBook.getCategoryId())) {
                categoryService.decrementTotalBooks(existingBook.getCategoryId());
                categoryService.incrementTotalBooks(request.categoryId());
            }
            existingBook.setCategoryId(request.categoryId());
        }
        if (request.publisher() != null) {
            existingBook.setPublisher(request.publisher());
        }
        if (request.publishingYear() != null) {
            existingBook.setPublishingYear(request.publishingYear());
        }
        //check if book exist
        checkIfBookAlreadyExist(existingBook);

        bookRepository.save(existingBook);
        var category = categoryService.getCategoryById(existingBook.getCategoryId());

        return new BookDto(existingBook, category);
    }

    @Transactional
    @Override
    public void deleteBook(String id) {
        var book = bookRepository.findById(id)
                .orElseThrow(() -> new BookNotFoundException(id));

        categoryService.decrementTotalBooks(book.getCategoryId());
        bookRepository.delete(book);
    }

    private void checkIfBookAlreadyExist(Book book) {
        var books = bookRepository.findAll();
        if (books.contains(book)) {
            throw new BookAlreadyExistException();
        }
    }
}
