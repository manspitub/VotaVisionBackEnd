package com.jacaranda.manuel.VotaVision.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.jacaranda.manuel.VotaVision.dto.AnsweredQuestionDto;
import com.jacaranda.manuel.VotaVision.dto.AnsweredSurveyDto;
import com.jacaranda.manuel.VotaVision.dto.CreateQuestionDto;
import com.jacaranda.manuel.VotaVision.dto.CreateSurveyDto;
import com.jacaranda.manuel.VotaVision.dto.ReportRequest;
import com.jacaranda.manuel.VotaVision.dto.SubmitAnswerDto;
import com.jacaranda.manuel.VotaVision.dto.SubmitSurveyDto;
import com.jacaranda.manuel.VotaVision.dto.SurveyDto;
import com.jacaranda.manuel.VotaVision.dto.SurveyDtoConverter;
import com.jacaranda.manuel.VotaVision.exception.SurveyMalformedException;
import com.jacaranda.manuel.VotaVision.exception.UnauthorizedException;
import com.jacaranda.manuel.VotaVision.exception.UserNotFoundException;
import com.jacaranda.manuel.VotaVision.model.Answer;
import com.jacaranda.manuel.VotaVision.model.AnswerOption;
import com.jacaranda.manuel.VotaVision.model.Category;
import com.jacaranda.manuel.VotaVision.model.Notification;
import com.jacaranda.manuel.VotaVision.model.Option;
import com.jacaranda.manuel.VotaVision.model.Participation;
import com.jacaranda.manuel.VotaVision.model.Question;
import com.jacaranda.manuel.VotaVision.model.QuestionType;
import com.jacaranda.manuel.VotaVision.model.Rol;
import com.jacaranda.manuel.VotaVision.model.Subscription;
import com.jacaranda.manuel.VotaVision.model.Survey;
import com.jacaranda.manuel.VotaVision.model.User;
import com.jacaranda.manuel.VotaVision.repository.CategoryRepository;
import com.jacaranda.manuel.VotaVision.repository.NotificationRepository;
import com.jacaranda.manuel.VotaVision.repository.ParticipationRepository;
import com.jacaranda.manuel.VotaVision.repository.SubscriptionRepository;
import com.jacaranda.manuel.VotaVision.repository.SurveyRepository;
import com.jacaranda.manuel.VotaVision.repository.UserRepository;

import jakarta.mail.MessagingException;

@Service
public class SurveyService {

	@Autowired
	private SurveyRepository surveyRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private CategoryRepository categoryRepository;

	@Autowired
	private SubscriptionRepository subscriptionRepository;

	@Autowired
	private NotificationRepository notificationRepository;

	@Autowired
	private SurveyDtoConverter converter;

	@Autowired
	private EmailService emailService;

	@Autowired
	private ParticipationRepository participationRepository;

