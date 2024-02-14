package schwarz.emil.dobrev.exception;

public class CustomerNotFoundException extends RuntimeException{

    public CustomerNotFoundException(String identifier, String value){
        super(String.format("Customer with %s: %s is not found",identifier, value));
    }
}
