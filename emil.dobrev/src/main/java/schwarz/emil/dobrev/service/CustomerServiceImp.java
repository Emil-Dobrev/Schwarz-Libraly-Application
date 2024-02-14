package schwarz.emil.dobrev.service;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import schwarz.emil.dobrev.dto.customer.CustomerDto;
import schwarz.emil.dobrev.dto.customer.UpdateCustomerRequest;
import schwarz.emil.dobrev.entity.Customer;
import schwarz.emil.dobrev.exception.CustomerNotFoundException;
import schwarz.emil.dobrev.repository.CustomerRepository;
import schwarz.emil.dobrev.service.interfaces.CustomerService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomerServiceImp implements CustomerService {

    private final CustomerRepository customerRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public List<CustomerDto> getAllCustomers() {
        return customerRepository
                .findAll()
                .stream()
                .map(this::mapCustomerToCustomerDto)
                .toList();
    }

    @Override
    public CustomerDto getCustomerById(String id) {
        var customer = customerRepository.findById(id)
                .orElseThrow(() -> new CustomerNotFoundException("id", id));
        return mapCustomerToCustomerDto(customer);
    }

    @Override
    public CustomerDto updateCustomer(UpdateCustomerRequest updateCustomerRequest, String email) {
        Customer customer = getCustomerByEmail(email);

        if (updateCustomerRequest.name() != null) {
            customer.setName(updateCustomerRequest.name());
        }

        if (updateCustomerRequest.password() != null) {
            customer.setPassword(passwordEncoder.encode(updateCustomerRequest.password()));
        }

        customerRepository.save(customer);

        return mapCustomerToCustomerDto(customer);
    }

    @Override
    public void deleteCustomer(String email) {
        var customer = getCustomerByEmail(email);
        customerRepository.delete(customer);
    }


    private Customer getCustomerByEmail(String email) {
        return customerRepository.findByEmail(email)
                .orElseThrow(() -> new CustomerNotFoundException("email", email));
    }

    private CustomerDto mapCustomerToCustomerDto(Customer customer) {
        return modelMapper.map(customer, CustomerDto.class);
    }
}
