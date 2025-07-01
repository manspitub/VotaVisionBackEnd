package com.jacaranda.manuel.VotaVision.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jacaranda.manuel.VotaVision.model.Category;
import com.jacaranda.manuel.VotaVision.model.Subscription;
import com.jacaranda.manuel.VotaVision.model.SubscriptionPK;
import com.jacaranda.manuel.VotaVision.model.User;

public interface SubscriptionRepository extends JpaRepository<Subscription, SubscriptionPK> {
	List<Subscription> findByUser(User user);

	List<Subscription> findByCategory(Category category);

	Optional<Subscription> findByUserAndCategory(User user, Category category);

	boolean existsByUserAndCategory(User user, Category category);

	void deleteByUserAndCategory(User user, Category category);
}
