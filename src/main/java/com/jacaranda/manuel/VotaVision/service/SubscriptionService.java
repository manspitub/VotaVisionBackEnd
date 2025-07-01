package com.jacaranda.manuel.VotaVision.service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.jacaranda.manuel.VotaVision.dto.ApiResponse;
import com.jacaranda.manuel.VotaVision.dto.SubscriptionDto;
import com.jacaranda.manuel.VotaVision.dto.UserPreferencesDto;
import com.jacaranda.manuel.VotaVision.exception.CategoryNotFoundException;
import com.jacaranda.manuel.VotaVision.exception.UserNotFoundException;
import com.jacaranda.manuel.VotaVision.model.Category;
import com.jacaranda.manuel.VotaVision.model.Subscription;
import com.jacaranda.manuel.VotaVision.model.User;
import com.jacaranda.manuel.VotaVision.repository.CategoryRepository;
import com.jacaranda.manuel.VotaVision.repository.SubscriptionRepository;
import com.jacaranda.manuel.VotaVision.repository.UserRepository;

import jakarta.transaction.Transactional;

@Service

public class SubscriptionService {

	@Autowired
	private SubscriptionRepository subscriptionRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private CategoryRepository categoryRepository;

	/**
	 * Suscribe un usuario a una categoría.
	 * 
	 * @param userEmail  email del usuario
	 * @param categoryId id de la categoría
	 * @return ApiResponse con resultado
	 */
	public ApiResponse subscribe(String userEmail, Long categoryId) {
		User user = userRepository.findFirstByEmail(userEmail)
				.orElseThrow(() -> new UserNotFoundException("Usuario no encontrado con email: " + userEmail));

		Category category = categoryRepository.findById(categoryId)
				.orElseThrow(() -> new CategoryNotFoundException("Categoría no encontrada con id: " + categoryId));

		boolean alreadySubscribed = subscriptionRepository.existsByUserAndCategory(user, category);
		if (alreadySubscribed) {
			return new ApiResponse("El usuario ya está suscrito a esta categoría", 409); // Conflict
		}

		Subscription subscription = new Subscription(user, category, new Date());
		subscriptionRepository.save(subscription);

		return new ApiResponse("Suscripción realizada con éxito", 201); // Created
	}

	/**
	 * Elimina la suscripción de un usuario a una categoría.
	 * 
	 * @param userEmail  email del usuario
	 * @param categoryId id de la categoría
	 * @return ApiResponse con resultado
	 */
	@Transactional
	public ApiResponse unsubscribe(String userEmail, Long categoryId) {
		User user = userRepository.findFirstByEmail(userEmail)
				.orElseThrow(() -> new UserNotFoundException("Usuario no encontrado con email: " + userEmail));

		Category category = categoryRepository.findById(categoryId)
				.orElseThrow(() -> new CategoryNotFoundException("Categoría no encontrada con id: " + categoryId));

		boolean existed = subscriptionRepository.existsByUserAndCategory(user, category);
		if (!existed) {
			System.out.println("Entreee");
			throw new CategoryNotFoundException("No estás suscrito a " + category.getName());
		}

		subscriptionRepository.deleteByUserAndCategory(user, category);

		return new ApiResponse("Suscripción eliminada con éxito", 200);
	}

	/**
	 * Obtiene las suscripciones del usuario.
	 * 
	 * @param userEmail email del usuario
	 * @return lista de SubscriptionDto
	 */
	public ResponseEntity<List<SubscriptionDto>> getSubscriptions(String userEmail) {
		User user = userRepository.findFirstByEmail(userEmail)
				.orElseThrow(() -> new UserNotFoundException("Usuario no encontrado con email: " + userEmail));

		List<Subscription> subscriptions = subscriptionRepository.findByUser(user);

		List<SubscriptionDto> dtos = subscriptions.stream().map(this::mapToDto).collect(Collectors.toList());

		return ResponseEntity.ok(dtos); // Siempre devuelve [] si no hay nada
	}

	public UserPreferencesDto getUserPreferences(String email) {
		User user = userRepository.findFirstByEmail(email)
				.orElseThrow(() -> new RuntimeException("Usuario no encontrado con email: " + email));

		UserPreferencesDto dto = new UserPreferencesDto();
		dto.setNotificationsEnabled(user.isNotificationsEnabled());
		return dto;
	}

	public void updateUserPreferences(String email, UserPreferencesDto preferences) {
		User user = userRepository.findFirstByEmail(email)
				.orElseThrow(() -> new UserNotFoundException("Usuario no encontrado"));
		user.setNotificationsEnabled(preferences.isNotificationsEnabled());
		userRepository.save(user);
	}

	// Conversión de entidad a DTO (podrías ajustarlo según los campos que
	// necesites)
	private SubscriptionDto mapToDto(Subscription subscription) {
		SubscriptionDto dto = new SubscriptionDto();
		dto.setCategoryId(subscription.getCategory().getId());
		dto.setCategoryName(subscription.getCategory().getName());
		dto.setSubscribedAt(subscription.getSubscriptionDate());
		return dto;
	}

}
