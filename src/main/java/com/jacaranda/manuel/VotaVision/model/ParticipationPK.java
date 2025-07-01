package com.jacaranda.manuel.VotaVision.model;

import java.io.Serializable;
import java.util.Objects;

public class ParticipationPK implements Serializable {

	private static final long serialVersionUID = 1L;
	private Long user; // Cambia a userId
	private Long survey; // Cambia a surveyId

	public ParticipationPK() {
	}

	public ParticipationPK(Long user, Long survey) {
		this.user = user;
		this.survey = survey;
	}

	public Long getUser() {
		return user;
	}

	public void setUser(Long user) {
		this.user = user;
	}

	public Long getSurvey() {
		return survey;
	}

	public void setSurvey(Long survey) {
		this.survey = survey;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;
		ParticipationPK that = (ParticipationPK) obj;
		return Objects.equals(user, that.user) && Objects.equals(survey, that.survey);
	}

	@Override
	public int hashCode() {
		return Objects.hash(user, survey);
	}
}
