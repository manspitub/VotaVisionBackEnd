package com.jacaranda.manuel.VotaVision.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.jacaranda.manuel.VotaVision.model.Survey;
import com.jacaranda.manuel.VotaVision.model.SurveyReport;
import com.jacaranda.manuel.VotaVision.model.SurveyReportStatus;
import com.jacaranda.manuel.VotaVision.model.User;

public interface SurveyReportRepository extends JpaRepository<SurveyReport, Long> {

	boolean existsByReporterAndSurveyAndNormalizedReasonAndStatus(User reporter, Survey survey, String normalizedReason,
			SurveyReportStatus status);

	Page<SurveyReport> findByStatus(SurveyReportStatus status, Pageable pageable);

	List<SurveyReport> findBySurvey(Survey survey);

	List<SurveyReport> findBySurveyAndStatus(Survey survey, SurveyReportStatus status);
}
