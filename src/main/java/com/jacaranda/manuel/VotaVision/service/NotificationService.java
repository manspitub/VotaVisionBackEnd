package com.jacaranda.manuel.VotaVision.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.jacaranda.manuel.VotaVision.dto.NotificationDto;
import com.jacaranda.manuel.VotaVision.dto.NotificationDtoConverter;
import com.jacaranda.manuel.VotaVision.model.Notification;
import com.jacaranda.manuel.VotaVision.model.User;
import com.jacaranda.manuel.VotaVision.repository.NotificationRepository;
import com.jacaranda.manuel.VotaVision.repository.UserRepository;

@Service
public class NotificationService {
	
	@Autowired
	private NotificationRepository notificationRepository;
	
	@Autowired
	private UserRepository userRepository;

	public List<NotificationDto> getNotificationsForUser(String email) {
		User user = userRepository.findFirstByEmail(email)
				.orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

		return notificationRepository.findByUserAndReadFalseOrderByCreatedAtDesc(user).stream()
				.map(NotificationDtoConverter::toDto)
				.collect(Collectors.toList());
	}

	
	public void markAllAsRead(String userEmail) {
	    List<Notification> userNotifications = notificationRepository.findByUserEmail(userEmail);
	    for (Notification notification : userNotifications) {
	        if (!notification.isRead()) {
	            notification.setRead(true);
	        }
	    }
	    notificationRepository.saveAll(userNotifications);
	}
	
	public void markAsRead(String userEmail, Long notificationId) {
	    Notification notification = notificationRepository.findByIdAndUserEmail(notificationId, userEmail)
	        .orElseThrow(() -> new UsernameNotFoundException("Notificación no encontrada"));
	    notification.setRead(true);
	    notificationRepository.save(notification);
	}


}
