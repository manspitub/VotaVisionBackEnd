package com.jacaranda.manuel.VotaVision.security.jwt;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.jacaranda.manuel.VotaVision.service.UserService;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthorizationFilter extends OncePerRequestFilter {

	// Crear clases necesarias
	@Autowired
	private UserService userService;
	@Autowired
	private JwtProvider jwtProvider;

	private String getJwtFromRequest(HttpServletRequest request) {
		String bearerToken = request.getHeader(jwtProvider.TOKEN_HEADER);

		if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(JwtProvider.TOKEN_PREFIX))
			return bearerToken.substring(JwtProvider.TOKEN_PREFIX.length());

		return null;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		String token = request.getHeader("Authorization");
		if (token != null) {
			try {

				UsernamePasswordAuthenticationToken authentication = jwtProvider.getAuthentication(token);
				SecurityContextHolder.getContext().setAuthentication(authentication);
			} catch (ExpiredJwtException e) {
				throw new RuntimeException("Token expirado");
			} catch (MalformedJwtException e) {
				throw new MalformedJwtException("Token mal formado");
			} catch (Exception e) {
				throw new RuntimeException("Error en autenticación: " + e.getMessage());
			}
		}
		filterChain.doFilter(request, response);
	}

}
