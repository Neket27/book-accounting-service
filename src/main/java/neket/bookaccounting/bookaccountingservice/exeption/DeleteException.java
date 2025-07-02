package neket.bookaccounting.bookaccountingservice.exeption;

import java.text.MessageFormat;

public class DeleteException extends RuntimeException {

    public DeleteException(String message) {
        super(message);
    }

    public DeleteException(String message, Throwable cause) {
        super(message, cause);
    }

    public DeleteException(String message, Object ... values) {
        super(MessageFormat.format(message, values));
    }
}