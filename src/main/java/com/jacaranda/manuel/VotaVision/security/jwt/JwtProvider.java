package com.jacaranda.manuel.VotaVision.security.jwt;

import java.security.Key;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import com.jacaranda.manuel.VotaVision.exception.PasswordResetTokenInvalidException;
import com.jacaranda.manuel.VotaVision.model.User;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;

@Service
public class JwtProvider {

	public static final String TOKEN_TYPE = "JWT";
	public static final String TOKEN_HEADER = "Authorization";
	public static final String TOKEN_PREFIX = "Bearer ";

	@Value("${jwt.secret}")
	private String jwtSecret;

	@Value("${jwt.duration:3600}") // 1 hora (3600 seconds)
	private int jwtLifeInSeconds;

	private JwtParser parser;

	private Key key;

	@PostConstruct
	public void init() {
		this.key = Keys.hmacShaKeyFor(jwtSecret.getBytes());
	}

	public String generateToken(Authentication authentication) {

		User user = (User) authentication.getPrincipal();

		Map<String, Object> payload = new HashMap<String, Object>();

		payload.put("email", user.getEmail());
		payload.put("role", user.getRole().toString());

		Date tokenExpirationDate = Date
				.from(LocalDateTime.now().plusSeconds(jwtLifeInSeconds).atZone(ZoneId.systemDefault()).toInstant());
		return "Bearer " + Jwts.builder().subject(user.getEmail()).issuedAt(tokenExpirationDate).claims(payload)
				.signWith(key).compact();
	}

	public UsernamePasswordAuthenticationToken decodeToken(String token) {
		if (token == null || !token.startsWith("Bearer ")) {
			throw new MalformedJwtException("Formato de token incorrecto");
		}

		// Eliminar "Bearer " del token
		token = token.substring(7);

		// Decodificar el JWT
		Claims claims = Jwts.parser().verifyWith(Keys.hmacShaKeyFor(jwtSecret.getBytes())).build()
				.parseSignedClaims(token).getPayload();

		// Extraer datos del token
		String username = claims.getSubject();
		String role = (String) claims.get("role");

		// Crear la lista de autoridades
		List<SimpleGrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority(role));

		// Retornar el objeto de autenticación con credenciales null (Spring maneja la
		// autenticación)
		return new UsernamePasswordAuthenticationToken(username, null, authorities);
	}

//	 public Integer getUserIdFromJwt(String token) {
//		    return Integer.parseInt(Jwts.parser()
//		            .setSigningKey(key)
//		            .build()
//		            .parseClaimsJws(token)
//		            .getBody()
//		            .getSubject());
//		}

	public UsernamePasswordAuthenticationToken getAuthentication(String token) {

		Claims claims = Jwts.parser().verifyWith(Keys.hmacShaKeyFor(jwtSecret.getBytes())).build()
				.parseSignedClaims(token.substring(7)).getPayload();

		String username = claims.getSubject();
		String role = claims.get("role", String.class); // Extrae el rol correctamente

		List<SimpleGrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority(role));

		return new UsernamePasswordAuthenticationToken(username, null, authorities);
	}

	public String generatePasswordResetToken(User user) {
		Map<String, Object> claims = new HashMap<>();
		claims.put("email", user.getEmail());
		claims.put("type", "password_reset");

		Date expirationDate = Date.from(LocalDateTime.now().plusMinutes(30) // Token válido por 30 min
				.atZone(ZoneId.systemDefault()).toInstant());

		return Jwts.builder().subject(user.getEmail()).issuedAt(new Date()).expiration(expirationDate).claims(claims)
				.signWith(key).compact();
	}

	public String validatePasswordResetToken(String token) {
		try {
			Claims claims = Jwts.parser().verifyWith(Keys.hmacShaKeyFor(jwtSecret.getBytes())).build()
					.parseSignedClaims(token).getPayload();

			String tokenType = claims.get("type", String.class);
			if (!"password_reset".equals(tokenType)) {
				throw new PasswordResetTokenInvalidException("Token inválido para restablecimiento de contraseña");
			}

			return claims.getSubject(); // Retorna el email del usuario

		} catch (ExpiredJwtException e) {
			throw new PasswordResetTokenInvalidException("El token de recuperación ha expirado");
		} catch (JwtException e) {
			throw new PasswordResetTokenInvalidException("Token inválido");
		}
	}

}
