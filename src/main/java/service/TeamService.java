package service;

import dto.user.TeamDto;
import entity.TeamData;
import entity.UserData;
import entity.UsersTeamsData;
import exception.ApplicationException;
import exception.ErrorCode;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import repository.TeamRepository;
import repository.UsersTeamsRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static utils.Helpers.throwAndLogError;


@Service
public class TeamService {
	@Autowired
	private AuditLogHeaderService auditLogHeaderService;

	private final Logger logger = LogManager.getLogger(TeamService.class);

	@Autowired
	private TeamRepository teamRepository;

	@Autowired
	private UsersTeamsRepository usersTeamsRepository;

	@Autowired
	private EntityManager entityManager;

	@Autowired
	private UserService userService;

	public TeamService() {
	}

	public TeamData getById(Long id) {
		return teamRepository.findById(id)
				.orElseThrow(() -> throwAndLogError(logger, new ApplicationException(ErrorCode.TEAM_NOT_FOUND, id)));
	}

	public List<TeamData> getAll() {
		return teamRepository.findAllActiveTeams();
	}

	public Set<TeamData> getByName(String name) {
		return teamRepository.findByName(name);
	}

	@PreAuthorize("hasAuthority('ROLE_ADMIN') || hasAuthority('ROLE_LEADER')")
	public Long createTeam(TeamData teamData) {
		if (!getByName(teamData.getName()).isEmpty()) {
			throw throwAndLogError(logger, new ApplicationException(ErrorCode.TEAM_NAME_IS_TAKEN, teamData.getName()));
		}

		teamData.setCreationDate(LocalDateTime.now());

		try {
			teamRepository.save(teamData);
		} catch (Exception e) {
			throw throwAndLogError(logger, new ApplicationException(ErrorCode.FAILED_TO_CREATE_TEAM));
		}

		logger.info("Team {} successfully created.", teamData.getName());
		return teamData.getId();
	}

	@Transactional
	@PreAuthorize("hasAuthority('ROLE_ADMIN') || hasAuthority('ROLE_LEADER')")
	public void updateTeam(TeamData updatedTeam) {
		updatedTeam.setModificationDate(LocalDateTime.now());
		entityManager.detach(updatedTeam);

		TeamData teamData = getById(updatedTeam.getId());
		auditLogHeaderService.createObjectChange(teamData, updatedTeam, userService.findAdmin(), teamData.getClass().getSimpleName(), teamData.getId());

		try {
			teamRepository.save(updatedTeam);
		} catch (Exception e) {
			throw throwAndLogError(logger, new ApplicationException(ErrorCode.FAILED_TO_DELETE_USER));
		}

		logger.info("Team {} successfully updated.", updatedTeam.getName());
	}

	@PreAuthorize("hasAuthority('ROLE_ADMIN') || hasAuthority('ROLE_LEADER')")
	public void deleteTeam(TeamData teamData) {
		teamData.setDeletionDate(LocalDateTime.now());

		try {
			teamRepository.save(teamData);
		} catch (ApplicationException e) {
			throw throwAndLogError(logger, new ApplicationException(ErrorCode.FAILED_TO_DELETE_TEAM));
		}

		logger.info("Team {} with id {} successfully deleted.", teamData.getName(), teamData.getId());
	}

	@PreAuthorize("hasAuthority('ROLE_ADMIN') || hasAuthority('ROLE_LEADER')")
	public void addUserToTeam(Long teamId, Long userId) {
		TeamData team = getById(teamId);

		UserData updatedUser = userService.getById(userId);

		boolean userInTeam = usersTeamsRepository.existsByUserIdAndTeamId(userId, teamId);
		if (userInTeam) {
			throw throwAndLogError(logger, new ApplicationException(ErrorCode.USER_ALREADY_IN_TEAM));
		}

		UsersTeamsData usersTeamsData = new UsersTeamsData();
		usersTeamsData.setUser(updatedUser);
		usersTeamsData.setTeam(team);

		try {
			updatedUser.getTeams().put(team.getId(), team);
			entityManager.detach(updatedUser);

			UserData user = userService.getById(userId);
			auditLogHeaderService.createObjectChange(user, updatedUser, userService.findAdmin(), user.getClass().getSimpleName(), userId);
			usersTeamsRepository.save(usersTeamsData);
		} catch (ApplicationException e) {
			logger.error("Error occurred while adding user to team", e);
			throw throwAndLogError(logger, new ApplicationException(ErrorCode.FAILED_TO_ADD_USER_TO_TEAM));
		}

		logger.info("User {} with id {} was successfully added to team with id {}.", updatedUser.getFirstName() + " " + updatedUser.getLastName(), updatedUser.getId(), team.getId());
	}

	@PreAuthorize("hasAuthority('ROLE_ADMIN') || hasAuthority('ROLE_LEADER')")
	public void removeUserFromTeam(Long teamId, Long userId) {
		TeamData team = getById(teamId);
		UserData deletedUser = userService.getById(userId);
		UsersTeamsData usersTeamsData = usersTeamsRepository.findByTeamAndUser(team, deletedUser);

		try {
			deletedUser.getTeams().remove(teamId);
			entityManager.detach(deletedUser);

			UserData user = userService.getById(userId);
			auditLogHeaderService.createObjectChange(user, deletedUser, userService.findAdmin(), user.getClass().getSimpleName(), user.getId());

			usersTeamsRepository.delete(usersTeamsData);
		} catch (ApplicationException e) {
			throw throwAndLogError(logger, new ApplicationException(ErrorCode.FAILED_TO_REMOVE_USER_FROM_TEAM));
		}

		logger.info("User with id {} was successfully removed from team with id {}.", deletedUser.getId(), team.getId());
	}

	@PreAuthorize("hasAuthority('ROLE_ADMIN') || hasAuthority('ROLE_LEADER')")
	public List<UserData> searchTeamMembers(Long teamId, String firstName, String lastName, String email) {
		getById(teamId);

		List<UserData> usersInTeams = usersTeamsRepository.searchTeamMembers(teamId, firstName, lastName, email);

		if (usersInTeams.isEmpty()) {
			throw throwAndLogError(logger, new ApplicationException(ErrorCode.NO_USERS_FOUND_WITH_CRITERIA));
		}

		return usersInTeams;
	}

	@PreAuthorize("hasAuthority('ROLE_ADMIN') || hasAuthority('ROLE_LEADER')")
	public List<TeamDto> findTeamsByUserId(Long userId) {
		List<TeamData> teams = usersTeamsRepository.findTeamsByUserId(userId);
		return teams
				.stream()
				.map(TeamDto::toDto)
				.collect(Collectors.toList());
	}
}
