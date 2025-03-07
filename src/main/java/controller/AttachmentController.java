package controller;

import dto.DtoUtil;
import dto.attachment.AttachmentDto;
import entity.AttachmentDataTypeData;
import exception.ApplicationException;
import exception.ErrorCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import service.AttachmentService;

@RestController
@RequestMapping("/api/attachments")
public class AttachmentController {

	@Autowired
	private AttachmentService attachmentService;

	@GetMapping("/{id}")
	public AttachmentDto getById(@PathVariable("id") Long id) {
		validateId(id, "attachment");
		return AttachmentDto.toDto(attachmentService.getAttachmentById(id));
	}

	private void validateId(Long id, String obj) {
		if (DtoUtil.isFieldNull(id)) {
			throw new ApplicationException(ErrorCode.NULL_VALUE_FORBIDDEN, "id", obj);
		}
	}

	@PostMapping("/createDataType")
	public Long createDataType(@RequestBody AttachmentDataTypeData attachmentDataType) {
		validateType(attachmentDataType.getType(), "attachmentDataType");
		return attachmentService.createAttachmentDataType(attachmentDataType);
	}

	private void validateType(String type, String obj) {
		if (DtoUtil.isStringNullOrEmpty(type)) {
			throw new ApplicationException(ErrorCode.NULL_VALUE_FORBIDDEN, "type", obj);
		}
		if (DtoUtil.isStringTooLong(type, 50)) {
			throw  new ApplicationException(ErrorCode.TOO_LONG_VALUE, "type", type.length(), 50);
		}
	}

	@PutMapping("/updateDataType")
	public void updateDataType(@RequestBody AttachmentDataTypeData updatedAttachmentDataType) {
		validateDataTypeUpdate(updatedAttachmentDataType);

		AttachmentDataTypeData attachmentDataType = attachmentService.getAttachmentDataTypeById(updatedAttachmentDataType.getId());
		attachmentDataType.setType(updatedAttachmentDataType.getType());

		attachmentService.updateAttachmentDataType(attachmentDataType);
	}

	private void validateDataTypeUpdate(AttachmentDataTypeData updatedAttachmentDataType) {
		validateId(updatedAttachmentDataType.getId(), "attachmentDataType");
		validateType(updatedAttachmentDataType.getType(), "attachmentDataType");
	}
}
