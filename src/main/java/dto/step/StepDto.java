package dto.step;

import dto.attachment.AttachmentDto;
import entity.StepData;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import entity.AttachmentData;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
public class StepDto {

	private Long id;
	private LocalDateTime creationDate;
	private LocalDateTime modificationDate;
	private LocalDateTime deletionDate;
	private Long caseId;
	private Long statusId;
	private String action;
	private Integer order;
	private String comment;
	private String result;
	private List<AttachmentDto> attachments;

	public StepDto(Long id, LocalDateTime creationDate, LocalDateTime modificationDate, LocalDateTime deletionDate, Long caseId, Long statusId, String action, Integer order, String comment, String result, List<AttachmentData> attachments) {
		this.id = id;
		this.creationDate = creationDate;
		this.modificationDate = modificationDate;
		this.deletionDate = deletionDate;
		this.caseId = caseId;
		this.statusId = statusId;
		this.action = action;
		this.order = order;
		this.comment = comment;
		this.result = result;
		this.attachments = attachments.stream()
				.map(AttachmentDto::toDto)
				.collect(Collectors.toList());
	}

	public static StepDto toDto(StepData step) {
		return new StepDto(
				step.getId(),
				step.getCreationDate(),
				step.getModificationDate(),
				step.getDeletionDate(),
				step.getCaseId(),
				step.getStatusId(),
				step.getAction(),
				step.getOrder(),
				step.getComment(),
				step.getResult(),
				new ArrayList<>(step.getAttachments().values())
		);
	}

	public static StepData toEntity(StepDto stepDto) {
		return new StepData(
				stepDto.getId(),
				stepDto.getCreationDate(),
				stepDto.getModificationDate(),
				stepDto.getDeletionDate(),
				stepDto.getCaseId(),
				stepDto.getStatusId(),
				stepDto.getAction(),
				stepDto.getOrder(),
				stepDto.getComment(),
				stepDto.getResult(),
				stepDto.getAttachments().stream()
						.map(AttachmentDto::toEntity)
						.collect(Collectors.toMap(AttachmentData::getId, attachmentData -> attachmentData))
		);
	}
}
