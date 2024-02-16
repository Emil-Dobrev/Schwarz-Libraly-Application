package schwarz.emil.dobrev.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import schwarz.emil.dobrev.entity.Category;

@Repository
public interface CategoryRepository extends MongoRepository<Category, String> {
}
