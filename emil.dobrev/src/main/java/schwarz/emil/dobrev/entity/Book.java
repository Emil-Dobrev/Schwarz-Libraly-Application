package schwarz.emil.dobrev.entity;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Objects;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Document(collection = "books")
public class Book {

    @Id
    private String id;
    @NotNull
    private String title;
    @NotNull
    private String author;
    @NotNull
    private String publisher;
    @NotNull
    private int publishingYear;
    @NotNull
    private String categoryId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Book book)) return false;
        return publishingYear == book.publishingYear && Objects.equals(title, book.title) && Objects.equals(author, book.author) && Objects.equals(publisher, book.publisher) && Objects.equals(categoryId, book.categoryId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, author, publisher, publishingYear, categoryId);
    }
}
