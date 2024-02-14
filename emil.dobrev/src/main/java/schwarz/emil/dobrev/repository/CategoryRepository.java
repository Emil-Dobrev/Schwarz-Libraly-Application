package schwarz.emil.dobrev.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import schwarz.emil.dobrev.entity.Category;

public interface CategoryRepository extends MongoRepository<Category, String> {
}
