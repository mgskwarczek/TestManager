package controller;

import dto.DtoUtil;
import dto.suit.*;
import entity.CaseData;
import entity.SuitData;
import entity.SuitStatusData;
import exception.ApplicationException;
import exception.ErrorCode;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import service.AuditLogHeaderService;
import service.ProjectService;
import service.SuitService;
import service.UserService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static utils.Helpers.throwAndLogError;


@RestController
@RequestMapping("/api/suits")
public class SuitController {

	@Autowired
	private SuitService suitService;

	@Autowired
	private ProjectService projectService;

	@Autowired
	private UserService userService;

	@Autowired
	private CaseController caseController;

	@Autowired
	private AuditLogHeaderService auditLogHeaderService;

	private final Logger logger = LogManager.getLogger(SuitController.class);

	@GetMapping("/{id}")
	public SuitDto getById(@PathVariable("id") Long id) {
		validateId(id, "suit");
		return SuitDto.toDto(suitService.getSuitById(id));
	}

	private void validateId(Long id, String obj) {
		if (DtoUtil.isFieldNull(id)) {
			throw throwAndLogError(logger, new ApplicationException(ErrorCode.NULL_VALUE_FORBIDDEN, "id", obj));
		}
	}

	@GetMapping("/search")
	public List<SuitTitleProjectTitleDateStatusNameDto> getSuitsByUserAndTitleAndProjectAndDates(
			@RequestParam(name = "userId") Long userId,
			@RequestParam(required = false, name = "suitTitle") String suitTitle,
			@RequestParam(required = false, name = "projectTitle") String projectTitle,
			@RequestParam(required = false, name = "lowerDate") LocalDateTime lowerDate,
			@RequestParam(required = false, name = "upperDate") LocalDateTime upperDate) {
		return suitService.getSuitsByUserIdAndTitleAndProjectTitleAndDate(userId, suitTitle, projectTitle, lowerDate, upperDate)
				.stream()
				.map(SuitTitleProjectTitleDateStatusNameDto::toDto)
				.collect(Collectors.toList());
	}

	@GetMapping("/searchFromProject")
	public List<SuitTitleCodeUserDto> getSuitsByProjectIdAndCodeAndTitleAndUser(
			@RequestParam(name = "projectId") Long projectId,
			@RequestParam(required = false, name = "code") String code,
			@RequestParam(required = false, name = "title") String title,
			@RequestParam(required = false, name = "firstName") String firstName,
			@RequestParam(required = false, name = "lastName") String lastName) {
		return suitService.getSuitsByProjectIdAndCodeAndTitleAndUser(projectId, code, title, firstName, lastName)
				.stream()
				.map(SuitTitleCodeUserDto::toDto)
				.collect(Collectors.toList());
	}

	@PostMapping("/create")
	public Long createSuit(@RequestBody SuitCreateDto suitCreateDto) {
		validateSuitCreateDto(suitCreateDto);
		SuitData suit = SuitCreateDto.toEntity(suitCreateDto);
		return suitService.createSuit(suit);
	}

	private void validateSuitCreateDto(SuitCreateDto suitCreateDto) {
		if (DtoUtil.isFieldNull(suitCreateDto.getProjectId())) {
			throw throwAndLogError(logger, new ApplicationException(ErrorCode.NULL_VALUE_FORBIDDEN, "projectId", "suit"));
		}
		if (DtoUtil.isFieldNull(suitCreateDto.getUserId())) {
			throw throwAndLogError(logger, new ApplicationException(ErrorCode.NULL_VALUE_FORBIDDEN, "userId", "suit"));
		}
		if (DtoUtil.isFieldNull(suitCreateDto.getStatusId())) {
			throw throwAndLogError(logger, new ApplicationException(ErrorCode.NULL_VALUE_FORBIDDEN, "statusId", "suit"));
		}
		if (DtoUtil.isStringNullOrEmpty(suitCreateDto.getTitle())) {
			throw throwAndLogError(logger, new ApplicationException(ErrorCode.NULL_VALUE_FORBIDDEN, "title", "suit"));
		}
		if (DtoUtil.isStringTooLong(suitCreateDto.getTitle(), 100)) {
			throw throwAndLogError(logger, new ApplicationException(ErrorCode.TOO_LONG_VALUE, "title", suitCreateDto.getTitle().length(), 100));
		}
		if (DtoUtil.isStringNullOrEmpty(suitCreateDto.getCode())) {
			throw throwAndLogError(logger, new ApplicationException(ErrorCode.NULL_VALUE_FORBIDDEN, "code", "suit"));
		}
		if (DtoUtil.isStringTooLong(suitCreateDto.getCode(), 30)) {
			throw throwAndLogError(logger, new ApplicationException(ErrorCode.TOO_LONG_VALUE, "code", suitCreateDto.getCode().length(), 30));
		}
		if (!DtoUtil.isFieldNull(suitCreateDto.getDescription())) {
			if (DtoUtil.isStringTooLong(suitCreateDto.getDescription(), 1000)) {
				throw throwAndLogError(logger, new ApplicationException(ErrorCode.TOO_LONG_VALUE, "description", suitCreateDto.getDescription().length(), 1000));
			}
		}
	}

