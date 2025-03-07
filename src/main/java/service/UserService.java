package service;

import controller.UserController;
import entity.UserData;
import exception.ApplicationException;
import exception.ErrorCode;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import repository.TeamRepository;
import repository.UserRepository;
import repository.UsersTeamsRepository;

import java.time.LocalDateTime;
import java.util.List;

import static controller.UserController.isValidEmail;
import static utils.Helpers.throwAndLogError;


@Service
public class UserService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private TeamRepository teamRepository;

	@Autowired
	private UsersTeamsRepository usersTeamsRepository;

	@Autowired
	private AuditLogHeaderService auditLogHeaderService;

	@Autowired
	private EntityManager entityManager;

	private final Logger logger = LogManager.getLogger(UserController.class);

	@PreAuthorize("hasAuthority('ROLE_ADMIN')")
	@Transactional
	private void saveUser(UserData user, ApplicationException exception) {
		try {
			userRepository.save(user);
		} catch (ApplicationException e) {
			logger.error("Failed to create user: {}", e.getMessage(), e);
			throw throwAndLogError(logger, new ApplicationException(ErrorCode.FAILED_TO_CREATE_USER, e));
		}
	}

	public UserData getById(Long id) {
		return userRepository.findById(id)
				.orElseThrow(() -> throwAndLogError(logger, new ApplicationException(ErrorCode.USER_NOT_FOUND, id)));
	}

	public List<UserData> getAll() {
		return userRepository.findAll();
	}

	@PreAuthorize("hasAuthority('ROLE_ADMIN')")
	public Page<UserData> getAllPaginated(org.springframework.data.domain.Pageable pageable) {
		return userRepository.findAll(pageable);
	}

	public List<UserData> getByFirstNameAndLastNameAndEmail(String firstName, String lastName, String email) {
		List<UserData> userData = userRepository.findByFirstNameAndLastNameAndEmail(firstName, lastName, email);

		if (userData.isEmpty()) {
			throw throwAndLogError(logger, new ApplicationException(ErrorCode.NO_USERS_FOUND_WITH_CRITERIA));
		}

		return userData;
	}

	public UserData getByEmail(String email) {
		if (!isValidEmail(email)) {
			throw throwAndLogError(logger, new ApplicationException(ErrorCode.INVALID_EMAIL_FORMAT, email));
		}

		UserData userData = userRepository.findByEmail(email);

		if (userData == null) {
			throw throwAndLogError(logger, new ApplicationException(ErrorCode.NO_USERS_FOUND_WITH_CRITERIA));
		}

		return userData;
	}

	@PreAuthorize("hasAuthority('ROLE_ADMIN')")
	public Long createUser(UserData userData) {
		if (userRepository.findByEmail(userData.getEmail()) != null) {
			throw throwAndLogError(logger, new ApplicationException(ErrorCode.EMAIL_IS_TAKEN, userData.getEmail()));
		}

		userData.setCreationDate(LocalDateTime.now());
		saveUser(userData, new ApplicationException(ErrorCode.FAILED_TO_CREATE_USER));

		logger.info("User with id {} has been successfully created.", userData.getId());
		return userData.getId();
	}

	//@PreAuthorize("hasAuthority('ROLE_ADMIN')")
	public void changePassword(UserData updatedUser) {
		updatedUser.setModificationDate(LocalDateTime.now());

		entityManager.detach(updatedUser);
		UserData user = getById(updatedUser.getId());
		auditLogHeaderService.createObjectChange(user, updatedUser, findAdmin(), user.getClass().getSimpleName(), user.getId());
		saveUser(updatedUser, new ApplicationException(ErrorCode.FAILED_TO_CHANGE_PASSWORD));

		logger.info("User password successfully changed.");
	}

	//@PreAuthorize("hasAuthority('ROLE_ADMIN')")
	@Transactional
	public void updateUser(UserData updatedUser) {
		updatedUser.setModificationDate(LocalDateTime.now());
		updatedUser.getTeams();
		entityManager.detach(updatedUser);

		UserData user = getById(updatedUser.getId());
		auditLogHeaderService.createObjectChange(user, updatedUser, findAdmin(), user.getClass().getSimpleName(), user.getId());

		saveUser(updatedUser, new ApplicationException(ErrorCode.FAILED_TO_UPDATE_USER));

		logger.info("User with id {} has been successfully updated.", updatedUser.getId());
	}

	@PreAuthorize("hasAuthority('ROLE_ADMIN')")
	public void deleteUser(Long id) {
		UserData userData = getById(id);

		userData.setDeletionDate(LocalDateTime.now());
		saveUser(userData, new ApplicationException(ErrorCode.FAILED_TO_DELETE_USER));

		logger.info("User with id {} was successfully deleted.", id);
	}

	public List<UserData> searchUsersNotInTeam(Long teamId, String firstName, String lastName, String email) {
		teamRepository.findById(teamId)
				.orElseThrow(() -> throwAndLogError(logger, new ApplicationException(ErrorCode.TEAM_NOT_FOUND, teamId)));

		List<UserData> usersNotInTeam = usersTeamsRepository.searchUsersNotInTeam(teamId, firstName, lastName, email);

		if (usersNotInTeam.isEmpty()) {
			throw throwAndLogError(logger, new ApplicationException(ErrorCode.NO_USERS_FOUND_WITH_CRITERIA));
		}

		return usersNotInTeam;
	}

	public Page<UserData> searchUsers(
			String firstName, String lastName, String email, String roleName,
			LocalDateTime creationLowerDate, LocalDateTime creationUpperDate,
			LocalDateTime modificationLowerDate, LocalDateTime modificationUpperDate,
			LocalDateTime deletionLowerDate, LocalDateTime deletionUpperDate,
			Pageable pageable
	) {
		return userRepository.searchUsers(
				firstName, lastName, email, roleName,
				creationLowerDate, creationUpperDate,
				modificationLowerDate, modificationUpperDate,
				deletionLowerDate, deletionUpperDate, pageable);
	}

	public List<UserData> findByRole(String roleName) {
		return userRepository.findByRoleName(roleName.toUpperCase());
	}

	@PreAuthorize("hasAuthority('ROLE_ADMIN')")
	public Long findAdmin() {
		List<UserData> admins = findByRole("admin");
		if (!admins.isEmpty()) {
			return admins.getFirst().getId();
		} else {
			logger.error("Could not find any admin for creatorId.");
			throw new NullPointerException();
		}
	}

	public boolean existsByEmail(String email) {
		return userRepository.existsByEmail(email);
	}

}
