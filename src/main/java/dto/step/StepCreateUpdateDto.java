package dto.step;

import dto.attachment.AttachmentDto;
import entity.StepData;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class StepCreateUpdateDto {

	private Long id;
	private Long statusId;
	private String action;
	private Integer order;
	private String comment;
	private String result;
	private StepState stepState;
	private List<AttachmentDto> attachments;

	public StepCreateUpdateDto(Long id, Long statusId, String action, Integer order, String comment, String result) {
		this.id = id;
		this.statusId = statusId;
		this.action = action;
		this.order = order;
		this.comment = comment;
		this.result = result;
	}

	public static StepCreateUpdateDto toDto(StepData step) {
		return new StepCreateUpdateDto(
				step.getId(),
				step.getStatusId(),
				step.getAction(),
				step.getOrder(),
				step.getComment(),
				step.getResult()
		);
	}

	public static StepData toEntity(StepCreateUpdateDto stepCreateUpdateDto) {
		return new StepData(
				stepCreateUpdateDto.getId(),
				stepCreateUpdateDto.getStatusId(),
				stepCreateUpdateDto.getAction(),
				stepCreateUpdateDto.getOrder(),
				stepCreateUpdateDto.getComment(),
				stepCreateUpdateDto.getResult()
		);
	}
}
