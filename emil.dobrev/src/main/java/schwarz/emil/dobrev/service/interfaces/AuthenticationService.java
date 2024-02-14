package schwarz.emil.dobrev.service.interfaces;

import schwarz.emil.dobrev.dto.customer.AuthenticationRequest;
import schwarz.emil.dobrev.dto.customer.AuthenticationResponse;
import schwarz.emil.dobrev.dto.customer.CustomerRegistrationRequest;

public interface AuthenticationService {

    AuthenticationResponse register(CustomerRegistrationRequest customerRegistrationRequest);
    AuthenticationResponse login(AuthenticationRequest authenticationRequest);
}
