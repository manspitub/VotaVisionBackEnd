package com.jacaranda.manuel.VotaVision.security.dto;

public class CreateUserDto {
	private String name;

	private String surname;

	private String email;

	private Integer age;

	private String city;

	private String profession;

	private String role;
	
	private String currentPassword;

	private String password;

	private String password2;

	public CreateUserDto() {
		super();
	}

	public CreateUserDto(String name, String email, Integer age, String city, String profession, String currentPassword, String password,
			String password2, String surname, String role) {
		super();
		this.name = name;
		this.email = email;
		this.age = age;
		this.city = city;
		this.profession = profession;
		this.currentPassword = currentPassword;
		this.password = password;
		this.password2 = password2;
		this.surname = surname;
		this.role = role;
	}
	
	

	public CreateUserDto(String password, String password2) {
		super();
		this.password = password;
		this.password2 = password2;
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

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getProfession() {
		return profession;
	}

	public void setProfession(String profession) {
		this.profession = profession;
	}
	

	public String getCurrentPassword() {
		return currentPassword;
	}

	public void setCurrentPassword(String currentPassword) {
		this.currentPassword = currentPassword;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPassword2() {
		return password2;
	}

	public void setPassword2(String password2) {
		this.password2 = password2;
	}

}
