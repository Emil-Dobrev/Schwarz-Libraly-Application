package schwarz.emil.dobrev.exception;

public class CategoryNotFoundException extends RuntimeException {
    public CategoryNotFoundException(String id){
        super(String.format("Category with id: %s is not found",id));
    }
}
