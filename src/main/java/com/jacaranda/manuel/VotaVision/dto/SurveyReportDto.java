package com.jacaranda.manuel.VotaVision.dto;

import java.util.Date;

import com.jacaranda.manuel.VotaVision.model.SurveyReport;

public class SurveyReportDto {
	private Long id;
	private Long surveyId;
	private String surveyTitle;
	private String creatorName;
	private String creatorEmail;
	private String reporterName;
	private String reporterEmail;
	private String reason;
	private Date reportedAt;
	private String status;
	private Date resolvedAt;
	private String resolvedByEmail;
	private String resolutionReason;

	public SurveyReportDto(SurveyReport report) {
		this.id = report.getId();
		this.surveyId = report.getSurvey() == null ? null : report.getSurvey().getId();
		this.surveyTitle = report.getSurveyTitleSnapshot();
		this.creatorName = report.getCreatorNameSnapshot();
		this.creatorEmail = report.getCreatorEmailSnapshot();
		this.reporterName = report.getReporter().getName() + " " + report.getReporter().getSurname();
		this.reporterEmail = report.getReporter().getEmail();
		this.reason = report.getReason();
		this.reportedAt = report.getReportedAt();
		this.status = report.getStatus().name();
		this.resolvedAt = report.getResolvedAt();
		this.resolvedByEmail = report.getResolvedBy() == null ? null : report.getResolvedBy().getEmail();
		this.resolutionReason = report.getResolutionReason();
	}

	public Long getId() {
		return id;
	}

	public Long getSurveyId() {
		return surveyId;
	}

	public String getSurveyTitle() {
		return surveyTitle;
	}

	public String getCreatorName() {
		return creatorName;
	}

	public String getCreatorEmail() {
		return creatorEmail;
	}

	public String getReporterName() {
		return reporterName;
	}

	public String getReporterEmail() {
		return reporterEmail;
	}

	public String getReason() {
		return reason;
	}

	public Date getReportedAt() {
		return reportedAt;
	}

	public String getStatus() {
		return status;
	}

	public Date getResolvedAt() {
		return resolvedAt;
	}

	public String getResolvedByEmail() {
		return resolvedByEmail;
	}

	public String getResolutionReason() {
		return resolutionReason;
	}
}
