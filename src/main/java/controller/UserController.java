package controller;

import dto.user.*;
import entity.RoleData;
import entity.UserData;
import exception.ApplicationException;
import exception.ErrorCode;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import service.*;

import java.util.stream.Collectors;

import static utils.Helpers.throwAndLogError;


@RestController
@RequestMapping("/users")
public class UserController {

	@Autowired
	private UserService userService;

	@Autowired
	private RoleService roleService;

	@Autowired
	private TeamService teamService;
	@Autowired
	private PasswordService passwordService;

	@Autowired
	private PasswordUtil passwordUtil;

	private final Logger logger = LogManager.getLogger(UserController.class);

	@GetMapping("/{id}")
	public UserDto getById(@PathVariable("id") Long id) {
		validateId(id);
		return UserDto.toDto(userService.getById(id));
	}

	public void validateId(Long id) {
		if (id == null) {
			throw throwAndLogError(logger, new ApplicationException(ErrorCode.NULL_VALUE_FORBIDDEN, "Id"));
		}
	}

	@GetMapping("/paginated")
	public ResponseEntity<Map<String, Object>> getPaginatedUsers(
			@RequestParam(defaultValue = "0", name = "page") int page,
			@RequestParam(defaultValue = "10", name = "size") int size
	) {
		Pageable paging = PageRequest.of(page, size);
		Page<UserData> pagedUsers = userService.getAllPaginated(paging);

		List<UserDetailsDto> usersDto = pagedUsers.getContent().stream()
				.map(UserDetailsDto::toDto)
				.collect(Collectors.toList());

		Map<String, Object> response = new HashMap<>();
		response.put("users", usersDto);
		response.put("currentPage", pagedUsers.getNumber());
		response.put("totalItems", pagedUsers.getTotalElements());
		response.put("totalPages", pagedUsers.getTotalPages());

		return ResponseEntity.ok(response);
	}

	@GetMapping("/searchByEmail")
	public UserDto getByEmail(@RequestParam("email") String email) {
		isValidEmail(email);
		logger.info("Email successfully validated: {}.", email);
		return UserDto.toDto(userService.getByEmail(email));
	}

