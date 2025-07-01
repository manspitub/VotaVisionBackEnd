package com.jacaranda.manuel.VotaVision.model;

import java.util.List;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinColumns;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "Respuesta")
public class Answer {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "pregunta_id", nullable = false)
	private Question question;

	@Column(name = "texto")
	private String text; // Usado para respuestas cortas o largas


	@OneToMany(mappedBy = "answer", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<AnswerOption> answerOptions;


	@ManyToOne
	@JoinColumns({ @JoinColumn(name = "encuesta_id"), @JoinColumn(name = "usuario_id") })
	private Participation participation;

	public Answer() {
		super();
	}

	public Answer(Question question, String text, Participation participation) {
		super();
		this.question = question;
		this.text = text;
		this.participation = participation;
	}

	
	public Answer(Question question, List<AnswerOption> answerOptions, Participation participation) {
		super();
		this.question = question;
		this.answerOptions = answerOptions;
		this.participation = participation;
	}

	public Participation getParticipation() {
		return participation;
	}

	public void setParticipation(Participation participation) {
		this.participation = participation;
	}

	public Question getQuestion() {
		return question;
	}

	public void setQuestion(Question question) {
		this.question = question;
	}

	public List<AnswerOption> getAnswerOptions() {
		return answerOptions;
	}

	public void setAnswerOptions(List<AnswerOption> answerOptions) {
		this.answerOptions = answerOptions;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
	
	

}