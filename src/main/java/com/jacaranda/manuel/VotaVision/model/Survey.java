package com.jacaranda.manuel.VotaVision.model;

import java.util.Date;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "Encuesta")
public class Survey {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotBlank(message = "El título no puede estar vacío")
	@Column(name = "titulo", nullable = false, length = 255)
	private String title;

	@Column(name = "descripcion")
	private String description;

	@ManyToOne
	@JoinColumn(name = "creador_id", nullable = false)
	private User creator;

	@NotNull(message = "La fecha de inicio no puede estar vacía")
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "fecha_inicio", nullable = false)
	private Date startDate;

	@NotNull(message = "La fecha de cierre no puede estar vacía")
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "fecha_cierre", nullable = false)
	private Date closeDate;

	@DecimalMin(value = "0.0", inclusive = true, message = "La recompensa no puede ser negativa")
	@Column(name = "recompensa", precision = 10)
	private Double reward;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "fecha_creacion", updatable = false, nullable = false)
	private Date createdAt;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "categoria_id", nullable = true)
	private Category category;


	@OneToMany(mappedBy = "survey", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Question> questions;
	
	@OneToMany(mappedBy = "survey", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Participation> participations;
	
	@OneToMany(mappedBy = "survey", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Notification> notifications;



	// Constructor por defecto: se asigna la fecha de creación automáticamente
	public Survey() {
		this.createdAt = new Date();
	}

	// Getters y Setters

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public User getCreator() {
		return creator;
	}

	public void setCreator(User creator) {
		this.creator = creator;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getCloseDate() {
		return closeDate;
	}

	public void setCloseDate(Date closeDate) {
		this.closeDate = closeDate;
	}

	public Double getReward() {
		return reward;
	}

	public void setReward(Double reward) {
		this.reward = reward;
	}


	public Date getCreatedAt() {
		return createdAt;
	}

	public List<Question> getQuestions() {
		return questions;
	}

	public void setQuestions(List<Question> questions) {
		this.questions = questions;
	}
	
	public List<Participation> getParticipations() {
	    return participations;
	}

	public void setParticipations(List<Participation> participations) {
	    this.participations = participations;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public List<Notification> getNotifications() {
		return notifications;
	}

	public void setNotifications(List<Notification> notifications) {
		this.notifications = notifications;
	}
	
	
	
	

	
	

}
