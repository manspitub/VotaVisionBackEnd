package com.jacaranda.manuel.VotaVision.dto;

import java.util.Date;
import java.util.List;

import com.jacaranda.manuel.VotaVision.model.Category;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

public class CreateSurveyDto {
	@NotBlank(message = "El título es obligatorio.")
	@Size(min = 3, message = "El título debe tener al menos 3 caracteres.")
	private String title;

	@NotBlank(message = "La descripción es obligatoria.")
	private String description;

	@NotNull(message = "La fecha de inicio es obligatoria.")
	private Date startDate;

	@NotNull(message = "La fecha de cierre es obligatoria.")
	private Date closeDate;

	@NotNull(message = "La recompensa es obligatoria.")
	@PositiveOrZero(message = "La recompensa no puede ser negativa.")
	private Double reward;

	@NotNull(message = "Las preguntas no pueden estar vacías.")
	@Size(min = 1, message = "Debe haber al menos una pregunta.")
	private List<@Valid CreateQuestionDto> questions;

	@NotNull(message = "La categoría es obligatoria.") // <-- nuevo campo obligatorio
	private Category category;

	public CreateSurveyDto() {
		super();
	}

	public CreateSurveyDto(String title, String description, Date startDate, Date closeDate, Double reward,
			List<CreateQuestionDto> questions, Category category) {
		super();
		this.title = title;
		this.description = description;
		this.startDate = startDate;
		this.closeDate = closeDate;
		this.reward = reward;
		this.questions = questions;
		this.category = category;
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

	public List<CreateQuestionDto> getQuestions() {
		return questions;
	}

	public void setQuestions(List<CreateQuestionDto> questions) {
		this.questions = questions;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	
}