	@DeleteMapping("/delete/{id}")
	public void deleteSuit(@PathVariable Long id) {
		validateId(id, "suit");
		for (CaseData cas : suitService.getExistingSuitById(id).getCases()) {
			caseController.deleteCase(cas.getId());
		}
		suitService.deleteSuit(id);
	}

	@PutMapping("/update")
	public void updateSuit(@RequestBody SuitUpdateDto suitUpdateDTO) {
		validateSuitUpdateDto(suitUpdateDTO);
		validateId(suitUpdateDTO.getId(), "suit");

		SuitData suit = suitService.getExistingSuitById(suitUpdateDTO.getId());
		suit.setStatusId(suitUpdateDTO.getStatusId());
		suit.setTitle(suitUpdateDTO.getTitle());
		suit.setDescription(suitUpdateDTO.getDescription());
		suit.setCode(suitUpdateDTO.getCode());

		suitService.updateSuit(suit);
	}

	private void validateSuitUpdateDto(SuitUpdateDto suitUpdateDto) {
		if (DtoUtil.isFieldNull(suitUpdateDto.getStatusId())) {
			throw throwAndLogError(logger, new ApplicationException(ErrorCode.NULL_VALUE_FORBIDDEN, "statusId", "suit"));
		}
		if (DtoUtil.isStringNullOrEmpty(suitUpdateDto.getTitle())) {
			throw throwAndLogError(logger, new ApplicationException(ErrorCode.NULL_VALUE_FORBIDDEN, "title", "suit"));
		}
		if (DtoUtil.isStringTooLong(suitUpdateDto.getTitle(), 100)) {
			throw throwAndLogError(logger, new ApplicationException(ErrorCode.TOO_LONG_VALUE, "title", suitUpdateDto.getTitle().length(), 100));
		}
		if (DtoUtil.isStringNullOrEmpty(suitUpdateDto.getCode())) {
			throw throwAndLogError(logger, new ApplicationException(ErrorCode.NULL_VALUE_FORBIDDEN, "code", "suit"));
		}
		if (DtoUtil.isStringTooLong(suitUpdateDto.getCode(), 30)) {
			throw throwAndLogError(logger, new ApplicationException(ErrorCode.TOO_LONG_VALUE, "code", suitUpdateDto.getCode().length(), 30));
		}
		if (!DtoUtil.isFieldNull(suitUpdateDto.getDescription())) {
			if (DtoUtil.isStringTooLong(suitUpdateDto.getDescription(), 1000)) {
				throw throwAndLogError(logger, new ApplicationException(ErrorCode.TOO_LONG_VALUE, "description", suitUpdateDto.getDescription().length(), 1000));
			}
		}
	}

	@PostMapping("/createStatus")
	public Long createSuitStatus(@RequestBody SuitStatusData suitStatus) {
		validateName(suitStatus.getName(), "suitStatus");
		return suitService.createSuitStatus(suitStatus);
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
	public void updateSuitStatus(@RequestBody SuitStatusData updatedSuitStatus) {
		validateSuitStatusUpdate(updatedSuitStatus);
		validateId(updatedSuitStatus.getId(), "suitStatus");

		SuitStatusData suitStatus = suitService.getSuitStatusById(updatedSuitStatus.getId());
		suitStatus.setName(updatedSuitStatus.getName());

		suitService.updateSuitStatus(suitStatus);
	}

	private void validateSuitStatusUpdate(SuitStatusData suitStatus) {
		validateId(suitStatus.getId(), "suitStatus");
		validateName(suitStatus.getName(), "suitStatus");
	}
}
