package com.jacaranda.manuel.VotaVision.dto;

public class RevokeReportActionRequest {
	private String reason;

	public RevokeReportActionRequest() {
		super();
	}

	public RevokeReportActionRequest(String reason) {
		super();
		this.reason = reason;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}
}
