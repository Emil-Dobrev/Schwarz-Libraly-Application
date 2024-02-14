package schwarz.emil.dobrev.integration;

import org.junit.Before;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import schwarz.emil.dobrev.controller.CustomerController;
import schwarz.emil.dobrev.dto.customer.UpdateCustomerRequest;
import schwarz.emil.dobrev.entity.Customer;
import schwarz.emil.dobrev.enums.Role;
import schwarz.emil.dobrev.repository.CustomerRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@AutoConfigureMockMvc
class CustomerControllerTest extends TestConfiguration {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CustomerRepository customerRepository;


    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders
                .standaloneSetup(CustomerController.class)
                .build();
    }



    @BeforeEach
    void beforeEach() {
        customerRepository.save(someCustomer());
    }

    @Test
    @WithMockUser
    void shouldGetUserById() throws Exception {
        var customer = someCustomer();
        // When
        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders
                        .get("/api/v1/customers/" + customer.getId())
                        .contentType(MediaType.APPLICATION_JSON)
        );

        // Then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.id").value(customer.getId()))
                .andExpect(jsonPath("$.name").value(customer.getName()))
                .andExpect(jsonPath("$.email").value(customer.getEmail()));
    }

    @Test
    @WithMockUser(username = "john@example.com", password = "EncodedPassword1!")
    @Rollback
    void shouldUpdateCustomer() throws Exception {
        var customer = someCustomer();

        var request = new UpdateCustomerRequest(
                "TestUser",
                null
        );

        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders
                        .patch("/api/v1/customers/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(request))
                        .accept(APPLICATION_JSON_VALUE)
        );

        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath(("$.name")).value(request.name()))
                .andExpect(jsonPath(("$.email")).value(customer.getEmail()));
    }

    @Test
    @Rollback
    @WithMockUser(username = "john@example.com", password = "EncodedPassword1!")
    void shouldDeleteUser() throws Exception {
        var initialSize = this.customerRepository.findAll().size();

        ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders
                        .delete("/api/v1/customers")
                        .contentType(MediaType.APPLICATION_JSON)
        );

        resultActions.andExpect(status().isNoContent());

        var afterDeleteSize = this.customerRepository.findAll().size();

        assertEquals(initialSize - 1, afterDeleteSize);
    }

    private Customer someCustomer() {
        return new Customer(
                "1",
                "John",
                "john@example.com",
                "EncodedPassword1!",
                List.of(Role.USER));
    }
}
