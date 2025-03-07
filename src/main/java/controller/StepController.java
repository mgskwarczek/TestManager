package controller;

import dto.DtoUtil;
import dto.step.StepDto;
import entity.StepStatusData;
import exception.ApplicationException;
import exception.ErrorCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import service.StepService;


@RestController
@RequestMapping("/api/steps")
public class StepController {

	@Autowired
	private StepService stepService;

	@GetMapping("/{id}")
	public StepDto getById(@PathVariable("id") Long id) {
		validateId(id, "step");
		System.out.println("id " + id);
		return StepDto.toDto(stepService.getStepById(id));
	}

	private void validateId(Long id, String obj) {
		if (DtoUtil.isFieldNull(id)) {
			throw new ApplicationException(ErrorCode.NULL_VALUE_FORBIDDEN, "id", obj);
		}
	}

	@PostMapping("/createStatus")
	public Long createStepStatus(@RequestBody StepStatusData stepStatus) {
		validateName(stepStatus.getName(), "stepStatus");
		return stepService.createStepStatus(stepStatus);
	}

	private void validateName(String name, String obj) {
		if (DtoUtil.isStringNullOrEmpty(name)) {
			throw new ApplicationException(ErrorCode.NULL_VALUE_FORBIDDEN, "name", obj);
		}
		if (DtoUtil.isStringTooLong(name,50)) {
			throw new ApplicationException(ErrorCode.TOO_LONG_VALUE, "name", name.length(), 50);
		}
	}

	@PutMapping("/updateStatus")
	public void updateStepStatus(@RequestBody StepStatusData updatedStepStatus) {
		validateStepStatusUpdate(updatedStepStatus);
		validateId(updatedStepStatus.getId(), "stepStatus");

		StepStatusData stepStatus = stepService.getStepStatusById(updatedStepStatus.getId());
		stepStatus.setName(updatedStepStatus.getName());

		stepService.updateStepStatus(stepStatus);
	}

	private void validateStepStatusUpdate(StepStatusData stepStatus) {
		validateId(stepStatus.getId(), "stepStatus");
		validateName(stepStatus.getName(), "stepStatus");
	}
}
