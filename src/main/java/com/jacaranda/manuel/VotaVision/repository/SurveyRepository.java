package com.jacaranda.manuel.VotaVision.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.jacaranda.manuel.VotaVision.model.Survey;

public interface SurveyRepository extends JpaRepository<Survey, Long> {

	// Encuestas activas (sin paginar)
	List<Survey> findByStartDateBeforeAndCloseDateAfter(Date now1, Date now2);
	
	// 🔥 Encuestas para administración filtradas por título
	List<Survey> findByTitleContainingIgnoreCase(String title);
	
	// Encuestas activas con búsqueda por título (sin paginar)
	List<Survey> findByTitleContainingIgnoreCaseAndStartDateBeforeAndCloseDateAfter(String title, Date now1, Date now2);

	// Encuestas por creador (sin paginar)
	List<Survey> findByCreatorEmail(String creatorEmail);

	// Encuestas por creador con búsqueda por título (sin paginar)
	List<Survey> findByCreatorEmailAndTitleContainingIgnoreCase(String creatorEmail, String title);
	
	@Query("SELECT DISTINCT s FROM Survey s JOIN s.participations p WHERE p.user.email = :email")
	List<Survey> findDistinctByParticipationsUserEmail(@Param("email") String email);

	
}
