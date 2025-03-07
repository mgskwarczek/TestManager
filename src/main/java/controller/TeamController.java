package controller;

import dto.user.*;
import entity.TeamData;
import entity.UserData;
import exception.ApplicationException;
import exception.ErrorCode;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import service.TeamService;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static utils.Helpers.throwAndLogError;


@RestController
@RequestMapping("/api/teams")
public class TeamController {
	private static final Logger logger = LogManager.getLogger(TeamController.class);

	@Autowired
	private TeamService teamService;

	@GetMapping("/{id}")
	public TeamDetailsDto getById(@PathVariable("id") Long id) {
		validateId(id);
		return TeamDetailsDto.toDto(teamService.getById(id));
	}

	public void validateId(Long id) {
		if (id == null) {
			throw throwAndLogError(logger, new ApplicationException(ErrorCode.NULL_VALUE_FORBIDDEN, "id"));
		}
	}

	@GetMapping("/all")
	public List<TeamDto> getAll() {
		return teamService.getAll()
				.stream()
				.map(TeamDto::toDto)
				.collect(Collectors.toList());
	}

	@GetMapping("/search")
	public Set<TeamDto> getByName(@RequestParam("name") String name) {
		validateTeamName(name);
		logger.info("Correctly validated name for search.");

		Set<TeamDto> teams = teamService.getByName(name)
				.stream()
				.map(TeamDto::toDto)
				.collect(Collectors.toSet());

		if(teams.isEmpty()) {
			throw new ApplicationException(ErrorCode.NO_TEAMS_FOUND_WITH_CRITERIA);
		}

		return teams;
	}

	private void validateTeamName(String name) {
		if (name == null || name.isEmpty()) {
			throw throwAndLogError(logger, new ApplicationException(ErrorCode.NULL_VALUE_FORBIDDEN, "name"));
		}

		if (name.length() > 100) {
			throw throwAndLogError(logger, new ApplicationException(ErrorCode.TOO_LONG_VALUE, "name", name.length(), 100));
		}
	}

	@PostMapping("/admin/create")
	public Long createTeam(@RequestBody TeamCreateDto teamCreateDto) {
		validateTeamCreateDto(teamCreateDto);

		TeamData team = TeamCreateDto.toEntity(teamCreateDto);

		return teamService.createTeam(team);
	}

	private void validateTeamCreateDto(TeamCreateDto teamCreateDto) {
		if (teamCreateDto.getName() == null || teamCreateDto.getName().isEmpty()) {
			throw throwAndLogError(logger, new ApplicationException(ErrorCode.NULL_VALUE_FORBIDDEN, "name"));
		}

		if (teamCreateDto.getName().length() > 100) {
			throw throwAndLogError(logger, new ApplicationException(ErrorCode.TOO_LONG_VALUE, "name", teamCreateDto.getName().length(), 100));
		}
	}

	@PutMapping("/admin/update")
	public void updateTeam(@RequestBody TeamUpdateDto teamUpdateDto) {
		validateTeamUpdateDto(teamUpdateDto);
		logger.info("Correctly validated teamUpdateDto.");

		TeamData team = teamService.getById(teamUpdateDto.getId());

		try {
			team.setName(teamUpdateDto.getName());
		} catch (Exception e) {
			throw throwAndLogError(logger, new ApplicationException(ErrorCode.TEAM_NAME_IS_TAKEN));
		}

		teamService.updateTeam(team);
	}

	private void validateTeamUpdateDto(TeamUpdateDto teamUpdateDto) {
		if (teamUpdateDto.getId() == null) {
			throw throwAndLogError(logger, new ApplicationException(ErrorCode.NULL_VALUE_FORBIDDEN, "id"));
		}
		if (teamUpdateDto.getName() != null) {
			if (teamUpdateDto.getName().length() > 100) {
				throw throwAndLogError(logger, new ApplicationException(ErrorCode.TOO_LONG_VALUE, "name", teamUpdateDto.getName().length(), 100));
			}
		}
	}

	@DeleteMapping("/admin/delete")
	public void deleteTeam(@RequestParam("id") Long id) {
		validateId(id);
		TeamData team = teamService.getById(id);
		teamService.deleteTeam(team);
	}

	@PostMapping("/addUser")
	public ResponseEntity<String> addUserToTeam(@RequestBody TeamUserDto teamUserDto) {
		validateTeamUserDto(teamUserDto);

		try {
			teamService.addUserToTeam(teamUserDto.getTeamId(), teamUserDto.getUserId());
			return ResponseEntity.ok("User has been successfully added to the team!");
		} catch(Exception e) {
			return  ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error while adding user to team.");
		}
	}

	@DeleteMapping("/removeUser")
	public ResponseEntity<Void> removeUserFromTeam(@RequestBody TeamUserDto teamUserDto) {
		validateTeamUserDto(teamUserDto);
		try {
			teamService.removeUserFromTeam(teamUserDto.getTeamId(), teamUserDto.getUserId());
			return ResponseEntity.noContent().build();
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
		}
	}

	private void validateTeamUserDto(TeamUserDto teamUserDto) {

		if (teamUserDto == null || teamUserDto.getTeamId() == null || teamUserDto.getUserId() == null) {
			logger.error("Validation failed. TeamUserDto: " + teamUserDto);
			throw throwAndLogError(logger, new ApplicationException(ErrorCode.FAILED_TO_ADD_USER_TO_TEAM));
		}
	}

	@GetMapping("/searchMembers")
	public List<UserDto> searchTeamMembers(
			@RequestParam("teamId") Long teamId,
			@RequestParam(required = false, name = "firstName") String firstName,
			@RequestParam(required = false, name = "lastName") String lastName,
			@RequestParam(required = false, name = "email") String email) {

		List<UserData> usersInTeams = teamService.searchTeamMembers(teamId, firstName, lastName, email);
		return usersInTeams
				.stream()
				.map(UserDto::toDto)
				.collect(Collectors.toList());
	}
}
















