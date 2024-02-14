package schwarz.emil.dobrev.integration;

import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import schwarz.emil.dobrev.controller.AuthenticationController;
import schwarz.emil.dobrev.dto.customer.AuthenticationRequest;
import schwarz.emil.dobrev.dto.customer.CustomerRegistrationRequest;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
class AuthenticationControllerTest  extends TestConfiguration{

    @Autowired
    private MockMvc mockMvc;

    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders
                .standaloneSetup(AuthenticationController.class)
                .build();
    }

    @Test
    @Rollback
    void registerTest() throws Exception {
        CustomerRegistrationRequest customerRegistrationRequest =
                CustomerRegistrationRequest.builder()
                        .email("Test@gmail.com")
                        .name("Test Test")
                        .password("Test123!")
                        .build();

        mockMvc.perform(
                        MockMvcRequestBuilders
                                .post("/api/v1/auth/register")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(asJsonString(customerRegistrationRequest))
                                .accept(APPLICATION_JSON_VALUE)
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.access_token").exists());
    }

    @Test
    @Rollback
    void loginTest() throws Exception {
        // Create a user in the database
        CustomerRegistrationRequest registrationRequest = CustomerRegistrationRequest.builder()
                .email("test@gmail.com")
                .name("Test Test")
                .password("Test123!")
                .build();
        mockMvc.perform(
                        MockMvcRequestBuilders
                                .post("/api/v1/auth/register")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(asJsonString(registrationRequest))
                                .accept(APPLICATION_JSON_VALUE)
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.access_token").exists());

        // Now, attempt to login with the created user
         AuthenticationRequest loginRequest = new AuthenticationRequest("test@gmail.com", "Test123!");
        mockMvc.perform(
                        MockMvcRequestBuilders
                                .post("/api/v1/auth/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(asJsonString(loginRequest))
                                .accept(APPLICATION_JSON_VALUE)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.access_token").exists());
    }

    @Test
    @Rollback
    void loginTestFailsIncorrectUser() throws Exception {
        CustomerRegistrationRequest registrationRequest = CustomerRegistrationRequest.builder()
                .email("testUser@gmail.com")
                .name("Test Test")
                .password("Test123!")
                .build();
        mockMvc.perform(
                        MockMvcRequestBuilders
                                .post("/api/v1/auth/register")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(asJsonString(registrationRequest))
                                .accept(APPLICATION_JSON_VALUE)
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.access_token").exists());

        AuthenticationRequest loginRequest = new AuthenticationRequest("wrongEmail@gmail.com", "Test123!");
        mockMvc.perform(
                        MockMvcRequestBuilders
                                .post("/api/v1/auth/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(asJsonString(loginRequest))
                                .accept(APPLICATION_JSON_VALUE)
                )
                .andExpect(status().is4xxClientError());
    }

}
