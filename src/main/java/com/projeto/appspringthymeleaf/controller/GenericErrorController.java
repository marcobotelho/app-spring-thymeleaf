package com.projeto.appspringthymeleaf.controller;

import java.util.Collection;
import java.util.Set;

import org.springframework.http.HttpStatus;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import com.projeto.appspringthymeleaf.record.AlertRecord;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;

@ControllerAdvice
public class GenericErrorController {

	private static final String VIEW_NAME = "erro";
	private static final String DEFAULT_ERROR_MESSAGE = "Erro inesperado.";
	private static final String ARGUMENT_ERROR_MESSAGE = "Erro de argumento inválido.";
	private static final String VALIDATION_ERROR_MESSAGE = "Erro de validação.";
	private static final String NOT_FOUND_ERROR_MESSAGE = "Erro de página não encontrada.";

	private AlertRecord createAlertRecord(String type, String title, String message) {
		return new AlertRecord(type, title, message);
	}

	private String buildErrorMessage(Collection<? extends ObjectError> errors) {
		StringBuilder sbMessages = new StringBuilder("<ul>");
		errors.forEach(error -> sbMessages.append("<li>").append(error.getDefaultMessage()).append("</li>"));
		sbMessages.append("</ul>");
		return sbMessages.toString();
	}

	private String buildErrorMessageForConstraintViolations(Set<ConstraintViolation<?>> violations) {
		StringBuilder sbMessages = new StringBuilder("<ul>");
		violations.forEach(violation -> sbMessages.append("<li>").append(violation.getMessage()).append("</li>"));
		sbMessages.append("</ul>");
		return sbMessages.toString();
	}

	@ExceptionHandler(Exception.class)
	public ModelAndView handleException(Exception ex) {
		AlertRecord alertRecord = createAlertRecord("danger", DEFAULT_ERROR_MESSAGE, ex.getMessage());
		return getModelAndView(alertRecord);
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ModelAndView handleValidationException(MethodArgumentNotValidException ex) {
		String errorMessage = buildErrorMessage(ex.getBindingResult().getAllErrors());
		AlertRecord alertRecord = createAlertRecord("danger", ARGUMENT_ERROR_MESSAGE, errorMessage);
		return getModelAndView(alertRecord);
	}

	@ExceptionHandler(ConstraintViolationException.class)
	public ModelAndView handleConstraintViolationException(ConstraintViolationException ex) {
		String errorMessage = buildErrorMessageForConstraintViolations(ex.getConstraintViolations());
		AlertRecord alertRecord = createAlertRecord("danger", VALIDATION_ERROR_MESSAGE, errorMessage);
		return getModelAndView(alertRecord);
	}

	@ExceptionHandler({ NoResourceFoundException.class, NoHandlerFoundException.class })
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public ModelAndView handleNoResourceFoundException(NoResourceFoundException ex, HttpServletRequest request) {
		String errorMessage = "<ul><li>" + request.getRequestURL().toString() + "</li></ul>";
		AlertRecord alertRecord = createAlertRecord("danger", NOT_FOUND_ERROR_MESSAGE, errorMessage);
		return getModelAndView(alertRecord);
	}

	private ModelAndView getModelAndView(AlertRecord alertRecord) {
		ModelAndView modelAndView = new ModelAndView("erro");
		modelAndView.addObject("alertRecord", alertRecord);
		return modelAndView;
	}
}
