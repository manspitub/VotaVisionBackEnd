package com.jacaranda.manuel.VotaVision.dto;

import java.util.List;

public class AnswerRequestDto {
	private Long questionId;
	private String text; // Para SHORT o LONG
	private List<Long> selectedOptionIds; // Para MULTIPLE_CHOICE

	public AnswerRequestDto(Long questionId, String text, List<Long> selectedOptionIds) {
		super();
		this.questionId = questionId;
		this.text = text;
		this.selectedOptionIds = selectedOptionIds;
	}

	public Long getQuestionId() {
		return questionId;
	}

	public void setQuestionId(Long questionId) {
		this.questionId = questionId;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public List<Long> getSelectedOptionIds() {
		return selectedOptionIds;
	}

	public void setSelectedOptionIds(List<Long> selectedOptionIds) {
		this.selectedOptionIds = selectedOptionIds;
	}

}
