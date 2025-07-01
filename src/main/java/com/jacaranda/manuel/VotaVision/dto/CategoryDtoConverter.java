package com.jacaranda.manuel.VotaVision.dto;

import org.springframework.stereotype.Component;

import com.jacaranda.manuel.VotaVision.model.Category;

@Component
public class CategoryDtoConverter {
	
	 public CategoryDto convertCategoryDto(Category category) {
	        CategoryDto dto = new CategoryDto();
	        
	        dto.setId(category.getId());
	        dto.setName(category.getName());
	        dto.setDescription(category.getDescription());

	        if (category.getSurveys() != null) {
	            dto.setTotalSurveys(category.getSurveys().size());
	        } else {
	            dto.setTotalSurveys(0);
	        }

	        // No seteamos `surveys` aquí, se hace opcionalmente en el servicio cuando se quiere incluir
	        return dto;
	    }
}
