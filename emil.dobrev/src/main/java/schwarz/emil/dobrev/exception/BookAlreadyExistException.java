package schwarz.emil.dobrev.exception;

public class BookAlreadyExistException extends RuntimeException{

    public BookAlreadyExistException() {
        super("Book already exist");
    }
}
