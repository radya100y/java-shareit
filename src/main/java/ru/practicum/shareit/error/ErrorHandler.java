package ru.practicum.shareit.error;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class ErrorHandler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleNotFoundException(final NotFoundException exc) {
        log.debug("Получен статус 404 Not found {}", exc.getMessage(), exc);
        return new ErrorResponse(exc.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleAlreadyExistException(final AlreadyExistException exc) {
        log.debug("Получен статус 409 Conflict {}", exc.getMessage(), exc);
        return new ErrorResponse(exc.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleBadArgumentException(final MethodArgumentNotValidException exc) {
        log.debug("Получен статус 400 Bad request {}", exc.getMessage(), exc);
        return new ErrorResponse(exc.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErrorResponse handleAccessException(final AccessException exc) {
        log.debug("Получен статус 403 forbidden {}", exc.getMessage(), exc);
        return new ErrorResponse(exc.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleValidateException(final ValidateException exc) {
        log.debug("Получен статус 400 Bad request {}", exc.getMessage(), exc);
        return new ErrorResponse(exc.getMessage());
    }

/*    @ExceptionHandler //Неправильный статус - не работает
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleIllegalArgumantException(final IllegalArgumentException exc) {
        log.debug("Получен статус 400 Bad request {}", exc.getMessage(), exc);
        return new ErrorResponse(exc.getMessage());
    }*/

}
