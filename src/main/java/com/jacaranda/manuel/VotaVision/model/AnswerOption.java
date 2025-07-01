package com.jacaranda.manuel.VotaVision.model;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "Respuesta_opcion")
@IdClass(AnswerOptionPK.class)
public class AnswerOption {

	@Id
	@ManyToOne
	@JoinColumn(name = "respuesta_id", nullable = false)
	private Answer answer;

	@Id
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "opcion_id", nullable = false)
	private Option option;

	public AnswerOption() {
		super();
	}

	public AnswerOption(Answer answer, Option option) {
		super();
		this.answer = answer;
		this.option = option;
	}

	public Answer getAnswer() {
		return answer;
	}

	public void setAnswer(Answer answer) {
		this.answer = answer;
	}

	public Option getOption() {
		return option;
	}

	public void setOption(Option option) {
		this.option = option;
	}

}
