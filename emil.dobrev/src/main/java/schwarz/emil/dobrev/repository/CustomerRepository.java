package schwarz.emil.dobrev.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import schwarz.emil.dobrev.entity.Customer;

import java.util.Optional;

public interface CustomerRepository extends MongoRepository<Customer, String> {

    Optional<Customer> findByEmail(String email);
}
