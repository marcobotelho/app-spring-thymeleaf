package com.projeto.appspringthymeleaf.controller;

import java.util.Map;
import java.util.stream.Collectors;

import org.hibernate.JDBCException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import com.projeto.appspringthymeleaf.record.AlertRecord;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;

@ControllerAdvice
public class GenericErrorController {

	private String getRequestView(HttpServletRequest request) {
		String view = request.getRequestURI().substring(request.getContextPath().length());
		return view;
	}

	private RedirectAttributes addAllAttributesFromRequest(RedirectAttributes redirectAttributes,
			HttpServletRequest request) {
		Map<String, String[]> parametros = request.getParameterMap();
		parametros.forEach((chave, valores) -> redirectAttributes.addAttribute(chave, valores[0]));
		return redirectAttributes;
	}

	// Método para extrair a chave duplicada da mensagem de erro
	private String extractDuplicateKey(String errorMessage) {
		int startIndex = errorMessage.indexOf("Key (");
		int endIndex = errorMessage.indexOf(")");
		if (errorMessage.contains("duplicate key") && startIndex != -1 && endIndex != -1) {
			return errorMessage.substring(startIndex + 5, endIndex);
		}
		return "";
	}

	private AlertRecord createAlertError(String message) {
		return new AlertRecord("danger", "Erro!", message);
	}

	@ExceptionHandler({ Exception.class, Throwable.class, RuntimeException.class })
	public String handleException(Exception ex,
			HttpServletRequest request, RedirectAttributes redirectAttributes) {
		redirectAttributes = addAllAttributesFromRequest(redirectAttributes, request);
		String errorMessage = ex.getMessage();
		redirectAttributes.addFlashAttribute("alertRecord",
				createAlertError(errorMessage));
		return "redirect:" + getRequestView(request);
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public String handleValidationException(MethodArgumentNotValidException ex,
			HttpServletRequest request, RedirectAttributes redirectAttributes) {
		redirectAttributes = addAllAttributesFromRequest(redirectAttributes, request);
		String errorMessage = ex.getBindingResult().getAllErrors().stream()
				.map(ObjectError::getDefaultMessage)
				.collect(Collectors.joining("; "))
				.concat(".");
		redirectAttributes.addFlashAttribute("alertRecord",
				createAlertError(errorMessage));
		return "redirect:" + getRequestView(request);
	}

	@ExceptionHandler(ConstraintViolationException.class)
	public String handleConstraintViolationException(ConstraintViolationException ex,
			HttpServletRequest request, RedirectAttributes redirectAttributes) {
		redirectAttributes = addAllAttributesFromRequest(redirectAttributes, request);
		String errorMessage = ex.getConstraintViolations().stream()
				.map(ConstraintViolation::getMessage)
				.collect(Collectors.joining("; "))
				.concat(".");
		redirectAttributes.addFlashAttribute("alertRecord",
				createAlertError(errorMessage));
		return "redirect:" + getRequestView(request);
	}

	@ExceptionHandler({ NoResourceFoundException.class, NoHandlerFoundException.class })
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public String handleNoResourceFoundException(NoResourceFoundException ex, HttpServletRequest request,
			Model model, RedirectAttributes redirectAttributes) {
		String errorMessage = "Caminho inválido '" + request.getRequestURL().toString() + "'.";
		model.addAttribute("alertRecord",
				createAlertError(errorMessage));
		return "erro";
	}

	@ExceptionHandler({ DataIntegrityViolationException.class, JDBCException.class })
	public String handleDataIntegrityViolationException(DataIntegrityViolationException ex,
			RedirectAttributes redirectAttributes, HttpServletRequest request) {
		redirectAttributes = addAllAttributesFromRequest(redirectAttributes, request);
		String errorMessage = "Chave já existente '" + extractDuplicateKey(ex.getMessage()) + "'.";
		redirectAttributes.addFlashAttribute("alertRecord",
				createAlertError(errorMessage));
		return "redirect:" + getRequestView(request);
	}
}
