package com.se.details.exception;

import com.se.details.resource.ApiErrorResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.convert.ConversionFailedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionHandlingController
{
	private final Logger log = LoggerFactory.getLogger(ExceptionHandlingController.class);

	@ExceptionHandler(InvalidRequestParameterException.class)
	public ResponseEntity<ApiErrorResource> invalidRequestParameterException(final InvalidRequestParameterException e)
	{
		log.error("Error: {} / {}", e.getErrorNumber(), e.getMessage());
		ApiError apiError = new ApiError(e.getErrorNumber(), e.getMessage(), e.getErrorsMap());
		return new ResponseEntity<>(new ApiErrorResource(apiError), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(ConversionFailedException.class)
	public ResponseEntity<String> handleEnumConversionExample(RuntimeException ex)
	{
		return new ResponseEntity<>("Conversion Error", HttpStatus.BAD_REQUEST);
	}

}
