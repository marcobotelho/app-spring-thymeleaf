package com.projeto.appspringthymeleaf.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import com.projeto.appspringthymeleaf.record.AlertRecord;

import jakarta.validation.ConstraintViolationException;

@ControllerAdvice
public class ErroController {

	@ExceptionHandler(Exception.class)
	public ModelAndView handleException(Exception ex) {
		AlertRecord alertRecord = new AlertRecord("danger", "Erro inesperado.", ex.getMessage());

		ModelAndView modelAndView = new ModelAndView("erro");
		modelAndView.addObject("alertRecord", alertRecord);
		return modelAndView;
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ModelAndView handleValidationException(MethodArgumentNotValidException ex) {

		StringBuilder sbMessages = new StringBuilder();
		sbMessages.append("<ul>");
		ex.getBindingResult().getFieldErrors().stream().forEach(error -> sbMessages.append("<li>").append("Arqumento '")
				.append(error.getField()).append("' inválido").append("</li>"));
		sbMessages.append("</ul>");

		AlertRecord alertRecord = new AlertRecord("danger", "Erro de arqumento inválido.", sbMessages.toString());

		ModelAndView modelAndView = new ModelAndView("erro");
		modelAndView.addObject("alertRecord", alertRecord);
		return modelAndView;
	}

	@ExceptionHandler(ConstraintViolationException.class)
	public ModelAndView handleConstraintViolationException(ConstraintViolationException ex) {

		StringBuilder sbMessages = new StringBuilder();
		sbMessages.append("<ul>");
		ex.getConstraintViolations().stream()
				.forEach(violation -> sbMessages.append("<li>").append(violation.getMessage()).append("</li>"));
		sbMessages.append("</ul>");

		AlertRecord alertRecord = new AlertRecord("danger", "Erro de validação.", sbMessages.toString());

		ModelAndView modelAndView = new ModelAndView("erro");
		modelAndView.addObject("alertRecord", alertRecord);
		return modelAndView;
	}

	@ExceptionHandler({ NoResourceFoundException.class, NoHandlerFoundException.class })
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public ModelAndView handleNoResourceFoundException(NoResourceFoundException ex) {
		StringBuilder sbMessages = new StringBuilder();
		sbMessages.append("<ul>").append("<li>").append(ex.getResourcePath()).append("</li>").append("</ul>");

		AlertRecord alertRecord = new AlertRecord("danger", "Erro de página não encontrada.", sbMessages.toString());

		ModelAndView modelAndView = new ModelAndView("erro");
		modelAndView.addObject("alertRecord", alertRecord);
		return modelAndView;
	}
}