	public void createSurvey(CreateSurveyDto dto, String emailCreator) throws Exception {
		User creator = userRepository.findFirstByEmail(emailCreator)
				.orElseThrow(() -> new UserNotFoundException("Usuario no encontrado"));

		if (creator.getRole() != Rol.CREATOR) {
			throw new UnauthorizedException("No tienes permisos para crear encuestas. Tu rol es " + creator.getRole());
		}

		Date now = new Date();

		if (dto.getStartDate().after(dto.getCloseDate())) {
			throw new SurveyMalformedException("La fecha de inicio no puede ser posterior a la fecha de cierre.");
		}

		if (dto.getStartDate().before(now)) {
			throw new SurveyMalformedException("La fecha de inicio no puede estar en el pasado.");
		}

		Survey survey = new Survey();
		survey.setTitle(dto.getTitle());
		survey.setDescription(dto.getDescription());
		survey.setStartDate(dto.getStartDate());
		survey.setCloseDate(dto.getCloseDate());
		survey.setReward(dto.getReward());
		survey.setCreator(creator);

		// Aquí obtienes la categoría y la asignas
		Category category = categoryRepository.findById(dto.getCategory().getId())
				.orElseThrow(() -> new SurveyMalformedException("Categoría no encontrada"));
		survey.setCategory(category);

		List<Question> questionList = new ArrayList<>();
		for (CreateQuestionDto qDto : dto.getQuestions()) {
			Question question = new Question();
			question.setText(qDto.getText());
			question.setType(qDto.getType());
			question.setIsMandatory(qDto.isRequired());
			question.setSurvey(survey);

			if (qDto.getType() == QuestionType.MULTIPLE_CHOICE) {
				if (qDto.getOptions() == null || qDto.getOptions().isEmpty()) {
					throw new SurveyMalformedException("La pregunta de tipo MULTIPLE_CHOICE debe tener opciones.");
				}
			}

			if (qDto.getType() == null) {
				throw new SurveyMalformedException("El tipo de pregunta no puede ser nulo.");
			}

			if (qDto.getText() == null || qDto.getText().trim().length() < 2) {
				throw new SurveyMalformedException("El texto de la pregunta debe tener al menos 2 caracteres.");
			}

			if (qDto.getType() == QuestionType.MULTIPLE_CHOICE && qDto.getOptions() != null) {
				List<Option> options = qDto.getOptions().stream().map(optText -> new Option(question, optText))
						.collect(Collectors.toList());
				question.setOptions(options);
			}

			questionList.add(question);
		}

		survey.setQuestions(questionList);
		surveyRepository.save(survey);

		// Encuesta ya guardada

		// Obtener usuarios con suscripción a esa categoría
		List<User> subscribedUsers = subscriptionRepository.findByCategory(category).stream().map(Subscription::getUser)
				.filter(user -> user.getRole() == Rol.USER).collect(Collectors.toList());

		for (User user : subscribedUsers) {
			Notification notification = new Notification(
					"Nueva encuesta en la categoría " + survey.getCategory().getName(), user, survey);
			notificationRepository.save(notification);
		}

	}

	public Page<SurveyDto> getActiveSurveysPaged(String userEmail, int pageNum, int pageSize, String orden,
			String searchTerm) {
		Date now = new Date();

		List<String> validOrders = List.of("id-Asc", "startDate-Asc", "startDate-Des", "closeDate-Asc", "closeDate-Des",
				"reward-Asc", "reward-Des", "responses-Asc", "responses-Des");

		if (!validOrders.contains(orden)) {
			orden = "id-Asc";
		}

		String[] ordenSplit = orden.split("-");
		String campo = ordenSplit[0];
		String direccion = ordenSplit[1];

		List<Survey> all;
		if (searchTerm == null || searchTerm.isBlank()) {
			all = surveyRepository.findByStartDateBeforeAndCloseDateAfter(now, now);
		} else {
			all = surveyRepository.findByTitleContainingIgnoreCaseAndStartDateBeforeAndCloseDateAfter(searchTerm.trim(),
					now, now);
		}

		List<SurveyDto> dtos = all.stream()
				.map((Function<Survey, SurveyDto>) survey -> converter.convertSurveyDto(survey, userEmail))
				.collect(Collectors.toList());

		// Orden manual si es por respuestas
		if (campo.equals("responses")) {
			dtos.sort((a, b) -> {
				int cmp = Integer.compare(a.getResponsesCount(), b.getResponsesCount());
				return direccion.equalsIgnoreCase("Asc") ? cmp : -cmp;
			});
		} else {
			Comparator<SurveyDto> comparator = switch (campo) {
			case "startDate" -> Comparator.comparing(SurveyDto::getStartDate);
			case "closeDate" -> Comparator.comparing(SurveyDto::getCloseDate);
			case "reward" -> Comparator.comparing(SurveyDto::getReward);
			case "id" -> Comparator.comparing(SurveyDto::getId);
			default -> Comparator.comparing(SurveyDto::getId);
			};
			if (direccion.equalsIgnoreCase("Des")) {
				comparator = comparator.reversed();
			}
			dtos.sort(comparator);
		}

		// Paginación manual
		int fromIndex = Math.min((pageNum - 1) * pageSize, dtos.size());
		int toIndex = Math.min(fromIndex + pageSize, dtos.size());
		List<SurveyDto> paged = dtos.subList(fromIndex, toIndex);

		return new PageImpl<>(paged, PageRequest.of(pageNum - 1, pageSize), dtos.size());
	}

