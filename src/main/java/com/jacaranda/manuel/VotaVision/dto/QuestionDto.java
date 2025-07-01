package com.jacaranda.manuel.VotaVision.dto;

import java.util.List;

import com.jacaranda.manuel.VotaVision.model.QuestionType;

public class QuestionDto {
	private Long id;
	private String text;
	private QuestionType type;
	private boolean required;
	private List<OptionDto> options; // Solo para tipo MULTIPLE_CHOICE

	public QuestionDto() {
	}

	public QuestionDto(Long id, String text, QuestionType type, boolean required, List<OptionDto> options) {
		this.id = id;
		this.text = text;
		this.type = type;
		this.required = required;
		this.options = options;
	}

	// Getters y setters
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public List<OptionDto> getOptions() {
		return options;
	}

	public void setOptions(List<OptionDto> options) {
		this.options = options;
	}
}
