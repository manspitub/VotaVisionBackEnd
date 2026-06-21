package com.jacaranda.manuel.VotaVision.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jacaranda.manuel.VotaVision.dto.SurveyReportDto;
import com.jacaranda.manuel.VotaVision.exception.SurveyMalformedException;
import com.jacaranda.manuel.VotaVision.exception.UnauthorizedException;
import com.jacaranda.manuel.VotaVision.exception.UserNotFoundException;
import com.jacaranda.manuel.VotaVision.model.Rol;
import com.jacaranda.manuel.VotaVision.model.Survey;
import com.jacaranda.manuel.VotaVision.model.SurveyReport;
import com.jacaranda.manuel.VotaVision.model.SurveyReportStatus;
import com.jacaranda.manuel.VotaVision.model.User;
import com.jacaranda.manuel.VotaVision.repository.SurveyReportRepository;
import com.jacaranda.manuel.VotaVision.repository.SurveyRepository;
import com.jacaranda.manuel.VotaVision.repository.UserRepository;

@Service
public class SurveyReportService {

	@Autowired
	private SurveyReportRepository surveyReportRepository;

	@Autowired
	private SurveyRepository surveyRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private EmailService emailService;

	@Transactional
	public void createReport(Long surveyId, String reason, String reporterEmail) {
		Survey survey = surveyRepository.findById(surveyId)
				.orElseThrow(() -> new SurveyMalformedException("Encuesta no encontrada"));
		User reporter = userRepository.findFirstByEmail(reporterEmail)
				.orElseThrow(() -> new UserNotFoundException("Usuario no encontrado"));

		String normalizedReason = normalizeReason(reason);
		if (surveyReportRepository.existsByReporterAndSurveyAndNormalizedReasonAndStatus(reporter, survey,
				normalizedReason, SurveyReportStatus.PENDING)) {
			throw new SurveyMalformedException("Ya has enviado este reporte para esta encuesta.");
		}

		SurveyReport report = new SurveyReport();
		report.setSurvey(survey);
		report.setReporter(reporter);
		report.setReason(reason.trim());
		report.setNormalizedReason(normalizedReason);
		report.setSurveyTitleSnapshot(survey.getTitle());
		report.setCreatorNameSnapshot(survey.getCreator().getName() + " " + survey.getCreator().getSurname());
		report.setCreatorEmailSnapshot(survey.getCreator().getEmail());
		surveyReportRepository.save(report);

		List<User> admins = userRepository.findByRole(Rol.ADMIN);
		for (User admin : admins) {
			emailService.sendSurveyReportAlertToAdmins(admin.getEmail(), reporter.getName(), reporter.getEmail(),
					survey.getTitle(), reason);
		}
	}

	public Page<SurveyReportDto> getReports(String adminEmail, SurveyReportStatus status, int page, int size) {
		requireAdmin(adminEmail);
		int safePage = Math.max(1, page);
		int safeSize = Math.max(1, Math.min(size, 50));
		return surveyReportRepository.findByStatus(status, PageRequest.of(safePage - 1, safeSize)).map(SurveyReportDto::new);
	}

	@Transactional
	public void dismissReport(Long reportId, String adminEmail, String reason) {
		User admin = requireAdmin(adminEmail);
		SurveyReport report = getPendingReport(reportId);
		report.setStatus(SurveyReportStatus.DISMISSED);
		report.setResolvedBy(admin);
		report.setResolvedAt(new Date());
		report.setResolutionReason(blankToDefault(reason, "Reporte descartado por moderación."));
		surveyReportRepository.save(report);
	}

