package com.jacaranda.manuel.VotaVision.dto;

import java.util.List;

import com.jacaranda.manuel.VotaVision.model.QuestionType;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class CreateQuestionDto {
	@NotBlank(message = "El texto de la pregunta es obligatorio.")
	@Size(min = 2, message = "El texto debe tener al menos 2 caracteres.")
	private String text;

	@NotNull(message = "El tipo de pregunta es obligatorio.")
	private QuestionType type;

	private boolean required;

	// Para validar si MULTIPLE_CHOICE tiene opciones
	@Valid
	private List<@NotBlank(message = "La opción no puede estar vacía.") String> options;

	public CreateQuestionDto(String text, QuestionType type, boolean required, List<String> options) {
		super();
		this.text = text;
		this.type = type;
		this.required = required;
		this.options = options;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public QuestionType getType() {
		return type;
	}

	public void setType(QuestionType type) {
		this.type = type;
	}

	public boolean isRequired() {
		return required;
	}

	public void setRequired(boolean required) {
		this.required = required;
	}

	public List<String> getOptions() {
		return options;
	}

	public void setOptions(List<String> options) {
		this.options = options;
	}

	// Getters y setters

}
