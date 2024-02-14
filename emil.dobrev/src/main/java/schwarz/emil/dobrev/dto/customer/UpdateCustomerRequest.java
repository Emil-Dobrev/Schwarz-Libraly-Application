package schwarz.emil.dobrev.dto.customer;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public record UpdateCustomerRequest(
        String name,
        String password
) {
    public UpdateCustomerRequest(String name, String password) {
        this.name = name;
        this.password = password;
        if (password() != null) {
            if (password().length() < 8 || password().length() > 30) {
                throw new IllegalArgumentException("Password must be between 8 and 30 characters");
            }
            if (!password().matches("^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?!.*\\s).*$")) {
                throw new IllegalArgumentException("Password should include upperCase, lowerCase and number");
            }
        }
    }
}
