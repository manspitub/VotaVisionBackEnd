package com.jacaranda.manuel.VotaVision.dto;

import java.math.BigDecimal;
import java.util.Date;

public class SubmitSurveyResponseDto {

	private String message;
	private Double rewardAdded;
	private BigDecimal totalReward;
	private Date answeredAt;
	private SurveyDto nextRecommendedSurvey;

	public SubmitSurveyResponseDto() {
	}

	public SubmitSurveyResponseDto(String message, Double rewardAdded, BigDecimal totalReward, Date answeredAt,
			SurveyDto nextRecommendedSurvey) {
		this.message = message;
		this.rewardAdded = rewardAdded;
		this.totalReward = totalReward;
		this.answeredAt = answeredAt;
		this.nextRecommendedSurvey = nextRecommendedSurvey;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Double getRewardAdded() {
		return rewardAdded;
	}

	public void setRewardAdded(Double rewardAdded) {
		this.rewardAdded = rewardAdded;
	}

	public BigDecimal getTotalReward() {
		return totalReward;
	}

	public void setTotalReward(BigDecimal totalReward) {
		this.totalReward = totalReward;
	}

	public Date getAnsweredAt() {
		return answeredAt;
	}

	public void setAnsweredAt(Date answeredAt) {
		this.answeredAt = answeredAt;
	}

	public SurveyDto getNextRecommendedSurvey() {
		return nextRecommendedSurvey;
	}

	public void setNextRecommendedSurvey(SurveyDto nextRecommendedSurvey) {
		this.nextRecommendedSurvey = nextRecommendedSurvey;
	}
}
