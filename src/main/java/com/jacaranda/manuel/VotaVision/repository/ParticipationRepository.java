package com.jacaranda.manuel.VotaVision.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jacaranda.manuel.VotaVision.model.Participation;
import com.jacaranda.manuel.VotaVision.model.ParticipationPK;

public interface ParticipationRepository extends JpaRepository<Participation, ParticipationPK> {
   
	Optional<Participation> findBySurveyIdAndUserEmail(Long surveyId, String userEmail);

	boolean existsBySurveyIdAndUserEmail(Long surveyId, String email);
	
    List<Participation> findAllByUserEmail(String userEmail); // (para lista de encuestas respondidas)

}