	public Page<SurveyDto> getSurveysByCreator(String creatorEmail, int pageNum, int pageSize, String orden,
			String search) throws Exception {

		User creator = userRepository.findFirstByEmail(creatorEmail)
				.orElseThrow(() -> new UserNotFoundException("Usuario no encontrado con email: " + creatorEmail));

		if (creator.getRole() != Rol.CREATOR) {
			throw new UnauthorizedException("No tienes encuestas propias. Tu rol es: " + creator.getRole());
		}

		List<String> validOrders = List.of("id-Asc", "startDate-Asc", "startDate-Des", "closeDate-Asc", "closeDate-Des",
				"reward-Asc", "reward-Des", "responses-Asc", "responses-Des");

		if (!validOrders.contains(orden)) {
			orden = "id-Asc";
		}

		String[] ordenSplit = orden.split("-");
		String campo = ordenSplit[0];
		String direccion = ordenSplit[1];

		// ⚠️ Recuperamos TODAS las encuestas del creador (sin paginar)
		List<Survey> all = (search == null || search.isBlank()) ? surveyRepository.findByCreatorEmail(creatorEmail)
				: surveyRepository.findByCreatorEmailAndTitleContainingIgnoreCase(creatorEmail, search);

		List<SurveyDto> dtos = all.stream()
				.map((Function<Survey, SurveyDto>) survey -> converter.convertSurveyDto(survey, creatorEmail))
				.collect(Collectors.toList());

		// Orden personalizado
		if (campo.equals("responses")) {
			dtos.sort((a, b) -> {
				int cmp = Integer.compare(a.getResponsesCount(), b.getResponsesCount());
				return direccion.equalsIgnoreCase("Asc") ? cmp : -cmp;
			});
		} else {
			Comparator<SurveyDto> comparator = switch (campo) {
			case "startDate" -> Comparator.comparing(SurveyDto::getStartDate);
			case "closeDate" -> Comparator.comparing(SurveyDto::getCloseDate);
			case "reward" -> Comparator.comparing(SurveyDto::getReward);
			case "id" -> Comparator.comparing(SurveyDto::getId);
			default -> Comparator.comparing(SurveyDto::getId);
			};
			if (direccion.equalsIgnoreCase("Des")) {
				comparator = comparator.reversed();
			}
			dtos.sort(comparator);
		}

		// Paginación manual
		int fromIndex = Math.min((pageNum - 1) * pageSize, dtos.size());
		int toIndex = Math.min(fromIndex + pageSize, dtos.size());
		List<SurveyDto> paged = dtos.subList(fromIndex, toIndex);

		return new PageImpl<>(paged, PageRequest.of(pageNum - 1, pageSize), dtos.size());
	}

