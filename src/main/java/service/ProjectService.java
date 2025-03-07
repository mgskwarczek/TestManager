package service;

import entity.ProjectData;
import entity.ProjectStatusData;
import exception.ApplicationException;
import exception.ErrorCode;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import repository.ProjectRepository;
import repository.ProjectStatusRepository;

import java.time.LocalDateTime;

import static utils.Helpers.throwAndLogError;


@Service
public class ProjectService {

	@Autowired
	private ProjectRepository projectRepository;

	@Autowired
	private ProjectStatusRepository projectStatusRepository;

	@Autowired
	private EntityManager entityManager;

	@Autowired
	private AuditLogHeaderService auditLogHeaderService;

	@Autowired
	private UserService userService;

	private final Logger logger = LogManager.getLogger(ProjectService.class);

	public ProjectData getProjectById(Long id) {
		return projectRepository.findById(id)
				.orElseThrow(() -> throwAndLogError(logger, new ApplicationException(ErrorCode.OBJECT_NOT_FOUND, "project", id)));
	}

	public ProjectData getExistingProjectById(Long id) {
		ProjectData project = projectRepository.findExistingById(id);

		if (project == null) {
			throw throwAndLogError(logger, new ApplicationException(ErrorCode.OBJECT_NOT_FOUND, "project", id));
		}

		return project;
	}

	@PreAuthorize("hasAuthority('ROLE_ADMIN') || hasAuthority('ROLE_LEADER')")
	public Long createProject(ProjectData project) {
		project.setCreationDate(LocalDateTime.now());
		projectRepository.save(project);

		logger.info("Project with id {} has been created.", project.getId());
		return project.getId();
	}

	@PreAuthorize("hasAuthority('ROLE_ADMIN') || hasAuthority('ROLE_LEADER')")
	@Transactional
	public void updateProject(ProjectData updatedProject) {
		updatedProject.setModificationDate(LocalDateTime.now());
		entityManager.detach(updatedProject);

		ProjectData project = getExistingProjectById(updatedProject.getId());
		auditLogHeaderService.createObjectChange(project, updatedProject, userService.findAdmin(), project.getClass().getSimpleName(), project.getId());

		projectRepository.save(updatedProject);
		logger.info("Project with id {} has been updated.", updatedProject.getId());
	}

	@PreAuthorize("hasAuthority('ROLE_ADMIN') || hasAuthority('ROLE_LEADER')")
	@Transactional
	public void deleteProject(Long id) {
		ProjectData project = getProjectById(id);
		project.setDeletionDate(LocalDateTime.now());

		projectRepository.save(project);
		logger.info("Project with id {} has been deleted.", id);
	}

	public ProjectStatusData getProjectStatusById(Long id) {
		return projectStatusRepository.findById(id)
				.orElseThrow(() -> throwAndLogError(logger, new ApplicationException(ErrorCode.OBJECT_NOT_FOUND, "project status", id)));
	}

	@PreAuthorize("hasAuthority('ROLE_ADMIN') || hasAuthority('ROLE_LEADER')")
	public Long createProjectStatus(ProjectStatusData projectStatus) {
		projectStatusRepository.save(projectStatus);
		logger.info("ProjectStatus with id {} and name {} has been created.", projectStatus.getId(), projectStatus.getName());
		return projectStatus.getId();
	}

	@PreAuthorize("hasAuthority('ROLE_ADMIN') || hasAuthority('ROLE_LEADER')")
	public void updateProjectStatus(ProjectStatusData projectStatus) {
		projectStatusRepository.save(projectStatus);
		logger.info("ProjectStatus with id {} has been updated.", projectStatus.getId());
	}
}
