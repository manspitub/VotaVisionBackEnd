package com.jacaranda.manuel.VotaVision.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.jacaranda.manuel.VotaVision.model.Rol;
import com.jacaranda.manuel.VotaVision.model.User;

public interface UserRepository extends JpaRepository<User, Long> {

	Optional<User> findFirstByEmail(String email);
	
	Page<User> findByEmailContainingIgnoreCaseOrNameContainingIgnoreCase(String email, String name, Pageable pageable);
	
	
	Page<User> findByRole(Rol role, Pageable pageable);
	List<User> findByRole(Rol role);
	long countByRole(Rol role);

	Page<User> findByRoleAndEmailContainingIgnoreCaseOrRoleAndNameContainingIgnoreCase(
		    Rol role1,
		    String email,
		    Rol role2,
		    String name,
		    Pageable pageable
		);



}
