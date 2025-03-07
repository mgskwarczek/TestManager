package controller;

import dto.DtoUtil;
import dto.project.ProjectCreateDto;
import dto.project.ProjectDto;
import dto.project.ProjectUpdateDto;
import entity.ProjectData;
import entity.ProjectStatusData;
import entity.SuitData;
import exception.ApplicationException;
import exception.ErrorCode;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import service.ProjectService;

import static utils.Helpers.throwAndLogError;


@RestController
@RequestMapping("/api/projects")
public class ProjectController {

	@Autowired
	private ProjectService projectService;

	@Autowired
	private SuitController suitController;

	private final Logger logger = LogManager.getLogger(ProjectController.class);

	@GetMapping("/{id}")
	public ProjectDto getById(@PathVariable("id") Long id) {
		validateId(id, "project");
		return ProjectDto.toDto(projectService.getProjectById(id));
	}

	private void validateId(Long id, String obj) {
		if (DtoUtil.isFieldNull(id)) {
			throw throwAndLogError(logger, new ApplicationException(ErrorCode.NULL_VALUE_FORBIDDEN, "id", obj));
		}
	}

	@PostMapping("/create")
	public Long createProject(@RequestBody ProjectCreateDto projectCreateDto) {
		validateProjectCreateDto(projectCreateDto);
		ProjectData project = ProjectCreateDto.toEntity(projectCreateDto);
		return projectService.createProject(project);
	}

	private void validateProjectCreateDto(ProjectCreateDto projectCreateDto) {
		if (DtoUtil.isFieldNull(projectCreateDto.getStatusId())) {
			throw throwAndLogError(logger, new ApplicationException(ErrorCode.NULL_VALUE_FORBIDDEN, "statusId", "project"));
		}
		if (DtoUtil.isStringNullOrEmpty(projectCreateDto.getTitle())) {
			throw throwAndLogError(logger, new ApplicationException(ErrorCode.NULL_VALUE_FORBIDDEN, "title", "project"));
		}
		if (DtoUtil.isStringTooLong(projectCreateDto.getTitle(), 100)) {
			throw throwAndLogError(logger, new ApplicationException(ErrorCode.TOO_LONG_VALUE, "title", projectCreateDto.getTitle().length(), 100));
		}
	}

	@DeleteMapping("/delete/{id}")
	public void deleteProject(@PathVariable("id") Long id) {
		validateId(id, "project");
		for (SuitData suit : projectService.getExistingProjectById(id).getSuits()) {
			suitController.deleteSuit(suit.getId());
		}
		projectService.deleteProject(id);
	}

	@PutMapping("/update")
	public void updateProject(@RequestBody ProjectUpdateDto projectUpdateDto) {
		validateProjectUpdateDto(projectUpdateDto);
		validateId(projectUpdateDto.getId(), "project");

		ProjectData project = projectService.getExistingProjectById(projectUpdateDto.getId());
		project.setStatusId(projectUpdateDto.getStatusId());
		project.setTitle(projectUpdateDto.getTitle());

		projectService.updateProject(project);
	}

	private void validateProjectUpdateDto(ProjectUpdateDto projectUpdateDto) {
		if (DtoUtil.isFieldNull(projectUpdateDto.getStatusId())) {
			throw new ApplicationException(ErrorCode.NULL_VALUE_FORBIDDEN, "statusId", "project");
		}
		if (DtoUtil.isStringNullOrEmpty(projectUpdateDto.getTitle())) {
			throw new ApplicationException(ErrorCode.NULL_VALUE_FORBIDDEN, "title", "project");
		}
		if (DtoUtil.isStringTooLong(projectUpdateDto.getTitle(), 100)) {
			throw new ApplicationException(ErrorCode.TOO_LONG_VALUE, "title", projectUpdateDto.getTitle().length(), 100);
		}
	}

	@PostMapping("/createStatus")
	public Long createProjectStatus(@RequestBody ProjectStatusData projectStatus) {
		validateName(projectStatus.getName(), "projectStatus");
		return projectService.createProjectStatus(projectStatus);
	}

	private void validateName(String name, String obj) {
		if (DtoUtil.isFieldNull(name)) {
			throw new ApplicationException(ErrorCode.NULL_VALUE_FORBIDDEN, "name", obj);
		}
		if (DtoUtil.isStringTooLong(name, 50)) {
			throw new ApplicationException(ErrorCode.TOO_LONG_VALUE, "name", name.length(), 50);
		}
	}

	@PutMapping("/updateStatus")
	public void updateProjectStatus(@RequestBody ProjectStatusData updatedProjectStatus) {
		validateProjectStatusUpdate(updatedProjectStatus);
		validateId(updatedProjectStatus.getId(), "projectStatus");

		ProjectStatusData projectStatus = projectService.getProjectStatusById(updatedProjectStatus.getId());
		projectStatus.setName(updatedProjectStatus.getName());

		projectService.updateProjectStatus(projectStatus);
	}

	private void validateProjectStatusUpdate(ProjectStatusData projectStatus) {
		validateId(projectStatus.getId(), "projectStatus");
		validateName(projectStatus.getName(), "projectStatus");
	}
}
