package schwarz.emil.dobrev.dto.book;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import jakarta.validation.constraints.NotNull;
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public record CreateBookRequest(
        @NotNull String title,
        @NotNull String author,
        @NotNull String publisher,
        @NotNull int publishingYear,
        @NotNull String categoryId
) {
}
