package com.jacaranda.manuel.VotaVision.dto;

import java.time.LocalDateTime;

import com.jacaranda.manuel.VotaVision.model.Notification;


public class NotificationDto {
    private Long id;
    private String message;
    private boolean read;
    private LocalDateTime createdAt;
    
    
    public NotificationDto() {
    	super();
    }
    
	public NotificationDto(Long id, String message, boolean read, LocalDateTime createdAt) {
		super();
		this.id = id;
		this.message = message;
		this.read = read;
		this.createdAt = createdAt;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public boolean isRead() {
		return read;
	}
	public void setRead(boolean read) {
		this.read = read;
	}
	public LocalDateTime getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

    
}