	public Page<SurveyDto> getSurveysAnsweredByUser(String userEmail, int pageNum, int pageSize, String orden,
			String search) throws Exception {
		User user = userRepository.findFirstByEmail(userEmail)
				.orElseThrow(() -> new UserNotFoundException("Usuario no encontrado con email: " + userEmail));

		List<String> validOrders = List.of("id-Asc", "startDate-Asc", "startDate-Des", "closeDate-Asc", "closeDate-Des",
				"reward-Asc", "reward-Des", "responses-Asc", "responses-Des");

		if (!validOrders.contains(orden)) {
			orden = "id-Asc";
		}

		String[] ordenSplit = orden.split("-");
		String campo = ordenSplit[0];
		String direccion = ordenSplit[1];

		// Encuestas respondidas por el usuario
		List<Survey> surveys = surveyRepository.findDistinctByParticipationsUserEmail(userEmail);

		// Filtrado por título si se busca algo
		if (search != null && !search.isBlank()) {
			surveys = surveys.stream().filter(s -> s.getTitle().toLowerCase().contains(search.toLowerCase()))
					.collect(Collectors.toList());
		}

		List<SurveyDto> dtos = surveys.stream().map(s -> converter.convertSurveyDto(s, userEmail))
				.collect(Collectors.toList());

		// Orden personalizado
		if (campo.equals("responses")) {
			dtos.sort((a, b) -> {
				int cmp = Integer.compare(a.getResponsesCount(), b.getResponsesCount());
				return direccion.equalsIgnoreCase("Asc") ? cmp : -cmp;
			});
		} else {
			Comparator<SurveyDto> comparator = switch (campo) {
			case "startDate" -> Comparator.comparing(SurveyDto::getStartDate);
			case "closeDate" -> Comparator.comparing(SurveyDto::getCloseDate);
			case "reward" -> Comparator.comparing(SurveyDto::getReward);
			case "id" -> Comparator.comparing(SurveyDto::getId);
			default -> Comparator.comparing(SurveyDto::getId);
			};
			if (direccion.equalsIgnoreCase("Des")) {
				comparator = comparator.reversed();
			}
			dtos.sort(comparator);
		}

		// Paginación manual
		int fromIndex = Math.min((pageNum - 1) * pageSize, dtos.size());
		int toIndex = Math.min(fromIndex + pageSize, dtos.size());
		List<SurveyDto> paged = dtos.subList(fromIndex, toIndex);

		return new PageImpl<>(paged, PageRequest.of(pageNum - 1, pageSize), dtos.size());
	}

	public Page<SurveyDto> getAllSurveysForAdmin(String adminEmail, int pageNum, int pageSize, String orden,
			String searchTerm) {
		User admin = userRepository.findFirstByEmail(adminEmail)
				.orElseThrow(() -> new UserNotFoundException("Usuario no encontrado con email: " + adminEmail));
		if (admin.getRole() != Rol.ADMIN) {
			throw new UnauthorizedException("No tienes permisos de administrador: " + adminEmail);
		}
		Date now = new Date();

		List<String> validOrders = List.of("id-Asc", "startDate-Asc", "startDate-Des", "closeDate-Asc", "closeDate-Des",
				"reward-Asc", "reward-Des", "responses-Asc", "responses-Des");

		if (!validOrders.contains(orden)) {
			orden = "id-Asc";
		}

		String[] ordenSplit = orden.split("-");
		String campo = ordenSplit[0];
		String direccion = ordenSplit[1];

		// Obtener todas las encuestas (con o sin filtro)
		List<Survey> all = (searchTerm == null || searchTerm.isBlank()) ? surveyRepository.findAll()
				: surveyRepository.findByTitleContainingIgnoreCase(searchTerm.trim());

		List<SurveyDto> dtos = all.stream().map(survey -> converter.convertSurveyDto(survey, adminEmail)) // sin pasar
																											// email,
																											// vista
																											// neutral
				.collect(Collectors.toList());

		// Ordenamiento
		if (campo.equals("responses")) {
			dtos.sort((a, b) -> {
				int cmp = Integer.compare(a.getResponsesCount(), b.getResponsesCount());
				return direccion.equalsIgnoreCase("Asc") ? cmp : -cmp;
			});
		} else {
			Comparator<SurveyDto> comparator = switch (campo) {
			case "startDate" -> Comparator.comparing(SurveyDto::getStartDate);
			case "closeDate" -> Comparator.comparing(SurveyDto::getCloseDate);
			case "reward" -> Comparator.comparing(SurveyDto::getReward);
			case "id" -> Comparator.comparing(SurveyDto::getId);
			default -> Comparator.comparing(SurveyDto::getId);
			};
			if (direccion.equalsIgnoreCase("Des")) {
				comparator = comparator.reversed();
			}
			dtos.sort(comparator);
		}

		// Paginación manual
		int fromIndex = Math.min((pageNum - 1) * pageSize, dtos.size());
		int toIndex = Math.min(fromIndex + pageSize, dtos.size());
		List<SurveyDto> paged = dtos.subList(fromIndex, toIndex);

		return new PageImpl<>(paged, PageRequest.of(pageNum - 1, pageSize), dtos.size());
	}

