package com.jacaranda.manuel.VotaVision.dto;

import com.jacaranda.manuel.VotaVision.model.Notification;

public class NotificationDtoConverter {

	 public static NotificationDto toDto(Notification notification) {
	        if (notification == null) {
	            return null;
	        }
	        NotificationDto dto = new NotificationDto();
	        dto.setId(notification.getId());
	        dto.setMessage(notification.getMessage());
	        dto.setRead(notification.isRead());
	        dto.setCreatedAt(notification.getCreatedAt());
	        return dto;
	    }

	    
}
