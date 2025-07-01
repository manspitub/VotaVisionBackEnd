package com.jacaranda.manuel.VotaVision.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.jacaranda.manuel.VotaVision.model.Notification;
import com.jacaranda.manuel.VotaVision.model.User;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
	List<Notification> findByUser(User user);

	List<Notification> findByUserAndReadFalse(User user);

	List<Notification> findByUserOrderByCreatedAtDesc(User user);

	List<Notification> findByUserEmail(String email);

	Optional<Notification> findByIdAndUserEmail(Long id, String userEmail);

	List<Notification> findByUserAndReadFalseOrderByCreatedAtDesc(User user);

}
