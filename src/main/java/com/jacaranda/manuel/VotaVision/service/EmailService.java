package com.jacaranda.manuel.VotaVision.service;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailService {

	@Autowired
	private JavaMailSender mailSender;

	@Value("${app.frontend-base-url}")
	private String frontendBaseUrl;

	public void sendConfirmationEmail(String to, String name, String surname, String token, boolean isAdmin,
			String rawPassword) throws MessagingException {
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

		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message, true);

		helper.setTo(to);
		helper.setSubject(subject);
		helper.setText(content.toString(), true); // true para HTML

		mailSender.send(message);
	}

	public void sendPasswordRecoveryEmail(String to, String token) throws MessagingException {
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

		// Configurar el correo electrónico
		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message, true);

		helper.setTo(to);
		helper.setSubject(subject);
		helper.setText(content, true); // `true` para contenido HTML

		mailSender.send(message);
	}

	public void sendUserDeletionEmail(String to, String name, String surname) throws MessagingException {
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

		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message, true);

		helper.setTo(to);
		helper.setSubject(subject);
		helper.setText(content, true); // HTML habilitado

		mailSender.send(message);
	}
	
	public void sendSurveyDeletionByAdminEmail(String to, String creatorName, String creatorSurname, String surveyTitle, String reason, String adminName, String adminEmail) throws MessagingException {
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

	    MimeMessage message = mailSender.createMimeMessage();
	    MimeMessageHelper helper = new MimeMessageHelper(message, true);

	    helper.setTo(to);
	    helper.setSubject(subject);
	    helper.setText(content, true);

	    mailSender.send(message);
	}
	
	
	public void sendSurveyReportAlertToAdmins(String adminEmail, String reporterName, String reporterEmail, String surveyTitle, String reason) throws MessagingException {

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

    MimeMessage message = mailSender.createMimeMessage();
    MimeMessageHelper helper = new MimeMessageHelper(message, true);

    helper.setTo(adminEmail); // O lista dinámica de admins
    helper.setSubject(subject);
    helper.setText(content, true);

    mailSender.send(message);
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
