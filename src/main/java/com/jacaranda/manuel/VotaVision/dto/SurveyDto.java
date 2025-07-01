package com.jacaranda.manuel.VotaVision.dto;

import java.util.Date;
import java.util.List;

public class SurveyDto {

    private Long id;
    private String title;
    private String description;
    private Date startDate;
    private Date closeDate;
    private Double reward;

    private CreatorSummaryDto creator;
    private CategorySummaryDto category;
    private List<QuestionDto> questions;
    
    private boolean canRespond;

    private int responsesCount;

    public SurveyDto() {}

    public SurveyDto(Long id, String title, String description, Date startDate, Date closeDate, Double reward, CreatorSummaryDto creator) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.startDate = startDate;
        this.closeDate = closeDate;
        this.reward = reward;
        this.creator = creator;
        
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

    public CreatorSummaryDto getCreator() {
        return creator;
    }

    public void setCreator(CreatorSummaryDto creator) {
        this.creator = creator;
    }

    public List<QuestionDto> getQuestions() {
        return questions;
    }

    public void setQuestions(List<QuestionDto> questions) {
        this.questions = questions;
    }

    public int getResponsesCount() {
        return responsesCount;
    }

    public void setResponsesCount(int responsesCount) {
        this.responsesCount = responsesCount;
    }

	public boolean isCanRespond() {
		return canRespond;
	}

	public void setCanRespond(boolean canRespond) {
		this.canRespond = canRespond;
	}

	public CategorySummaryDto getCategory() {
		return category;
	}

	public void setCategory(CategorySummaryDto category) {
		this.category = category;
	}
	
	
    
    
}
