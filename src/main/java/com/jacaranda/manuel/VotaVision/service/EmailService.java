package com.jacaranda.manuel.VotaVision.service;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.mail.MailSendException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import jakarta.annotation.PostConstruct;

@Service
public class EmailService {

	private static final Logger log = LoggerFactory.getLogger(EmailService.class);

	@Value("${app.frontend-base-url}")
	private String frontendBaseUrl;

	@Value("${brevo.api-key}")
	private String brevoApiKey;

	@Value("${brevo.sender-name:VotaVision}")
	private String senderName;

	@Value("${brevo.sender-email}")
	private String senderEmail;

	@Value("${brevo.timeout-ms:10000}")
	private int brevoTimeoutMs;

	private RestClient brevoClient;

	@PostConstruct
	private void init() {
		SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
		Duration timeout = Duration.ofMillis(brevoTimeoutMs);
		requestFactory.setConnectTimeout(timeout);
		requestFactory.setReadTimeout(timeout);

		this.brevoClient = RestClient.builder()
				.baseUrl("https://api.brevo.com/v3")
				.requestFactory(requestFactory)
				.defaultHeader("api-key", brevoApiKey)
				.defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
				.defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
				.build();
	}

	public void sendConfirmationEmail(String to, String name, String surname, String token, boolean isAdmin,
			String rawPassword) {
		String subject = isAdmin ? "🛠 Has sido registrado como Creador en Votavision"
				: "🔥 ¡Activa tu cuenta ahora y únete a nuestra comunidad!";

		String confirmationUrl = buildFrontendUrl("/verify-email?token=" + urlEncode(token));

		StringBuilder content = new StringBuilder();
		content.append("<div style='font-family: Arial, sans-serif; max-width: 600px; margin: 0 auto; padding: 20px; ")
				.append("border: 1px solid #ddd; border-radius: 10px; background-color: #f9f9f9; text-align: center;'>")
				.append("<h2 style='color: #333;'>¡Hola ").append(name).append(" ").append(surname).append("!</h2>");

		if (isAdmin) {
			content.append(
					"<p style='font-size: 16px; color: #555;'>Un administrador te ha dado de alta como <strong>creador</strong> en Votavision.</p>")
					.append("<p style='font-size: 16px; color: #555;'>Estos son tus datos de acceso:</p>")
					.append("<ul style='list-style: none; padding: 0; font-size: 16px; color: #333;'>")
					.append("<li><strong>Correo electrónico:</strong> ").append(to).append("</li>")
					.append("<li><strong>Contraseña:</strong> ").append(rawPassword).append("</li>").append("</ul>");
		} else {
			content.append("<p style='font-size: 16px; color: #555;'>Gracias por registrarte en nuestra plataforma. "
					+ "Para completar tu registro y disfrutar de todas las funciones, por favor confirma tu cuenta haciendo clic en el siguiente botón:</p>");
		}

		content.append("<a href='").append(confirmationUrl)
				.append("' style='display: inline-block; padding: 12px 20px; margin: 20px 0; font-size: 18px; ")
				.append("color: #fff; background-color: #28a745; text-decoration: none; border-radius: 5px;'>✅ Activar mi cuenta</a>")
				.append("<p style='font-size: 14px; color: #777;'>Si no solicitaste esta activación, ignora este correo. Tu cuenta permanecerá segura.</p>")
				.append("<hr style='margin: 20px 0;'>")
				.append("<p style='font-size: 14px; color: #999;'>Saludos,<br><strong>El equipo de Soporte</strong></p>")
				.append("</div>");

		sendEmail(to, subject, content.toString());
	}

	public void sendPasswordRecoveryEmail(String to, String token) {
		String subject = "🔑 Recuperación de contraseña - Acción requerida";
		String recoveryUrl = buildFrontendUrl("/forgot-password?token=" + urlEncode(token)); // Token generado

		// Contenido del correo con HTML mejorado
		String content = "<div style='font-family: Arial, sans-serif; max-width: 600px; margin: 0 auto; padding: 20px; "
				+ "border: 1px solid #ddd; border-radius: 10px; background-color: #f9f9f9; text-align: center;'>"
				+ "<h2 style='color: #333;'>🔒 ¿Olvidaste tu contraseña?</h2>"
				+ "<p style='font-size: 16px; color: #555;'>No te preocupes, pasa hasta en las mejores familias. "
				+ "Puedes restablecer tu contraseña haciendo clic en el siguiente botón:</p>" + "<a href='"
				+ recoveryUrl + "' style='display: inline-block; padding: 12px 20px; margin: 20px 0; font-size: 18px; "
				+ "color: #fff; background-color: #007bff; text-decoration: none; border-radius: 5px;'>🔑 Restablecer contraseña</a>"
				+ "<p style='font-size: 14px; color: #777;'>Si no solicitaste este restablecimiento, ignora este correo. "
				+ "Tu cuenta permanecerá segura.</p>" + "<hr style='margin: 20px 0;'>"
				+ "<p style='font-size: 14px; color: #999;'>Saludos,<br><strong>El equipo de Soporte</strong></p>"
				+ "</div>";

		sendEmail(to, subject, content);
	}

