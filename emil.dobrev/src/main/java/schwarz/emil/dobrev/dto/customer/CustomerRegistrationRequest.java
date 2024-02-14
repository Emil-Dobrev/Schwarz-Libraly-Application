package schwarz.emil.dobrev.dto.customer;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerRegistrationRequest {
    @NotEmpty
    String name;
    @NotBlank
    @Email(message = "Invalid email")
    String email;
    @NotBlank
    @Size(min = 8, max = 30, message = "Password must be between 8 and 30 characters")
    @Pattern(
            regexp = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?!.*\\s).*$",
            message = "Password should include upperCase, lowerCase and number"
    )
    String password;
}
