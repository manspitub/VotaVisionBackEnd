package com.jacaranda.manuel.VotaVision.dto;

import java.util.Date;
import java.util.List;

public class AnsweredSurveyDto extends SurveyDto{
	 private Date answeredAt;
	    private List<AnsweredQuestionDto> userAnswers;

	    public AnsweredSurveyDto(SurveyDto baseDto) {
	        super(
	            baseDto.getId(),
	            baseDto.getTitle(),
	            baseDto.getDescription(),
	            baseDto.getStartDate(),
	            baseDto.getCloseDate(),
	            baseDto.getReward(),
	            baseDto.getCreator()
	        );
	        setCategory(baseDto.getCategory());
	        setQuestions(baseDto.getQuestions());
	        setCanRespond(baseDto.isCanRespond());
	        setResponsesCount(baseDto.getResponsesCount());
	    }

	    public Date getAnsweredAt() {
	        return answeredAt;
	    }

	    public void setAnsweredAt(Date answeredAt) {
	        this.answeredAt = answeredAt;
	    }

	    public List<AnsweredQuestionDto> getUserAnswers() {
	        return userAnswers;
	    }

	    public void setUserAnswers(List<AnsweredQuestionDto> userAnswers) {
	        this.userAnswers = userAnswers;
	    }
}