	public SurveyDto getSurveyById(Long id, String userEmail) throws Exception {
		Survey survey = surveyRepository.findById(id)
				.orElseThrow(() -> new SurveyMalformedException("Encuesta no encontrada"));

		// Validar si pertenece al usuario
//		if (!survey.getCreator().getEmail().equals(userEmail)) {
//			throw new UnauthorizedException("No tienes permisos para ver esta encuesta.");
//		}

		return converter.convertSurveyDto(survey, userEmail);
	}

	public AnsweredSurveyDto getSurveyWithAnswers(Long surveyId, String userEmail) throws Exception {
		Survey survey = surveyRepository.findById(surveyId)
				.orElseThrow(() -> new SurveyMalformedException("Encuesta no encontrada"));

		SurveyDto baseDto = converter.convertSurveyDto(survey, userEmail);

		Optional<Participation> participationOpt = participationRepository.findBySurveyIdAndUserEmail(surveyId,
				userEmail);
		if (participationOpt.isEmpty()) {
			throw new SurveyMalformedException("El usuario no ha participado en esta encuesta.");
		}

		Participation participation = participationOpt.get();

		AnsweredSurveyDto dto = new AnsweredSurveyDto(baseDto);
		dto.setAnsweredAt(participation.getDate());

		List<AnsweredQuestionDto> answeredQuestions = participation.getAnswers().stream().map(answer -> {
			Long questionId= answer.getQuestion().getId();
			List<String> selectedAnswer;
			System.out.println("questionId "+questionId);
			if (answer.getAnswerOptions() != null && !answer.getAnswerOptions().isEmpty()) {
				selectedAnswer = answer.getAnswerOptions().stream().map(ao -> ao.getOption().getText())
						.collect(Collectors.toList());
			} else {
				selectedAnswer = List.of(answer.getText()); // respuesta escrita
			}

			return new AnsweredQuestionDto(questionId, selectedAnswer);
		}).collect(Collectors.toList());

		dto.setUserAnswers(answeredQuestions);
		
		return dto;	
	}

