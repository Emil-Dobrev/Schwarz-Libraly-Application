package schwarz.emil.dobrev.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import schwarz.emil.dobrev.dto.customer.CustomerDto;
import schwarz.emil.dobrev.dto.customer.UpdateCustomerRequest;
import schwarz.emil.dobrev.entity.Customer;
import schwarz.emil.dobrev.enums.Role;
import schwarz.emil.dobrev.repository.CustomerRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerServiceImpTest {

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private CustomerServiceImp customerService;

    private Customer customer;

    @BeforeEach
    void setUp() {
        this.customer = new Customer(
                "1",
                "John",
                "john@example.com",
                "EncodedPassword1!",
                List.of(Role.USER));
    }

    @Test
    void getAllCustomers() {
        var customerDto = new CustomerDto(customer.getId(), customer.getName(), customer.getEmail());

        when(customerRepository.findAll()).thenReturn(List.of(customer, customer));
        when(modelMapper.map(customer, CustomerDto.class)).thenReturn(customerDto);
        this.customerService.getAllCustomers();

        verify(customerRepository, times(1)).findAll();
    }

    @Test
    void updateCustomer_NameAndPasswordUpdated_ReturnsUpdatedCustomer() {
        // Arrange
        String email = this.customer.getEmail();
        UpdateCustomerRequest updateRequest = new UpdateCustomerRequest("New Name", "Password1!");
        Customer testCustomer = this.customer;
        when(customerRepository.findByEmail(email)).thenReturn(Optional.of(testCustomer));
        when(passwordEncoder.encode(updateRequest.password())).thenReturn("EncodedPassword1!");

        customerService.updateCustomer(updateRequest, email);

        verify(customerRepository, times(1)).save(testCustomer);
    }

    @Test
    void updateCustomer_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class,
                () -> customerService.updateCustomer(new UpdateCustomerRequest("New Name", "test"), this.customer.getEmail()));
    }


    @Test
    void shouldDeleteCustomer() {
        String email = this.customer.getEmail();
        Customer customer = this.customer;
        when(customerRepository.findByEmail(customer.getEmail())).thenReturn(Optional.of(customer));

        // Act
        customerService.deleteCustomer(email);

        // Assert
        verify(customerRepository, times(1)).delete(customer);
    }
}