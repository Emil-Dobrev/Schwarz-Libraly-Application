package schwarz.emil.dobrev.exception;

public class BookNotFoundException extends RuntimeException{

    public BookNotFoundException(String id) {
        super(String.format("Book with id: %s is not found", id));
    }
}
