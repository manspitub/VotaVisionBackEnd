package com.jacaranda.manuel.VotaVision.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jacaranda.manuel.VotaVision.dto.ApiResponse;
import com.jacaranda.manuel.VotaVision.dto.CategoryDetailsDto;
import com.jacaranda.manuel.VotaVision.dto.CategoryDto;
import com.jacaranda.manuel.VotaVision.exception.CategoryNotFoundException;
import com.jacaranda.manuel.VotaVision.service.CategoryService;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {
	@Autowired
    private CategoryService categoryService;

	@GetMapping
    public ResponseEntity<?> getAllCategoriesPaged(
        @RequestParam(defaultValue = "1") int page,
        @RequestParam(defaultValue = "10") int size,
        @RequestParam(defaultValue = "id-Asc") String sort,
        @RequestParam(defaultValue = "") String search
    ) {
        try {
            Page<CategoryDto> categoriesPage = categoryService.getAllCategoriesPaged(page, size, sort, search);
            return ResponseEntity.ok(categoriesPage);
        } catch (Exception e) {
            throw e;
        }
    }

    // Obtener categoría por id
    @GetMapping("/{id}")
    public ResponseEntity<CategoryDetailsDto> getCategoryById(@PathVariable Long id) {
        CategoryDetailsDto category = categoryService.getCategoryById(id);
        if (category == null) {
            throw new CategoryNotFoundException("Categoría no encontrada con id: " + id);
        }
        return ResponseEntity.ok(category);
    }

    @PostMapping
    public ResponseEntity<ApiResponse> createCategory(@RequestBody CategoryDto categoryDto) {
    	String currentUserEmail = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        categoryService.createCategory(categoryDto, currentUserEmail);
        return new ResponseEntity<>(new ApiResponse("Categoría creada con éxito", 201), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse> updateCategory(@PathVariable Long id, @RequestBody CategoryDto categoryDto) {
    	String currentUserEmail = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        categoryService.updateCategory(id, categoryDto, currentUserEmail);
        return ResponseEntity.ok(new ApiResponse("Categoría actualizada con éxito", 200));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteCategory(@PathVariable Long id) {
    	String currentUserEmail = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        categoryService.deleteCategory(id, currentUserEmail);
        return ResponseEntity.ok(new ApiResponse("Categoría eliminada con éxito", 200));
    }

}
