package com.jacaranda.manuel.VotaVision.security.jwt;

public class JwtUserResponse {

	private String name;
	private String email;
	private String role;
	private String surname;
	private boolean confirmed;
	private String token;

	// Default constructor
	public JwtUserResponse() {
		super();
	}

	// Constructor with all parameters
	public JwtUserResponse(String name, String email, String role, String surname, boolean confirmed, String token) {
		super();
		this.name = name;
		this.email = email;
		this.role = role;
		this.surname = surname;
		this.confirmed = confirmed;
		this.token = token;
	}

	public JwtUserResponse(String name, String email, String role, String surname, boolean confirmed) {
		super();
		this.name = name;
		this.email = email;
		this.role = role;
		this.surname = surname;
		this.confirmed = confirmed;
	}

	// Getters and Setters
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public boolean isConfirmed() {
		return confirmed;
	}

	public void setConfirmed(boolean confirmed) {
		this.confirmed = confirmed;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}
}
