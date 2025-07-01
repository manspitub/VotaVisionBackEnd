package com.jacaranda.manuel.VotaVision.dto;

import java.util.List;

public class AnsweredQuestionDto {
	private Long questionId;
    private List<String> selectedAnswer;

    public AnsweredQuestionDto(Long questionText, List<String> selectedOptions) {
        this.questionId = questionText;
        this.selectedAnswer = selectedOptions;
    }

	public Long getQuestionId() {
		return questionId;
	}

	public void setQuestionId(Long questionText) {
		this.questionId = questionText;
	}

	public List<String> getSelectedAnswers() {
		return selectedAnswer;
	}

	public void setSelectedAnswers(List<String> selectedOptions) {
		this.selectedAnswer = selectedOptions;
	}
    
    
}
