//=================================================================================================
// SpringBoot, SpringData - Volcano Island Simple App - Copyright (C) 2020, Yan Avery
//=================================================================================================

package org.pacifico.volcano.api.exception;

import lombok.extern.slf4j.Slf4j;
import org.pacifico.volcano.beans.AppExceptionBean;
import org.pacifico.volcano.exception.InvalidParameterException;
import org.pacifico.volcano.exception.ReservationNotFoundException;
import org.pacifico.volcano.utils.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.UUID;

//=================================================================================================
@Order(Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice
@Slf4j
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {
    @Autowired
    private Mapper exceptionMapper;

    /**
     * Handles exceptions of type InvalidParameterException, translating them into HTTP 400 errors.
     */
    @ExceptionHandler(InvalidParameterException.class)
    protected ResponseEntity<Object> handleInvalidParameterException(InvalidParameterException ex, WebRequest request) {
        AppExceptionBean exceptionBean = exceptionMapper.toBean(ex);
        return handleExceptionInternal(ex, exceptionBean, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    /**
     * Handles exceptions of type ReservationNotFoundException, translating them into HTTP 404 errors.
     */
    @ExceptionHandler(ReservationNotFoundException.class)
    protected ResponseEntity<Object> handleReservationNotFoundException(ReservationNotFoundException ex, WebRequest request) {
        AppExceptionBean exceptionBean = exceptionMapper.toBean(ex);
        return handleExceptionInternal(ex, exceptionBean, new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }

    /**
     * Handles all other exception types, translating them to HTTP 400 errors
     * and logging more details (including stack) for further engineering team troubleshooting.
     */
    @ExceptionHandler(Exception.class)
    protected ResponseEntity<Object> handleAllOtherErrors(Exception ex, WebRequest request) {
        AppExceptionBean exceptionBean = new AppExceptionBean(String.format("Something unexpected happened! " +
                "We'll be looking into it, but if you want to further discuss this specific problem, " +
                "please use the following identifier when contacting support: %s.", UUID.randomUUID()));
        log.error(exceptionBean.getMessage(), ex);
        return handleExceptionInternal(ex, exceptionBean, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }
}
