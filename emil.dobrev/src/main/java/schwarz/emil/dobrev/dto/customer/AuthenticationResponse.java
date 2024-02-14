package schwarz.emil.dobrev.dto.customer;

import com.fasterxml.jackson.annotation.JsonProperty;

public record AuthenticationResponse(
        @JsonProperty("access_token")
        String token
) {
}
