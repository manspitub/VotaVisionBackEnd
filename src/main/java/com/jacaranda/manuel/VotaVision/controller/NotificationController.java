package com.jacaranda.manuel.VotaVision.controller;

import java.util.List;

import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.jacaranda.manuel.VotaVision.dto.NotificationDto;
import com.jacaranda.manuel.VotaVision.service.NotificationService;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {
    
    @Autowired
    private NotificationService notificationService;

    @GetMapping
    public ResponseEntity<List<NotificationDto>> getUserNotifications() {
        String userEmail = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<NotificationDto> notifications = notificationService.getNotificationsForUser(userEmail);
        return ResponseEntity.ok(notifications);
    }
    
    
    // Nuevo endpoint para marcar una notificación individual como leída
    @PutMapping("/{id}/mark-as-read")
    @ResponseStatus(HttpStatus.OK)
    public void markNotificationAsRead(@PathVariable Long id) {
        String userEmail = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        notificationService.markAsRead(userEmail, id);
    }
}

