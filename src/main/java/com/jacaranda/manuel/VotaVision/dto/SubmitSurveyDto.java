package com.jacaranda.manuel.VotaVision.dto;

import java.util.List;

public class SubmitSurveyDto {
	private Long surveyId;
    private List<SubmitAnswerDto> answers;
	public SubmitSurveyDto(Long surveyId, List<SubmitAnswerDto> answers) {
		super();
		this.surveyId = surveyId;
		this.answers = answers;
	}
	public Long getSurveyId() {
		return surveyId;
	}
	public void setSurveyId(Long surveyId) {
		this.surveyId = surveyId;
	}
	public List<SubmitAnswerDto> getAnswers() {
		return answers;
	}
	public void setAnswers(List<SubmitAnswerDto> answers) {
		this.answers = answers;
	}
    
    
    // getters y setters
}
