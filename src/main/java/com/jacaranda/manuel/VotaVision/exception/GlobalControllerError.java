package com.jacaranda.manuel.VotaVision.exception;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailAuthenticationException;
import org.springframework.mail.MailParseException;
import org.springframework.mail.MailSendException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import io.jsonwebtoken.security.SignatureException;
import jakarta.validation.ConstraintViolationException;

@RestControllerAdvice
public class GlobalControllerError {

	@ExceptionHandler(value = InvalidPasswordException.class)
	public ResponseEntity<ApiError> handleInvalidPasswordException(InvalidPasswordException e) {
		ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, LocalDateTime.now(), e.getMessage());

		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiError);
	}

	@ExceptionHandler(value = UserAlreadyExistsException.class)
	public ResponseEntity<ApiError> handleUserAlreadyExistsException(UserAlreadyExistsException e) {
		ApiError apiError = new ApiError(HttpStatus.CONFLICT, LocalDateTime.now(), e.getMessage());

		return ResponseEntity.status(HttpStatus.CONFLICT).body(apiError);
	}

	@ExceptionHandler(value = EmailMismatchException.class)
	public ResponseEntity<ApiError> handleEmailMismatchException(EmailMismatchException e) {
		ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, LocalDateTime.now(), e.getMessage());

		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiError);
	}

	@ExceptionHandler(value = EmailNotConfirmedException.class)
	public ResponseEntity<ApiError> handleEmailNotConfirmedException(EmailNotConfirmedException e) {
		ApiError apiError = new ApiError(HttpStatus.FORBIDDEN, LocalDateTime.now(), e.getMessage());

		return ResponseEntity.status(HttpStatus.FORBIDDEN).body(apiError);
	}

	@ExceptionHandler(value = MissingFieldException.class)
	public ResponseEntity<ApiError> handleMissingFieldException(MissingFieldException e) {
		ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, LocalDateTime.now(), e.getMessage());

		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiError);
	}

	@ExceptionHandler(PasswordResetTokenInvalidException.class)
	public ResponseEntity<ApiError> handlePasswordResetTokenInvalidException(PasswordResetTokenInvalidException e) {
		ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, LocalDateTime.now(), e.getMessage());
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiError);
	}

	@ExceptionHandler(UserAlreadyEnabledException.class)
	public ResponseEntity<ApiError> handleUserAlreadyEnabledException(UserAlreadyEnabledException e) {
		ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, LocalDateTime.now(), e.getMessage());
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiError);
	}

	@ExceptionHandler(SignatureException.class)
	public ResponseEntity<ApiError> handleSignatureException(SignatureException e) {
		ApiError apiError = new ApiError(HttpStatus.UNAUTHORIZED, LocalDateTime.now(), "Firma del token inválida");
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(apiError);
	}

	@ExceptionHandler(ConstraintViolationException.class)
	public ResponseEntity<ApiError> handleConstraintViolationException(ConstraintViolationException e) {
		String errors = e.getConstraintViolations().stream()
				.map(violation -> "Campo '" + violation.getPropertyPath() + "': " + violation.getMessage())
				.collect(Collectors.joining(", ")); // Juntamos todos los mensajes con una coma
		ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, LocalDateTime.now(), errors);
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiError);
	}

	@ExceptionHandler(BadCredentialsException.class)
	public ResponseEntity<ApiError> handleSignatureException(BadCredentialsException e) {
		ApiError apiError = new ApiError(HttpStatus.UNAUTHORIZED, LocalDateTime.now(),
				"Usuario o contraseña incorrectos");
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(apiError);
	}

	@ExceptionHandler(UserNotFoundException.class)
	public ResponseEntity<ApiError> handleUserNotFoundException(UserNotFoundException e) {
		ApiError apiError = new ApiError(HttpStatus.NOT_FOUND, LocalDateTime.now(), e.getMessage());
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(apiError);
	}

	@ExceptionHandler(CategoryNotFoundException.class)
	public ResponseEntity<ApiError> handleUserNotFoundException(CategoryNotFoundException e) {
		ApiError apiError = new ApiError(HttpStatus.NOT_FOUND, LocalDateTime.now(), e.getMessage());
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(apiError);
	}

	@ExceptionHandler(UnauthorizedException.class)
	public ResponseEntity<ApiError> handleUnauthorizedException(UnauthorizedException e) {
		ApiError apiError = new ApiError(HttpStatus.FORBIDDEN, LocalDateTime.now(), e.getMessage());
		return ResponseEntity.status(HttpStatus.FORBIDDEN).body(apiError);
	}

	@ExceptionHandler(SurveyMalformedException.class)
	public ResponseEntity<ApiError> handleUnauthorizedException(SurveyMalformedException e) {
		ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, LocalDateTime.now(), e.getMessage());
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiError);
	}
	
	@ExceptionHandler(DataIntegrityViolationException.class)
	public ResponseEntity<ApiError> handleDataIntegrityViolationException(DataIntegrityViolationException e) {
		String message = "Error de integridad de datos";

		if (e.getRootCause() != null && e.getRootCause().getMessage() != null &&
			e.getRootCause().getMessage().contains("Duplicate entry")) {
			message = "El valor que intentas registrar ya existe en el sistema";
		}

		ApiError apiError = new ApiError(HttpStatus.CONFLICT, LocalDateTime.now(), message);
		return ResponseEntity.status(HttpStatus.CONFLICT).body(apiError);
	}
	
	@ExceptionHandler(MailAuthenticationException.class)
	public ResponseEntity<ApiError> handleMailAuthenticationException(MailAuthenticationException e) {
		ApiError apiError = new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, LocalDateTime.now(),
				"No se pudo autenticar con el servidor de correo. Contacta con el administrador.");
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(apiError);
	}

	@ExceptionHandler(MailSendException.class)
	public ResponseEntity<ApiError> handleMailSendException(MailSendException e) {
		ApiError apiError = new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, LocalDateTime.now(),
				"No se pudo enviar el correo. Inténtalo más tarde.");
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(apiError);
	}

	@ExceptionHandler(MailParseException.class)
	public ResponseEntity<ApiError> handleMailParseException(MailParseException e) {
		ApiError apiError = new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, LocalDateTime.now(),
				"Error al construir el contenido del correo.");
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(apiError);
	}

}
