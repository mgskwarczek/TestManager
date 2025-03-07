package controller;

import dto.DtoUtil;
import dto.attachment.AttachmentDto;
import dto.attachment.AttachmentState;
import dto.cas.CaseCreateDto;
import dto.cas.CaseDto;
import dto.cas.CaseTitleCoderUserStatusDto;
import dto.cas.CaseUpdateDto;
import dto.step.StepCreateUpdateDto;
import dto.step.StepState;
import entity.*;
import exception.ApplicationException;
import exception.ErrorCode;
import jakarta.transaction.Transactional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import service.AttachmentService;
import service.CaseService;
import service.StepService;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static utils.Helpers.throwAndLogError;


@RestController
@RequestMapping("/api/cases")
public class CaseController {

	@Autowired
	private CaseService caseService;

	@Autowired
	private StepService stepService;

	@Autowired
	private AttachmentService attachmentService;

	private final Logger logger = LogManager.getLogger(CaseController.class);

	@GetMapping("/{id}")
	public CaseDto getById(@PathVariable("id") Long id) {
		validateId(id, "case");
		return CaseDto.toDto(caseService.getCaseById(id));
	}

	private void validateId(Long id, String obj) {
		if (DtoUtil.isFieldNull(id)) {
			throw throwAndLogError(logger, new ApplicationException(ErrorCode.NULL_VALUE_FORBIDDEN, "id", obj));
		}
	}

	@GetMapping("/searchFromSuit")
	public List<CaseTitleCoderUserStatusDto> getCasesBySuitIdAndCodeAndTitleAndStatusIdAndUser(
			@RequestParam(name = "suitId") Long suitId,
			@RequestParam(required = false, name = "code") String code,
			@RequestParam(required = false, name = "title") String title,
			@RequestParam(required = false, name = "statusId") Long statusId,
			@RequestParam(required = false, name = "firstName") String firstName,
			@RequestParam(required = false, name = "lastName") String lastName) {
		return caseService.getCasesBySuitIdAndCodeAndTitleAndStatusIdAndUser(suitId, code, title, statusId, firstName, lastName)
				.stream()
				.map(CaseTitleCoderUserStatusDto::toDto)
				.collect(Collectors.toList());
	}

	@PostMapping("/create")
	public Long createCase(@RequestBody CaseCreateDto caseCreateDto) {
		validateCaseCreateDto(caseCreateDto);
		CaseData cas = CaseCreateDto.toEntity(caseCreateDto);
		caseService.createCase(cas);
		for (StepCreateUpdateDto stepCreateUpdateDto : caseCreateDto.getSteps()) {
			createStep(stepCreateUpdateDto, cas);
		}
		return cas.getId();
	}

