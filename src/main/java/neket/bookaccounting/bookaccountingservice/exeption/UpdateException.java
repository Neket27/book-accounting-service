package neket.bookaccounting.bookaccountingservice.exeption;


import java.text.MessageFormat;

public class UpdateException extends RuntimeException {

    public UpdateException(String message) {
        super(message);
    }

    public UpdateException(String message, Throwable cause) {
        super(message, cause);
    }

    public UpdateException(String message, Object ... values) {
        super(MessageFormat.format(message, values));
    }
}
