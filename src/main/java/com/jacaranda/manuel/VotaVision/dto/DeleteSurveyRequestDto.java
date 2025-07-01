package com.jacaranda.manuel.VotaVision.dto;

public class DeleteSurveyRequestDto {
    private Long surveyId;
    private String reason;

    public Long getSurveyId() {
        return surveyId;
    }

    public void setSurveyId(Long surveyId) {
        this.surveyId = surveyId;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}