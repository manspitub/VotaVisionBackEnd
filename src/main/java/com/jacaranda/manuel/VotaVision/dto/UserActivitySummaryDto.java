package com.jacaranda.manuel.VotaVision.dto;

import java.math.BigDecimal;

public class UserActivitySummaryDto {

	private BigDecimal rewardBalance;
	private int answeredSurveys;
	private int availableSurveys;
	private int recommendedSurveys;
	private SurveyDto bestRecommendedSurvey;

	public UserActivitySummaryDto() {
	}

	public UserActivitySummaryDto(BigDecimal rewardBalance, int answeredSurveys, int availableSurveys,
			int recommendedSurveys, SurveyDto bestRecommendedSurvey) {
		this.rewardBalance = rewardBalance;
		this.answeredSurveys = answeredSurveys;
		this.availableSurveys = availableSurveys;
		this.recommendedSurveys = recommendedSurveys;
		this.bestRecommendedSurvey = bestRecommendedSurvey;
	}

	public BigDecimal getRewardBalance() {
		return rewardBalance;
	}

	public void setRewardBalance(BigDecimal rewardBalance) {
		this.rewardBalance = rewardBalance;
	}

	public int getAnsweredSurveys() {
		return answeredSurveys;
	}

	public void setAnsweredSurveys(int answeredSurveys) {
		this.answeredSurveys = answeredSurveys;
	}

	public int getAvailableSurveys() {
		return availableSurveys;
	}

	public void setAvailableSurveys(int availableSurveys) {
		this.availableSurveys = availableSurveys;
	}

	public int getRecommendedSurveys() {
		return recommendedSurveys;
	}

	public void setRecommendedSurveys(int recommendedSurveys) {
		this.recommendedSurveys = recommendedSurveys;
	}

	public SurveyDto getBestRecommendedSurvey() {
		return bestRecommendedSurvey;
	}

	public void setBestRecommendedSurvey(SurveyDto bestRecommendedSurvey) {
		this.bestRecommendedSurvey = bestRecommendedSurvey;
	}
}
