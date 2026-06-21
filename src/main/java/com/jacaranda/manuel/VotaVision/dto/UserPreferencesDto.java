package com.jacaranda.manuel.VotaVision.dto;

public class UserPreferencesDto {
    private Boolean notificationsEnabled;
    private Boolean recommendationsEnabled;
    
    public UserPreferencesDto() {
    	super();
    }

	public UserPreferencesDto(Boolean notificationsEnabled) {
		super();
		this.notificationsEnabled = notificationsEnabled;
		this.recommendationsEnabled = true;
	}

	public UserPreferencesDto(Boolean notificationsEnabled, Boolean recommendationsEnabled) {
		super();
		this.notificationsEnabled = notificationsEnabled;
		this.recommendationsEnabled = recommendationsEnabled;
	}

	public Boolean isNotificationsEnabled() {
		return notificationsEnabled;
	}

	public void setNotificationsEnabled(Boolean notificationsEnabled) {
		this.notificationsEnabled = notificationsEnabled;
	}

	public Boolean isRecommendationsEnabled() {
		return recommendationsEnabled;
	}

	public void setRecommendationsEnabled(Boolean recommendationsEnabled) {
		this.recommendationsEnabled = recommendationsEnabled;
	}
    
    
    
}