	public void updateSurvey(Long surveyId, CreateSurveyDto dto, String userEmail) throws Exception {

		Survey existingSurvey = surveyRepository.findById(surveyId)
				.orElseThrow(() -> new SurveyMalformedException("Encuesta no encontrada"));

		User user = userRepository.findFirstByEmail(userEmail)
				.orElseThrow(() -> new UserNotFoundException("Usuario no encontrado"));

		if (!existingSurvey.getCreator().getEmail().equals(userEmail)) {
			throw new UnauthorizedException("No puedes modificar esta encuesta.");
		}

		boolean hasResponses = existingSurvey.getQuestions().stream()
				.anyMatch(q -> q.getAnswers() != null && !q.getAnswers().isEmpty());

		Date now = new Date();

		if (hasResponses) {
			// Verificar que solo se modifica closeDate (y que es válida)
			boolean somethingElseModified = (dto.getTitle() != null
					&& !dto.getTitle().equals(existingSurvey.getTitle()))
					|| (dto.getDescription() != null && !dto.getDescription().equals(existingSurvey.getDescription()))
					|| (dto.getReward() != null && !dto.getReward().equals(existingSurvey.getReward()))
					|| (dto.getStartDate() != null && !dto.getStartDate().equals(existingSurvey.getStartDate()))
					|| (dto.getQuestions() != null && !dto.getQuestions().isEmpty())
					// Añadimos control para la categoría:
					|| (dto.getCategory() != null && !dto.getCategory().equals(existingSurvey.getCategory()));

			if (somethingElseModified) {
				throw new SurveyMalformedException(
						"Esta encuesta ya tiene respuestas. Solo se permite cambiar la fecha de cierre.");
			}

			// Validar que closeDate es válida
			if (dto.getCloseDate() == null) {
				throw new SurveyMalformedException(
						"La encuesta ya tiene respuestas. Debes especificar una nueva fecha de cierre.");
			}

			if (dto.getCloseDate().before(existingSurvey.getStartDate())) {
				throw new SurveyMalformedException(
						"La nueva fecha de cierre no puede ser anterior a la fecha de inicio.");
			}

			existingSurvey.setCloseDate(dto.getCloseDate());
			surveyRepository.save(existingSurvey);
			return;
		}

		// Si no hay respuestas, aplicar actualizaciones parciales
		if (dto.getTitle() != null) {
			existingSurvey.setTitle(dto.getTitle());
		}

		if (dto.getDescription() != null) {
			existingSurvey.setDescription(dto.getDescription());
		}

		if (dto.getReward() != null) {
			existingSurvey.setReward(dto.getReward());
		}

		if (dto.getStartDate() != null) {
			if (dto.getCloseDate() != null && dto.getStartDate().after(dto.getCloseDate())) {
				throw new SurveyMalformedException("La fecha de inicio no puede ser posterior a la fecha de cierre.");
			}

			existingSurvey.setStartDate(dto.getStartDate());
		}

		if (dto.getCloseDate() != null) {
			if (existingSurvey.getStartDate() != null && dto.getCloseDate().before(existingSurvey.getStartDate())) {
				throw new SurveyMalformedException("La fecha de cierre no puede ser anterior a la fecha de inicio.");
			}

			existingSurvey.setCloseDate(dto.getCloseDate());
		}

		// NUEVO: actualizar categoría si viene en el DTO y es distinta
		if (dto.getCategory() != null && !dto.getCategory().equals(existingSurvey.getCategory())) {
			existingSurvey.setCategory(dto.getCategory());
		}

		if (dto.getQuestions() != null) {
			existingSurvey.getQuestions().clear();

			List<Question> updatedQuestions = dto.getQuestions().stream().map(qDto -> {
				Question q = new Question();
				q.setText(qDto.getText());
				q.setType(qDto.getType());
				q.setIsMandatory(qDto.isRequired());
				q.setSurvey(existingSurvey);

				if (qDto.getType() == QuestionType.MULTIPLE_CHOICE && qDto.getOptions() != null) {
					List<Option> options = qDto.getOptions().stream().map(opt -> new Option(q, opt))
							.collect(Collectors.toList());
					q.setOptions(options);
				}

				return q;
			}).collect(Collectors.toList());

			existingSurvey.getQuestions().clear();
			existingSurvey.getQuestions().addAll(updatedQuestions);

		}

		surveyRepository.save(existingSurvey);
	}

	public void deleteSurvey(Long surveyId, String userEmail) throws Exception {
		Survey survey = surveyRepository.findById(surveyId)
				.orElseThrow(() -> new SurveyMalformedException("Encuesta no encontrada"));

		// Comprobar si el usuario es el creador
		if (!survey.getCreator().getEmail().equals(userEmail)) {
			throw new UnauthorizedException("No tienes permisos para eliminar esta encuesta.");
		}

		surveyRepository.delete(survey);
	}

