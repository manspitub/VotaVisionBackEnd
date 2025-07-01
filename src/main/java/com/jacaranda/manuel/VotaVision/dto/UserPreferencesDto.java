package com.jacaranda.manuel.VotaVision.dto;

public class UserPreferencesDto {
    private boolean notificationsEnabled;
    
    public UserPreferencesDto() {
    	super();
    }

	public UserPreferencesDto(boolean notificationsEnabled) {
		super();
		this.notificationsEnabled = notificationsEnabled;
	}

	public boolean isNotificationsEnabled() {
		return notificationsEnabled;
	}

	public void setNotificationsEnabled(boolean notificationsEnabled) {
		this.notificationsEnabled = notificationsEnabled;
	}
    
    
    
}
