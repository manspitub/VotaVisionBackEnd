package com.jacaranda.manuel.VotaVision.service;

import java.util.List;
import java.util.Optional;

import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.jacaranda.manuel.VotaVision.dto.UserDto;
import com.jacaranda.manuel.VotaVision.dto.UserDtoConverter;
import com.jacaranda.manuel.VotaVision.exception.InvalidPasswordException;
import com.jacaranda.manuel.VotaVision.exception.MissingFieldException;
import com.jacaranda.manuel.VotaVision.exception.UnauthorizedException;
import com.jacaranda.manuel.VotaVision.exception.UserAlreadyEnabledException;
import com.jacaranda.manuel.VotaVision.exception.UserAlreadyExistsException;
import com.jacaranda.manuel.VotaVision.exception.UserNotFoundException;
import com.jacaranda.manuel.VotaVision.model.Rol;
import com.jacaranda.manuel.VotaVision.model.User;
import com.jacaranda.manuel.VotaVision.repository.UserRepository;
import com.jacaranda.manuel.VotaVision.security.dto.CreateUserDto;

import io.jsonwebtoken.lang.Collections;

@Service
public class UserService implements UserDetailsService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private UserDtoConverter converter;

	@Autowired
	private EmailService emailService;

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

		return this.userRepository.findFirstByEmail(email)
				.orElseThrow(() -> new UsernameNotFoundException("Credenciales Erróneas"));

	}

	public User findUserByEmail(String email) {
		return userRepository.findFirstByEmail(email)
				.orElseThrow(() -> new UsernameNotFoundException("No existe una cuenta con ese correo"));
	}

	public Page<UserDto> getAllUsersPaged(String emailCurrentLogged, int pageNum, int pageSize, String orden,
			String searchTerm, String roleStr // nuevo parámetro opcional recibido del controlador
	) throws Exception {
		if (!isAdminUser(emailCurrentLogged)) {
			throw new UnauthorizedException("No tienes permiso para hacer esto");
		}

		// Validar página mínima
		pageNum = Math.max(1, pageNum);

		// Validar orden con valores permitidos
		List<String> validOrders = List.of("id-Asc", "id-Des", "surname-Asc", "surname-Des", "gender-Asc", "gender-Des",
				"locality-Asc", "locality-Des");
		if (!validOrders.contains(orden)) {
			orden = "id-Asc";
		}

		String[] ordenSplit = orden.split("-");
		String campo = ordenSplit[0];
		String direccion = ordenSplit[1];
		Sort sort = direccion.equalsIgnoreCase("Asc") ? Sort.by(campo).ascending() : Sort.by(campo).descending();

		Pageable pageable = PageRequest.of(pageNum - 1, pageSize, sort);

		Page<User> usersPage;

		boolean hasSearch = searchTerm != null && !searchTerm.isBlank();
		boolean hasRole = roleStr != null && !roleStr.isBlank();

		if (hasRole) {
			Rol role;
			try {
				role = Rol.valueOf(roleStr.toUpperCase()); // convierte el string a enum
			} catch (IllegalArgumentException e) {
				throw new UserNotFoundException("Rol inválido: " + roleStr);
			}

			if (hasSearch) {
				usersPage = userRepository.findByRoleAndEmailContainingIgnoreCaseOrRoleAndNameContainingIgnoreCase(role,
						searchTerm, role, searchTerm, pageable);
			} else {
				usersPage = userRepository.findByRole(role, pageable);
			}

		} else {
			if (hasSearch) {
				usersPage = userRepository.findByEmailContainingIgnoreCaseOrNameContainingIgnoreCase(searchTerm,
						searchTerm, pageable);
			} else {
				usersPage = userRepository.findAll(pageable);
			}
		}

		return usersPage.map(converter::convertUserDto);
	}

	public User addUser(CreateUserDto user) throws Exception {

		checkEmail(user);
		checkPass(user);

		User newUser = new User();
		newUser.setAge(user.getAge());
		newUser.setCity(user.getCity());
		newUser.setConfirmed(false);
		newUser.setEmail(user.getEmail());
		newUser.setName(user.getName());
		newUser.setSurname(user.getSurname());
		newUser.setProfession(user.getProfession());
		newUser.setPassword(passwordEncoder.encode(user.getPassword()));

		// Verifica si hay un usuario logueado y si es ADMIN
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication != null && authentication.isAuthenticated()
				&& !"anonymousUser".equals(authentication.getPrincipal())) {

			// Recuperar el usuario autenticado
			String currentEmail = authentication.getName();
			User currentUser = userRepository.findFirstByEmail(currentEmail).orElse(null);

			if (currentUser != null && currentUser.getRole() == Rol.ADMIN) {
				newUser.setRole(Rol.CREATOR); // Solo si el logueado es ADMIN
			}
		}

		return userRepository.save(newUser);
	}

	public User updateUser(Long id, CreateUserDto updatedUser, String emailCurrentUser) throws Exception {

		Optional<User> existingUserOpt = userRepository.findById(id);
		if (existingUserOpt.isEmpty()) {
			throw new UserNotFoundException("Usuario no encontrado");
		}
		User user = existingUserOpt.get();

		// Si no es admin Y está intentando editar a otro usuario → excepción
		if (!isAdminUser(emailCurrentUser) && !user.getEmail().equals(emailCurrentUser)) {
			throw new UnauthorizedException("No tienes permisos para actualizar usuarios");
		}

		if (updatedUser.getEmail() != null) {
			checkEmailUpdate(user, updatedUser.getEmail());
			user.setEmail(updatedUser.getEmail());
		}

		if (updatedUser.getPassword() != null) {
			checkPass(updatedUser); // Validación de nueva contraseña

			if (updatedUser.getCurrentPassword() == null || updatedUser.getCurrentPassword().isBlank()) {
				throw new InvalidPasswordException("Debes proporcionar tu contraseña actual para cambiarla");
			}

			if (!passwordEncoder.matches(updatedUser.getCurrentPassword(), user.getPassword())) {
				throw new InvalidPasswordException("La contraseña actual no es correcta");
			}

			user.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
		}

		if (updatedUser.getName() != null) {
			user.setName(updatedUser.getName());
		}
		if (updatedUser.getSurname() != null) {
			user.setSurname(updatedUser.getSurname());
		}
		if (updatedUser.getAge() != null) {
			user.setAge(updatedUser.getAge());
		}
		if (updatedUser.getCity() != null) {
			user.setCity(updatedUser.getCity());
		}
		if (updatedUser.getProfession() != null) {
			user.setProfession(updatedUser.getProfession());
		}
		if (updatedUser.getRole() != null) {
			user.setRole(Rol.valueOf(updatedUser.getRole()));
		}

		return userRepository.save(user);

	}

	public void deleteUser(Long id, String emailCurrentUser) throws Exception {
		User currentUser = userRepository.findFirstByEmail(emailCurrentUser).orElseThrow(
				() -> new UserNotFoundException("Usuario actual con email '" + emailCurrentUser + "' no encontrado"));

		User userToDelete = userRepository.findById(id)
				.orElseThrow(() -> new UserNotFoundException("Usuario a eliminar con ID '" + id + "' no encontrado"));

		// Si no es admin y está intentando borrar a otro usuario
		if (!isAdminUser(emailCurrentUser) && !userToDelete.getEmail().equals(emailCurrentUser)) {
			throw new UnauthorizedException(
					"No tienes permisos para eliminar al usuario con email '" + userToDelete.getEmail() + "'");
		}

		userRepository.deleteById(id);

		// Enviar email de confirmación
		emailService.sendUserDeletionEmail(userToDelete.getEmail(), userToDelete.getName(), userToDelete.getSurname());

	}

	public String confirmUserAccount(UsernamePasswordAuthenticationToken authentication) {
		String email = authentication.getName(); // Extraemos el email del usuario

		// Buscar usuario por email
		Optional<User> userOpt = userRepository.findFirstByEmail(email);
		if (userOpt.isEmpty()) {
			throw new UsernameNotFoundException("Usuario no encontrado");
		}

		User user = userOpt.get();

		if (user.getConfirmed()) {
			throw new UserAlreadyEnabledException("Ya has confirmado tu email");
		}

		user.setConfirmed(true);
		userRepository.save(user);

		return "Cuenta activada con éxito";
	}

	/**
	 * Restablece la contraseña de un usuario.
	 * 
	 * @throws Exception
	 */
	public void resetPassword(String email, String newPassword, String confirmPassword) throws Exception {
		if (!newPassword.equals(confirmPassword)) {
			throw new InvalidPasswordException("Las contraseñas no coinciden");
		}

		// Buscar usuario por email
		User user = findUserByEmail(email);
		CreateUserDto userEditPass = new CreateUserDto(newPassword, confirmPassword);
		checkPass(userEditPass);

		// Actualizar la contraseña
		user.setPassword(passwordEncoder.encode(newPassword));
		userRepository.save(user);
	}

	private void checkPass(CreateUserDto user) throws Exception {
		if (!user.getPassword().equals(user.getPassword2())) {
			throw new InvalidPasswordException("Las contraseñas no coinciden");
		}

		// Expresión regular para validar la seguridad de la contraseña
		String passwordRegex = "^(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$";

		if (!user.getPassword().matches(passwordRegex)) {
			throw new InvalidPasswordException(
					"La contraseña debe tener al menos 8 caracteres, incluyendo una mayúscula, un número y un carácter especial.");
		}
	}

	private void checkEmail(CreateUserDto user) throws Exception {
		if (userRepository.findFirstByEmail(user.getEmail()).isPresent()) {
			throw new UserAlreadyExistsException("El email ya esta registrado");
		}
	}

	private void checkEmailUpdate(User user, String newEmail) throws Exception {
		if (!user.getEmail().equals(newEmail) && userRepository.findFirstByEmail(newEmail).isPresent()) {
			throw new Exception("El email ya está registrado por otro usuario");
		}
	}

	public boolean isAdminUser(String emailCurrentUser) throws Exception {
		Optional<User> currentUser = userRepository.findFirstByEmail(emailCurrentUser);

		if (currentUser.isEmpty()) {
			throw new Exception("Usuario actual no encontrado");
		}

		return currentUser.get().getRole().equals(Rol.ADMIN);
	}

}