	public void deleteSurveyByAdmin(Long surveyId, String adminEmail, String reason) throws Exception {
		Survey survey = surveyRepository.findById(surveyId)
				.orElseThrow(() -> new SurveyMalformedException("Encuesta no encontrada"));

		User admin = userRepository.findFirstByEmail(adminEmail)
				.orElseThrow(() -> new UserNotFoundException("Admin no encontrado"));

		if (admin.getRole() != Rol.ADMIN) {
			throw new UnauthorizedException("No tienes permisos para eliminar encuestas como administrador.");
		}

		User creator = survey.getCreator();

		// Mandamos el correo con la justificacion
		emailService.sendSurveyDeletionByAdminEmail(creator.getEmail(), creator.getName(), creator.getSurname(),
				survey.getTitle(), reason, admin.getName(), admin.getEmail());

		// Eliminar la encuesta
		surveyRepository.delete(survey);
	}

	public SurveyDto startSurvey(Long surveyId, String userEmail) throws Exception {
		Survey survey = surveyRepository.findById(surveyId)
				.orElseThrow(() -> new SurveyMalformedException("Encuesta no encontrada"));

		if (!survey.getCloseDate().after(new Date())) {
			throw new SurveyMalformedException("La encuesta está cerrada.");
		}

		if (survey.getParticipations().stream().anyMatch(p -> p.getUser().getEmail().equals(userEmail))) {
			throw new SurveyMalformedException("Ya has respondido esta encuesta.");
		}

		SurveyDto dto = converter.convertSurveyDto(survey, userEmail);
		Collections.shuffle(dto.getQuestions()); // Preguntas en orden aleatorio
		return dto;
	}

	public void submitSurvey(SubmitSurveyDto dto, String userEmail) throws Exception {
		User user = userRepository.findFirstByEmail(userEmail)
				.orElseThrow(() -> new UserNotFoundException("Usuario no encontrado"));

		Survey survey = surveyRepository.findById(dto.getSurveyId())
				.orElseThrow(() -> new SurveyMalformedException("Encuesta no encontrada"));

		// Comprobar si ya respondió
		Optional<Participation> existing = survey.getParticipations().stream()
				.filter(p -> p.getUser().getId().equals(user.getId())).findFirst();

		if (existing.isPresent()) {
			throw new SurveyMalformedException("Ya has respondido a esta encuesta.");
		}

		// Crear nueva participación
		Participation participation = new Participation();
		participation.setUser(user);
		participation.setSurvey(survey);
		participation.setDate(new Date());

		List<Answer> processedAnswers = new ArrayList<>();
		List<Long> answeredQuestions = new ArrayList<>();

		for (SubmitAnswerDto answerDto : dto.getAnswers()) {
			Question question = survey.getQuestions().stream().filter(q -> q.getId().equals(answerDto.getQuestionId()))
					.findFirst().orElseThrow(() -> new SurveyMalformedException("Pregunta no válida"));

			Answer answer = new Answer();
			answer.setQuestion(question);
			answer.setParticipation(participation);

			if (question.getType() == QuestionType.MULTIPLE_CHOICE) {
				List<AnswerOption> answerOptions = answerDto.getOptionIds().stream().map(optionId -> {
					Option opt = question.getOptions().stream().filter(o -> o.getId().equals(optionId)).findFirst()
							.orElseThrow(() -> new SurveyMalformedException("Opción inválida"));
					return new AnswerOption(answer, opt);
				}).toList();
				answer.setAnswerOptions(answerOptions);
			} else {
				if (question.getIsMandatory()
						&& (answerDto.getText() == null || answerDto.getText().trim().isEmpty())) {
					throw new SurveyMalformedException("Falta respuesta de texto. En: " + answerDto.getQuestionId());
				}

				answer.setText(answerDto.getText());
			}

			processedAnswers.add(answer);
			answeredQuestions.add(question.getId());
		}

		// Validar que TODAS las preguntas obligatorias estén respondidas
		for (Question q : survey.getQuestions()) {
			if (Boolean.TRUE.equals(q.getIsMandatory()) && !answeredQuestions.contains(q.getId())) {
				throw new SurveyMalformedException("Debes responder todas las preguntas obligatorias.");
			}
		}

		participation.setAnswers(processedAnswers);

		// Añadir participación (se guarda en cascada)
		survey.getParticipations().add(participation);
		user.setReward(user.getReward().add(BigDecimal.valueOf(survey.getReward())));

		surveyRepository.save(survey);
		userRepository.save(user);
	}

