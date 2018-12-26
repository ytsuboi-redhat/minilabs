package com.redhat.labsjp.sample.todobackend.controller.exceptionhandler;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class RestControllerExceptionHandler extends ResponseEntityExceptionHandler {

	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException e,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		return handleExceptionInternal(e, new ErrorResponse("E001", "input arguments not valid!"), null,
				HttpStatus.BAD_REQUEST, request);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<Object> handleInternalServerError(Exception e, WebRequest request) {
		return handleExceptionInternal(e, new ErrorResponse("E999", "Exception occured!!"), null,
				HttpStatus.INTERNAL_SERVER_ERROR, request);
	}

}
