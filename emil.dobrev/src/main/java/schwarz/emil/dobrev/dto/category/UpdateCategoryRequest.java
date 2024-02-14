package schwarz.emil.dobrev.dto.category;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public record UpdateCategoryRequest(
        String name,
        String description
) {
}