	@Transactional
	public void deleteReportedSurvey(Long reportId, String adminEmail, String reason) {
		User admin = requireAdmin(adminEmail);
		if (reason == null || reason.trim().isEmpty()) {
			throw new SurveyMalformedException("Debes indicar un motivo para eliminar la encuesta.");
		}

		SurveyReport report = getPendingReport(reportId);
		Survey survey = report.getSurvey();
		if (survey == null) {
			throw new SurveyMalformedException("La encuesta asociada a este reporte ya no existe.");
		}

		User creator = survey.getCreator();
		emailService.sendSurveyDeletionByAdminEmail(creator.getEmail(), creator.getName(), creator.getSurname(),
				survey.getTitle(), reason.trim(), admin.getName(), admin.getEmail());

		survey.setModerationDeleted(true);
		survey.setModerationDeletedAt(new Date());
		survey.setModerationDeletionReason(reason.trim());
		surveyRepository.save(survey);

		List<SurveyReport> reports = surveyReportRepository.findBySurvey(survey);
		Date resolvedAt = new Date();
		for (SurveyReport surveyReport : reports) {
			if (surveyReport.getStatus() == SurveyReportStatus.PENDING) {
				surveyReport.setStatus(SurveyReportStatus.SURVEY_DELETED);
				surveyReport.setResolvedBy(admin);
				surveyReport.setResolvedAt(resolvedAt);
				surveyReport.setResolutionReason(reason.trim());
			}
		}
		surveyReportRepository.saveAll(reports);
	}

	@Transactional
	public void revokeReportAction(Long reportId, String adminEmail, String reason) {
		User admin = requireAdmin(adminEmail);
		SurveyReport report = surveyReportRepository.findById(reportId)
				.orElseThrow(() -> new SurveyMalformedException("Reporte no encontrado"));
		if (report.getStatus() == SurveyReportStatus.PENDING) {
			throw new SurveyMalformedException("No hay una acción resuelta que revocar.");
		}
		if (report.getStatus() == SurveyReportStatus.REVOKED) {
			throw new SurveyMalformedException("Esta acción ya fue revocada.");
		}

		String revokeReason = blankToDefault(reason, "Acción de moderación revocada por un administrador.");
		SurveyReportStatus previousStatus = report.getStatus();
		Survey survey = report.getSurvey();

		if (previousStatus == SurveyReportStatus.SURVEY_DELETED) {
			if (survey == null) {
				throw new SurveyMalformedException("No se puede revocar esta eliminación porque la encuesta ya no existe.");
			}
			survey.setModerationDeleted(false);
			survey.setModerationDeletedAt(null);
			survey.setModerationDeletionReason(null);
			surveyRepository.save(survey);

			List<SurveyReport> deletedReports = surveyReportRepository.findBySurveyAndStatus(survey,
					SurveyReportStatus.SURVEY_DELETED);
			for (SurveyReport deletedReport : deletedReports) {
				markRevoked(deletedReport, admin, revokeReason);
			}
			surveyReportRepository.saveAll(deletedReports);
		} else {
			markRevoked(report, admin, revokeReason);
			surveyReportRepository.save(report);
		}

		emailService.sendReportActionRevokedEmail(report.getCreatorEmailSnapshot(),
				report.getCreatorNameSnapshot(), report.getSurveyTitleSnapshot(), actionLabel(previousStatus),
				revokeReason, admin.getName(), admin.getEmail());
	}

	private SurveyReport getPendingReport(Long reportId) {
		SurveyReport report = surveyReportRepository.findById(reportId)
				.orElseThrow(() -> new SurveyMalformedException("Reporte no encontrado"));
		if (report.getStatus() != SurveyReportStatus.PENDING) {
			throw new SurveyMalformedException("Este reporte ya fue resuelto.");
		}
		return report;
	}

	private User requireAdmin(String email) {
		User admin = userRepository.findFirstByEmail(email)
				.orElseThrow(() -> new UserNotFoundException("Admin no encontrado"));
		if (admin.getRole() != Rol.ADMIN) {
			throw new UnauthorizedException("No tienes permisos de administrador.");
		}
		return admin;
	}

	private String normalizeReason(String reason) {
		if (reason == null || reason.trim().isEmpty()) {
			throw new SurveyMalformedException("Debes indicar un motivo para reportar.");
		}
		return reason.trim().replaceAll("\\s+", " ").toLowerCase();
	}

	private String blankToDefault(String value, String defaultValue) {
		return value == null || value.trim().isEmpty() ? defaultValue : value.trim();
	}

	private void markRevoked(SurveyReport report, User admin, String reason) {
		report.setStatus(SurveyReportStatus.REVOKED);
		report.setResolvedBy(admin);
		report.setResolvedAt(new Date());
		report.setResolutionReason(reason);
	}

	private String actionLabel(SurveyReportStatus status) {
		return status == SurveyReportStatus.SURVEY_DELETED ? "eliminación de encuesta" : "descarte de reporte";
	}
}
