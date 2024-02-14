package schwarz.emil.dobrev.dto.customer;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import jakarta.validation.constraints.NotEmpty;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public record AuthenticationRequest(
        @NotEmpty String email,
        @NotEmpty String password
) {
}
