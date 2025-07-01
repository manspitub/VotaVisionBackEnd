package com.jacaranda.manuel.VotaVision.dto;

import java.util.Date;
import java.util.List;

public class CategoryDetailsDto {
	private String name;
	private List<SurveySummaryDto> surveys;

	public CategoryDetailsDto() {
		super();
	}

	public CategoryDetailsDto(String name, List<SurveySummaryDto> surveys) {
		this.name = name;
		this.surveys = surveys;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<SurveySummaryDto> getSurveys() {
		return surveys;
	}

	public void setSurveys(List<SurveySummaryDto> surveys) {
		this.surveys = surveys;
	}

	public static class SurveySummaryDto {

		private String title;
		private Date startDate;
		private Date closeDate;

		public SurveySummaryDto() {
			super();
		}

		public SurveySummaryDto(String title, Date startDate, Date closeDate) {

			this.title = title;
			this.startDate = startDate;
			this.closeDate = closeDate;
		}

		public String getTitle() {
			return title;
		}

		public void setTitle(String title) {
			this.title = title;
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
	}
}
