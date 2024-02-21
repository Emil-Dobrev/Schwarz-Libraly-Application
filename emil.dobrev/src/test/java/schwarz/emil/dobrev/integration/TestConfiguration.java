package schwarz.emil.dobrev.integration;

import org.junit.jupiter.api.BeforeAll;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.shaded.com.fasterxml.jackson.core.JsonProcessingException;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

@Testcontainers
public class TestConfiguration {
    static ObjectMapper objectMapper = new ObjectMapper();

    @Container
    @ServiceConnection
    static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:6.0.9")
            .withExposedPorts(27017)
            .withSharding();

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.uri", mongoDBContainer::getConnectionString);
        registry.add("spring.datasource.url", mongoDBContainer::getReplicaSetUrl);
        registry.add("spring.datasource.username", mongoDBContainer::getContainerName);
    }

    @BeforeAll
    static void beforeAll()  {
        mongoDBContainer.start();
    }


    protected    <T> String asJsonString(T object) throws JsonProcessingException {
        return objectMapper.writeValueAsString(object);
    }
}
