package com.jacaranda.manuel.VotaVision.model;

import java.util.Date;
import java.util.List;
import java.util.Objects;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

@Entity
@Table(name = "Participacion")
@IdClass(ParticipationPK.class)

/**
 * Un usuario solo puede participar una vez en una encuesta
 */
public class Participation {

	@Id
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "usuario_id") // Debe coincidir con ParticipationPK
	private User user;

	@Id
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "encuesta_id") // Debe coincidir con ParticipationPK
	private Survey survey;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "fecha", updatable = false, nullable = false)
	private Date date;

	@OneToMany(mappedBy = "participation", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Answer> answers;

	// Constructores
	public Participation() {
	}

	public Participation(User user, Survey survey, Date date) {
		this.user = user;
		this.survey = survey;
		this.date = date;
	}

	// Getters y setters
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Survey getSurvey() {
		return survey;
	}

	public void setSurvey(Survey survey) {
		this.survey = survey;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public List<Answer> getAnswers() {
		return answers;
	}

	public void setAnswers(List<Answer> answers) {
		this.answers = answers;
	}

	@Override
	public int hashCode() {
		return Objects.hash(survey, user);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Participation other = (Participation) obj;
		return Objects.equals(survey, other.survey) && Objects.equals(user, other.user);
	}

}
