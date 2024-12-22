package org.emangini.servolution.common.util.http;

import org.emangini.servolution.api.exceptions.InvalidInputException;
import org.emangini.servolution.api.exceptions.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.NotActiveException;

import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY;

public class GlobalControllerExceptionHandler {

    @ResponseStatus(NOT_FOUND)
    @ExceptionHandler(NotActiveException.class)
    public @ResponseBody HttpErrorInfo handleNotFoundException(
            ServerHttpRequest request, NotFoundException exception)
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

        return new HttpErrorInfo(httpStatus, path, message);
    }
}
