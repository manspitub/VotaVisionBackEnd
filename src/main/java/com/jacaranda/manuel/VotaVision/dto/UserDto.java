package com.jacaranda.manuel.VotaVision.dto;

import java.math.BigDecimal;
import java.util.Date;

public class UserDto {

    private Long id;
    private String name;
    private String surname;
    private String email;
    private Integer age;
    private String city;
    private String profession;
    private Boolean confirmed;
    private Date createdAt;
    private String role;
    private BigDecimal reward;

    // Default constructor (important for serialization/deserialization)
    public UserDto() {}

    // Parameterized constructor
    public UserDto(Long id, String name, String surname, String email, Integer age, String city, String profession,
                   Boolean confirmed, Date createdAt, String role, BigDecimal reward) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.age = age;
        this.city = city;
        this.profession = profession;
        this.confirmed = confirmed;
        this.createdAt = createdAt;
        this.role = role;
        this.reward = reward;
    }

    // Getters and setters (recommended for proper encapsulation)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
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

    public Boolean getConfirmed() {
        return confirmed;
    }

    public void setConfirmed(Boolean confirmed) {
        this.confirmed = confirmed;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

	public BigDecimal getReward() {
		return reward;
	}

	public void setReward(BigDecimal reward) {
		this.reward = reward;
	}
    
    
}
