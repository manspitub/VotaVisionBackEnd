package com.jacaranda.manuel.VotaVision.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "Usuario")
public class User implements UserDetails {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotBlank(message = "El nombre no puede estar vacío")
	@Size(min = 3, max = 100, message = "El nombre debe tener entre 3 y 100 caracteres")
	@Column(name = "nombre", nullable = false, length = 100)
	private String name;

	@NotBlank(message = "El apellido no puede estar vacío")
	@Size(min = 3, max = 100, message = "El apellido debe tener entre 3 y 100 caracteres")
	@Column(name = "apellido", nullable = false, length = 100)
	private String surname;

	@NotBlank(message = "El correo no puede estar vacío")
	@Email(message = "Debe ser un email válido")
	@Column(name = "correo", nullable = false, unique = true)
	private String email;
	
	@NotNull(message = "La recompensa no puede ser nula")
	@DecimalMin(value = "0.0", inclusive = true, message = "La recompensa no puede ser negativa")
	@Digits(integer = 10, fraction = 2, message = "La recompensa debe tener hasta 10 dígitos enteros y 2 decimales")
	@Column(name = "recompensa", nullable = false)
	private BigDecimal reward = BigDecimal.ZERO;


	@NotNull(message = "La edad no puede estar vacía")
	@Min(value = 18, message = "La edad debe ser al menos 18 años")
	@Max(value = 100, message = "La edad no puede ser mayor a 100 años")
	@Column(name = "edad", nullable = false)
	private Integer age;

	@Size(min = 2, max = 100, message = "La ciudad debe tener entre 2 y 100 caracteres")
	@Column(name = "ciudad", length = 100)
	private String city;

	@Size(max = 100, message = "La profesión no debe superar los 100 caracteres")
	@Column(name = "profesion", length = 100)
	private String profession;

	@NotBlank(message = "La contraseña no puede estar vacía")
	@Size(min = 8, message = "La contraseña debe tener al menos 8 caracteres")
	@Column(name = "contrasena", nullable = false)
	private String password;

	@Column(name = "confirmado")
	private Boolean confirmed = false;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "fecha_creacion", updatable = false, nullable = false)
	private Date createdAt = new Date();
	
	@Column(name = "habilitar_notificaciones", nullable = false)
	private boolean notificationsEnabled = true;

	@Enumerated(EnumType.STRING)
	@Column(name = "rol", nullable = false)
	private Rol role;

	@OneToMany(mappedBy = "creator", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Survey> surveys;

	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Participation> participations;
	
	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Subscription> subscriptions;

	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Notification> notifications;

	
	// Constructor por defecto
	public User() {
		super();
		this.role = Rol.USER;
		this.surveys = new ArrayList<Survey>();
		this.participations = new ArrayList<Participation>();
		this.subscriptions = new ArrayList<Subscription>();
		this.notifications = new ArrayList<Notification>();
		this.notificationsEnabled = true;
	}

	// Getters y Setters

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

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	@Override
	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
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

	public Rol getRole() {
		return role;
	}

	public void setRole(Rol role) {
		this.role = role;
	}
	
	public BigDecimal getReward() {
		return reward;
	}

	public void setReward(BigDecimal reward) {
		this.reward = reward;
	}


	public List<Survey> getSurveys() {
		return surveys;
	}

	public void setSurveys(List<Survey> surveys) {
		this.surveys = surveys;
	}

	public List<Participation> getParticipations() {
		return participations;
	}

	public void setParticipations(List<Participation> participations) {
		this.participations = participations;
	}

	// Implementación de UserDetails

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return List.of(new SimpleGrantedAuthority(this.role.toString()));
	}

	@Override
	public String getUsername() {
		return this.email;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	public boolean isNotificationsEnabled() {
		return notificationsEnabled;
	}

	public void setNotificationsEnabled(boolean notificationsEnabled) {
		this.notificationsEnabled = notificationsEnabled;
	}
	
	
	
}
