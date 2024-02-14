package schwarz.emil.dobrev.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import schwarz.emil.dobrev.dto.customer.AuthenticationRequest;
import schwarz.emil.dobrev.dto.customer.AuthenticationResponse;
import schwarz.emil.dobrev.dto.customer.CustomerRegistrationRequest;
import schwarz.emil.dobrev.service.interfaces.AuthenticationService;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> userRegister(
            @Valid @RequestBody CustomerRegistrationRequest customerRegistrationRequest
    ) {
        log.info("Register request for customer with email: {}", customerRegistrationRequest.getEmail());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(authenticationService.register(customerRegistrationRequest));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(
            @RequestBody AuthenticationRequest authenticationRequest
    ) {
        log.info("Login request for customer with email: {}", authenticationRequest.email());
        return ResponseEntity.ok()
                .body(authenticationService.login(authenticationRequest));
    }
}
