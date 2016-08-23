package MailTestHelper.errors;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Created by kenji on 2016/08/01.
 */
@ResponseStatus(value= HttpStatus.NOT_FOUND, reason="No such resources error")
public class NotFoundException extends RuntimeException {
    public NotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotFoundException(String message) {
        super(message);
    }
}
