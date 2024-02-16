package schwarz.emil.dobrev.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import schwarz.emil.dobrev.entity.Book;

@Repository
public interface BookRepository extends MongoRepository<Book, String> {
}