	private void validateCaseCreateDto(CaseCreateDto caseCreateDto) {
		if (DtoUtil.isFieldNull(caseCreateDto.getSuitId())) {
			throw throwAndLogError(logger, new ApplicationException(ErrorCode.NULL_VALUE_FORBIDDEN, "suitId", "case"));
		}
		if (DtoUtil.isFieldNull(caseCreateDto.getUserId())) {
			throw throwAndLogError(logger, new ApplicationException(ErrorCode.NULL_VALUE_FORBIDDEN, "userId", "case"));
		}
		if (DtoUtil.isStringNullOrEmpty(caseCreateDto.getTitle())) {
			throw throwAndLogError(logger, new ApplicationException(ErrorCode.NULL_VALUE_FORBIDDEN, "title", "case"));
		}
		if (DtoUtil.isStringTooLong(caseCreateDto.getTitle(), 100)) {
			throw throwAndLogError(logger, new ApplicationException(ErrorCode.TOO_LONG_VALUE, "title", caseCreateDto.getTitle().length(), 100));
		}
		if (DtoUtil.isFieldNull(caseCreateDto.getTypeId())) {
			throw throwAndLogError(logger, new ApplicationException(ErrorCode.NULL_VALUE_FORBIDDEN, "typeId", "case"));
		}
		if (DtoUtil.isFieldNull(caseCreateDto.getPriorityId())) {
			throw throwAndLogError(logger, new ApplicationException(ErrorCode.NULL_VALUE_FORBIDDEN, "priorityId", "case"));
		}
		if (DtoUtil.isFieldNull(caseCreateDto.getStatusId())) {
			throw throwAndLogError(logger, new ApplicationException(ErrorCode.NULL_VALUE_FORBIDDEN, "statusId", "case"));
		}
		if (DtoUtil.isStringNullOrEmpty(caseCreateDto.getCode())) {
			throw throwAndLogError(logger, new ApplicationException(ErrorCode.NULL_VALUE_FORBIDDEN, "code", "case"));
		}
		if (DtoUtil.isStringTooLong(caseCreateDto.getCode(), 100)) {
			throw throwAndLogError(logger, new ApplicationException(ErrorCode.TOO_LONG_VALUE, "name", caseCreateDto.getCode().length(), 30));
		}
		if (!DtoUtil.isFieldNull(caseCreateDto.getPreConditions())) {
			if (DtoUtil.isStringTooLong(caseCreateDto.getPreConditions(), 1000)) {
				throw new ApplicationException(ErrorCode.TOO_LONG_VALUE, "name", caseCreateDto.getPreConditions().length(), 1000);
			}
		}
		if (!DtoUtil.isFieldNull(caseCreateDto.getPostConditions())) {
			if (DtoUtil.isStringTooLong(caseCreateDto.getPostConditions(), 1000)) {
				throw throwAndLogError(logger, new ApplicationException(ErrorCode.TOO_LONG_VALUE, "name", caseCreateDto.getPostConditions().length(), 1000));
			}
		}
		if (!DtoUtil.isFieldNull(caseCreateDto.getSummary())) {
			if (DtoUtil.isStringTooLong(caseCreateDto.getSummary(), 1000)) {
				throw throwAndLogError(logger, new ApplicationException(ErrorCode.TOO_LONG_VALUE, "name", caseCreateDto.getSummary().length(), 1000));
			}
		}
		for (StepCreateUpdateDto stepCreateUpdateDto : caseCreateDto.getSteps()) {
			validateStepCreateDto(stepCreateUpdateDto);
		}
	}

	@Transactional
	private void createStep(StepCreateUpdateDto stepCreateUpdateDto, CaseData caseData) {
		StepData step = StepCreateUpdateDto.toEntity(stepCreateUpdateDto);
		step.setCaseId(caseData.getId());

		stepService.saveStep(step);
		updateAttachments(stepCreateUpdateDto.getAttachments(), step);
		caseData.addStep(step);
	}

	private void validateStepCreateDto(StepCreateUpdateDto stepCreateUpdateDto) {
		if (DtoUtil.isFieldNull(stepCreateUpdateDto.getStatusId())) {
			throw throwAndLogError(logger, new ApplicationException(ErrorCode.NULL_VALUE_FORBIDDEN, "statusId", "step"));
		}
		if (DtoUtil.isStringNullOrEmpty(stepCreateUpdateDto.getAction())) {
			throw throwAndLogError(logger, new ApplicationException(ErrorCode.NULL_VALUE_FORBIDDEN, "action", "step"));
		}
		if (DtoUtil.isStringTooLong(stepCreateUpdateDto.getAction(), 100)) {
			throw throwAndLogError(logger, new ApplicationException(ErrorCode.TOO_LONG_VALUE, "action", stepCreateUpdateDto.getAction().length(), 100));
		}
		if (DtoUtil.isFieldNull(stepCreateUpdateDto.getOrder())) {
			throw throwAndLogError(logger, new ApplicationException(ErrorCode.NULL_VALUE_FORBIDDEN, "order", "step"));
		}
		if (!DtoUtil.isFieldNull(stepCreateUpdateDto.getComment())) {
			if (DtoUtil.isStringTooLong(stepCreateUpdateDto.getComment(), 1000)) {
				throw throwAndLogError(logger, new ApplicationException(ErrorCode.TOO_LONG_VALUE, "comment", stepCreateUpdateDto.getComment().length(), 1000));
			}
		}
		if (!DtoUtil.isFieldNull(stepCreateUpdateDto.getResult())) {
			if (DtoUtil.isStringTooLong(stepCreateUpdateDto.getResult(), 1000)) {
				throw throwAndLogError(logger, new ApplicationException(ErrorCode.TOO_LONG_VALUE, "result", stepCreateUpdateDto.getResult().length(), 1000));
			}
		}
	}

