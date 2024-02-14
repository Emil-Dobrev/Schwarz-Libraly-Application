package schwarz.emil.dobrev.dto.category;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import jakarta.validation.constraints.NotNull;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public record CreateCategoryRequest(
        @NotNull String name,
        @NotNull String description
) {
}