	public void reportSurvey(ReportRequest request, String reporterEmail) throws MessagingException {
		Survey survey = surveyRepository.findById(request.getSurveyId())
				.orElseThrow(() -> new SurveyMalformedException("Encuesta no encontrada"));

		User reporter = userRepository.findFirstByEmail(reporterEmail)
				.orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

		List<User> admins = userRepository.findByRole(Rol.ADMIN);

		for (User admin : admins) {
			String subject = "🚨 Reporte de encuesta recibido";
			String body = String.format("El usuario %s ha reportado la encuesta \"%s\" con el siguiente motivo:\n\n%s",
					reporter.getEmail(), survey.getTitle(), request.getReason());
			emailService.sendSurveyReportAlertToAdmins(admin.getEmail(), reporter.getName(), reporter.getEmail(),
					survey.getTitle(), request.getReason());
		}
	}

	private void checkDates(Date start, Date close) {
		Date now = new Date();

		if (start.after(close)) {
			throw new SurveyMalformedException("La fecha de inicio no puede ser posterior a la de cierre.");
		}

		if (start.before(now)) {
			throw new SurveyMalformedException("La fecha de inicio no puede estar en el pasado.");
		}
	}

	private void checkCloseDate(Date nuevaCierre, Date inicioActual) {
		if (nuevaCierre.before(inicioActual)) {
			throw new SurveyMalformedException("La nueva fecha de cierre no puede ser anterior a la fecha de inicio.");
		}
	}

	private void asignarDatosBasicos(Survey survey, CreateSurveyDto dto) {
		survey.setTitle(dto.getTitle());
		survey.setDescription(dto.getDescription());
		survey.setStartDate(dto.getStartDate());
		survey.setCloseDate(dto.getCloseDate());
		survey.setReward(dto.getReward());
	}

	private void actualizarPreguntas(Survey survey, CreateSurveyDto dto) {
		List<Question> updatedQuestions = new ArrayList<>();

		for (CreateQuestionDto qDto : dto.getQuestions()) {
			validarPregunta(qDto);

			Question q = new Question();
			q.setText(qDto.getText());
			q.setType(qDto.getType());
			q.setIsMandatory(qDto.isRequired());
			q.setSurvey(survey);

			if (qDto.getType() == QuestionType.MULTIPLE_CHOICE && qDto.getOptions() != null) {
				List<Option> options = qDto.getOptions().stream().map(opt -> new Option(q, opt))
						.collect(Collectors.toList());
				q.setOptions(options);
			}

			updatedQuestions.add(q);
		}

		survey.getQuestions().clear(); // elimina las antiguas
		survey.setQuestions(updatedQuestions); // añade las nuevas
	}

	private void validarPregunta(CreateQuestionDto qDto) {
		if (qDto.getType() == null) {
			throw new SurveyMalformedException("El tipo de pregunta no puede ser nulo.");
		}

		if (qDto.getText() == null || qDto.getText().trim().length() < 2) {
			throw new SurveyMalformedException("El texto de la pregunta debe tener al menos 2 caracteres.");
		}

		if (qDto.getType() == QuestionType.MULTIPLE_CHOICE
				&& (qDto.getOptions() == null || qDto.getOptions().isEmpty())) {
			throw new SurveyMalformedException("La pregunta de tipo MULTIPLE_CHOICE debe tener opciones.");
		}
	}

}
