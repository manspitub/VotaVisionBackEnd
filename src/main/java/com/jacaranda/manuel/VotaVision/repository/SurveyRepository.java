package com.jacaranda.manuel.VotaVision.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.jacaranda.manuel.VotaVision.model.Survey;

public interface SurveyRepository extends JpaRepository<Survey, Long> {

	// Encuestas activas (sin paginar)
	@Query("SELECT s FROM Survey s WHERE s.startDate < :now1 AND s.closeDate > :now2 "
			+ "AND (s.moderationDeleted = false OR s.moderationDeleted IS NULL)")
	List<Survey> findVisibleActive(@Param("now1") Date now1, @Param("now2") Date now2);
	
	// 🔥 Encuestas para administración filtradas por título
	@Query("SELECT s FROM Survey s WHERE LOWER(s.title) LIKE LOWER(CONCAT('%', :title, '%')) "
			+ "AND (s.moderationDeleted = false OR s.moderationDeleted IS NULL)")
	List<Survey> findVisibleByTitleContainingIgnoreCase(@Param("title") String title);
	
	// Encuestas activas con búsqueda por título (sin paginar)
	@Query("SELECT s FROM Survey s WHERE LOWER(s.title) LIKE LOWER(CONCAT('%', :title, '%')) "
			+ "AND s.startDate < :now1 AND s.closeDate > :now2 "
			+ "AND (s.moderationDeleted = false OR s.moderationDeleted IS NULL)")
	List<Survey> findVisibleActiveByTitleContainingIgnoreCase(@Param("title") String title,
			@Param("now1") Date now1, @Param("now2") Date now2);

	// Encuestas por creador (sin paginar)
	@Query("SELECT s FROM Survey s WHERE s.creator.email = :creatorEmail "
			+ "AND (s.moderationDeleted = false OR s.moderationDeleted IS NULL)")
	List<Survey> findVisibleByCreatorEmail(@Param("creatorEmail") String creatorEmail);

	// Encuestas por creador con búsqueda por título (sin paginar)
	@Query("SELECT s FROM Survey s WHERE s.creator.email = :creatorEmail "
			+ "AND LOWER(s.title) LIKE LOWER(CONCAT('%', :title, '%')) "
			+ "AND (s.moderationDeleted = false OR s.moderationDeleted IS NULL)")
	List<Survey> findVisibleByCreatorEmailAndTitleContainingIgnoreCase(@Param("creatorEmail") String creatorEmail,
			@Param("title") String title);
	
	@Query("SELECT DISTINCT s FROM Survey s JOIN s.participations p WHERE p.user.email = :email "
			+ "AND (s.moderationDeleted = false OR s.moderationDeleted IS NULL)")
	List<Survey> findVisibleDistinctByParticipationsUserEmail(@Param("email") String email);

	@Query("SELECT s FROM Survey s WHERE (s.moderationDeleted = false OR s.moderationDeleted IS NULL)")
	List<Survey> findAllVisible();

	
}
