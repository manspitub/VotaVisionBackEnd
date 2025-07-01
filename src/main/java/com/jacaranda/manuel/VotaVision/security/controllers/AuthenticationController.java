package com.jacaranda.manuel.VotaVision.security.controllers;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;

import org.springframework.http.ResponseEntity;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jacaranda.manuel.VotaVision.dto.ApiResponse;
import com.jacaranda.manuel.VotaVision.dto.PasswordResetRequest;
import com.jacaranda.manuel.VotaVision.dto.UserDto;
import com.jacaranda.manuel.VotaVision.dto.UserDtoConverter;
import com.jacaranda.manuel.VotaVision.exception.EmailNotConfirmedException;

import com.jacaranda.manuel.VotaVision.model.Rol;
import com.jacaranda.manuel.VotaVision.model.User;

import com.jacaranda.manuel.VotaVision.security.dto.CreateUserDto;
import com.jacaranda.manuel.VotaVision.security.dto.LoginDtoUser;
import com.jacaranda.manuel.VotaVision.security.dto.PasswordRecoveryRequest;
import com.jacaranda.manuel.VotaVision.security.jwt.JwtProvider;
import com.jacaranda.manuel.VotaVision.security.jwt.JwtUserResponse;
import com.jacaranda.manuel.VotaVision.service.EmailService;
import com.jacaranda.manuel.VotaVision.service.UserService;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {

	@Autowired
	private AuthenticationManager authenticationManager;
	@Autowired
	private JwtProvider jwtProvider;
	@Autowired
	private UserService userService;
	@Autowired
	private EmailService emailService;
	@Autowired
	private UserDtoConverter userDtoConverter;

//	@PostMapping("/signin")
//	public ResponseEntity<?> authenticateUser(@RequestBody CreateUserDto loginRequest) throws Exception {
//		Authentication authentication;
//		// Si el usuario y el password que le paso son los adecuados me
//		// devuele un autentication. Si no lo encuentra, lanza una exception
//		try {
//			authentication = authenticationManager.authenticate(
//					new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
//		} catch (Exception e) {
//			throw new Exception(e.getMessage());
//		}
//		User user = (User) authentication.getPrincipal();
//		String jwt = jwtProvider.generateToken(authentication);
//		return ResponseEntity.ok(jwt);
//	}

	@GetMapping("greetings")
	public ResponseEntity<String> greetings() {
		return ResponseEntity.status(HttpStatus.CREATED).body("Ola");
	}

	// Para loguearte debes tener el atributo confirmed a true
	@PostMapping("/login")
	public ResponseEntity<?> loginUser(@RequestBody LoginDtoUser loginDtoUser) {

		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(loginDtoUser.getEmail(), loginDtoUser.getPassword()));

		SecurityContextHolder.getContext().setAuthentication(authentication);
		User user = (User) authentication.getPrincipal();

		// **Verificar si el usuario está confirmado**
		if (!user.getConfirmed()) {
			throw new EmailNotConfirmedException("Aún no has confirmado tu Email");
		}

		String jwt = jwtProvider.generateToken(authentication);

		return ResponseEntity.status(HttpStatus.OK).body(convertUserClientToJwtUserResponse(user, jwt));
	}

	@PostMapping("/signup")
	public ResponseEntity<?> signUpUser(@RequestBody CreateUserDto newUser) throws Exception {
		try {

			User userCreated = userService.addUser(newUser);
			boolean isCreatedByAdmin = userCreated.getRole().equals(Rol.CREATOR);
			Authentication authentication = authenticationManager
					.authenticate(new UsernamePasswordAuthenticationToken(newUser.getEmail(), newUser.getPassword()));

			SecurityContextHolder.getContext().setAuthentication(authentication);
			String jwt = jwtProvider.generateToken(authentication);

			emailService.sendConfirmationEmail(userCreated.getEmail(), userCreated.getName(), userCreated.getSurname(),
					jwt, isCreatedByAdmin, newUser.getPassword());
			return ResponseEntity.status(HttpStatus.CREATED).body(userDtoConverter.convertUserDto(userCreated));
		} catch (Exception e) {
			throw e;
		}
	}

	@GetMapping("/confirm")
	public ResponseEntity<?> confirmAccount(@RequestParam("token") String token) {
		try {
			// Decodificar el token
			UsernamePasswordAuthenticationToken authentication = jwtProvider.decodeToken(token);
			userService.confirmUserAccount(authentication);
			return ResponseEntity.ok(new ApiResponse("Cuenta activada con éxito", HttpStatus.OK.value()));
		} catch (Exception e) {
			throw e;
		}

	}

	@PostMapping("/forgot-password")
	public ResponseEntity<?> sendForgotPasswordEmail(@RequestBody PasswordRecoveryRequest request) throws Exception {

		try {
			// Buscar usuario por email
			User user = userService.findUserByEmail(request.getEmail());

			// Generar token para restablecer contraseña
			String resetToken = jwtProvider.generatePasswordResetToken(user);
			// Enviar correo con el enlace
			emailService.sendPasswordRecoveryEmail(user.getEmail(), resetToken);
			return ResponseEntity.ok(new ApiResponse("Correo de recuperación enviado", HttpStatus.OK.value()));
		} catch (Exception e) {

			throw e;
		}

	}

	@PostMapping("/reset-password")
	public ResponseEntity<?> resetPassword(@RequestParam("token") String token,
			@RequestBody PasswordResetRequest request) throws Exception {
		try {
			// Validar y extraer el email desde el token
			String email = jwtProvider.validatePasswordResetToken(token);

			// Llamar al servicio para actualizar la contraseña
			userService.resetPassword(email, request.getPassword(), request.getConfirmPassword());

			return ResponseEntity.ok(new ApiResponse("Contraseña actualizada correctamente", HttpStatus.OK.value()));
		} catch (Exception e) {
			throw e;
		}

	}

	@PutMapping("/users/{id}")
	public ResponseEntity<?> updateUser(@PathVariable Long id, @RequestBody CreateUserDto updatedUser)
			throws Exception {
		try {
			String emailCurrentUser = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			User user = userService.updateUser(id, updatedUser, emailCurrentUser);
			return ResponseEntity.ok(userDtoConverter.convertUserDto(user));
		} catch (Exception e) {
			throw e;
		}
	}

	@DeleteMapping("/users/{id}")
	public ResponseEntity<?> deleteUser(@PathVariable Long id) throws Exception {
		try {
			String emailCurrentUser = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

			userService.deleteUser(id, emailCurrentUser);

			return ResponseEntity.ok(new ApiResponse("Usuario eliminado correctamente", HttpStatus.OK.value()));

		} catch (Exception e) {
			throw e;
		}
	}

	@GetMapping("/users")
	public ResponseEntity<?> getAllUsers(
	        @RequestParam(defaultValue = "1") int page,
	        @RequestParam(defaultValue = "10") int size,
	        @RequestParam(defaultValue = "id-Asc") String sort,
	        @RequestParam(defaultValue = "") String search,
	        @RequestParam(defaultValue = "") String role
	) throws Exception {
	    try {
	        String emailCurrentUser = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

	        Page<UserDto> usersPage = userService.getAllUsersPaged(emailCurrentUser, page, size, sort, search, role);

	        return ResponseEntity.ok(usersPage);
	    } catch (Exception e) {
	        throw e;
	    }
	}


	@GetMapping("/users/me")
	public ResponseEntity<?> getMyUser() throws Exception {
		try {
			String emailCurrentUser = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			User user = userService.findUserByEmail(emailCurrentUser);
			return ResponseEntity.ok(userDtoConverter.convertUserDto(user));
		} catch (Exception e) {
			throw e;
		}
	}

	private JwtUserResponse convertUserClientToJwtUserResponse(User user, String jwt) {
		JwtUserResponse response = new JwtUserResponse(user.getName(), // name
				user.getEmail(), // email
				user.getRole().toString(), // role
				user.getSurname(), // surname
				user.getConfirmed().booleanValue(), // confirmed (assuming there's a method isConfirmed() in User)
				jwt // token
		);
		return response;
	}

	private JwtUserResponse convertUserToJwtUserResponseWithoutToken(User user) {
		return new JwtUserResponse(user.getName(), user.getEmail(), user.getRole().toString(), user.getSurname(),
				user.getConfirmed());
	}
}
