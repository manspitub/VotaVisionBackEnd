package com.jacaranda.manuel.VotaVision.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jacaranda.manuel.VotaVision.dto.ApiResponse;
import com.jacaranda.manuel.VotaVision.dto.DeleteReportedSurveyRequest;
import com.jacaranda.manuel.VotaVision.dto.DismissReportRequest;
import com.jacaranda.manuel.VotaVision.dto.RevokeReportActionRequest;
import com.jacaranda.manuel.VotaVision.dto.SurveyReportDto;
import com.jacaranda.manuel.VotaVision.model.SurveyReportStatus;
import com.jacaranda.manuel.VotaVision.service.SurveyReportService;

@RestController
@RequestMapping("/api/reports")
public class SurveyReportController {

	@Autowired
	private SurveyReportService surveyReportService;

	@GetMapping
	public ResponseEntity<Page<SurveyReportDto>> getReports(
			@RequestParam(defaultValue = "PENDING") SurveyReportStatus status,
			@RequestParam(defaultValue = "1") int page,
			@RequestParam(defaultValue = "10") int size) {
		String adminEmail = SecurityContextHolder.getContext().getAuthentication().getName();
		return ResponseEntity.ok(surveyReportService.getReports(adminEmail, status, page, size));
	}

	@PutMapping("/{id}/dismiss")
	public ResponseEntity<ApiResponse> dismissReport(@PathVariable Long id, @RequestBody DismissReportRequest request) {
		String adminEmail = SecurityContextHolder.getContext().getAuthentication().getName();
		String reason = request == null ? null : request.getReason();
		surveyReportService.dismissReport(id, adminEmail, reason);
		return ResponseEntity.ok(new ApiResponse("Reporte descartado correctamente", HttpStatus.OK.value()));
	}

	@DeleteMapping("/{id}/survey")
	public ResponseEntity<ApiResponse> deleteReportedSurvey(@PathVariable Long id,
			@RequestBody DeleteReportedSurveyRequest request) {
		String adminEmail = SecurityContextHolder.getContext().getAuthentication().getName();
		String reason = request == null ? null : request.getReason();
		surveyReportService.deleteReportedSurvey(id, adminEmail, reason);
		return ResponseEntity.ok(new ApiResponse("Encuesta eliminada desde el reporte", HttpStatus.OK.value()));
	}

	@PutMapping("/{id}/revoke")
	public ResponseEntity<ApiResponse> revokeReportAction(@PathVariable Long id,
			@RequestBody RevokeReportActionRequest request) {
		String adminEmail = SecurityContextHolder.getContext().getAuthentication().getName();
		String reason = request == null ? null : request.getReason();
		surveyReportService.revokeReportAction(id, adminEmail, reason);
		return ResponseEntity.ok(new ApiResponse("Acción de moderación revocada", HttpStatus.OK.value()));
	}
}
