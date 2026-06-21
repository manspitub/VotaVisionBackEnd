package com.jacaranda.manuel.VotaVision.dto;

import java.util.ArrayList;
import java.util.List;

public class SurveyResultsDto {

	private Long surveyId;
	private String title;
	private String status;
	private int totalResponses;
	private double participationRate;
	private List<QuestionResultsDto> questions = new ArrayList<>();

	public Long getSurveyId() {
		return surveyId;
	}

	public void setSurveyId(Long surveyId) {
		this.surveyId = surveyId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public int getTotalResponses() {
		return totalResponses;
	}

	public void setTotalResponses(int totalResponses) {
		this.totalResponses = totalResponses;
	}

	public double getParticipationRate() {
		return participationRate;
	}

	public void setParticipationRate(double participationRate) {
		this.participationRate = participationRate;
	}

	public List<QuestionResultsDto> getQuestions() {
		return questions;
	}

	public void setQuestions(List<QuestionResultsDto> questions) {
		this.questions = questions;
	}

	public static class QuestionResultsDto {
		private Long questionId;
		private String text;
		private String type;
		private List<OptionResultsDto> options = new ArrayList<>();
		private List<String> answers = new ArrayList<>();

		public Long getQuestionId() {
			return questionId;
		}

		public void setQuestionId(Long questionId) {
			this.questionId = questionId;
		}

		public String getText() {
			return text;
		}

		public void setText(String text) {
			this.text = text;
		}

		public String getType() {
			return type;
		}

		public void setType(String type) {
			this.type = type;
		}

		public List<OptionResultsDto> getOptions() {
			return options;
		}

		public void setOptions(List<OptionResultsDto> options) {
			this.options = options;
		}

		public List<String> getAnswers() {
			return answers;
		}

		public void setAnswers(List<String> answers) {
			this.answers = answers;
		}
	}

	public static class OptionResultsDto {
		private Long optionId;
		private String text;
		private long count;
		private double percentage;

		public Long getOptionId() {
			return optionId;
		}

		public void setOptionId(Long optionId) {
			this.optionId = optionId;
		}

		public String getText() {
			return text;
		}

		public void setText(String text) {
			this.text = text;
		}

		public long getCount() {
			return count;
		}

		public void setCount(long count) {
			this.count = count;
		}

		public double getPercentage() {
			return percentage;
		}

		public void setPercentage(double percentage) {
			this.percentage = percentage;
		}
	}
}
