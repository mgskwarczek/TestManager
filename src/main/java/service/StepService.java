package service;

import entity.AttachmentData;
import entity.StepData;
import entity.StepStatusData;
import exception.ApplicationException;
import exception.ErrorCode;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import repository.StepRepository;
import repository.StepStatusRepository;

import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.Map;

import static utils.Helpers.throwAndLogError;


@Service
public class StepService {

	@Autowired
	private StepRepository stepRepository;

	@Autowired
	private StepStatusRepository stepStatusRepository;

	@Autowired
	private AuditLogHeaderService auditLogHeaderService;

	@Autowired
	private EntityManager entityManager;

	@Autowired
	private UserService userService;

	@Autowired
	private AttachmentService attachmentService;

	private final Logger logger = LogManager.getLogger(StepService.class);

	@Transactional
	public StepData getStepById(Long id) {
		return stepRepository.findById(id)
				.orElseThrow(() -> throwAndLogError(logger, new ApplicationException(ErrorCode.OBJECT_NOT_FOUND, "step", id)));
	}

	public StepData getExistingStepById(Long id) {
		StepData step = stepRepository.findExistingById(id);

		if (step == null) {
			throw throwAndLogError(logger, new ApplicationException(ErrorCode.OBJECT_NOT_FOUND, "step", id));
		}

		return step;
	}

	public void saveStep(StepData step) {
		step.setCreationDate(LocalDateTime.now());
		stepRepository.save(step);
		logger.info("Step with id {} has been created.", step.getId());
	}

	@Transactional
	public void updateStep(StepData updatedStep) {
		updatedStep.setModificationDate(LocalDateTime.now());
		updatedStep.getAttachments().size();
		entityManager.detach(updatedStep);
		StepData step = getStepById(updatedStep.getId());

		auditLogHeaderService.createObjectChange(step, updatedStep, userService.findAdmin(), step.getClass().getSimpleName(), step.getId());
		stepRepository.save(updatedStep);
		logger.info("Step with id {} has been updated.", step.getId());
	}

	@Transactional
	public void deleteStep(Long id) {
		StepData updatedStep = getExistingStepById(id);
		Iterator<Map.Entry<Long, AttachmentData>> iterator = updatedStep.getAttachments().entrySet().iterator();
		while (iterator.hasNext()) {
			Map.Entry<Long, AttachmentData> attachment = iterator.next();
			attachmentService.deleteAttachment(attachment.getKey());
			iterator.remove();
		}
		updatedStep.setDeletionDate(LocalDateTime.now());

		entityManager.detach(updatedStep);
		StepData step = getExistingStepById(id);
		auditLogHeaderService.createObjectChange(updatedStep, step, userService.findAdmin(), step.getClass().getSimpleName(), step.getId());
		stepRepository.save(updatedStep);
		logger.info("Step with id {} has been removed from case with id {}.", step.getId(), step.getCaseId());
	}

	public Long createStepStatus(StepStatusData stepStatus) {
		stepStatusRepository.save(stepStatus);
		logger.info("Step status with id {} and name {} has been created.", stepStatus.getId(), stepStatus.getName());
		return stepStatus.getId();
	}

	public void updateStepStatus(StepStatusData stepStatus) {
		stepStatusRepository.save(stepStatus);
		logger.info("Step status with id {} has been updated.", stepStatus.getId());
	}

	public StepStatusData getStepStatusById(Long id) {
		return stepStatusRepository.findById(id)
				.orElseThrow(() -> throwAndLogError(logger, new ApplicationException(ErrorCode.OBJECT_NOT_FOUND, "stepStatus", id)));
	}
}