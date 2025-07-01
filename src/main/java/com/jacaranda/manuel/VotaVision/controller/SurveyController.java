package com.jacaranda.manuel.VotaVision.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jacaranda.manuel.VotaVision.dto.AnsweredSurveyDto;
import com.jacaranda.manuel.VotaVision.dto.ApiResponse;
import com.jacaranda.manuel.VotaVision.dto.CreateSurveyDto;
import com.jacaranda.manuel.VotaVision.dto.DeleteSurveyRequestDto;
import com.jacaranda.manuel.VotaVision.dto.ReportRequest;
import com.jacaranda.manuel.VotaVision.dto.SubmitSurveyDto;
import com.jacaranda.manuel.VotaVision.dto.SurveyDto;
import com.jacaranda.manuel.VotaVision.service.SurveyService;

@RestController
@RequestMapping("/api/surveys")
public class SurveyController {

	@Autowired
	private SurveyService surveyService;

	@PostMapping
	public ResponseEntity<?> createSurvey(@RequestBody CreateSurveyDto createSurveyDto) throws Exception {
		try {
			String currentUserEmail = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			surveyService.createSurvey(createSurveyDto, currentUserEmail);
			return ResponseEntity.ok(new ApiResponse("Encuesta creada correctamente", HttpStatus.CREATED.value()));
		} catch (Exception e) {
			throw e;
		}
	}

	@GetMapping("/active")
	public ResponseEntity<?> getActiveSurveys(@RequestParam(defaultValue = "1") int page,
			@RequestParam(defaultValue = "10") int size, @RequestParam(defaultValue = "id-Asc") String sort,
			@RequestParam(defaultValue = "") String search) {
		try {
			String currentUserEmail = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			Page<SurveyDto> surveys = surveyService.getActiveSurveysPaged(currentUserEmail, page, size, sort, search);

			return ResponseEntity.ok(surveys);
		} catch (Exception e) {
			throw e;
		}
	}

	@GetMapping("/my-surveys")
	public ResponseEntity<?> getMySurveys(@RequestParam(defaultValue = "1") int page,
			@RequestParam(defaultValue = "10") int size, @RequestParam(defaultValue = "id-Asc") String sort,
			@RequestParam(defaultValue = "") String search) throws Exception {
		try {
			String currentUserEmail = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			Page<SurveyDto> mySurveys = surveyService.getSurveysByCreator(currentUserEmail, page, size, sort, search);
			return ResponseEntity.ok(mySurveys);
		} catch (Exception e) {
			throw e;
		}
	}
	
	@GetMapping("/answered")
	public ResponseEntity<?> getAnsweredSurveys(
	        @RequestParam(defaultValue = "1") int page,
	        @RequestParam(defaultValue = "10") int size,
	        @RequestParam(defaultValue = "id-Asc") String sort,
	        @RequestParam(defaultValue = "") String search) throws Exception {
	    try {
	        String currentUserEmail = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	        Page<SurveyDto> answeredSurveys = surveyService.getSurveysAnsweredByUser(currentUserEmail, page, size, sort, search);
	        return ResponseEntity.ok(answeredSurveys);
	    } catch (Exception e) {
	        throw e;
	    }
	}

	@GetMapping("/admin/all")
	public ResponseEntity<?> getAllSurveysForAdmin(@RequestParam(defaultValue = "1") int page,
			@RequestParam(defaultValue = "10") int size, @RequestParam(defaultValue = "id-Asc") String sort,
			@RequestParam(defaultValue = "") String search) throws Exception {
		String email = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Page<SurveyDto> allSurveys = surveyService.getAllSurveysForAdmin(email, page, size, sort, search);
		return ResponseEntity.ok(allSurveys);
	}
	
	@GetMapping("/{id}/answered")
	public ResponseEntity<AnsweredSurveyDto> getSurveyAnswered(@PathVariable Long id) throws Exception {
		try {
			String email = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		    return ResponseEntity.ok(surveyService.getSurveyWithAnswers(id, email));	
		} catch (Exception e) {
			throw e;
		}
		
	}

	@GetMapping("/{id}")
	public ResponseEntity<?> getSurveyById(@PathVariable Long id) throws Exception {
		try {
			String currentUserEmail = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			SurveyDto survey = surveyService.getSurveyById(id, currentUserEmail);
			return ResponseEntity.ok(survey);
		} catch (Exception e) {
			throw e;
		}
	}

	@PutMapping("/{id}")
	public ResponseEntity<?> updateSurvey(@PathVariable Long id, @RequestBody CreateSurveyDto updatedSurvey)
			throws Exception {
		try {
			String currentUserEmail = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			surveyService.updateSurvey(id, updatedSurvey, currentUserEmail);
			return ResponseEntity.ok(new ApiResponse("Encuesta actualizada correctamente", HttpStatus.OK.value()));
		} catch (Exception e) {
			throw e;
		}
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteSurvey(@PathVariable Long id) throws Exception {
		try {
			String currentUserEmail = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			surveyService.deleteSurvey(id, currentUserEmail);
			return ResponseEntity.ok(new ApiResponse("Encuesta eliminada correctamente", HttpStatus.OK.value()));
		} catch (Exception e) {
			throw e;
		}
	}

	@DeleteMapping("/admin")
	public ResponseEntity<?> deleteSurveyByAdmin(@RequestBody DeleteSurveyRequestDto dto) throws Exception {
		try {
			String adminEmail = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			surveyService.deleteSurveyByAdmin(dto.getSurveyId(), adminEmail, dto.getReason());
			return ResponseEntity.ok(new ApiResponse("Encuesta eliminada por el administrador", HttpStatus.OK.value()));
		} catch (Exception e) {
			throw e;
		}

	}

	@GetMapping("/{surveyId}/start")
	public ResponseEntity<?> startSurvey(@PathVariable Long surveyId) throws Exception {
		try {
			String email = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			SurveyDto dto = surveyService.startSurvey(surveyId, email);
			return ResponseEntity.ok(dto);
		} catch (Exception e) {
			throw e;
		}

	}

	@PostMapping("/submit")
	public ResponseEntity<?> submitSurvey(@RequestBody SubmitSurveyDto dto) throws Exception {
		try {
			String email = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			surveyService.submitSurvey(dto, email);
			return ResponseEntity.ok(new ApiResponse("Encuesta enviada correctamente", HttpStatus.OK.value()));
		} catch (Exception e) {
			throw e;
		}

	}

	@PostMapping("report")
	public ResponseEntity<?> reportSurvey(@RequestBody ReportRequest request) throws Exception {

		try {
			String reporterEmail = SecurityContextHolder.getContext().getAuthentication().getName();
			surveyService.reportSurvey(request, reporterEmail);
			return ResponseEntity.ok(new ApiResponse("Reporte enviado a los administradores", HttpStatus.OK.value()));
		} catch (Exception e) {
			throw e;
		}

	}

}