	private static final String EMAIL_REGEX = "^(?=.{1,64}@)[A-Za-z0-9._-]+(?<!\\.)@[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";
	private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);

	public static boolean isValidEmail(String email) {
		if (email == null || email.length() > 254) {
			return false;
		}
		Matcher matcher = EMAIL_PATTERN.matcher(email);
		return matcher.matches();
	}

	@GetMapping("/search")
	public List<UserDto> getByFirstNameAndLastNameAndEmail(
			@RequestParam(required = false, name = "firstName") String firstName,
			@RequestParam(required = false, name = "lastName") String lastName,
			@RequestParam(required = false, name = "email") String email) {
		validateCriteria(firstName, lastName, email);
		logger.info("Criteria for user search successfully validated.");
		return userService.getByFirstNameAndLastNameAndEmail(firstName, lastName, email)
				.stream()
				.map(UserDto::toDto)
				.collect(Collectors.toList());
	}

	private void validateCriteria(String firstName, String lastName, String email) {
		if (firstName == null && lastName == null && email == null) {
			throw throwAndLogError(logger, new ApplicationException(ErrorCode.EMPTY_SEARCH_CRITERIA));
		}
	}

	private void validateUserCreateDto(UserCreateDto userCreateDto) {
		if (userCreateDto.getFirstName() == null || userCreateDto.getFirstName().isEmpty()) {
			throw throwAndLogError(logger, new ApplicationException(ErrorCode.NULL_VALUE_FORBIDDEN, "firstName"));
		}

		if (userCreateDto.getFirstName().length() > 100) {
			throw throwAndLogError(logger, new ApplicationException(ErrorCode.TOO_LONG_VALUE, "firstName", userCreateDto.getFirstName().length(), 100));
		}

		if (userCreateDto.getLastName() == null || userCreateDto.getLastName().isEmpty()) {
			throw throwAndLogError(logger, new ApplicationException(ErrorCode.NULL_VALUE_FORBIDDEN, "lastName"));
		}

		if (userCreateDto.getLastName().length() > 100) {
			throw throwAndLogError(logger, new ApplicationException(ErrorCode.TOO_LONG_VALUE, "lastName", userCreateDto.getLastName().length(), 100));
		}

		if (userCreateDto.getEmail() == null || userCreateDto.getEmail().isEmpty()) {
			throw throwAndLogError(logger, new ApplicationException(ErrorCode.NULL_VALUE_FORBIDDEN, "email"));
		}
		if (!isValidEmail(userCreateDto.getEmail())) {
			throw throwAndLogError(logger, new ApplicationException(ErrorCode.INVALID_EMAIL_FORMAT, "email"));
		}
	}

	@PutMapping("/update")
	@Transactional
	public void updateUser(@Valid @RequestBody UserUpdateDto userUpdateDto) {
		validateUserUpdateDto(userUpdateDto);
		logger.info("UserUpdateDto successfully validated.");

		UserData userData = userService.getById(userUpdateDto.getId());

		userData.setFirstName(userUpdateDto.getFirstName());
		userData.setLastName(userUpdateDto.getLastName());
		userData.setEmail(userUpdateDto.getEmail());
		userData.setRoleId(userUpdateDto.getRoleId());

		userService.updateUser(userData);
	}

	private void validateUserUpdateDto(UserUpdateDto userUpdateDto) {
		if (userUpdateDto.getId() == null) {
			throw throwAndLogError(logger, new ApplicationException(ErrorCode.NULL_VALUE_FORBIDDEN, "id"));
		}

		if (userUpdateDto.getFirstName() != null) {
			if (userUpdateDto.getFirstName().length() > 100) {
				throw throwAndLogError(logger, new ApplicationException(ErrorCode.TOO_LONG_VALUE, "firstName", userUpdateDto.getFirstName().length(), 100));
			}
		}

		if (userUpdateDto.getLastName() != null) {
			if (userUpdateDto.getLastName().length() > 100) {
				throw throwAndLogError(logger, new ApplicationException(ErrorCode.TOO_LONG_VALUE, "lastName", userUpdateDto.getLastName().length(), 100));
			}
		}

		if (userUpdateDto.getEmail() != null) {
			if (!isValidEmail(userUpdateDto.getEmail())) {
				throw throwAndLogError(logger, new ApplicationException(ErrorCode.INVALID_EMAIL_FORMAT, "email"));
			}
		}

		if (!isValidEmail(userUpdateDto.getEmail())) {
			throw throwAndLogError(logger, new ApplicationException(ErrorCode.INVALID_EMAIL_FORMAT, userUpdateDto.getEmail()));
		}
	}

	@PostMapping("/changePassword")
	public ResponseEntity<String> changePassword(@RequestBody PasswordChangeDto passwordChangeDto) throws NoSuchAlgorithmException, InvalidKeySpecException {
		validateId(passwordChangeDto.getUserId());
		UserData userData = userService.getById(passwordChangeDto.getUserId());

		if (!verifyPassword(passwordChangeDto.getCurrentPassword(), userData)) {
			throw throwAndLogError(logger, new ApplicationException(ErrorCode.INCORRECT_PASSWORD));
		}
		if (verifyPassword(passwordChangeDto.getNewPassword(), userData)) {
			throw throwAndLogError(logger, new ApplicationException(ErrorCode.PASSWORD_SAME_AS_OLD));
		}

		String newSalt = PasswordUtil.generateSalt();
		String newHash = PasswordUtil.hashPassword(passwordChangeDto.getNewPassword(), newSalt);


		userData.setPassword(newHash);
		userData.setSalt(newSalt);

		userData.setModificationDate(LocalDateTime.now());

		userService.changePassword(userData);
		return ResponseEntity.ok("Password changed successfully.");
	}

	private boolean verifyPassword(String password, UserData userData) throws NoSuchAlgorithmException, InvalidKeySpecException {
		String salt = userData.getSalt();
		String hashCurrentPassword = PasswordUtil.hashPassword(password, salt);
		return hashCurrentPassword.equals(userData.getPassword());
	}


	@DeleteMapping("/delete")
	public void deleteUser(@RequestParam("id") @NotNull Long id) {
		validateId(id);
		userService.deleteUser(id);
	}

	@GetMapping("/searchUsersNotInTeam")
	public List<UserDto> searchUsersNotInTeam(@RequestParam("teamId") Long teamId,
											  @RequestParam(required = false, name = "firstName") String firstName,
											  @RequestParam(required = false, name = "lastName") String lastName,
											  @RequestParam(required = false, name = "email") String email) {
		validateId(teamId);
		List<UserData> usersNotInTeam = userService.searchUsersNotInTeam(teamId, firstName, lastName, email);
		return usersNotInTeam
				.stream()
				.map(UserDto::toDto)
				.collect(Collectors.toList());
	}

	@GetMapping("/findByRole")
	public List<UserDetailsDto> findByRole(@RequestParam("roleName") String roleName) {
		List<UserData> role = userService.findByRole(roleName);
		return role
				.stream()
				.map(UserDetailsDto::toDto)
				.collect(Collectors.toList());
	}

	@GetMapping("/allUsers")
	public ResponseEntity<Map<String, Object>> searchUsers(
			@RequestParam(required = false, name = "firstName") String firstName,
			@RequestParam(required = false, name = "lastName") String lastName,
			@RequestParam(required = false, name = "email") String email,
			@RequestParam(required = false, name = "roleName") String roleName,
			@RequestParam(required = false, name = "creationLowerDate") LocalDateTime creationLowerDate,
			@RequestParam(required = false, name = "creationUpperDate") LocalDateTime creationUpperDate,
			@RequestParam(required = false, name = "modificationLowerDate") LocalDateTime modificationLowerDate,
			@RequestParam(required = false, name = "modificationUpperDate") LocalDateTime modificationUpperDate,
			@RequestParam(required = false, name = "deletionLowerDate") LocalDateTime deletionLowerDate,
			@RequestParam(required = false, name = "deletionUpperDate") LocalDateTime deletionUpperDate,
			@RequestParam(defaultValue = "0", name = "page") int page,
			@RequestParam(defaultValue = "10", name = "size") int size
	) {
		Pageable pageable = PageRequest.of(page, size);

		Page<UserData> pagedUsers = userService.searchUsers(firstName, lastName, email, roleName,
				creationLowerDate, creationUpperDate,
				modificationLowerDate, modificationUpperDate,
				deletionLowerDate, deletionUpperDate, pageable);

		List<UserDetailsDto> usersDto = pagedUsers.getContent().stream()
				.map(UserDetailsDto::toDto)
				.collect(Collectors.toList());

		Map<String, Object> response = new HashMap<>();
		response.put("users", usersDto);
		response.put("currentPage", pagedUsers.getNumber());
		response.put("totalItems", pagedUsers.getTotalElements());
		response.put("totalPages", pagedUsers.getTotalPages());

		return ResponseEntity.ok(response);
	}

	@GetMapping("/getAllRoles")
	public ResponseEntity<List<RoleData>> getAllRoles() {
		List<RoleData> roles = roleService.getAllRoles();
		return ResponseEntity.ok(roles);
	}

	@GetMapping("/teams/{userId}")
	public ResponseEntity<List<TeamDto>> getUserTeams(@PathVariable("userId") Long userId) {
		List<TeamDto> team = teamService.findTeamsByUserId(userId);
		return ResponseEntity.ok(team);
	}
}
