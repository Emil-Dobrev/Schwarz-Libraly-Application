package schwarz.emil.dobrev.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import schwarz.emil.dobrev.dto.customer.CustomerDto;
import schwarz.emil.dobrev.dto.customer.UpdateCustomerRequest;
import schwarz.emil.dobrev.service.interfaces.CustomerService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v1/customers")
public class CustomerController {

    private final CustomerService customerService;

    @GetMapping
    public ResponseEntity<List<CustomerDto>> getAllCustomers() {
        log.info("Request for getting all customers");
        return ResponseEntity.ok().body(customerService.getAllCustomers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomerDto> getCustomerById(
            @PathVariable String id
    ) {
        log.info("Request for getting customer with id: {}", id);
        return ResponseEntity.ok()
                .body(customerService.getCustomerById(id));
    }

    @PatchMapping("/update")
    public ResponseEntity<CustomerDto> updateCustomer(
            @RequestBody UpdateCustomerRequest updateCustomerRequest,
            Authentication authentication
    ) {
        log.info("Updating customer: {}", authentication.getName());
        return ResponseEntity.ok()
                .body(
                        this.customerService.updateCustomer(
                                updateCustomerRequest,
                                authentication.getName()
                        ));
    }

    @DeleteMapping
    public ResponseEntity<HttpStatus> deleteCustomer(Authentication authentication) {
        log.info("Deleting customer: {}", authentication.getName());
        this.customerService.deleteCustomer(authentication.getName());
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
