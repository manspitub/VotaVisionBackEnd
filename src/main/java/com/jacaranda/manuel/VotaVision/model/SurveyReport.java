package com.jacaranda.manuel.VotaVision.model;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

@Entity
@Table(name = "ReporteEncuesta")
public class SurveyReport {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "encuesta_id", nullable = true)
	private Survey survey;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "reporter_id", nullable = false)
	private User reporter;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "resolved_by_id", nullable = true)
	private User resolvedBy;

	@Column(name = "motivo", nullable = false, length = 1000)
	private String reason;

	@Column(name = "motivo_normalizado", nullable = false, length = 1000)
	private String normalizedReason;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "fecha_reporte", nullable = false, updatable = false)
	private Date reportedAt = new Date();

	@Enumerated(EnumType.STRING)
	@Column(name = "estado", nullable = false, length = 32, columnDefinition = "varchar(32)")
	private SurveyReportStatus status = SurveyReportStatus.PENDING;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "fecha_resolucion")
	private Date resolvedAt;

	@Column(name = "motivo_resolucion", length = 1000)
	private String resolutionReason;

	@Column(name = "encuesta_titulo_snapshot", nullable = false)
	private String surveyTitleSnapshot;

	@Column(name = "creador_nombre_snapshot", nullable = false)
	private String creatorNameSnapshot;

	@Column(name = "creador_email_snapshot", nullable = false)
	private String creatorEmailSnapshot;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Survey getSurvey() {
		return survey;
	}

	public void setSurvey(Survey survey) {
		this.survey = survey;
	}

	public User getReporter() {
		return reporter;
	}

	public void setReporter(User reporter) {
		this.reporter = reporter;
	}

	public User getResolvedBy() {
		return resolvedBy;
	}

	public void setResolvedBy(User resolvedBy) {
		this.resolvedBy = resolvedBy;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public String getNormalizedReason() {
		return normalizedReason;
	}

	public void setNormalizedReason(String normalizedReason) {
		this.normalizedReason = normalizedReason;
	}

	public Date getReportedAt() {
		return reportedAt;
	}

	public void setReportedAt(Date reportedAt) {
		this.reportedAt = reportedAt;
	}

	public SurveyReportStatus getStatus() {
		return status;
	}

	public void setStatus(SurveyReportStatus status) {
		this.status = status;
	}

	public Date getResolvedAt() {
		return resolvedAt;
	}

	public void setResolvedAt(Date resolvedAt) {
		this.resolvedAt = resolvedAt;
	}

	public String getResolutionReason() {
		return resolutionReason;
	}

	public void setResolutionReason(String resolutionReason) {
		this.resolutionReason = resolutionReason;
	}

	public String getSurveyTitleSnapshot() {
		return surveyTitleSnapshot;
	}

	public void setSurveyTitleSnapshot(String surveyTitleSnapshot) {
		this.surveyTitleSnapshot = surveyTitleSnapshot;
	}

	public String getCreatorNameSnapshot() {
		return creatorNameSnapshot;
	}

	public void setCreatorNameSnapshot(String creatorNameSnapshot) {
		this.creatorNameSnapshot = creatorNameSnapshot;
	}

	public String getCreatorEmailSnapshot() {
		return creatorEmailSnapshot;
	}

	public void setCreatorEmailSnapshot(String creatorEmailSnapshot) {
		this.creatorEmailSnapshot = creatorEmailSnapshot;
	}
}