	public void sendUserDeletionEmail(String to, String name, String surname) {
		String subject = "🗑 Cuenta eliminada - VotaVision";

		String content = "<div style='font-family: Arial, sans-serif; max-width: 600px; margin: 0 auto; padding: 20px; "
				+ "border: 1px solid #ddd; border-radius: 10px; background-color: #fffbe6; text-align: center;'>"
				+ "<h2 style='color: #d9534f;'>Tu cuenta ha sido eliminada</h2>"
				+ "<p style='font-size: 16px; color: #555;'>Hola <strong>" + name + " " + surname + "</strong>,</p>"
				+ "<p style='font-size: 16px; color: #555;'>Te informamos que tu cuenta en <strong>VotaVision</strong> ha sido eliminada correctamente, junto con todas las encuestas asociadas.</p>"
				+ "<p style='font-size: 14px; color: #999;'>Si esta acción no fue realizada por ti o tienes alguna duda, por favor contacta a nuestro equipo de soporte.</p>"
				+ "<hr style='margin: 20px 0;'>"
				+ "<p style='font-size: 14px; color: #999;'>Saludos,<br><strong>El equipo de Soporte de VotaVision</strong></p>"
				+ "</div>";

		sendEmail(to, subject, content);
	}
	
	public void sendSurveyDeletionByAdminEmail(String to, String creatorName, String creatorSurname, String surveyTitle, String reason, String adminName, String adminEmail) {
	    String subject = "🗑 Encuesta eliminada por moderación - VotaVision";

	    String content = "<div style='font-family: Arial, sans-serif; max-width: 600px; margin: 0 auto; padding: 20px; "
	            + "border: 1px solid #ddd; border-radius: 10px; background-color: #fff3f3; text-align: center;'>"
	            + "<h2 style='color: #d9534f;'>Encuesta eliminada por un moderador</h2>"
	            + "<p style='font-size: 16px; color: #555;'>Hola <strong>" + creatorName + " " + creatorSurname + "</strong>,</p>"
	            + "<p style='font-size: 16px; color: #555;'>Te informamos que tu encuesta titulada <strong>\"" + surveyTitle + "\"</strong> ha sido eliminada por un administrador.</p>"
	            + "<p style='font-size: 16px; color: #d9534f;'><strong>Motivo:</strong> " + reason + "</p>"
	            + "<p style='font-size: 16px; color: #555;'><strong>Moderador responsable:</strong><br>"
	            + adminName + " (" + adminEmail + ")</p>"
	            + "<p style='font-size: 14px; color: #999;'>Si consideras que esta acción fue un error, por favor contacta con nuestro equipo de soporte.</p>"
	            + "<hr style='margin: 20px 0;'>"
	            + "<p style='font-size: 14px; color: #999;'>Saludos,<br><strong>El equipo de Soporte de VotaVision</strong></p>"
	            + "</div>";

	    sendEmail(to, subject, content);
	}
	
	
	public void sendSurveyReportAlertToAdmins(String adminEmail, String reporterName, String reporterEmail, String surveyTitle, String reason) {

    String subject = "🚩 Encuesta reportada por un usuario - Revisión requerida";

    // Codificar el título para usarlo en el enlace
    
    String reviewLink = buildFrontendUrl("/surveys/moderate/" + urlEncode(surveyTitle));

    String content = "<div style='font-family: Arial, sans-serif; max-width: 600px; margin: 0 auto; padding: 20px; "
            + "border: 1px solid #ddd; border-radius: 10px; background-color: #fff3cd;'>"
            + "<h2 style='color: #856404;'>Encuesta reportada por un usuario</h2>"
            + "<p style='font-size: 16px; color: #333;'>El usuario <strong>" + reporterName + "</strong> (" + reporterEmail + ") ha reportado una encuesta para su revisión.</p>"
            + "<p style='font-size: 16px;'><strong>Encuesta reportada:</strong> \"" + surveyTitle + "\"</p>"
            + "<p style='font-size: 16px; color: #d9534f;'><strong>Motivo del reporte:</strong> " + reason + "</p>"
            + "<p style='font-size: 16px;'>Puedes revisarla en el siguiente enlace del panel de administración:</p>"
            + "<p style='margin: 20px 0;'><a href='" + reviewLink + "' style='padding: 10px 15px; background-color: #856404; color: white; text-decoration: none; border-radius: 5px;'>Revisar encuesta</a></p>"
            + "<hr style='margin: 20px 0;'>"
            + "<p style='font-size: 14px; color: #999;'>Este mensaje ha sido generado automáticamente por el sistema VotaVision.</p>"
            + "</div>";

    sendEmail(adminEmail, subject, content);
}

	private void sendEmail(String to, String subject, String htmlContent) {
		Map<String, Object> payload = new HashMap<>();
		payload.put("sender", Map.of("name", senderName, "email", senderEmail));
		payload.put("to", List.of(Map.of("email", to)));
		payload.put("subject", subject);
		payload.put("htmlContent", htmlContent);

		try {
			brevoClient.post()
					.uri("/smtp/email")
					.body(payload)
					.retrieve()
					.toBodilessEntity();
		} catch (RestClientResponseException e) {
			log.error("Brevo rechazó el correo. status={}, body={}, to={}, sender={}",
					e.getStatusCode(), e.getResponseBodyAsString(), to, senderEmail, e);
			throw new MailSendException("No se pudo enviar el correo con Brevo. Respuesta: "
					+ e.getResponseBodyAsString(), e);
		} catch (RestClientException e) {
			log.error("Error conectando con Brevo. to={}, sender={}", to, senderEmail, e);
			throw new MailSendException("No se pudo enviar el correo con Brevo", e);
		}
}

	private String buildFrontendUrl(String path) {
		String baseUrl = frontendBaseUrl.endsWith("/") ? frontendBaseUrl.substring(0, frontendBaseUrl.length() - 1)
				: frontendBaseUrl;
		return baseUrl + path;
	}

	private String urlEncode(String value) {
		return URLEncoder.encode(value, StandardCharsets.UTF_8);
	}

	

}
