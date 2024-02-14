package schwarz.emil.dobrev.dto.book;

public record UpdateBookRequest(
        String title,
        String author,
        String publisher,
        Integer publishingYear,
        String categoryId
) {
}