	private void updateAttachments(List<AttachmentDto> attachmentsData, StepData step) {
		for (AttachmentDto attachment : attachmentsData) {
			if (attachment.getAttachmentState() == AttachmentState.NEW) {
				validateAttachmentCreateDto(attachment);
				AttachmentData newAttachment = AttachmentDto.toEntity(attachment);
				newAttachment.setStepId(step.getId());
				attachmentService.createAttachment(newAttachment);
				step.addAttachment(newAttachment);
			}
			if (attachment.getAttachmentState() == AttachmentState.DELETED) {
				validateId(attachment.getId(), "attachment");
				attachmentService.deleteAttachment(attachment.getId());
				step.removeAttachment(attachment.getId());
			}
		}
	}

	private void validateAttachmentCreateDto(AttachmentDto attachmentDto) {
		if (DtoUtil.isFieldNull(attachmentDto.getData())) {
			throw throwAndLogError(logger, throwAndLogError(logger, new ApplicationException(ErrorCode.NULL_VALUE_FORBIDDEN, "data", "attachment")));
		}
		if (DtoUtil.isFieldNull(attachmentDto.getTypeId())) {
			throw throwAndLogError(logger, throwAndLogError(logger, new ApplicationException(ErrorCode.NULL_VALUE_FORBIDDEN, "typeId", "attachment")));
		}
		if (DtoUtil.isFieldNull(attachmentDto.getAttachmentState())) {
			throw throwAndLogError(logger, throwAndLogError(logger, new ApplicationException(ErrorCode.NULL_VALUE_FORBIDDEN, "attachmentState", "attachment")));
		}
		if (DtoUtil.isStringNullOrEmpty(attachmentDto.getName())) {
			throw throwAndLogError(logger, throwAndLogError(logger, new ApplicationException(ErrorCode.NULL_VALUE_FORBIDDEN, "name", "attachment")));
		}
		if (DtoUtil.isStringTooLong(attachmentDto.getName(), 255)) {
			throw throwAndLogError(logger, throwAndLogError(logger, new ApplicationException(ErrorCode.TOO_LONG_VALUE, "action", attachmentDto.getName().length(), 255)));
		}
	}

	@DeleteMapping("/delete/{id}")
	// czy to też powinienem audytować - w teorii nie, bo jest del_date od tego ale idk
	public void deleteCase(@PathVariable Long id) {
		validateId(id, "case");
		CaseData caseData = caseService.getExistingCaseById(id);
		for (Map.Entry<Long, StepData> step : caseData.getSteps().entrySet()) {
			deleteStep(step.getValue().getId(), caseData);
		}
		caseService.deleteCase(id);
	}

	@PutMapping("/update")
	@Transactional
	public void updateCase(@RequestBody CaseUpdateDto caseUpdateDto) {
		validateCaseUpdateDto(caseUpdateDto);
		validateId(caseUpdateDto.getId(), "case");

		CaseData cas = caseService.getExistingCaseById(caseUpdateDto.getId());
		cas.getSteps().size();

		cas.setUserId(caseUpdateDto.getUserId());
		cas.setTitle(caseUpdateDto.getTitle());
		cas.setTypeId(caseUpdateDto.getTypeId());
		cas.setPriorityId(caseUpdateDto.getPriorityId());
		cas.setStatusId(caseUpdateDto.getStatusId());
		cas.setTime(caseUpdateDto.getTime());
		cas.setPreConditions(caseUpdateDto.getPreConditions());
		cas.setPostConditions(caseUpdateDto.getPostConditions());
		cas.setCode(caseUpdateDto.getCode());
		cas.setSummary(caseUpdateDto.getSummary());

		if (caseUpdateDto.getSteps() != null) {
			for (StepCreateUpdateDto updatedStep : caseUpdateDto.getSteps()) {
				if (updatedStep.getStepState() == StepState.NEW) {
					createStep(updatedStep, cas);
				}
				else if (updatedStep.getStepState() == StepState.MODIFIED) {
					updateStep(updatedStep, cas);
				}
				else if (updatedStep.getStepState() == StepState.DELETED) {
					deleteStep(updatedStep.getId(), cas);
				}
			}
		}

		caseService.updateCase(cas);
	}

