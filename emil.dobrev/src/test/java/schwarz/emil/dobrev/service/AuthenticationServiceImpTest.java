package schwarz.emil.dobrev.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import schwarz.emil.dobrev.dto.customer.AuthenticationRequest;
import schwarz.emil.dobrev.dto.customer.AuthenticationResponse;
import schwarz.emil.dobrev.dto.customer.CustomerRegistrationRequest;
import schwarz.emil.dobrev.entity.Customer;
import schwarz.emil.dobrev.enums.Role;
import schwarz.emil.dobrev.repository.CustomerRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthenticationServiceImpTest {


    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private JwtService jwtService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private AuthenticationManager authenticationManager;


    private AuthenticationServiceImp authenticationService;

    private Customer customer;

    @BeforeEach
    void setUp() {
        this.customer = new Customer(
                "1",
                "John",
                "john@example.com",
                "encodedPassword",
                List.of(Role.USER));
        this.authenticationService = new AuthenticationServiceImp(
                customerRepository, jwtService, passwordEncoder, modelMapper, authenticationManager);
    }

    @Test
    void testRegister() {
        // Prepare
        CustomerRegistrationRequest registrationRequest = new CustomerRegistrationRequest("John", "john@example.com", "password");
        when(modelMapper.map(registrationRequest, Customer.class)).thenReturn(customer);
        when(passwordEncoder.encode(registrationRequest.getPassword())).thenReturn("encodedPassword");

        // Mock repository save method
        when(customerRepository.save(customer)).thenReturn(customer);

        // Mock JWT service
        String expectedToken = "generated_token";
        when(jwtService.generateToken(customer)).thenReturn(expectedToken);

        // Execute
        AuthenticationResponse response = authenticationService.register(registrationRequest);

        // Verify
        assertNotNull(response);
        verify(customerRepository, times(1)).save(any(Customer.class));
    }

    @Test
    void testLogin() {
        // Prepare
        AuthenticationRequest authenticationRequest = new AuthenticationRequest("john@example.com", "password");

        when(customerRepository.findByEmail(authenticationRequest.email())).thenReturn(Optional.of(customer));
        when(jwtService.generateToken(customer)).thenReturn("generated_token");

        // Execute
        AuthenticationResponse response = authenticationService.login(authenticationRequest);

        // Verify
        assertNotNull(response);
        verify(customerRepository, times(1)).findByEmail(any(String.class));
    }
}