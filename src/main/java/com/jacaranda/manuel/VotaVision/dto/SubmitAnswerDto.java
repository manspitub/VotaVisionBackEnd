package com.jacaranda.manuel.VotaVision.dto;

import java.util.List;

public class SubmitAnswerDto {
    private Long questionId;
    private String text;
    private List<Long> optionIds; // para MULTIPLE_CHOICE
    
	public SubmitAnswerDto(Long questionId, String text, List<Long> optionIds) {
		super();
		this.questionId = questionId;
		this.text = text;
		this.optionIds = optionIds;
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

	public List<Long> getOptionIds() {
		return optionIds;
	}

	public void setOptionIds(List<Long> optionIds) {
		this.optionIds = optionIds;
	}
    
    
    // getters y setters
}
