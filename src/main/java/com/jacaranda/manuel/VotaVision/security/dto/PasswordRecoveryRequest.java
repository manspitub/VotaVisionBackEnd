package com.jacaranda.manuel.VotaVision.security.dto;

public class PasswordRecoveryRequest {
	private String email;

	public PasswordRecoveryRequest() {
	}

	public PasswordRecoveryRequest(String email) {
		this.email = email;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
}
