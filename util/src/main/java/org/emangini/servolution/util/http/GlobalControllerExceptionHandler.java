package org.emangini.servolution.util.http;

import lombok.extern.slf4j.Slf4j;
import org.emangini.servolution.api.exceptions.BadRequestException;
import org.emangini.servolution.api.exceptions.InvalidInputException;
import org.emangini.servolution.api.exceptions.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY;

@RestControllerAdvice
@Slf4j
public class GlobalControllerExceptionHandler {

    @ResponseStatus(BAD_REQUEST)
    @ExceptionHandler(BadRequestException.class)
    public @ResponseBody HttpErrorInfo handleBadRequestException(
            ServerHttpRequest request, BadRequestException exception) {
        return createHttpErrorInfo(BAD_REQUEST, request, exception);
    }

    @ResponseStatus(NOT_FOUND)
    @ExceptionHandler(NotFoundException.class)
    public @ResponseBody HttpErrorInfo handleNotFoundException(ServerHttpRequest request, NotFoundException exception)
    {
            return createHttpErrorInfo(NOT_FOUND, request, exception);
    }

    @ResponseStatus(UNPROCESSABLE_ENTITY)
    @ExceptionHandler(InvalidInputException.class)
    public @ResponseBody HttpErrorInfo handleInvalidInputException(
            ServerHttpRequest request, InvalidInputException exception)
    {
        return createHttpErrorInfo(UNPROCESSABLE_ENTITY, request, exception);
    }

    private HttpErrorInfo createHttpErrorInfo(
            HttpStatus httpStatus,
            ServerHttpRequest request,
            Exception exception) {

        final String path = request.getPath().pathWithinApplication().value();
        final String message = exception.getMessage();

        log.debug("Returning HTTP status: {} for path: {}, message: {}", httpStatus, path, message);

        return new HttpErrorInfo(httpStatus, path, message);
    }
}
