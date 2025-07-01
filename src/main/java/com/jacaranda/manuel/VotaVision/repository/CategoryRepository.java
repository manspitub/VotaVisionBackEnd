package com.jacaranda.manuel.VotaVision.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.jacaranda.manuel.VotaVision.model.Category;

import jakarta.transaction.Transactional;

@Transactional
public interface CategoryRepository extends JpaRepository<Category, Long> {
	
	Page<Category> findByNameContainingIgnoreCase(String name, Pageable pageable);


}
