package neket.bookaccounting.bookaccountingservice.exeption;

import java.text.MessageFormat;

public class NotFoundException extends RuntimeException {

    public NotFoundException(String message) {
        super(message);
    }

    public NotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotFoundException(String message, Object ... values) {
        super(MessageFormat.format(message, values));
    }
}