	private void validateCaseUpdateDto(CaseUpdateDto caseUpdateDto) {
		if (DtoUtil.isFieldNull(caseUpdateDto.getUserId())) {
			throw throwAndLogError(logger, new ApplicationException(ErrorCode.NULL_VALUE_FORBIDDEN, "userId", "case"));
		}
		if (DtoUtil.isFieldNull(caseUpdateDto.getTitle())) {
			throw throwAndLogError(logger, new ApplicationException(ErrorCode.NULL_VALUE_FORBIDDEN, "title", "case"));
		}
		if (DtoUtil.isStringTooLong(caseUpdateDto.getTitle(), 100)) {
			throw throwAndLogError(logger, new ApplicationException(ErrorCode.TOO_LONG_VALUE, "title", caseUpdateDto.getTitle().length(), 100));
		}
		if (DtoUtil.isFieldNull(caseUpdateDto.getTypeId())) {
			throw throwAndLogError(logger, new ApplicationException(ErrorCode.NULL_VALUE_FORBIDDEN, "typeId", "case"));
		}
		if (DtoUtil.isFieldNull(caseUpdateDto.getPriorityId())) {
			throw throwAndLogError(logger, new ApplicationException(ErrorCode.NULL_VALUE_FORBIDDEN, "priorityId", "case"));
		}
		if (DtoUtil.isFieldNull(caseUpdateDto.getStatusId())) {
			throw throwAndLogError(logger, new ApplicationException(ErrorCode.NULL_VALUE_FORBIDDEN, "statusId", "case"));
		}
		if (!DtoUtil.isFieldNull(caseUpdateDto.getPreConditions())) {
			if (DtoUtil.isStringTooLong(caseUpdateDto.getPreConditions(), 1000)) {
				throw throwAndLogError(logger, new ApplicationException(ErrorCode.TOO_LONG_VALUE, "preConditions", caseUpdateDto.getPreConditions().length(), 1000));
			}
		}
		if (!DtoUtil.isFieldNull(caseUpdateDto.getPostConditions())) {
			if (DtoUtil.isStringTooLong(caseUpdateDto.getPostConditions(), 1000)) {
				throw throwAndLogError(logger, new ApplicationException(ErrorCode.TOO_LONG_VALUE, "postConditions", caseUpdateDto.getPostConditions().length(), 1000));
			}
		}
		if (DtoUtil.isFieldNull(caseUpdateDto.getCode())) {
			throw throwAndLogError(logger, new ApplicationException(ErrorCode.NULL_VALUE_FORBIDDEN, "code", "case"));
		}
		if (!DtoUtil.isFieldNull(caseUpdateDto.getSummary())) {
			if (DtoUtil.isStringTooLong(caseUpdateDto.getSummary(), 1000)) {
				throw throwAndLogError(logger, new ApplicationException(ErrorCode.TOO_LONG_VALUE, "postConditions", caseUpdateDto.getSummary().length(), 1000));
			}
		}
		if (caseUpdateDto.getSteps() != null) {
			for (StepCreateUpdateDto step : caseUpdateDto.getSteps()) {
				if (step.getStepState() == StepState.NEW) {
					validateStepCreateDto(step);
				}
				if (step.getStepState() == StepState.MODIFIED) {
					validateId(step.getId(), "step");
					validateStepUpdateDto(step);
				}
				if (step.getStepState() == StepState.DELETED) {
					validateId(step.getId(), "step");
				}
			}
		}
	}

	@Transactional
	private void deleteStep(Long id, CaseData caseData) {
		validateId(id, "step");
		stepService.deleteStep(id);
		logger.info("Step with id {} has been deleted.", id);
		caseData.removeStep(id);
	}

	@Transactional
	private void updateStep(StepCreateUpdateDto stepCreateUpdateDto, CaseData caseData) {
			StepData step = caseData.getSteps().get(stepCreateUpdateDto.getId());

			step.setStatusId(stepCreateUpdateDto.getStatusId());
			step.setAction(stepCreateUpdateDto.getAction());
			step.setOrder(stepCreateUpdateDto.getOrder());
			step.setComment(stepCreateUpdateDto.getComment());
			step.setResult(stepCreateUpdateDto.getResult());

			updateAttachments(stepCreateUpdateDto.getAttachments(), step);
			stepService.updateStep(step);
	}

