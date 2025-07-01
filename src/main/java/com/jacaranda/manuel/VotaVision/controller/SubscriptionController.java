package com.jacaranda.manuel.VotaVision.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jacaranda.manuel.VotaVision.dto.ApiResponse;
import com.jacaranda.manuel.VotaVision.dto.SubscriptionDto;
import com.jacaranda.manuel.VotaVision.dto.UserPreferencesDto;
import com.jacaranda.manuel.VotaVision.model.Subscription;
import com.jacaranda.manuel.VotaVision.model.User;
import com.jacaranda.manuel.VotaVision.repository.UserRepository;
import com.jacaranda.manuel.VotaVision.service.SubscriptionService;

@RestController
@RequestMapping("/api/subscriptions")
public class SubscriptionController {

	@Autowired
	private SubscriptionService subscriptionService;
	
	@Autowired
	private UserRepository userRepository;

	/**
	 * Suscribe al usuario autenticado a la categoría indicada.
	 */
	@PostMapping("/{categoryId}")
	public ResponseEntity<?> subscribe(@PathVariable Long categoryId) throws Exception {
		try {
			String userEmail = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			subscriptionService.subscribe(userEmail, categoryId);
			return ResponseEntity.ok(new ApiResponse("Suscripción realizada con éxito.", HttpStatus.OK.value()));
		} catch (Exception e) {
			throw e; // Se puede mejorar con un manejo más específico
		}
	}

	/**
	 * Cancela la suscripción del usuario autenticado a la categoría indicada.
	 */
	@DeleteMapping("/{categoryId}")
	public ResponseEntity<?> unsubscribe(@PathVariable Long categoryId) throws Exception {
		try {
			String userEmail = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			subscriptionService.unsubscribe(userEmail, categoryId);
			return ResponseEntity.ok(new ApiResponse("Suscripción eliminada con éxito.", HttpStatus.OK.value()));
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * Devuelve las suscripciones del usuario autenticado.
	 */
	@GetMapping
	public ResponseEntity<?> getMySubscriptions() throws Exception {
		try {
			String userEmail = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			return subscriptionService.getSubscriptions(userEmail);
			
		} catch (Exception e) {
			throw e;
		}
	}
	
	/**
	 * Devuelve las preferencias del usuario autenticado (ej. notificaciones).
	 */
	@GetMapping("/preferences")
	public ResponseEntity<?> getPreferences() throws Exception {
	    try {
	        String email = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	        UserPreferencesDto preferences = subscriptionService.getUserPreferences(email);
	        return ResponseEntity.ok(preferences);
	    } catch (Exception e) {
	        throw e;
	    }
	}

	
	@PutMapping("/preferences")
	public ResponseEntity<?> updatePreferences(@RequestBody UserPreferencesDto preferences) throws Exception {
	    try {
	        String email = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	        subscriptionService.updateUserPreferences(email, preferences);
	        return ResponseEntity.ok(new ApiResponse("Preferencias actualizadas correctamente.", HttpStatus.OK.value()));
	    } catch (Exception e) {
	        throw e;
	    }
	}

}
