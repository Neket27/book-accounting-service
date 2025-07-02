package neket.bookaccounting.bookaccountingservice.exeption;

import java.text.MessageFormat;

public class CreateException extends RuntimeException {

    public CreateException(String message) {
        super(message);
    }

    public CreateException(String message, Throwable cause) {
        super(message, cause);
    }

    public CreateException(String message, Object ... values) {
        super(MessageFormat.format(message, values));
    }
}
