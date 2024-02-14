package schwarz.emil.dobrev.service;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import schwarz.emil.dobrev.dto.customer.AuthenticationRequest;
import schwarz.emil.dobrev.dto.customer.AuthenticationResponse;
import schwarz.emil.dobrev.dto.customer.CustomerRegistrationRequest;
import schwarz.emil.dobrev.entity.Customer;
import schwarz.emil.dobrev.enums.Role;
import schwarz.emil.dobrev.exception.CustomerNotFoundException;
import schwarz.emil.dobrev.repository.CustomerRepository;
import schwarz.emil.dobrev.service.interfaces.AuthenticationService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImp implements AuthenticationService {

    private final CustomerRepository customerRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;
    private final AuthenticationManager authenticationManager;

    @Override
    public AuthenticationResponse register(CustomerRegistrationRequest customerRegistrationRequest) {
        var customer = modelMapper.map(customerRegistrationRequest, Customer.class);
        customer.setPassword(passwordEncoder.encode(customerRegistrationRequest.getPassword()));
        customer.setRoles(List.of(Role.USER));
        customerRepository.save(customer);
        var token = jwtService.generateToken(customer);
        return new AuthenticationResponse(token);
    }

    @Override
    public AuthenticationResponse login(AuthenticationRequest authenticationRequest) {
        var authenticationToken = new UsernamePasswordAuthenticationToken(
                authenticationRequest.email(),
                authenticationRequest.password()
        );

        authenticationManager.authenticate(authenticationToken);
        var token = getToken(authenticationRequest);
        return new AuthenticationResponse(token);
    }

    private String getToken(AuthenticationRequest request) {
        var customer = customerRepository.findByEmail(request.email())
                .orElseThrow(() -> new CustomerNotFoundException("email", request.email()));
        return jwtService.generateToken(customer);
    }
}

