package service;

import entity.AttachmentData;
import entity.AttachmentDataTypeData;
import exception.ApplicationException;
import exception.ErrorCode;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import repository.AttachmentDataTypeTypeRepository;
import repository.AttachmentRepository;

import java.time.LocalDateTime;

@Service
public class AttachmentService {

	@Autowired
	private AttachmentRepository attachmentRepository;

	@Autowired
	private AttachmentDataTypeTypeRepository attachmentDataTypeTypeRepository;

	@Autowired
	private EntityManager entityManager;

	@Autowired
	private AuditLogHeaderService auditLogHeaderService;

	@Autowired
	private UserService userService;

	private final Logger logger = LogManager.getLogger(AttachmentService.class);

	public AttachmentData getAttachmentById(Long id) {
		return attachmentRepository.findById(id)
				.orElseThrow(() -> new ApplicationException(ErrorCode.OBJECT_NOT_FOUND, "attachment", id));
	}

	public AttachmentData getExistingAttachmentById(Long id) {
		AttachmentData attachment = attachmentRepository.findExistingById(id);

		if (attachment == null) {
			throw new ApplicationException(ErrorCode.OBJECT_NOT_FOUND, "attachment", id);
		}

		return attachment;
	}

	public Long createAttachment(AttachmentData updatedAttachment) {
		updatedAttachment.setCreationDate(LocalDateTime.now());
		attachmentRepository.save(updatedAttachment);
		logger.info("Attachment with id {} and stepId {} has been added.", updatedAttachment.getId(), updatedAttachment.getStepId());
		return updatedAttachment.getId();
	}

	@PreAuthorize("hasAuthority('ROLE_ADMIN') || hasAuthority('ROLE_LEADER')")
	@Transactional
	public void deleteAttachment(Long id) {
		AttachmentData updatedAttachment = getAttachmentById(id);
		updatedAttachment.setDeletionDate(LocalDateTime.now());
		attachmentRepository.save(updatedAttachment);
		logger.info("Attachment with id {} has been removed from step with id {}.", updatedAttachment.getId(), updatedAttachment.getStepId());
	}

	public AttachmentDataTypeData getAttachmentDataTypeById(Long id) {
		return attachmentDataTypeTypeRepository.findById(id)
				.orElseThrow(() -> new ApplicationException(ErrorCode.OBJECT_NOT_FOUND, "attachmentDataType", id));
	}

	public Long createAttachmentDataType(AttachmentDataTypeData attachmentDataType) {
		attachmentDataTypeTypeRepository.save(attachmentDataType);
		return attachmentDataType.getId();
	}

	public void updateAttachmentDataType(AttachmentDataTypeData updatedAttachmentDataType) {
		entityManager.detach(updatedAttachmentDataType);
		attachmentDataTypeTypeRepository.save(updatedAttachmentDataType);
	}
}
