package com.jacaranda.manuel.VotaVision.dto;

import java.util.List;

import com.jacaranda.manuel.VotaVision.dto.CategoryDetailsDto.SurveySummaryDto;

public class CategoryDto {
	
	private Long id;
    private String name;
    private String description;
    private int totalSurveys;  
    private List<SurveySummaryDto> surveys;  // <-- nuevo campo

    public CategoryDto() {
        super();
    }

    public CategoryDto(String name, String description) {
        super();
        this.name = name;
        this.description = description;
    }

    public String getName() {
        return name;
    }
    
    

    public CategoryDto(Long id, String name, String description, int totalSurveys, List<SurveySummaryDto> surveys) {
		super();
		this.id = id;
		this.name = name;
		this.description = description;
		this.totalSurveys = totalSurveys;
		this.surveys = surveys;
	}

	public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getTotalSurveys() {
        return totalSurveys;
    }

    public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setTotalSurveys(int totalSurveys) {
        this.totalSurveys = totalSurveys;
    }

    public List<SurveySummaryDto> getSurveys() {
        return surveys;
    }

    public void setSurveys(List<SurveySummaryDto> surveys) {
        this.surveys = surveys;
    }
    
    
}
