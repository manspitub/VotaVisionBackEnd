package com.jacaranda.manuel.VotaVision.dto;

public class ReportRequest {
	private Long surveyId;
    private String reason;
    
    public ReportRequest() {
    	super();
    }
    
	public ReportRequest(Long surveyId, String reason) {
		super();
		this.surveyId = surveyId;
		this.reason = reason;
	}

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
