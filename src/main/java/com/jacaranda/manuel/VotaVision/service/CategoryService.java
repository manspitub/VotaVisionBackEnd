package com.jacaranda.manuel.VotaVision.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.jacaranda.manuel.VotaVision.dto.CategoryDetailsDto;
import com.jacaranda.manuel.VotaVision.dto.CategoryDetailsDto.SurveySummaryDto;
import com.jacaranda.manuel.VotaVision.dto.CategoryDto;
import com.jacaranda.manuel.VotaVision.dto.CategoryDtoConverter;
import com.jacaranda.manuel.VotaVision.dto.SurveyDtoConverter;
import com.jacaranda.manuel.VotaVision.exception.CategoryNotFoundException;
import com.jacaranda.manuel.VotaVision.exception.UnauthorizedException;
import com.jacaranda.manuel.VotaVision.model.Category;
import com.jacaranda.manuel.VotaVision.model.Rol;
import com.jacaranda.manuel.VotaVision.model.User;
import com.jacaranda.manuel.VotaVision.repository.CategoryRepository;
import com.jacaranda.manuel.VotaVision.repository.SurveyRepository;
import com.jacaranda.manuel.VotaVision.repository.UserRepository;

@Service
public class CategoryService {

	@Autowired
	private CategoryRepository categoryRepository;

	@Autowired
	private CategoryDtoConverter categoryDtoConverter;

	@Autowired
	private SurveyDtoConverter surveyDtoConverter;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private SurveyRepository surveyRepository;

	public Page<CategoryDto> getAllCategoriesPaged(int page, int size, String sort, String search) {
		String[] sortParts = sort.split("-");
		String sortField = sortParts[0];
		Sort.Direction direction = sortParts[1].equalsIgnoreCase("Desc") ? Sort.Direction.DESC : Sort.Direction.ASC;

		Pageable pageable = PageRequest.of(page - 1, size, Sort.by(direction, sortField));

		Page<Category> categoryPage;
		if (search != null && !search.isEmpty()) {
			categoryPage = categoryRepository.findByNameContainingIgnoreCase(search, pageable);
		} else {
			categoryPage = categoryRepository.findAll(pageable);
		}

		return categoryPage.map(categoryDtoConverter::convertCategoryDto);
	}

	public CategoryDetailsDto getCategoryById(Long id) {
		Category category = categoryRepository.findById(id)
				.orElseThrow(() -> new CategoryNotFoundException("Categoría no encontrada"));

		List<SurveySummaryDto> surveyDtos = category.getSurveys().stream()
				.map(surveyDtoConverter::convertSurveySummaryDto).collect(Collectors.toList());

		return new CategoryDetailsDto(category.getName(), surveyDtos);
	}

	// Crear categoría
	public void createCategory(CategoryDto categoryDto, String userEmail) {
		checkAdminRole(userEmail);

		Category category = new Category();
		category.setName(categoryDto.getName());
		category.setDescription(categoryDto.getDescription());

		categoryRepository.save(category);
	}

	// Actualizar categoría
	public void updateCategory(Long id, CategoryDto categoryDto, String userEmail) {
		checkAdminRole(userEmail);

		Category category = categoryRepository.findById(id)
				.orElseThrow(() -> new CategoryNotFoundException("Categoría no encontrada"));

		category.setName(categoryDto.getName());
		category.setDescription(categoryDto.getDescription());

		categoryRepository.save(category);
	}

	// Eliminar categoría
	public void deleteCategory(Long id, String userEmail) {
		checkAdminRole(userEmail);

		Category category = categoryRepository.findById(id)
				.orElseThrow(() -> new CategoryNotFoundException("Categoría no encontrada"));

		categoryRepository.delete(category);
	}

	private void checkAdminRole(String userEmail) {
		User user = userRepository.findFirstByEmail(userEmail)
				.orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

		if (!user.getRole().equals(Rol.ADMIN)) {
			throw new UnauthorizedException("Acceso denegado: solo ADMIN puede realizar esta acción");
		}
	}

}
