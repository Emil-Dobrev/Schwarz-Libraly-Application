package schwarz.emil.dobrev.service.interfaces;

import schwarz.emil.dobrev.dto.customer.CustomerDto;
import schwarz.emil.dobrev.dto.customer.UpdateCustomerRequest;

import java.util.List;

public interface CustomerService {

    List<CustomerDto> getAllCustomers();
    CustomerDto getCustomerById(String id);
    CustomerDto updateCustomer(UpdateCustomerRequest updateCustomerRequest, String email);
    void deleteCustomer(String email);
}
