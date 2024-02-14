package schwarz.emil.dobrev.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import schwarz.emil.dobrev.entity.Book;

public interface BookRepository extends MongoRepository<Book, String> {
}
