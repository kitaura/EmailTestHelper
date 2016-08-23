package MailTestHelper.errors;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Created by kenji on 2016/08/01.
 */
@ResponseStatus(value= HttpStatus.INTERNAL_SERVER_ERROR, reason="Unexpected error")
public class ApplicationException extends RuntimeException {
    public ApplicationException(String message, Throwable cause) {
        super(message, cause);
    }

    public ApplicationException(String message) {
        super(message);
    }
}
