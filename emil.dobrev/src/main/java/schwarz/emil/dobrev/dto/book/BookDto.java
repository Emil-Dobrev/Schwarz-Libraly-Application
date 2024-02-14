package schwarz.emil.dobrev.dto.book;

import schwarz.emil.dobrev.entity.Book;
import schwarz.emil.dobrev.entity.Category;

public record BookDto(
       String id,
       String title,
       String author,
       String publisher,
       int publishingYear,
       Category category
) {

   public BookDto(Book book, Category category) {
        this(
                book.getId(),
                book.getTitle(),
                book.getAuthor(),
                book.getPublisher(),
                book.getPublishingYear(),
                category
        );
    }
}