	private void validateStepUpdateDto(StepCreateUpdateDto stepCreateUpdateDto) {
		if (DtoUtil.isFieldNull(stepCreateUpdateDto.getStatusId())) {
			throw throwAndLogError(logger, new ApplicationException(ErrorCode.NULL_VALUE_FORBIDDEN, "statusId", "step"));
		}
		if (DtoUtil.isStringNullOrEmpty(stepCreateUpdateDto.getAction())) {
			throw throwAndLogError(logger, new ApplicationException(ErrorCode.NULL_VALUE_FORBIDDEN, "action", "step"));
		}
		if (DtoUtil.isStringTooLong(stepCreateUpdateDto.getAction(), 100)) {
			throw throwAndLogError(logger, new ApplicationException(ErrorCode.TOO_LONG_VALUE, "action", stepCreateUpdateDto.getAction().length(), 100));
		}
		if (DtoUtil.isFieldNull(stepCreateUpdateDto.getOrder())) {
			throw throwAndLogError(logger, new ApplicationException(ErrorCode.NULL_VALUE_FORBIDDEN, "order", "step"));
		}
		if (!DtoUtil.isFieldNull(stepCreateUpdateDto.getComment())) {
			if (DtoUtil.isStringTooLong(stepCreateUpdateDto.getComment(), 1000)) {
				throw throwAndLogError(logger, new ApplicationException(ErrorCode.TOO_LONG_VALUE, "comment", stepCreateUpdateDto.getComment().length(), 1000));
			}
		}
		if (!DtoUtil.isFieldNull(stepCreateUpdateDto.getResult())) {
			if (DtoUtil.isStringTooLong(stepCreateUpdateDto.getResult(), 1000)) {
				throw throwAndLogError(logger, new ApplicationException(ErrorCode.TOO_LONG_VALUE, "result", stepCreateUpdateDto.getResult().length(), 1000));
			}
		}
	}

	@PostMapping("/createStatus")
	public Long createCaseStatus(@RequestBody CaseStatusData caseStatus) {
		validateName(caseStatus.getName(), "caseStatus");
		return caseService.createCaseStatus(caseStatus);
	}

	private void validateName(String name, String obj) {
		if (DtoUtil.isStringNullOrEmpty(name)) {
			throw throwAndLogError(logger, new ApplicationException(ErrorCode.NULL_VALUE_FORBIDDEN, "name", obj));
		}
		if (DtoUtil.isStringTooLong(name, 50)) {
			throw throwAndLogError(logger, new ApplicationException(ErrorCode.TOO_LONG_VALUE, "name", name.length(), 50));
		}
	}

	@PutMapping("/updateStatus")
	public void updateCaseStatus(@RequestBody CaseStatusData updatedCaseStatus) {
		validateCaseStatusUpdate(updatedCaseStatus);

		CaseStatusData caseStatus = caseService.getCaseStatusById(updatedCaseStatus.getId());
		caseStatus.setName(updatedCaseStatus.getName());

		caseService.updateCaseStatus(caseStatus);
	}

	private void validateCaseStatusUpdate(CaseStatusData caseStatus) {
		validateId(caseStatus.getId(), "caseStatus");
		validateName(caseStatus.getName(), "caseStatus");
		logger.info("Case status has been ");
	}

	@PostMapping("/createType")
	public Long createCaseType(@RequestBody CaseTestTypeData caseTestType) {
		validateName(caseTestType.getName(), "caseTestType");
		return caseService.createCaseTestType(caseTestType);
	}

	@PutMapping("/updateType")
	public void updateCaseType(@RequestBody CaseTestTypeData updatedCaseTestType) {
		validateCaseTypeUpdate(updatedCaseTestType);

		CaseTestTypeData caseTestType = caseService.getTestTypeById(updatedCaseTestType.getId());
		caseTestType.setName(updatedCaseTestType.getName());

		caseService.updateCaseTestType(caseTestType);
	}

	private void validateCaseTypeUpdate(CaseTestTypeData caseTestType) {
		validateId(caseTestType.getId(), "caseTestType");
		validateName(caseTestType.getName(), "caseTestType");
	}

	@PostMapping("/createPriority")
	public Long createCasePriority(@RequestBody CasePriorityData casePriority) {
		validateName(casePriority.getName(), "casePriority");
		return caseService.createCasePriority(casePriority);
	}

	@PutMapping("/updatePriority")
	public void updateCasePriority(@RequestBody CasePriorityData updatedCasePriority) {
		validateCasePriorityUpdate(updatedCasePriority);

		CasePriorityData casePriority = caseService.getCasePriorityById(updatedCasePriority.getId());
		casePriority.setName(updatedCasePriority.getName());

		caseService.updateCasePriority(casePriority);
	}

	private void validateCasePriorityUpdate(CasePriorityData casePriority) {
		validateId(casePriority.getId(), "casePriority");
		validateName(casePriority.getName(), "casePriority");
	}
}
